import pandas as pd
from tqdm import tqdm


def create_user(User, first_name='', last_name='', email='', username='', password='test123456'):
	return User.objects.create_user(first_name=first_name,
			last_name=last_name,
			email=email,
			username=username,
			password=password)


def create_person(User, Token, Person, **kwargs):
	user = create_user(User, **kwargs)
	Token.objects.create(user=user)
	return Person.objects.create(user=user)


def create_item(Item, **kwargs):
	Item.objects.create(**kwargs)


def create_all_customers(User, Token, Customer):
	customers_df = pd.read_csv('database/csvs/customers.csv')
	for _, row in tqdm(customers_df.iterrows()):
		kwargs = {'first_name': row.first_name,
				'last_name': row.last_name,
				'email': row.email,
				'username': row.first_name.lower() + row.last_name.lower()}
		customer = create_person(User, Token, Customer, **kwargs)
		customer.create_pending_order()


def create_all_pilots(User, Token, Pilot):
	pilots_df = pd.read_csv('database/csvs/pilots.csv')
	for _, row in tqdm(pilots_df.iterrows()):
		kwargs = {'first_name': row.first_name,
				'last_name': row.last_name,
				'email': row.email,
				'username': row.first_name.lower() + row.last_name.lower()}
		pilot = create_person(User, Token, Pilot, **kwargs)
		pilot.region = row.region
		pilot.save()


def create_all_sellers(User, Token, Seller):
	sellers_df = pd.read_csv('database/csvs/sellers.csv')
	for _, row in tqdm(sellers_df.iterrows()):
		kwargs = {'first_name': row.first_name,
				'last_name': row.last_name,
				'email': row.email,
				'username': row.username}
		create_person(User, Token, Seller, **kwargs)


def create_all_items(Item, Seller):
	items_df = pd.read_csv('database/csvs/items.csv')
	for _, row in tqdm(items_df.iterrows()):
		kwargs = {'name': row.item_name,
				'description': row.description,
				'price': row.price,
				'seller': Seller.objects.get(user__username=row.seller)}
		create_item(Item, **kwargs)

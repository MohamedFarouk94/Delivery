import pandas as pd
from tqdm import tqdm


def create_user(User, first_name='', last_name='', email='', username='', password='test123456'):
	return User.objects.create_user(first_name=first_name,
			last_name=last_name,
			email=email,
			username=username,
			password=password)


def create_person(Person, **kwargs):
	user = create_user(kwargs)
	return Person.objects.create(user=user)


def create_all_customers(Customer):
	customers_df = pd.read_csv('csvs/customers.csv')
	for _, row in tqdm(customers_df.iterrows()):
		kwargs = {'first_name': row.first_name,
				'last_name': row.last_name,
				'email': row.email,
				'username': row.first_name.lowercase() + row.last_name.lowercase()}
		customer = create_person(Customer, **kwargs)
		customer.create_pending_order()


def create_all_pilots(Pilot):
	pilots_df = pd.read_csv('csvs/pilots.csv')
	for _, row in tqdm(pilots_df.iterrows()):
		kwargs = {'first_name': row.first_name,
				'last_name': row.last_name,
				'email': row.email,
				'username': row.first_name.lowercase() + row.last_name.lowercase()}
		create_person(Pilot, **kwargs)


def create_all_sellers(Seller):
	pass

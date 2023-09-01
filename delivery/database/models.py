from django.db import models
from django.contrib.auth.models import User
from .strings import ORDER_STATUS_CHOICES, PERSON_STATUS_CHOICES, pn, od, ac
from datetime import datetime as dt


# Create your models here.

class Person(models.Model):
	# first_name, last_name, username, email, password & date_joined are in User
	user = models.OneToOneField(User, on_delete=models.CASCADE)
	status = models.CharField(max_length=10, choices=PERSON_STATUS_CHOICES, default=ac)

	def to_dict(self):
		return {
			'id': self.user.id,
			'group': self.__class__.plural,
			'first-name': self.user.first_name,
			'last-name': self.user.last_name,
			'username': self.user.username,
			'status': self.status,
			'email': self.user.email,
			'date-joined': self.user.date_joined
		}


class Seller(Person):
	plural = 'sellers'

	def get_items(self):
		return Item.objects.filter(seller=self)


class Customer(Person):
	plural = 'customers'

	def get_orders(self):
		return Order.objects.filter(customer=self)

	def get_pending_order(self):
		return Order.objects.get(customer=self, status=pn)

	def create_pending_order(self):
		if len(Order.objects.filter(customer=self, status=pn)):
			raise AttributeError('A pending order for this customer already exists.')

		return Order.objects.create(customer=self)

	def get_basket(self):
		return self.get_pending_order().get_basket()

	def add_to_basket(self, item, quantity):
		return self.get_pending_order().add_to_basket(item, quantity)

	def make_order(self, region):
		self.get_pending_order().make_order(region)
		self.create_pending_order()


class Pilot(Person):
	plural = 'persons'


class Item(models.Model):
	plural = 'items',

	id = models.BigAutoField(primary_key=True)
	name = models.CharField(max_length=50)
	seller = models.ForeignKey(Seller, on_delete=models.CASCADE)
	description = models.TextField(blank=True)
	price = models.FloatField()
	rating = models.FloatField(null=True)
	n_raters = models.IntegerField(default=0)
	n_orders = models.IntegerField(default=0)
	n_buyouts = models.IntegerField(default=0)
	# Later add image

	def to_dict(self):
		return {
			'id': self.id,
			'name': self.name,
			'seller-id': self.seller.id,
			'seller-username': self.seller.username,
			'description': self.description,
			'price': self.price,
			'rating': self.rating,
			'number-of-raters': self.n_raters,
			'number-of-orders': self.n_orders,
			'number-of-buyouts': self.n_buyouts,
		}


class Order(models.Model):
	plural = 'orders',

	id = models.BigAutoField(primary_key=True)
	customer = models.ForeignKey(Customer, on_delete=models.CASCADE)
	region = models.CharField(max_length=20, blank=True)
	pilot = models.ForeignKey(Pilot, on_delete=models.SET_NULL, null=True)
	status = models.CharField(max_length=10, choices=ORDER_STATUS_CHOICES, default=pn)
	date_ordered = models.DateTimeField(null=True)
	date_pilot_assigned = models.DateTimeField(null=True)
	date_completed = models.DateTimeField(null=True)

	def raise_error_if_not_pending(self):
		if self.status != pn:
			raise AttributeError('This order is already ordered.')

	def raise_error_if_item_exists(self, item):
		if len(BasketItem.objects.filter(order=self, item=item)):
			raise AttributeError('This item is already in the basket.')

	def raise_error_if_item_doesnt_exist(self, item):
		if not len(BasketItem.objects.filter(order=self, item=item)):
			raise AttributeError('This item is not in the basket.')

	def get_basket(self):
		return BasketItem.objects.filter(order=self)

	def get_quantity_of_item(self, item):
		self.raise_error_if_not_pending()
		try:
			self.raise_error_if_item_doesnt_exist(item)
			return BasketItem.objects.get(order=self, item=item).get_quantity()
		except AttributeError:
			return 0

	def edit_quantity_of_item(self, item, quantity):
		self.raise_error_if_not_pending()
		self.raise_error_if_item_doesnt_exist(item)

		BasketItem.objects.get(order=self, item=item).edit_quantity(quantity)

	def add_to_basket(self, item, quantity):
		self.raise_error_if_not_pending()
		self.raise_error_if_item_exists(item)

		return BasketItem.objects.create(order=self, item=item, quantity=quantity)

	def remove_from_basket(self, item):
		self.raise_error_if_not_pending()
		self.raise_error_if_item_doesnt_exist(item)

		BasketItem.objects.filter(order=self, item=item).delete()

	def make_order(self, region):
		if self.status != pn:
			raise AttributeError('This order is already ordered.')

		self.status = od
		self.region = region
		self.date_ordered = dt.now()
		self.save()

	def to_dict(self):
		return {
			'id': self.id,
			'customer-id': self.customer.user.id,
			'customer-username': self.customer.user.username,
			'pilot-id': self.pilot.user.id if self.pilot else 'N/A',
			'pilot-username': self.pilot.user.username if self.pilot else 'N/A',
			'status': self.status,
			'date-ordered': self.date_ordered if self.date_ordered else 'N/A',
			'date-pilot-assigned': self.date_pilot_assigned if self.date_pilot_assigned else 'N/A',
			'date-completed': self.date_completed if self.date_completed else 'N/A'
		}


class BasketItem(models.Model):
	plural = 'basket_items',

	id = models.BigAutoField(primary_key=True),
	order = models.ForeignKey(Order, on_delete=models.CASCADE)
	item = models.ForeignKey(Item, on_delete=models.CASCADE)
	quantity = models.IntegerField(default=1)

	def get_quantity(self):
		return self.quantity

	def edit_quantity(self, quantity):
		self.quantity = quantity

	def to_dict(self):
		return {
			'id': self.id,
			'order-id': self.order.id,
			'item-id': self.item.id,
			'quantity': self.quantity
		}

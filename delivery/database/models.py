from django.db import models
from django.contrib.auth.models import User
from .strings import ORDER_STATUS_CHOICES, PERSON_STATUS_CHOICES, ac, pn


# Create your models here.

class Person(models.Model):
	# first_name, last_name, username, email, password & date_joined are in User
	user = models.OneToOneField(User, on_delete=models.CASCADE)
	status = models.CharField(max_length=10, choices=PERSON_STATUS_CHOICES, default=ac)

	def to_dict(self):
		return {
			'group': self.__class__.plural,
			'first-name': self.user.first_name,
			'last-name': self.user.last_name,
			'username': self.user.username,
			'status': self.status,
			'email': self.user.email,
			'date-joined': self.date_joined
		}


class Seller(Person):
	plural = 'sellers'


class Customer(Person):
	plural = 'customers'


class Pilot(Person):
	plural = 'persons'

	def get_basket(self):
		# To Be Implemented
		return {}


class Item(models.Model):
	plural = 'items',

	name = models.CharField(max_length=50),
	seller = models.ForeignKey(Seller, on_delete=models.CASCADE),
	description = models.TextField(blank=True),
	price = models.FloatField(),
	rating = models.FloatField(null=True),
	n_raters = models.IntegerField(default=0),
	n_orders = models.IntegerField(default=0),
	n_buyouts = models.IntegerField(default=0)
	# Later add image

	def to_dict(self):
		return {
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

	customer = models.ForeignKey(Customer, on_delete=models.CASCADE),
	pilot = models.ForeignKey(Pilot, on_delete=models.SET_NULL, null=True),
	status = models.CharField(max_length=10, choices=ORDER_STATUS_CHOICES, default=pn),
	date_purchased = models.DateTimeField(null=None),
	date_completed = models.DateTimeField(null=None)

	def to_dict(self):
		return {
			'customer-id': self.customer.user.id,
			'customer-username': self.customer.user.username,
			'pilot-id': self.pilot.user.id if self.pilot else 'N/A',
			'pilot-username': self.pilot.user.username if self.pilot else 'N/A',
			'status': self.status,
			'date-purchased': self.date_purchased if self.date_purchased else 'N/A',
			'date-completed': self.date_completed if self.date_completed else 'N/A'
		}


class BasketItem(models.Model):
	plural = 'basket_items',

	order = models.ForeignKey(Order, on_delete=models.CASCADE),
	item = models.ForeignKey(Item, on_delete=models.CASCADE),
	quantity = models.IntegerField(default=1)

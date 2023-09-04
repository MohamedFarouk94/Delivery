from django.db import models
from django.contrib.auth.models import User
from .strings import ORDER_STATUS_CHOICES, PERSON_STATUS_CHOICES, pn, ac


# Create your models here.

class Person(models.Model):
	# first_name, last_name, username, email, password & date_joined are in User
	user = models.OneToOneField(User, on_delete=models.CASCADE)
	status = models.CharField(max_length=10, choices=PERSON_STATUS_CHOICES, default=ac)

	from .dbmethods.person import to_dict


class Seller(Person):
	plural = 'sellers'

	from .dbmethods.seller import get_items


class Customer(Person):
	plural = 'customers'

	from .dbmethods.customer import get_orders, get_pending_order, create_pending_order
	from .dbmethods.customer import get_basket, edit_quantity_of_item, add_to_basket, remove_from_basket
	from .dbmethods.customer import make_order


class Pilot(Person):
	plural = 'persons'

	region = models.CharField(max_length=20)

	from .dbmethods.pilot import to_dict


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
	image = models.ImageField(default='no_image_available.jpg')  # Assign image by path after 'images/' (not including images/)

	from .dbmethods.item import to_dict, display_image


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

	from .dbmethods.order import raise_error_if_item_exists, raise_error_if_not_pending, raise_error_if_item_doesnt_exist
	from .dbmethods.order import get_basket, get_quantity_of_item, edit_quantity_of_item, add_to_basket, remove_from_basket
	from .dbmethods.order import make_order, to_dict


class BasketItem(models.Model):
	plural = 'basket_items',

	id = models.BigAutoField(primary_key=True),
	order = models.ForeignKey(Order, on_delete=models.CASCADE)
	item = models.ForeignKey(Item, on_delete=models.CASCADE)
	quantity = models.IntegerField(default=1)

	from .dbmethods.basket_item import get_quantity, edit_quantity, to_dict

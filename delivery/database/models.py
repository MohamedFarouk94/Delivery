from django.db import models
from django.contrib.contenttypes.fields import GenericForeignKey
from django.contrib.contenttypes.models import ContentType
from django.contrib.auth.models import User
from .strings import ORDER_STATUS_CHOICES, PERSON_STATUS_CHOICES, PERSON_CATEGORY_CHOICES, pn, ac, tbd
from .validators import price_validator, rating_validator, quantity_validator


# Create your models here.

class Person(models.Model):
	ORDER = None
	ITEM = None
	REVIEW = None
	CHILDREN = None

	# id, first_name, last_name, username, email, password & date_joined are in User
	user = models.OneToOneField(User, on_delete=models.CASCADE)
	status = models.CharField(max_length=10, choices=PERSON_STATUS_CHOICES, default=ac)
	category = models.CharField(max_length=10, choices=PERSON_CATEGORY_CHOICES, default=tbd)
	rating = models.FloatField(null=True, blank=True)
	n_raters = models.IntegerField(default=0)

	from .dbmethods.person import get_orders, get_region, to_dict
	from .dbmethods.ratings import update_rating, undo_rating


class Seller(Person):

	from .dbmethods.seller import get_items, add_item, delete_item, edit_item, set_image


class Customer(Person):

	from .dbmethods.customer import get_orders, get_pending_order, create_pending_order
	from .dbmethods.customer import get_basket, get_basket_item, edit_quantity_of_item, add_to_basket, remove_from_basket
	from .dbmethods.customer import make_order, send_item_review, send_order_review


class Pilot(Person):

	region = models.CharField(max_length=20)
	n_drops = models.IntegerField(default=0)
	busy = models.BooleanField(default=False)

	from .dbmethods.pilot import get_available_orders, accept_order, drop_order, complete_order, report_order, to_dict


class Item(models.Model):
	editable_attributes = ['name', 'description', 'price']

	id = models.BigAutoField(primary_key=True)
	name = models.CharField(max_length=50)
	seller = models.ForeignKey(Seller, on_delete=models.CASCADE)
	description = models.TextField(blank=True)
	price = models.FloatField(validators=[price_validator])
	rating = models.FloatField(null=True, blank=True)
	n_raters = models.IntegerField(default=0)
	n_orders = models.IntegerField(default=0)
	n_buyouts = models.IntegerField(default=0)
	image = models.ImageField(default='no_image_available.jpg')  # Assign image by path after 'images/' (not including images/)

	from .dbmethods.item import display_image, get_b64img, set_image, assign_image, edit, to_dict, save
	from .dbmethods.ratings import update_rating, undo_rating


class Order(models.Model):
	BASKET_ITEM = None

	id = models.BigAutoField(primary_key=True)
	customer = models.ForeignKey(Customer, on_delete=models.CASCADE)
	region = models.CharField(max_length=20, blank=True)
	pilot = models.ForeignKey(Pilot, on_delete=models.SET_NULL, null=True)
	status = models.CharField(max_length=10, choices=ORDER_STATUS_CHOICES, default=pn)
	date_ordered = models.DateTimeField(null=True)
	date_pilot_assigned = models.DateTimeField(null=True)
	date_concluded = models.DateTimeField(null=True)

	from .dbmethods.order import raise_error_if_item_exists, raise_error_if_not_pending, raise_error_if_basket_empty, raise_error_if_item_doesnt_exist
	from .dbmethods.order import raise_error_if_completed, raise_error_if_problem, raise_error_if_on_way, raise_error_if_canceled
	from .dbmethods.order import get_basket, get_basket_item, get_quantity_of_item, edit_quantity_of_item, add_to_basket, remove_from_basket
	from .dbmethods.order import get_delivery_fee, get_prices_sum, get_deserved_amount
	from .dbmethods.order import assign_pilot, remove_pilot, complete, problem
	from .dbmethods.order import make_order, cancel_order, update_rating, undo_rating, know_this_person, to_dict


class BasketItem(models.Model):

	id = models.BigAutoField(primary_key=True),
	order = models.ForeignKey(Order, on_delete=models.CASCADE)
	item = models.ForeignKey(Item, on_delete=models.CASCADE)
	quantity = models.IntegerField(default=1, validators=[quantity_validator])

	from .dbmethods.basket_item import get_quantity, edit_quantity, get_deserved_amount, to_dict, save


class Review(models.Model):

	id = models.BigAutoField(primary_key=True)

	reviewer_id = models.BigIntegerField()
	reviewer_type = models.ForeignKey(ContentType, on_delete=models.CASCADE, related_name='REVIEWER')
	reviewer = GenericForeignKey('reviewer_type', 'reviewer_id')

	reviewed_id = models.BigIntegerField()
	reviewed_type = models.ForeignKey(ContentType, on_delete=models.CASCADE, related_name='REVIEWED')
	reviewed = GenericForeignKey('reviewed_type', 'reviewed_id')

	date_created = models.DateTimeField(auto_now_add=True)
	taken_in_calculation = models.BooleanField(default=True)
	text = models.TextField(blank=True)
	rating = models.IntegerField(default=5, validators=[rating_validator])

	from .dbmethods.review import deactivate, to_dict, save, delete


if not Person.ORDER:
	print('Person.ORDER')
	Person.ORDER = Order

if not Person.ITEM:
	print('Person.ITEM')
	Person.ITEM = Item

if not Person.REVIEW:
	print('Person.REVIEW')
	Person.REVIEW = Review

if not Person.CHILDREN:
	print('Person.CHILDREN')
	Person.CHILDREN = {'Customer': Customer, 'Pilot': Pilot}

if not Order.BASKET_ITEM:
	print('Order.BASKET_ITEM')
	Order.BASKET_ITEM = BasketItem

print()

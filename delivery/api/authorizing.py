from database.models import Item, Person
from django.http import HttpResponseForbidden


# utils

def get_sender_category(request):
	return Person.objects.get(user=request.user).category


def assert_sender_is(request, supposed):
	try:
		assert get_sender_category(request) == supposed
		return True, None
	except AssertionError:
		return False, HttpResponseForbidden(f'{{"details": "Sender is not {supposed}."}}')


def assert_sender_specific(request, item, item_attr):
	try:
		assert getattr(item, item_attr).user == request.user
		return True, None
	except AssertionError:
		return False, HttpResponseForbidden(f'{{"details": "Sender is not the item {item_attr}"}}')


# General Requests

def helloWorld(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def test(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def whoAmI(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getItems(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getItem(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getImage(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getSellers(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getSeller(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getSellerItems(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


# Seller Requests

def editItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is the item owner
	if flag:
		flag, response = assert_sender_specific(request, item, 'seller')

	return flag, response


def addItem(request, **kwargs):
	flag, response = True, None

	# Checking sender is a seller
	if flag:
		flag, response = assert_sender_is(request, 'Seller')

	return flag, response


def deleteItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is the item owner
	if flag:
		flag, response = assert_sender_specific(request, item, 'seller')

	return flag, response


def setImage(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is the item owner
	if flag:
		flag, response = assert_sender_specific(request, item, 'seller')

	return flag, response


# Customer Requests

def getBasket(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def addToBasket(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def removeFromBasket(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def editQuantity(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def makeOrder(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response

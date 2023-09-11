from database.models import Item, Seller, Order
from django.http import HttpResponseBadRequest, HttpResponseNotFound
from django.core.exceptions import ObjectDoesNotExist


# utils

def assert_object_exists(cls, **kwargs):
	try:
		cls.objects.get(id=kwargs['id'])
		return True, None
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound(f'{{"details": "{cls.__name__} not found."}}')


def assert_attributes_exist(valid, coming):
	try:
		for key in coming:
			assert key in valid
		return True, None
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "Key word not found"}')


def assert_attributes_contain(needed_words, coming):
	try:
		for key in needed_words:
			assert key in coming
		return True, None
	except AssertionError:
		return False, HttpResponseBadRequest(f'{{"details": "Request does not contain {key}."}}')


# General Requests

def helloWorld(request, **kwargs):
	flag, response = True, None

	# Thers's no need to check anything
	return flag, response


def test(request, **kwargs):
	flag, response = True, None

	# Thers's no need to check anything
	return flag, response


def whoAmI(request, **kwargs):
	flag, response = True, None

	# Thers's no need to check anything
	return flag, response


def getItems(request, **kwargs):
	flag, response = True, None

	# Thers's no need to check anything
	return flag, response


def getItem(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	return flag, response


def getImage(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	return flag, response


def getSellers(request, **kwargs):
	flag, response = True, None

	# Thers's no need to check anything
	return flag, response


def getSeller(request, **kwargs):
	flag, response = True, None

	# Checking that seller exists
	if flag:
		flag, response = assert_object_exists(Seller, **kwargs)

	return flag, response


def getSellerItems(request, **kwargs):
	flag, response = True, None

	# Checking that seller exists
	if flag:
		flag, response = assert_object_exists(Seller, **kwargs)

	return flag, response


# Seller Requests

def editItem(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	# Checking attributes exist in Item.editable_attributes
	if flag:
		flag, response = assert_attributes_exist(Item.editable_attributes, request.data)

	return flag, response


def addItem(request, **kwargs):
	flag, response = True, None

	# Checking attributes exist in Item.editable_attributes
	if flag:
		flag, response = assert_attributes_exist(Item.editable_attributes, request.data)

	return flag, response


def deleteItem(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	return flag, response


def setImage(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	# Checking b64img is in data
	if flag:
		flag, response = assert_attributes_contain(['b64img'], request.data)

	return flag, response


# Customer Requests

def getBasket(request, **kwargs):
	flag, response = True, None

	# Thers's no need to check anything
	return flag, response


def addToBasket(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	# Checking quantity is in data
	if flag:
		flag, response = assert_attributes_contain(['quantity'], request.data)

	return flag, response


def removeFromBasket(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	return flag, response


def editQuantity(request, **kwargs):
	flag, response = True, None

	# Checking that item exists
	if flag:
		flag, response = assert_object_exists(Item, **kwargs)

	# Checking quantity is in data
	if flag:
		flag, response = assert_attributes_contain(['quantity'], request.data)

	return flag, response


def makeOrder(request, **kwargs):
	flag, response = True, None

	# Checking region is in data
	if flag:
		flag, response = assert_attributes_contain(['region'], request.data)

	return flag, response


def cancelOrder(request, **kwargs):
	flag, response = True, None

	# Checking that order exists
	if flag:
		flag, response = assert_object_exists(Order, **kwargs)

	return flag, response


# Customer & Pilot Requests

def getOrders(request, **kwargs):
	flag, response = True, None

	# Thers's no need to check anything
	return flag, response


def getOrder(request, **kwargs):
	flag, response = True, None

	# Checking that order exists
	if flag:
		flag, response = assert_object_exists(Order, **kwargs)

	return flag, response

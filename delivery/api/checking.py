from database.models import Item, Seller
from django.http import HttpResponseBadRequest, HttpResponseNotFound
from django.core.exceptions import ObjectDoesNotExist


# General Requests

def helloWorld(request, **kwargs):
	# Thers's no need to check anything
	return True, None


def test(request, **kwargs):
	# Thers's no need to check anything
	return True, None


def whoAmI(request, **kwargs):
	# Thers's no need to check anything
	return True, None


def getItems(request, **kwargs):
	# Thers's no need to check anything
	return True, None


def getItem(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
		return True, None
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')


def getImage(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
		return True, None
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')


def getSellers(request, **kwargs):
	# Thers's no need to check anything
	return True, None


def getSeller(request, **kwargs):
	# Checking that seller exists
	try:
		Seller.objects.get(id=kwargs['id'])
		return True, None
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Seller not found."}')


def getSellerItems(request, **kwargs):
	# Checking that seller exists
	try:
		Seller.objects.get(id=kwargs['id'])
		return True, None
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Seller not found."}')


# Seller Requests

def editItem(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')

	# Checking attributes exist in Item.editable_attributes
	try:
		for key in request.data.keys():
			assert key in Item.editable_attributes
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "Found unknown key word"}')

	return True, None


def addItem(request, **kwargs):
	# Checking attributes exist in Item.editable_attributes
	try:
		for key in request.data.keys():
			assert key in Item.editable_attributes
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "Found unknown key word"}')

	return True, None


def deleteItem(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
		return True, None
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')


def setImage(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')

	# Checking b64img is in data
	try:
		assert 'b64img' in request.data
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "No image found"}')

	return True, None


# Customer Request

def getBasket(request, **kwargs):
	# Thers's no need to check anything
	return True, None


def addToBasket(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')

	# Checking quantity is in data
	try:
		assert 'quantity' in request.data
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "No quntity found"}')

	return True, None


def removeFromBasket(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')

	return True, None


def editQuantity(request, **kwargs):
	# Checking that item exists
	try:
		Item.objects.get(id=kwargs['id'])
	except ObjectDoesNotExist:
		return False, HttpResponseNotFound('{"details": "Item not found."}')

	# Checking quantity is in data
	try:
		assert 'quantity' in request.data
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "No quntity found"}')

	return True, None


def makeOrder(request, **kwargs):
	# Checking region is in data
	try:
		assert 'region' in request.data
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "No region found"}')

	return True, None

from database.models import Item
from django.http import HttpResponseBadRequest, HttpResponseNotFound
from django.core.exceptions import ObjectDoesNotExist


def dummy_check(*awargs, **kwargs):
	return True, None


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


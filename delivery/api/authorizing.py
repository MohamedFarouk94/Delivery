from database.models import Item, Person
from django.http import HttpResponseForbidden


def dummy_authorize(*args, **kwargs):
	return True, None


def editItem(request, **kwargs):
	# Checking sender is the item owner
	item = Item.objects.get(id=kwargs['id'])
	if request.user != item.seller.user:
		return False, HttpResponseForbidden('{"details": "Sender is not the item owner"}')

	return True, None


def addItem(request, **kwargs):
	# Checking sender is a seller
	if Person.objects.get(user=request.user).category != 'Seller':
		return False, HttpResponseForbidden('{"details": "Sender is not a seller"}')

	return True, None


def deleteItem(request, **kwargs):
	# Checking sender is the item owner
	item = Item.objects.get(id=kwargs['id'])
	if request.user != item.seller.user:
		return False, HttpResponseForbidden('{"details": "Sender is not the item owner"}')

	return True, None


def setImage(request, **kwargs):
	# Checking sender is the item owner
	item = Item.objects.get(id=kwargs['id'])
	if request.user != item.seller.user:
		return False, HttpResponseForbidden('{"details": "Sender is not the item owner"}')

	return True, None

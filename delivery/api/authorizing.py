from database.models import Item, Person
from django.http import HttpResponseForbidden


# General Requests

def helloWorld(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def test(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def whoAmI(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def getItems(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def getItem(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def getImage(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def getSellers(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def getSeller(request, **kwargs):
	# There's no need to authorize anything
	return True, None


def getSellerItems(request, **kwargs):
	# There's no need to authorize anything
	return True, None


# Seller Requests

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

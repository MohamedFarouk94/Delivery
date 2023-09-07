from database.models import Person, Item, Seller
from rest_framework.response import Response
from django.http import HttpResponseBadRequest
from django.core.exceptions import ValidationError


# General Requests

def helloWorld(request, **kwargs):
	return True, Response({'first-word': 'Hello,', 'second-word': 'world!'})


def test(request, **kwargs):
	return True, HttpResponseBadRequest('{"details": "Your request is bad"}')


def whoAmI(request, **kwargs):
	return True, Response(Person.objects.get(user=request.user).to_dict())


def getItems(request, **kwargs):
	return True, Response([item.to_dict() for item in Item.objects.all()])


def getItem(request, **kwargs):
	return True, Response(Item.objects.get(id=kwargs['id']).to_dict())


def getImage(request, **kwargs):
	return True, Response({'b64imd': Item.objects.get(id=kwargs['id']).get_b64img().decode()})


def getSellers(request, **kwargs):
	return True, Response([seller.to_dict() for seller in Seller.objects.all()])


def getSeller(request, **kwargs):
	return True, Response(Seller.objects.get(id=kwargs['id']).to_dict())


def getSellerItems(request, **kwargs):
	return True, Response([item.to_dict() for item in Seller.objects.get(id=kwargs['id']).get_items(Item)])


# Seller Requests

def editItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	try:
		item.edit(**request.data)
		return True, Response(item.to_dict())
	except ValueError:
		return False, HttpResponseBadRequest('{"details": "Found value error in some attribute"}')
	except ValidationError:
		return False, HttpResponseBadRequest('{"details": "Found invalid value for some attribute"}')
	except Exception:
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def addItem(request, **kwargs):
	try:
		item = Seller.objects.get(user=request.user).add_item(Item, **request.data)
		return True, Response(item.to_dict())
	except ValueError:
		return False, HttpResponseBadRequest('{"details": "Found value error in some attribute"}')
	except ValidationError:
		return False, HttpResponseBadRequest('{"details": "Found invalid value for some attribute"}')
	except Exception:
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def deleteItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	seller = Seller.objects.get(user=request.user)
	try:
		return True, Response(seller.delete_item(item))
	except Exception:
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def setImage(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	try:
		return True, Response(item.set_image(request.data['b64img'].encode('utf-8')))
	except Exception:
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')

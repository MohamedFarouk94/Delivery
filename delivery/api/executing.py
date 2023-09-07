from database.models import Item, Seller
from rest_framework.response import Response
from django.http import HttpResponseBadRequest
from django.core.exceptions import ValidationError


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

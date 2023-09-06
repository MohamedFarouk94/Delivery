from database.models import Item
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

from database.models import Item
from django.http import HttpResponseForbidden


def dummy_authorize(*args, **kwargs):
	return True, None


def editItem(request, **kwargs):
	# Checking sender is the item owner
	item = Item.objects.get(id=kwargs['id'])
	if request.user != item.seller.user:
		return False, HttpResponseForbidden('{"details": "Sender is not the item owner"}')

	return True, None

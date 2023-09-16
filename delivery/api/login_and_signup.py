from database.models import Seller, Customer, Pilot
from django.core.exceptions import ObjectDoesNotExist
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from django.http import HttpResponse, HttpResponseBadRequest, HttpResponseNotFound


CATEGORIES = {'Seller': Seller, 'Customer': Customer, 'Pilot': Pilot}


def login(request):
	try:
		category, username, password = request.data['category'], request.data['username'], request.data['password']
		person = CATEGORIES[category].objects.get(user__username=username)
		assert person.user.check_password(password)
		token = Token.objects.get(user=person.user)
		return Response({"token": token.key})
	except KeyError:
		return HttpResponseBadRequest('{"details": "Not valid request."}')
	except ObjectDoesNotExist:
		return HttpResponseNotFound('{"details": "User not found."}')
	except AssertionError:
		return HttpResponse('{"details": "Password is not correct."}', status=401)

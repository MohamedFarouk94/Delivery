from rest_framework.response import Response
from django.http import HttpResponseForbidden, HttpResponseBadRequest
from rest_framework.decorators import api_view
from database.models import Item, Seller
from django.shortcuts import get_object_or_404


@api_view(['GET'])
def helloWorld(request):
	return Response({'first-word': 'Hello,', 'second-word': 'world!'})


@api_view(['GET'])
def test(request):
	return HttpResponseBadRequest('{"details": "Your request is bad"}')


@api_view(['GET'])
def getItmes(request):
	return Response([item.to_dict() for item in Item.objects.all()])


@api_view(['GET'])
def getItem(request, **kwargs):
	return Response(get_object_or_404(Item, id=kwargs['id']).to_dict())


@api_view((['GET']))
def getSellers(request):
	return Response([seller.to_dict() for seller in Seller.objects.all()])


@api_view((['GET']))
def getSeller(request, **kwargs):
	return Response(get_object_or_404(Seller, id=kwargs['id']).to_dict())


@api_view((['GET']))
def getSellerItems(request, **kwargs):
	return Response([item.to_dict() for item in get_object_or_404(Seller, id=kwargs['id']).get_items(Item)])

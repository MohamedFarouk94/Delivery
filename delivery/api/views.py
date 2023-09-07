from rest_framework.response import Response
from django.http import HttpResponseForbidden, HttpResponseBadRequest
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from database.models import Item, Seller, Person
from django.shortcuts import get_object_or_404
from rest_framework.permissions import IsAuthenticated
from rest_framework.authentication import TokenAuthentication
from .check_auth_exec import check_auth_exec


@api_view(['GET'])
def helloWorld(request):
	return Response({'first-word': 'Hello,', 'second-word': 'world!'})


@api_view(['GET'])
def test(request):
	return HttpResponseBadRequest('{"details": "Your request is bad"}')


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def whoAmI(request):
	return Response(Person.objects.get(user=request.user).to_dict())


@api_view(['GET'])
def getItems(request):
	return Response([item.to_dict() for item in Item.objects.all()])


@api_view(['GET'])
def getItem(request, **kwargs):
	return Response(get_object_or_404(Item, id=kwargs['id']).to_dict())


@api_view((['GET']))
def getImage(request, **kwargs):
	return Response({'b64img': get_object_or_404(Item, id=kwargs['id']).get_b64img().decode()})


@api_view((['GET']))
def getSellers(request):
	return Response([seller.to_dict() for seller in Seller.objects.all()])


@api_view((['GET']))
def getSeller(request, **kwargs):
	return Response(get_object_or_404(Seller, id=kwargs['id']).to_dict())


@api_view((['GET']))
def getSellerItems(request, **kwargs):
	return Response([item.to_dict() for item in get_object_or_404(Seller, id=kwargs['id']).get_items(Item)])


@api_view(['PUT'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def editItem(request, **kwargs):
	return check_auth_exec('editItem', request, **kwargs)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def addItem(request, **kwargs):
	return check_auth_exec('addItem', request, **kwargs)


@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def deleteItem(request, **kwargs):
	return check_auth_exec('deleteItem', request, **kwargs)


@api_view(['PATCH'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def setImage(request, **kwargs):
	return check_auth_exec('setImage', request, **kwargs)

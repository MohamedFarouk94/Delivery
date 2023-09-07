from rest_framework.response import Response
from django.http import HttpResponseForbidden, HttpResponseBadRequest
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from database.models import Item, Seller, Person
from django.shortcuts import get_object_or_404
from rest_framework.permissions import IsAuthenticated
from rest_framework.authentication import TokenAuthentication
from .check_auth_exec import check_auth_exec


# General Requests

@api_view(['GET'])
def helloWorld(request, **kwargs):
	return check_auth_exec('helloWorld', request, **kwargs)


@api_view(['GET'])
def test(request, **kwargs):
	return check_auth_exec('test', request, **kwargs)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def whoAmI(request, **kwargs):
	return check_auth_exec('whoAmI', request, **kwargs)


@api_view(['GET'])
def getItems(request, **kwargs):
	return check_auth_exec('getItems', request, **kwargs)


@api_view(['GET'])
def getItem(request, **kwargs):
	return check_auth_exec('getItem', request, **kwargs)


@api_view((['GET']))
def getImage(request, **kwargs):
	return check_auth_exec('getImage', request, **kwargs)


@api_view((['GET']))
def getSellers(request, **kwargs):
	return check_auth_exec('getSellers', request, **kwargs)


@api_view((['GET']))
def getSeller(request, **kwargs):
	return check_auth_exec('getSeller', request, **kwargs)


@api_view((['GET']))
def getSellerItems(request, **kwargs):
	return check_auth_exec('getSellerItems', request, **kwargs)


# Seller Requests

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

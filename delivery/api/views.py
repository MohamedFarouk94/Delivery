from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.authentication import TokenAuthentication
from . import login_and_signup
from .check_auth_exec import check_auth_exec


# Login and Sign up

@api_view(['GET'])
def login(request, **kwargs):
	return login_and_signup.login(request)


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


@api_view(['GET'])
def getAllItemReviews(request, **kwargs):
	return check_auth_exec('getAllItemReviews', request, **kwargs)


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


@api_view((['GET']))
def getReviewsOfAnItem(request, **kwargs):
	return check_auth_exec('getReviewsOfAnItem', request, **kwargs)


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


# Customer Requests

@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def getBasket(request, **kwargs):
	return check_auth_exec('getBasket', request, **kwargs)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def addToBasket(request, **kwargs):
	return check_auth_exec('addToBasket', request, **kwargs)


@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def removeFromBasket(request, **kwargs):
	return check_auth_exec('removeFromBasket', request, **kwargs)


@api_view(['PATCH'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def editQuantity(request, **kwargs):
	return check_auth_exec('editQuantity', request, **kwargs)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def makeOrder(request, **kwargs):
	return check_auth_exec('makeOrder', request, **kwargs)


@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def cancelOrder(request, **kwargs):
	return check_auth_exec('cancelOrder', request, **kwargs)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def getMyItemReview(request, **kwargs):
	return check_auth_exec('getMyItemReview', request, **kwargs)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def sendItemReview(request, **kwargs):
	return check_auth_exec('sendItemReview', request, **kwargs)


@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def deleteMyItemReview(request, **kwargs):
	return check_auth_exec('deleteMyItemReview', request, **kwargs)


@api_view(['PUT'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def editMyItemReview(request, **kwargs):
	return check_auth_exec('editMyItemReview', request, **kwargs)


# Customer & Pilot Requests

@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def getOrders(request, **kwargs):
	return check_auth_exec('getOrders', request, **kwargs)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def getOrder(request, **kwargs):
	return check_auth_exec('getOrder', request, **kwargs)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def sendOrderReview(request, **kwargs):
	return check_auth_exec('sendOrderReview', request, **kwargs)


# Pilot Requests

@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def getAvailableOrders(request, **kwargs):
	return check_auth_exec('getAvailableOrders', request, **kwargs)


@api_view(['PATCH'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def acceptOrder(request, **kwargs):
	return check_auth_exec('acceptOrder', request, **kwargs)


@api_view(['PATCH'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def dropOrder(request, **kwargs):
	return check_auth_exec('dropOrder', request, **kwargs)


@api_view(['PATCH'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def completeOrder(request, **kwargs):
	return check_auth_exec('completeOrder', request, **kwargs)


@api_view(['PATCH'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def reportOrder(request, **kwargs):
	return check_auth_exec('reportOrder', request, **kwargs)

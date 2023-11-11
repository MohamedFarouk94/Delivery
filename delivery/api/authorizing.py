from database.models import Item, Person, Order
from django.http import HttpResponseForbidden


# utils

def get_sender_category(request):
	return Person.objects.get(user=request.user).category


def assert_sender_is(request, supposed):
	try:
		assert get_sender_category(request) == supposed or get_sender_category(request) in supposed
		return True, None
	except AssertionError:
		return False, HttpResponseForbidden(f'{{"details": "Sender is not {supposed}."}}')


def assert_sender_specific(request, obj, attr):
	try:
		assert getattr(obj, attr).user == request.user
		return True, None
	except AssertionError:
		return False, HttpResponseForbidden(f'{{"details": "Sender is not the {obj.__class__.__name__.lower()} {attr}"}}')


def assert_sender_true(request, f):
	try:
		assert f(request)
		return True, None
	except AssertionError:
		return False, HttpResponseForbidden(f'{{"details": "Sender is not authorized to access this information."}}')


def assert_resource_true(f, **kwargs):
	try:
		assert f(**kwargs)
		return True, None
	except AssertionError:
		return False, HttpResponseForbidden(f'{{"details": "Sender is not authorized to access this information."}}')


# General Requests

def helloWorld(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def test(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def whoAmI(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getItems(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getItem(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getAllItemReviews(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getImage(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getSellers(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getSeller(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getSellerItems(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


def getReviewsOfAnItem(request, **kwargs):
	flag, response = True, None

	# There's no need to authorize anything
	return flag, response


# Seller Requests

def editItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is the item owner
	if flag:
		flag, response = assert_sender_specific(request, item, 'seller')

	return flag, response


def addItem(request, **kwargs):
	flag, response = True, None

	# Checking sender is a seller
	if flag:
		flag, response = assert_sender_is(request, 'Seller')

	return flag, response


def deleteItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is the item owner
	if flag:
		flag, response = assert_sender_specific(request, item, 'seller')

	return flag, response


def setImage(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is the item owner
	if flag:
		flag, response = assert_sender_specific(request, item, 'seller')

	return flag, response


# Customer Requests

def getBasket(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def addToBasket(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def removeFromBasket(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def editQuantity(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def makeOrder(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def cancelOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is the order's customer
	if flag:
		flag, response = assert_sender_specific(request, order, 'customer')

	return flag, response


def getMyItemReview(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def sendItemReview(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def deleteMyItemReview(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


def editMyItemReview(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer
	if flag:
		flag, response = assert_sender_is(request, 'Customer')

	return flag, response


# Customer & Pilot Requests

def getOrders(request, **kwargs):
	flag, response = True, None

	# Checking sender is a customer or a pilot
	if flag:
		flag, response = assert_sender_is(request, ['Customer', 'Pilot'])

	return flag, response


def getOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is related to order
	if flag:
		flag, response = assert_sender_true(request, lambda r: order.know_this_person(Person.objects.get(user=r.user)))

	return flag, response


def getOrderBasket(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is related to order
	if flag:
		flag, response = assert_sender_true(request, lambda r: order.know_this_person(Person.objects.get(user=r.user)))

	return flag, response


def sendOrderReview(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is related to order
	if flag:
		flag, response = assert_sender_true(request, lambda r: order.know_this_person(Person.objects.get(user=r.user)))

	return flag, response


# Pilot Requests

def getAvailableOrders(request, **kwargs):
	flag, response = True, None

	# Checking sender is a pilot
	if flag:
		flag, response = assert_sender_is(request, 'Pilot')

	return flag, response


def acceptOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is a pilot
	if flag:
		flag, response = assert_sender_is(request, 'Pilot')

	# Checking sender is related to order
	if flag:
		flag, response = assert_sender_true(request, lambda r: order.know_this_person(Person.objects.get(user=r.user)))

	return flag, response


def dropOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is a pilot
	if flag:
		flag, response = assert_sender_is(request, 'Pilot')

	# Checking sender is related to order
	if flag:
		flag, response = assert_sender_true(request, lambda r: order.know_this_person(Person.objects.get(user=r.user)))

	return flag, response


def completeOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is a pilot
	if flag:
		flag, response = assert_sender_is(request, 'Pilot')

	# Checking sender is related to order
	if flag:
		flag, response = assert_sender_true(request, lambda r: order.know_this_person(Person.objects.get(user=r.user)))

	return flag, response


def reportOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	flag, response = True, None

	# Checking sender is a pilot
	if flag:
		flag, response = assert_sender_is(request, 'Pilot')

	# Checking sender is related to order
	if flag:
		flag, response = assert_sender_true(request, lambda r: order.know_this_person(Person.objects.get(user=r.user)))

	return flag, response

from database.models import Person, Item, Seller, Customer, Pilot, Order, Review
from rest_framework.response import Response
from django.http import HttpResponseBadRequest
from django.core.exceptions import ValidationError
from database.exceptions import BasketException, OrderException


# utils

def list_to_dict(lst):
	return [o.to_dict() for o in lst]


def img_to_dict(b64img):
	return {'b64img': b64img}


# General Requests

def helloWorld(request, **kwargs):
	try:
		return True, Response({'first-word': 'Hello,', 'second-word': 'world!'})
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def test(request, **kwargs):
	try:
		return True, HttpResponseBadRequest('{"details": "Your request is bad"}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def whoAmI(request, **kwargs):
	try:
		return True, Response(Person.objects.get(user=request.user).to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getItems(request, **kwargs):
	items = Item.objects.all()
	try:
		return True, Response(list_to_dict(items))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	try:
		return True, Response(item.to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getReviewsOfItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	try:
		reviews = item.get_reviews()
		return True, Response(list_to_dict(reviews))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getImage(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	b64img = item.get_b64img().decode()
	try:
		return True, Response(img_to_dict(b64img))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getSellers(request, **kwargs):
	sellers = Seller.objects.all()
	try:
		return True, Response(list_to_dict(sellers))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getSeller(request, **kwargs):
	seller = Seller.objects.get(id=kwargs['id'])
	try:
		return True, Response(seller.to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getSellerItems(request, **kwargs):
	seller = Seller.objects.get(id=kwargs['id'])
	items = seller.get_items()
	try:
		return True, Response(list_to_dict(items))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getItemReviews(request, **kwargs):
	reviews = [review for review in Review.objects.all() if isinstance(review.reviewed, Item)]
	try:
		return True, Response(list_to_dict(reviews))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getItemReview(request, **kwargs):
	review = Review.objects.get(id=kwargs['id'])
	try:
		return True, Response(review.to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


# Seller Requests

def editItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	seller = Seller.objects.get(user=request.user)
	try:
		seller.edit_item(item, **request.data)
		return True, Response(item.to_dict())
	except ValueError:
		return False, HttpResponseBadRequest('{"details": "Found value error in some attribute"}')
	except ValidationError:
		return False, HttpResponseBadRequest('{"details": "Found invalid value for some attribute"}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def addItem(request, **kwargs):
	try:
		item = Seller.objects.get(user=request.user).add_item(**request.data)
		return True, Response(item.to_dict())
	except ValueError:
		return False, HttpResponseBadRequest('{"details": "Found value error in some attribute"}')
	except ValidationError:
		return False, HttpResponseBadRequest('{"details": "Found invalid value for some attribute"}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def deleteItem(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	seller = Seller.objects.get(user=request.user)
	try:
		item = seller.delete_item(item)
		return True, Response(item.to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def setImage(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	seller = Seller.objects.get(user=request.user)
	b64img = request.data['b64img'].encode('utf-8')
	try:
		item = seller.set_image(item, b64img)
		return True, Response(item.to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


# Customer Requests

def getBasket(request, **kwargs):
	customer = Customer.objects.get(user=request.user)
	try:
		return True, Response([basket_item.to_dict() for basket_item in customer.get_basket()])
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def addToBasket(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	customer = Customer.objects.get(user=request.user)
	quantity = request.data['quantity']
	try:
		basket_item = customer.add_to_basket(item, quantity)
		return True, Response(basket_item.to_dict())
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order is not pending"}')
	except BasketException:
		return False, HttpResponseBadRequest('{"details": "Item is already in basket"}')
	except ValidationError:
		return False, HttpResponseBadRequest('{"details": "Found invalid value for quantity"}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def removeFromBasket(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	customer = Customer.objects.get(user=request.user)
	try:
		basket_item = customer.remove_from_basket(item)
		return True, Response(basket_item.to_dict())
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order is not pending"}')
	except BasketException:
		return False, HttpResponseBadRequest('{"details": "Item is not in basket"}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def editQuantity(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	customer = Customer.objects.get(user=request.user)
	quantity = request.data['quantity']
	try:
		basket_item = customer.edit_quantity_of_item(item, quantity)
		return True, Response(basket_item.to_dict())
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order is not pending"}')
	except BasketException:
		return False, HttpResponseBadRequest('{"details": "Item is not in basket"}')
	except ValidationError:
		return False, HttpResponseBadRequest('{"details": "Found invalid value for quantity"}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def makeOrder(request, **kwargs):
	customer = Customer.objects.get(user=request.user)
	region = request.data['region']
	try:
		order = customer.make_order(region)
		return True, Response(order.to_dict())
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order is not pending"}')
	except BasketException:
		return False, HttpResponseBadRequest('{"details": "Empty basket cannot be ordered."}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def cancelOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	try:
		order = order.cancel_order()
		return True, Response(order.to_dict())
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order cannot be canceled."}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def sendItemReview(request, **kwargs):
	item = Item.objects.get(id=kwargs['id'])
	customer = Customer.objects.get(user=request.user)
	rating = request.data['rating']
	text = request.data['text']
	try:
		review = customer.send_item_review(item, rating, text)
		return True, Response(review.to_dict())
	except ValueError:
		return False, HttpResponseBadRequest('{"details": "Found value error in some attribute"}')
	except ValidationError:
		return False, HttpResponseBadRequest('{"details": "Found invalid value for some attribute"}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def deleteItemReview(request, **kwargs):
	review = Review.objects.get(id=kwargs['id'])
	customer = Customer.objects.get(user=request.user)
	try:
		review = customer.delete_item_review(review)
		return True, Response(review.to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


# Customer & Pilot Requests

def getOrders(request, **kwargs):
	person = Person.objects.get(user=request.user)
	try:
		orders = person.get_orders()
		return True, Response(list_to_dict(orders))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def getOrder(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	try:
		return True, Response(order.to_dict())
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def sendOrderReview(request, **kwargs):
	order = Order.objects.get(id=kwargs['id'])
	person = Person.objects.get(user=request.user)
	rating = request.data['rating']
	text = request.data['text']
	try:
		review = person.send_order_review(order, rating, text)
		return True, Response(review.to_dict())
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "This order cannot be reviewed."}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


# Pilot Requests

def getAvailableOrders(request, **kwargs):
	pilot = Pilot.objects.get(user=request.user)
	try:
		orders = pilot.get_available_orders()
		return True, Response(list_to_dict(orders))
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def acceptOrder(request, **kwargs):
	pilot = Pilot.objects.get(user=request.user)
	order = Order.objects.get(id=kwargs['id'])
	try:
		order = pilot.accept_order(order)
		return True, Response(order.to_dict())
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "Pilot cannot take more than one order in a time."}')
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order is already taken."}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def dropOrder(request, **kwargs):
	pilot = Pilot.objects.get(user=request.user)
	order = Order.objects.get(id=kwargs['id'])
	try:
		order = pilot.drop_order(order)
		return True, Response(order.to_dict())
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "Order cannot be dropped by the sender."}')
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order cannot be dropped."}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def completeOrder(request, **kwargs):
	pilot = Pilot.objects.get(user=request.user)
	order = Order.objects.get(id=kwargs['id'])
	try:
		order = pilot.complete_order(order)
		return True, Response(order.to_dict())
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "Order cannot be completed by the sender."}')
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order cannot be completed."}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')


def reportOrder(request, **kwargs):
	pilot = Pilot.objects.get(user=request.user)
	order = Order.objects.get(id=kwargs['id'])
	try:
		order = pilot.report_order(order)
		return True, Response(order.to_dict())
	except AssertionError:
		return False, HttpResponseBadRequest('{"details": "Order cannot be reported by the sender."}')
	except OrderException:
		return False, HttpResponseBadRequest('{"details": "Order cannot be reported."}')
	except Exception as e:
		print('#UNKNOWN ERROR#', str(e))
		return False, HttpResponseBadRequest('{"details": "Something wrong happened"}')

from database.strings import pn
from database.exceptions import OrderException


def get_orders(self, Order):
	return Order.objects.filter(customer=self)


def get_pending_order(self, Order):
	return Order.objects.get(customer=self, status=pn)


def create_pending_order(self, Order):
	if len(Order.objects.filter(customer=self, status=pn)):
		raise OrderException('A pending order for this customer already exists.')

	return Order.objects.create(customer=self)


def get_basket(self, Order, BasketItem):
	return self.get_pending_order(Order).get_basket(BasketItem)


def get_basket_item(self, Order, BasketItem, item):
	return self.get_pending_order(Order).get_basket_item(BasketItem, item)


def edit_quantity_of_item(self, Order, BasketItem, item, quantity):
	self.get_pending_order(Order).edit_quantity_of_item(BasketItem, item, quantity)


def add_to_basket(self, Order, BasketItem, item, quantity):
	return self.get_pending_order(Order).add_to_basket(BasketItem, item, quantity)


def remove_from_basket(self, Order, BasketItem, item):
	item_dict = self.get_basket_item(Order, BasketItem, item).to_dict()
	self.get_pending_order(Order).remove_from_basket(BasketItem, item)
	return item_dict


def make_order(self, Order, BasketItem, region):
	ordered = self.get_pending_order(Order).make_order(BasketItem, region)
	self.create_pending_order(Order)
	return ordered


def send_item_review(self, Review, item, rating, text):
	Review.objects.create(reviewer=self, reviewed=item, rating=rating, text=text)


def send_order_review(self, Review, order, rating, text):
	order.raise_error_if_not_completed()
	Review.objects.create(reviewer=self, reviewed=order, rating=rating, text=text)

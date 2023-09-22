from database.strings import pn
from database.exceptions import OrderException, ReviewException


def get_orders(self):
	Order = self.ORDER
	return Order.objects.filter(customer=self)


def get_pending_order(self):
	Order = self.ORDER
	return Order.objects.get(customer=self, status=pn)


def create_pending_order(self):
	Order = self.ORDER
	if len(Order.objects.filter(customer=self, status=pn)):
		raise OrderException('A pending order for this customer already exists.')

	return Order.objects.create(customer=self)


def get_basket(self):
	return self.get_pending_order().get_basket()


def get_basket_item(self, item):
	return self.get_pending_order().get_basket_item(item)


def edit_quantity_of_item(self, item, quantity):
	return self.get_pending_order().edit_quantity_of_item(item, quantity)


def add_to_basket(self, item, quantity):
	return self.get_pending_order().add_to_basket(item, quantity)


def remove_from_basket(self, item):
	return self.get_pending_order().remove_from_basket(item)


def make_order(self, region):
	ordered = self.get_pending_order().make_order(region)
	self.create_pending_order()
	return ordered


def raise_error_if_item_reviewed(self, item):
	try:
		self.get_item_review(item)
	except ReviewException:
		return
	raise ReviewException("This item is already reviewed by this customer.")


def get_item_review(self, item):
	reviews = item.get_reviews()
	try:
		[review for review in reviews if review.reviewer == self][0]
	except IndexError:
		raise ReviewException('This item is not reviewed by this customer.')


def send_item_review(self, item, rating, text):
	self.raise_error_if_item_reviewed(item)
	Review = self.REVIEW
	return Review.objects.create(reviewer=self, reviewed=item, rating=rating, text=text)


def delete_item_review(self, item):
	review = self.get_item_review(item)
	review.delete()
	return review


def send_order_review(self, order, rating, text):
	order.raise_error_if_not_completed()
	order.raise_error_if_reviewed_by_customer()
	Review = self.REVIEW
	return Review.objects.create(reviewer=self, reviewed=order, rating=rating, text=text)

from database.strings import pn
from database.exceptions import OrderException


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


def get_last_item_review(self, item):
	Review = self.REVIEW

	prev_reviews = [review for review in Review.objects.filter(reviewed_type__model='item')
		if review.reviewer == self
		and review.reviewed == item]

	if not len(prev_reviews):
		return None

	return sorted(prev_reviews, key=lambda x: x.id)[-1]


def get_active_item_review(self, item):
	prev_reviews = [review for review in item.get_reviews()
		if review.reviewer == self
		and review.reviewed == item
		and review.taken_in_calculation]

	assert len(prev_reviews) <= 1

	if not len(prev_reviews):
		return None

	return prev_reviews[0]


def send_item_review(self, item, rating, text):
	Review = self.REVIEW

	active_review = self.get_active_item_review(item)
	if active_review:
		active_review.deactivate()
		item = active_review.reviewed

	return Review.objects.create(reviewer=self, reviewed=item, rating=rating, text=text)


def delete_item_review(self, review):
	review.delete()
	if review.taken_in_calculation:
		last_review = self.get_last_item_review(review.reviewed)
		if last_review:
			last_review.activate()
	return review


def send_order_review(self, order, rating, text):
	order.raise_error_if_not_completed()
	order.raise_error_if_reviewed_by_customer()
	Review = self.REVIEW
	return Review.objects.create(reviewer=self, reviewed=order, rating=rating, text=text)

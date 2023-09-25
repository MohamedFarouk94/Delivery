from database.strings import pn, od, cm, ow, av, cd, pb
from django.utils import timezone as dt
from database.exceptions import BasketException, OrderException


def raise_error_if(self, status):
	if self.status == status:
		raise OrderException(f'This order is already {status}.')


def raise_error_if_not(self, status):
	if self.status != status:
		raise OrderException(f'This order is not {status}.')


def raise_error_if_not_pending(self):
	self.raise_error_if_not(pn)


def raise_error_if_not_completed(self):
	self.raise_error_if_not(cm)


def raise_error_if_on_way(self):
	self.raise_error_if(ow)


def raise_error_if_arrived(self):
	self.raise_error_if(av)


def raise_error_if_completed(self):
	self.raise_error_if(cm)


def raise_error_if_canceled(self):
	self.raise_error_if(cd)


def raise_error_if_problem(self):
	self.raise_error_if(pb)


def raise_error_if_basket_empty(self):
	if not len(self.get_basket()):
		raise BasketException('Empty basket cannot be ordered.')


def raise_error_if_item_exists(self, item):
	BasketItem = self.BASKET_ITEM

	if len(BasketItem.objects.filter(order=self, item=item)):
		raise BasketException('This item is already in the basket.')


def raise_error_if_item_doesnt_exist(self, item):
	BasketItem = self.BASKET_ITEM

	if not len(BasketItem.objects.filter(order=self, item=item)):
		raise BasketException('This item is not in the basket.')


def get_basket_item(self, item):
	BasketItem = self.BASKET_ITEM

	self.raise_error_if_item_doesnt_exist(item)
	return BasketItem.objects.filter(order=self, item=item).first()


def get_basket(self):
	BasketItem = self.BASKET_ITEM

	return BasketItem.objects.filter(order=self)


def get_quantity_of_item(self, item):
	BasketItem = self.BASKET_ITEM

	self.raise_error_if_not_pending()
	try:
		self.raise_error_if_item_doesnt_exist(item)
		return BasketItem.objects.get(order=self, item=item).get_quantity()
	except BasketException:
		return 0


def edit_quantity_of_item(self, item, quantity):
	BasketItem = self.BASKET_ITEM

	self.raise_error_if_not_pending()
	self.raise_error_if_item_doesnt_exist(item)

	return BasketItem.objects.get(order=self, item=item).edit_quantity(quantity)


def add_to_basket(self, item, quantity):
	BasketItem = self.BASKET_ITEM

	self.raise_error_if_not_pending()
	self.raise_error_if_item_exists(item)

	return BasketItem.objects.create(order=self, item=item, quantity=quantity)


def remove_from_basket(self, item):
	BasketItem = self.BASKET_ITEM

	self.raise_error_if_not_pending()
	self.raise_error_if_item_doesnt_exist(item)

	basket_item = BasketItem.objects.get(order=self, item=item)
	basket_item.delete()
	return basket_item


def get_delivery_fee(self):
	return 10


def get_prices_sum(self):
	return sum([basket_item.get_deserved_amount() for basket_item in self.get_basket()])


def get_deserved_amount(self):
	return self.get_prices_sum() + self.get_delivery_fee()


def make_order(self, region):
	self.raise_error_if_not_pending()
	self.raise_error_if_basket_empty()

	self.status = od
	self.region = region
	self.date_ordered = dt.now()
	self.save()

	return self


def cancel_order(self):
	self.raise_error_if_on_way()
	self.raise_error_if_arrived()
	self.raise_error_if_completed()
	self.raise_error_if_problem()
	self.raise_error_if_canceled()

	self.status = cd
	self.date_concluded = dt.now()
	self.save()

	return self


def assign_pilot(self, pilot):
	self.raise_error_if_on_way()
	self.raise_error_if_arrived()
	self.raise_error_if_completed()
	self.raise_error_if_canceled()
	self.raise_error_if_problem()

	self.pilot = pilot
	self.date_pilot_assigned = dt.now()
	self.status = ow

	self.save()
	return self


def remove_pilot(self):
	self.raise_error_if_arrived()
	self.raise_error_if_completed()
	self.raise_error_if_canceled()
	self.raise_error_if_problem()

	self.pilot = None
	self.date_pilot_assigned = None
	self.status = od

	self.save()
	return self


def complete(self):
	self.raise_error_if_completed()
	self.raise_error_if_canceled()
	self.raise_error_if_problem()

	self.date_concluded = dt.now()
	self.status = cm

	self.save()
	return self


def problem(self):
	self.raise_error_if_completed()
	self.raise_error_if_canceled()
	self.raise_error_if_problem()

	self.date_concluded = dt.now()
	self.status = pb

	self.save()
	return self


def update_rating(self, r, from_whom='customer'):
	self.raise_error_if_not_completed()

	to = 'pilot' if from_whom == 'customer' else 'customer'
	return getattr(self, to).update_rating()


def undo_rating(self, r, from_whom='customer'):
	self.raise_error_if_not_completed()

	to = 'pilot' if from_whom == 'customer' else 'customer'
	return getattr(self, to).undo_rating()


def know_this_person(self, person):
	if self.status == od and person.category == 'Pilot' and person.get_region() == self.region:
		return True

	u1 = self.customer.user
	u2 = self.pilot.user if self.pilot else None
	if person.user in [u1, u2]:
		return True

	return False


def is_reviewed_by(self, model):
	Review = self.REVIEW

	order_reviews = Review.objects.filter(reviewed_type__model='order')
	this_order_reviews = [review for review in order_reviews if review.reviewed == self]
	meant_review = [review for review in this_order_reviews if review.reviewer_type.model == model]
	if len(meant_review):
		return True
	return False


def is_reviewed_by_customer(self):
	return self.is_reviewed_by('customer')


def is_reviewed_by_pilot(self):
	return self.is_reviewed_by('pilot')


def raise_error_if_reviewed_by_customer(self):
	if self.is_reviewed_by_customer():
		raise OrderException('This order is already reviewed by a customer.')


def raise_error_if_reviewed_by_pilot(self):
	if self.is_reviewed_by_pilot():
		raise OrderException('This order is already reviewed by a pilot.')


def to_dict(self):
	return {
		'id': self.id,
		'customerId': self.customer.user.id,
		'customerUsername': self.customer.user.username,
		'pilotId': self.pilot.user.id if self.pilot else 'N/A',
		'pilotUsername': self.pilot.user.username if self.pilot else 'N/A',
		'status': self.status,
		'totalPrice': self.get_deserved_amount(),
		'region': self.region if self.region else 'N/A',
		'dateOrdered': self.date_ordered if self.date_ordered else 'N/A',
		'datePilotAssigned': self.date_pilot_assigned if self.date_pilot_assigned else 'N/A',
		'dateConcluded': self.date_concluded if self.date_concluded else 'N/A'
	}

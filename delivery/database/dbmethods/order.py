from database.strings import pn, od
from datetime import datetime as dt


def raise_error_if_not_pending(self):
	if self.status != pn:
		raise AttributeError('This order is already ordered.')


def raise_error_if_item_exists(self, BasketItem, item):
	if len(BasketItem.objects.filter(order=self, item=item)):
		raise AttributeError('This item is already in the basket.')


def raise_error_if_item_doesnt_exist(self, BasketItem, item):
	if not len(BasketItem.objects.filter(order=self, item=item)):
		raise AttributeError('This item is not in the basket.')


def get_basket(self, BasketItem):
	return BasketItem.objects.filter(order=self)


def get_quantity_of_item(self, BasketItem, item):
	self.raise_error_if_not_pending()
	try:
		self.raise_error_if_item_doesnt_exist(item)
		return BasketItem.objects.get(order=self, item=item).get_quantity()
	except AttributeError:
		return 0


def edit_quantity_of_item(self, BasketItem, item, quantity):
	self.raise_error_if_not_pending()
	self.raise_error_if_item_doesnt_exist(item)

	BasketItem.objects.get(order=self, item=item).edit_quantity(quantity)


def add_to_basket(self, BasketItem, item, quantity):
	self.raise_error_if_not_pending()
	self.raise_error_if_item_exists(item)

	return BasketItem.objects.create(order=self, item=item, quantity=quantity)


def remove_from_basket(self, BasketItem, item):
	self.raise_error_if_not_pending()
	self.raise_error_if_item_doesnt_exist(item)

	BasketItem.objects.filter(order=self, item=item).delete()


def make_order(self, region):
	self.raise_error_if_not_pending()

	self.status = od
	self.region = region
	self.date_ordered = dt.now()
	self.save()


def to_dict(self):
	return {
		'id': self.id,
		'customer-id': self.customer.user.id,
		'customer-username': self.customer.user.username,
		'pilot-id': self.pilot.user.id if self.pilot else 'N/A',
		'pilot-username': self.pilot.user.username if self.pilot else 'N/A',
		'status': self.status,
		'date-ordered': self.date_ordered if self.date_ordered else 'N/A',
		'date-pilot-assigned': self.date_pilot_assigned if self.date_pilot_assigned else 'N/A',
		'date-completed': self.date_completed if self.date_completed else 'N/A'
	}

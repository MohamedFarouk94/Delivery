from database.strings import pn, od, cm
from datetime import datetime as dt
from database.exceptions import BasketException, OrderException


def raise_error_if_not_pending(self):
	if self.status != pn:
		raise OrderException('This order is already ordered.')


def raise_error_if_not_completed(self):
	if self.status != cm:
		raise OrderException('This order in not completed.')


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


def make_order(self, region):
	self.raise_error_if_not_pending()
	self.raise_error_if_basket_empty()

	self.status = od
	self.region = region
	self.date_ordered = dt.now()
	self.save()

	return self


def to_dict(self):
	return {
		'id': self.id,
		'customer-id': self.customer.user.id,
		'customer-username': self.customer.user.username,
		'pilot-id': self.pilot.user.id if self.pilot else 'N/A',
		'pilot-username': self.pilot.user.username if self.pilot else 'N/A',
		'status': self.status,
		'region': self.region if self.region else 'N/A',
		'date-ordered': self.date_ordered if self.date_ordered else 'N/A',
		'date-pilot-assigned': self.date_pilot_assigned if self.date_pilot_assigned else 'N/A',
		'date-completed': self.date_completed if self.date_completed else 'N/A'
	}

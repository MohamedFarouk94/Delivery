from database.strings import pn


def get_orders(self, Order):
	return Order.objects.filter(customer=self)


def get_pending_order(self, Order):
	return Order.objects.get(customer=self, status=pn)


def create_pending_order(self, Order):
	if len(Order.objects.filter(customer=self, status=pn)):
		raise AttributeError('A pending order for this customer already exists.')

	return Order.objects.create(customer=self)


def get_basket(self):
	return self.get_pending_order().get_basket()


def edit_quantity_of_item(self, item, quantity):
	self.get_pending_order().edit_quantity_of_item(item, quantity)


def add_to_basket(self, item, quantity):
	return self.get_pending_order().add_to_basket(item, quantity)


def remove_from_basket(self, item):
	self.get_pending_order().remove_from_basket(item)


def make_order(self, Order, region):
	self.get_pending_order().make_order(region)
	self.create_pending_order(Order)

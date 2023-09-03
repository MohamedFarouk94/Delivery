def get_quantity(self):
	return self.quantity


def edit_quantity(self, quantity):
	self.quantity = quantity


def to_dict(self):
	return {
		'id': self.id,
		'order-id': self.order.id,
		'item-id': self.item.id,
		'quantity': self.quantity
	}

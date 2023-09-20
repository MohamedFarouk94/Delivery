def save(self, *args, **kwargs):
	self.full_clean()  # This will trigger the validation errors
	super(self.__class__, self).save(*args, **kwargs)


def get_quantity(self):
	return self.quantity


def edit_quantity(self, quantity):
	self.quantity = quantity
	self.save()
	return self


def get_deserved_amount(self):
	return self.quantity * self.item.price


def to_dict(self):
	return {
		'id': self.id,
		'customerId': self.order.customer.user.id,
		'customerUsername': self.order.customer.user.username,
		'orderId': self.order.id,
		'itemId': self.item.id,
		'itemName': self.item.name,
		'quantity': self.quantity,
		'totalPrice': self.get_deserved_amount()
	}

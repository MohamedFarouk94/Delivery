def save(self, *args, **kwargs):
	self.full_clean()  # This will trigger the validation errors
	super(self.__class__, self).save(*args, **kwargs)


def get_quantity(self):
	return self.quantity


def edit_quantity(self, quantity):
	self.quantity = quantity
	self.save()


def to_dict(self):
	return {
		'id': self.id,
		'customer-id': self.order.customer.user.id,
		'customer-username': self.order.customer.user.username,
		'order-id': self.order.id,
		'item-id': self.item.id,
		'item-name': self.item.name,
		'quantity': self.quantity,
		'total-price': self.quantity * self.item.price
	}

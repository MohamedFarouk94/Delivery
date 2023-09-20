# methods for class Person in database/models.py

def get_region(self):
	if self.category != 'Pilot':
		raise AttributeError('This person has no region attribute')

	return self.CHILDREN['Pilot'].objects.get(user=self.user).region


def get_orders(self):
	Order = self.ORDER

	if self.category == 'Seller':
		raise AttributeError('Seller object has no attribute orders.')

	category = self.category.lower()
	kwargs = {category: self}
	return Order.objects.filter(**kwargs)


def send_order_review(self, order, rating, text):
	if self.category == 'Seller':
		raise AttributeError('Seller object has no attribute orders.')

	pilot_or_customer = self.CHILDREN[self.category].objects.get(user=self.user)
	return pilot_or_customer.send_order_review(order, rating, text)


def to_dict(self):
	return {
		'id': self.user.id,
		'category': self.category,
		'firstName': self.user.first_name,
		'lastName': self.user.last_name,
		'username': self.user.username,
		'status': self.status,
		'email': self.user.email,
		'dateJoined': self.user.date_joined
	}

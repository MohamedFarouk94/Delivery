# methods for class Person in database/models.py

def get_orders(self):
	Order = self.ORDER

	if self.category == 'Seller':
		raise AttributeError('Seller object has no attribute orders.')

	category = self.category.lower()
	kwargs = {category: self}
	return Order.objects.filter(**kwargs)


def to_dict(self):
	return {
		'id': self.user.id,
		'category': self.category,
		'first-name': self.user.first_name,
		'last-name': self.user.last_name,
		'username': self.user.username,
		'status': self.status,
		'email': self.user.email,
		'date-joined': self.user.date_joined
	}

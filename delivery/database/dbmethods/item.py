def save(self, *args, **kwargs):
	self.full_clean()  # This will trigger the validation errors
	super(self.__class__, self).save(*args, **kwargs)


def edit(self, **kwargs):
	# This is a very dangerous method
	# Do all the needed safety actions before calling
	# Assure that all kwargs are in self.__class__.editable_attributes
	self.__dict__.update(**kwargs)
	self.save()
	return self


def get_reviews(self):
	Review = self.REVIEW

	return [review for review in Review.objects.filter(reviewed_type__model='item') if review.reviewed == self]


def to_dict(self):
	return {
		'id': self.id,
		'name': self.name,
		'sellerId': self.seller.user.id,
		'sellerUsername': self.seller.user.username,
		'description': self.description,
		'price': self.price,
		'image': self.image.url,
		'rating': self.rating,
		'numberOfRaters': self.n_raters,
		'numberOfOrders': self.n_orders,
		'numberOfBuyouts': self.n_buyouts,
	}

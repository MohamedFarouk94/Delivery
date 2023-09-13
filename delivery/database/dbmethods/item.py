import matplotlib.pyplot as plt
from PIL import Image
import base64


def save(self, *args, **kwargs):
	self.full_clean()  # This will trigger the validation errors
	super(self.__class__, self).save(*args, **kwargs)


def get_b64img(self):
	with open(self.image.path, 'rb') as img:
		b64img = base64.encodebytes(img.read())
	return b64img


def assign_image(self):
	self.image = f"{self.seller.user.username}/{self.name.lower().replace(' ', '_')}.jpg"
	self.save()


def set_image(self, b64img):
	img = open(f"images/{self.seller.user.username}/{self.name.lower().replace(' ', '_')}.jpg", 'wb')
	img.write(base64.decodebytes(b64img))
	self.assign_image()
	return self


def display_image(self):
	plt.imshow(Image.open(self.image.path))
	plt.title(self.name)
	plt.show()


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
		'seller-id': self.seller.user.id,
		'seller-username': self.seller.user.username,
		'description': self.description,
		'price': self.price,
		'image': self.image.url,
		'rating': self.rating,
		'number-of-raters': self.n_raters,
		'number-of-orders': self.n_orders,
		'number-of-buyouts': self.n_buyouts,
	}

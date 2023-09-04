import matplotlib.pyplot as plt
from PIL import Image


def display_image(self):
	plt.imshow(Image.open(self.image.path))
	plt.title(self.name)
	plt.show()


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

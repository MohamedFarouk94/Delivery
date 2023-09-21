import matplotlib.pyplot as plt
from PIL import Image
import base64


def get_b64img(self):
	with open(self.image.path, 'rb') as img:
		b64img = base64.encodebytes(img.read())
	return b64img


def assign_image(self):
	try:
		self.image = f"{self.seller.user.username}/{self.name.lower().replace(' ', '_')}.jpg"
	except AttributeError:
		self.image = f"{self.seller.user.username}/{self.user.username}.jpg"
	self.save()


def set_image(self, b64img):
	try:
		img = open(f"images/{self.seller.user.username}/{self.name.lower().replace(' ', '_')}.jpg", 'wb')
	except AttributeError:
		img = open(f"images/{self.seller.user.username}/{self.user.username}.jpg", 'wb')
	img.write(base64.decodebytes(b64img))
	self.assign_image()
	return self


def display_image(self):
	plt.imshow(Image.open(self.image.path))
	try:
		plt.title(self.name)
	except AttributeError:
		plt.title(self.user.username)
	plt.show()

def get_items(self):
	Item = self.ITEM
	return Item.objects.filter(seller=self)


def add_item(self, **kwargs):
	# This is a very dangerous method
	# Do all the needed safety actions before calling
	# Assure that all kwargs are in self.__class__.editable_attributes
	Item = self.ITEM
	return Item.objects.create(seller=self, **kwargs)


def edit_item(self, item, **kwargs):
	# This is a very dangerous method
	# Do all the needed safety actions before calling
	# Assure that all kwargs are in self.__class__.editable_attributes
	assert item.seller == self
	return item.edit(**kwargs)


def set_item_image(self, item, b64img):
	assert item.seller == self
	return item.set_image(b64img)


def delete_item(self, item):
	assert item.seller == self
	item.delete()
	return item


def to_dict(self):
	super_dict = super(self.__class__, self).to_dict()
	super_dict['image'] = self.get_b64img()
	return super_dict

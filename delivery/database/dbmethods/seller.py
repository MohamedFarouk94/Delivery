def get_items(self, Item):
	return Item.objects.filter(seller=self)


def add_item(self, Item, **kwargs):
	# This is a very dangerous method
	# Do all the needed safety actions before calling
	# Assure that all kwargs are in self.__class__.editable_attributes
	return Item.objects.create(seller=self, **kwargs)


def delete_item(self, item):
	assert item.seller == self
	item_dict = item.to_dict()
	item.delete()
	return item_dict

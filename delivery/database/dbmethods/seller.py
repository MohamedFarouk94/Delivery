def get_items(self, Item):
	return Item.objects.filter(seller=self)

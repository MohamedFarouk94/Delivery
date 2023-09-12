from database.strings import od, ow


def get_available_orders(self):
	Order = self.ORDER
	return Order.objects.filter(region=self.region, status=od)


def accept_order(self, order):
	assert not self.busy

	order.assign_pilot(self)
	self.busy = True
	self.save()
	return order


def drop_order(self, order):
	assert order.pilot == self
	assert order.status == ow

	order.remove_pilot()
	self.busy = False
	self.n_drops += 1
	self.save()
	return order


def complete_order(self, order):
	assert order.pilot == self
	assert order.status == ow

	order.complete()
	self.busy = False
	self.save()
	return order


def report_order(self, order):
	assert order.pilot == self
	assert order.status == ow

	order.problem()
	self.busy = False
	self.save()
	return order


def to_dict(self):
	super_dict = super(self.__class__, self).to_dict()
	super_dict['region'] = self.region
	return super_dict

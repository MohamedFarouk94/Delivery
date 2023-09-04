def to_dict(self):
	super_dict = super(self.__class__, self).to_dict()
	super_dict['region'] = self.region
	return super_dict

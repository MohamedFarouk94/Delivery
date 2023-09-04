def to_dict(self):
	super_dict = super().to_dict()
	super_dict['region'] = self.region
	return super_dict

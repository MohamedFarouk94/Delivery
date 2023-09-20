def save(self, *args, **kwargs):
	self.full_clean()
	if self.taken_in_calculation:
		self.reviewed.update_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	super(self.__class__, self).save(*args, **kwargs)


def delete(self, *args, **kwargs):
	if self.taken_in_calculation:
		self.reviewed.undo_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	super(self.__class__, self).delete(*args, **kwargs)


def deactivate(self):
	assert self.taken_in_calculation
	self.taken_in_calculation = False
	self.reviewed.undo_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	self.save()
	return self


def activate(self):
	assert not self.taken_in_calculation
	self.taken_in_calculation = True
	self.reviewed.update_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	self.save()
	return self


def to_dict(self):
	return {
		'id': self.id,
		'dateCreated': self.date_created,
		'reviewerType': self.reviewer.__class__.__name__,
		'reviewedType': self.reviewed.__class__.__name__,
		'reviewerId': self.reviewer.user.id,
		'reviewedId': self.reviewed.pk,
		'takenInCalculation': self.taken_in_calculation,
		'rating': self.rating,
		'text': self.text
	}

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
	self.taken_in_calculation = False
	self.reviewed.undo_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	self.save()
	return self


def to_dict(self):
	return {
		'id': self.id,
		'date-created': self.date_created,
		'reviewer-type': self.reviewer.__class__.__name__,
		'reviewed-type': self.reviewed.__class__.__name__,
		'reviewer-id': self.reviewer.user.id,
		'reviewed-id': self.reviewed.pk,
		'taken-in-calculation': self.taken_in_calculation,
		'rating': self.rating,
		'text': self.text
	}

def save(self, *args, **kwargs):
	self.reviewed.update_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	super(self.__class__, self).save(*args, **kwargs)


def delete(self, *args, **kwargs):
	self.reviewed.undo_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	super(self.__class__, self).delete(*args, **kwargs)


def to_dict(self):
	return {
		'id': self.id,
		'reviewer-type': self.reviewer.__class__.__name__,
		'reviewed-type': self.reviewed.__class__.__name__,
		'reviewer-id': self.reviewer.user.id,
		'reviewed-id': self.reviewed.pk,
		'rating': self.rating,
		'text': self.text
	}

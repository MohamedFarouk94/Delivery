def save(self, *args, **kwargs):
	self.full_clean()
	self.reviewed.update_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	super(self.__class__, self).save(*args, **kwargs)


def delete(self, *args, **kwargs):
	self.reviewed.undo_rating(self.rating, from_whom=self.reviewer.__class__.__name__.lower())
	super(self.__class__, self).delete(*args, **kwargs)


def to_dict(self):
	return {
		'id': self.id if self.id else 0,
		'dateCreated': self.date_created.strftime("%d/%m/%Y %H:%M"),
		'reviewerType': self.reviewer.__class__.__name__,
		'reviewedType': self.reviewed.__class__.__name__,
		'reviewerId': self.reviewer.user.id,
		'reviewedId': self.reviewed.pk,
		'reviewerUsername': self.reviewer.user.username,
		'rating': self.rating,
		'text': self.text
	}

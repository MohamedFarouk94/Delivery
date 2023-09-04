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

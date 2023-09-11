def update_rating(self, r, **kwargs):
	if not self.n_raters:
		self.rating = r
		self.n_raters = 1
		self.save()
		return self

	R_old = self.rating
	n = self.n_raters

	R_new = (n * R_old + r) / (n + 1)

	self.rating = R_new
	self.n_raters += 1

	self.save()
	return self


def undo_rating(self, r, **kwargs):
	if self.n_raters == 1:
		self.rating = None
		self.n_raters = 0
		self.save()
		return self

	R_new = self.rating
	n = self.n_raters

	R_old = (n * R_new - r) / (n - 1)

	self.rating = R_old
	self.n_raters -= 1

	self.save()
	return self

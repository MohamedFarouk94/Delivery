class BasketException(Exception):
	def __init__(self, message, errors):
		super().__init__(message)
		self.errors.errors


class OrderException(Exception):
	def __init__(self, message, errors):
		super().__init__(message)
		self.errors.errors

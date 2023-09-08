class BasketException(Exception):
	def __init__(self, message):
		super().__init__(message)


class OrderException(Exception):
	def __init__(self, message):
		super().__init__(message)

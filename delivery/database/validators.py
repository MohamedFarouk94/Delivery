from django.core.exceptions import ValidationError


def price_validator(price):
	if price <= 0:
		print(price, "#####")
		raise ValidationError('price should be strickly greater than zero.')


def rating_validator(rating):
	if rating not in [1, 2, 3, 4, 5]:
		raise ValidationError('rating should be an integer in range [1,5]')

from django.urls import path  # , re_path
from . import views
# from rest_framework.authtoken.views import obtain_auth_token


urlpatterns = [
	path('login', views.login, name='login'),

	path('', views.helloWorld, name='helloWorld'),
	path('test', views.test, name='test'),
	path('who-am-i', views.whoAmI, name='whoAmI'),
	path('items', views.getItems, name='getItems'),
	path('items/<int:id>', views.getItem, name='getItem'),
	path('items/<int:id>/image', views.getImage, name='getImage'),
	path('items/<int:id>/reviews', views.getReviewsOfItem, name='getReviewsOfItem'),
	path('sellers', views.getSellers, name='getSellers'),
	path('sellers/<int:id>', views.getSeller, name='getSeller'),
	path('sellers/<int:id>/items', views.getSellerItems, name='getSellerItems'),
	path('item-reviews', views.getItemReviews, name='getItemReviews'),
	path('item-reviews/<int:id>', views.getItemReview, name='getItemReview'),

	path('items/<int:id>/edit', views.editItem, name='editItem'),
	path('items/add', views.addItem, name='addItem'),
	path('items/<int:id>/delete', views.deleteItem, name='deleteItem'),
	path('items/<int:id>/set-image', views.setImage, name='setImage'),

	path('basket', views.getBasket, name='getBasket'),
	path('items/<int:id>/add-to-basket', views.addToBasket, name='addToBasket'),
	path('items/<int:id>/remove-from-basket', views.removeFromBasket, name='removeFromBasket'),
	path('items/<int:id>/edit-quantity', views.editQuantity, name='editQuantity'),
	path('make-order', views.makeOrder, name='makeOrder'),
	path('orders/<int:id>/cancel', views.cancelOrder, name='cancelOrder'),
	path('items/<int:id>/send-review', views.sendItemReview, name='sendItemReview'),
	path('item-reviews/<int:id>/delete', views.deleteItemReview, name='deleteItemReview'),



	path('orders', views.getOrders, name='getOrders'),
	path('orders/<int:id>', views.getOrder, name='getOrder'),
	path('orders/<int:id>/send-review', views.sendOrderReview, name='sendOrderReview'),

	path('available-orders', views.getAvailableOrders, name='getAvailableOrders'),
	path('orders/<int:id>/accept', views.acceptOrder, name='acceptOrder'),
	path('orders/<int:id>/drop', views.dropOrder, name='dropOrder'),
	path('orders/<int:id>/complete', views.completeOrder, name='completeOrder'),
	path('orders/<int:id>/report', views.reportOrder, name='reportOrder'),
]

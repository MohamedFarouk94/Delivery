from django.urls import path  # , re_path
from . import views
# from rest_framework.authtoken.views import obtain_auth_token


urlpatterns = [
	path('', views.helloWorld, name='helloWorld'),
	path('test', views.test, name='test'),
	path('who-am-i', views.whoAmI, name='whoAmI'),
	path('items', views.getItems, name='getItems'),
	path('items/<int:id>', views.getItem, name='getItem'),
	path('items/<int:id>/image', views.getImage, name='getImage'),
	path('sellers', views.getSellers, name='getSellers'),
	path('sellers/<int:id>', views.getSeller, name='getSeller'),
	path('sellers/<int:id>/items', views.getSellerItems, name='getSellerItems'),
	path('items/<int:id>/edit', views.editItem, name='editItem'),
	path('items/add', views.addItem, name='addItem'),
	path('items/<int:id>/delete', views.deleteItem, name='deleteItem'),
	path('items/<int:id>/set-image', views.setImage, name='setImage'),
]

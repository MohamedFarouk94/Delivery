from django.urls import path  # , re_path
from . import views
# from rest_framework.authtoken.views import obtain_auth_token


urlpatterns = [
	path('', views.helloWorld, name='helloWorld'),
	path('test', views.test, name='test'),
	path('who-am-i', views.whoAmI, name='whoAmI'),
	path('items', views.getItmes, name='getItems'),
	path('items/<int:id>', views.getItem, name='getItem'),
	path('sellers', views.getSellers, name='getSellers'),
	path('sellers/<int:id>', views.getSeller, name='getSeller'),
	path('sellers/<int:id>/items', views.getSellerItems, name='getSellerItems'),
	path('items/<int:id>/edit', views.editItem, name='editItem'),
]

from django.urls import path  # , re_path
from . import views
# from rest_framework.authtoken.views import obtain_auth_token


urlpatterns = [
	path('', views.helloWorld, name='helloWorld'),
	path('items', views.getItmes, name='getItems'),
	path('items/<int:id>', views.getItem, name='getItem')
]

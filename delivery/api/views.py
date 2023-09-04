from rest_framework.response import Response
from rest_framework.decorators import api_view
from database.models import Item
from django.shortcuts import get_object_or_404


@api_view(['GET'])
def helloWorld(request):
	return Response({'first-word': 'Hello,', 'second-word': 'world!'})


@api_view(['GET'])
def getItmes(request):
	return Response([item.to_dict() for item in Item.objects.all()])


@api_view(['GET'])
def getItem(request, **kwargs):
	return Response(get_object_or_404(Item, id=kwargs['id']).to_dict())

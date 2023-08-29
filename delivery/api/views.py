from rest_framework.response import Response
from rest_framework.decorators import api_view


@api_view(['GET'])
def helloWorld(request):
	return Response({'first-word': 'Hello,', 'second-word': 'world!'})

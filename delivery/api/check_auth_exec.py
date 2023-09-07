from . import checking, authorizing, executing


def check_auth_exec(fname, request, **kwargs):
	fcheck, fauth, fexec = [getattr(o, fname) for o in [checking, authorizing, executing]]
	flag, response = True, None

	if flag:
		flag, response = fcheck(request, **kwargs)
	if flag:
		flag, response = fauth(request, **kwargs)
	if flag:
		flag, response = fexec(request, **kwargs)

	return response

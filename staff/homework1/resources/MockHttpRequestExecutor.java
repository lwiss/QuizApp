public class MockHttpRequestExecutor extends HttpRequestExecutor {

	@Override
	public HttpResponse execute(
            final HttpRequest request,
            final HttpClientConnection conn,
            final HttpContext context) 
                throws IOException, HttpException {
        HttpResponse response = null;  // Do some fancy stuff here
    	return response;
    }
}

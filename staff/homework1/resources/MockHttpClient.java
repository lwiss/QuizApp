/** To use this, call SwengHttpClientFactory.setInstance(new MockHttpClient())
 * in your testing code. Remember that the app always has to use
 * SwengHttpClientFactory.getInstance() if it needs an HttpClient. */
public class MockHttpClient extends DefaultHttpClient {
    @Override
    protected HttpRequestExecutor createRequestExecutor() {
        return new MockHttpRequestExecutor();
    }
}

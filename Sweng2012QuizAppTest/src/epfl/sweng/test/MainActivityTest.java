package epfl.sweng.test;

import java.io.IOException;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import com.jayway.android.robotium.solo.Solo;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * 
 * @author Eagles
 * 
 */
public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private static final int TIME = 5000;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
	}

	protected void testAthentication() {
		assertTrue("EditText for username",
				solo.searchText("GASPAR Username"));
		assertTrue("EditText for password",
				solo.searchText("GASPAR Password"));
		assertTrue("EditText for log inbutton",
				solo.searchText("Log in using Tequila"));
		solo.clickOnText("Log in using Tequila");
	}

	/**
	 * To use this, call SwengHttpClientFactory.setInstance(new
	 * MockHttpClient()) in your testing code. Remember that the app always has
	 * to use SwengHttpClientFactory.getInstance() if it needs an HttpClient.
	 */
	public class MockHttpClient extends DefaultHttpClient {
		@Override
		protected HttpRequestExecutor createRequestExecutor() {
			return new MockHttpRequestExecutor();
		}
	}

	/**
	 * 
	 * @author crazybhy
	 * 
	 */
	public class MockHttpRequestExecutor extends HttpRequestExecutor {
		@Override
		public HttpResponse execute(final HttpRequest request,
				final HttpClientConnection conn, final HttpContext context)
			throws IOException, HttpException {
			HttpResponse response = null;
			if (request.getRequestLine().getUri().toString().equals("login")) {
				if (request.getRequestLine().getMethod().equals("POST")) {
					final int statusOk = 200;
					response = new BasicHttpResponse(new BasicStatusLine(
							HttpVersion.HTTP_1_1, statusOk, "OK"));
					response.setEntity(new StringEntity(
							"{"
									+ "\"session\": \"test\","
									+ "\"message\":"
									+ "\"Here's your session id."
									+ "Please include the following HTTP header in your subsequent requests:\n"
									+ "Authorization: Tequila test\"" + "}"));
					response.setHeader("Content-type",
							"application/json;charset=utf-8");
				} else {
					String token = "token_test";
					final int statusOk = 200;
					response = new BasicHttpResponse(new BasicStatusLine(
							HttpVersion.HTTP_1_1, statusOk, "OK"));
					response.setEntity(new StringEntity("{" + " \"token\":"
							+ token + " }"));
					response.setHeader("Content-type",
							"application/json;charset=utf-8");
				}
			} else if (request.getRequestLine().getUri().toString()
					.equals("cgi-bin/tequila/login")) {
				final int statusOk = 302;
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusOk, "OK"));
			}
			return response;
		}

	}
}

package epfl.sweng.test;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.RedirectHandler;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;

/**
 * 
 * @author crazybhy
 * 
 */
public class AuthenticationTest extends
		ActivityInstrumentationTestCase2<AuthenticationActivity> {
	private Solo solo;
	private static final int TIME = 1000;

	public AuthenticationTest() {
		super(AuthenticationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
	}

	public void testAuthenticationActivity() {
		solo.clickOnText("GASPAR Username");
		solo.clickOnText("GASPAR Password");
		Button login = solo.getButton("Log in using Tequila");
		assertTrue("Buton enabled", login.isEnabled());
	}

	public void testAuthenticationOK() {
		solo.clearEditText(0);
		solo.enterText(0, "username");
		solo.clearEditText(1);
		solo.enterText(1, "password");
		solo.clickOnText("Log in using Tequila");		
	}

	/**
	 * To use this, call SwengHttpClientFactory.setInstance(new
	 * MockHttpClient()) in your testing code. Remember that the app always has
	 * to use SwengHttpClientFactory.getInstance() if it needs an HttpClient.
	 */
	public class MockHttpClient extends DefaultHttpClient {

		final private RedirectHandler fREDIRECTNOFOLLOW = new RedirectHandler() {
			public boolean isRedirectRequested(HttpResponse response,
					HttpContext context) {
				return false;
			}

			public URI getLocationURI(HttpResponse response, HttpContext context)
				throws org.apache.http.ProtocolException {
				return null;
			}
		};

		public MockHttpClient() {
			setRedirectHandler(fREDIRECTNOFOLLOW);
		}

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
			final int statusTequileOK = 200;
			final int statusSwengOK = 302;
			RequestLine requestLine = request.getRequestLine();
			String method = requestLine.getMethod();
			String uri = requestLine.getUri();
			Log.d("Method", method);
			Log.d("Uri", uri);
			if (method.equals("GET") && uri.equals("/login")) {
				Log.d("Message1", "Server send first response");
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusTequileOK, "OK"));
				response.setEntity(new StringEntity("{"
						+ "\"token\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
						+ "\"message\":"
						+ "\"Here's your authentication token."
						+ " Please validate it with Tequila "
						+ "at https://tequila.epfl.ch/cgi-bin/tequila/login\""
						+ " }"));
				response.setHeader("Content-type",
						"/application/json;charset=utf-8");
			} else if (method.equals("POST")
					&& uri.equals("/cgi-bin/tequila/login")) {
				Log.d("Message2", "Server send second response");
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusSwengOK, "Found"));
				response.setHeader("Location", "whatever");
			} else if (method.equals("POST") && uri.equals("/login")) {
				Log.d("Message3", "Server send third response");
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusTequileOK, "OK"));
				response.setEntity(new StringEntity("{"
								+ "\"session\": \"sessionId\","
								+ "\"message\":"
								+ "\"Here's your session id."
								+ "Please include the following HTTP header in your subsequent requests:\n"
								+ "Authorization: Tequila sessionId\""
								+ " }"));
				response.setHeader("Content-type",
						"application/json;charset=utf-8");
			}

			return response;
		}
	}

}

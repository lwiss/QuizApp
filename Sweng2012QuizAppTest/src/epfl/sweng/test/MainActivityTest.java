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

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.test.ActivityInstrumentationTestCase2;

import android.widget.Button;

/**
 * 
 * @author crazybhy
 * 
 */
public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private static final int TIME = 1000;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
	}

	public void testALogIn() {
		assertTrue("username exists", solo.searchText("GASPAR Username"));
		assertTrue("password exists", solo.searchText("GASPAR Password"));
		Button login = solo.getButton("Log in using Tequila");
		assertTrue("Buton enabled", login.isEnabled());
		solo.clearEditText(0);
		solo.enterText(0, "username");
		solo.clearEditText(1);
		solo.enterText(1, "password");
		solo.clickOnText("Log in using Tequila");
		assertTrue(solo.waitForActivity("MainActivity"));

	}

	public void testButton1() {

		solo.assertCurrentActivity("Main Activity", MainActivity.class);
		assertTrue("Show question button exist",
				solo.searchText("Show a random question"));
		/*
		 * solo.clickOnText("Show a random question");
		 * solo.assertCurrentActivity("ShowQuestionsActivity",
		 * ShowQuestionsActivity.class); solo.goBackToActivity("MainActivity");
		 * solo.assertCurrentActivity("MainActivity", MainActivity.class);
		 */
	}

	public void testButton2() {
		assertTrue("Submit quiz button exist",
				solo.searchText("Submit quiz question"));
		solo.clickOnText("Submit quiz question");
		solo.assertCurrentActivity("EditQuestionActivity",
				EditQuestionActivity.class);
		solo.goBackToActivity("MainActivity");
		solo.assertCurrentActivity("MainActivity", MainActivity.class);
	}

	public void testButton3() {
		assertTrue("Take a quizz button exist", solo.searchText("Take a Quiz"));
		/*
		 * solo.clickOnText("Take a Quiz");
		 * solo.assertCurrentActivity("ShowAvailableQuizzesActivity",
		 * ShowAvailableQuizzesActivity.class);
		 * solo.goBackToActivity("MainActivity");
		 * solo.assertCurrentActivity("MainActivity", MainActivity.class);
		 */
	}

	public void testZLogOut() {
		assertTrue("Log out button exist", solo.searchText("Log out"));
		solo.clickOnText("Log out");
		assertTrue("Log in button exist",
				solo.searchText("Log in using Tequila"));
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
			if (method.equals("GET") && uri.equals("/login")) {
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
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusSwengOK, "Found"));
				response.setHeader("Location", "whatever");
			} else if (method.equals("POST") && uri.equals("/login")) {
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusTequileOK, "OK"));
				response.setEntity(new StringEntity(
						"{"
								+ "\"session\": \"sessionId\","
								+ "\"message\":"
								+ "\"Here's your session id."
								+ "Please include the following HTTP header in your subsequent requests:\n"
								+ "Authorization: Tequila sessionId\"" + " }"));
				response.setHeader("Content-type",
						"application/json;charset=utf-8");
			}

			return response;
		}
	}

}

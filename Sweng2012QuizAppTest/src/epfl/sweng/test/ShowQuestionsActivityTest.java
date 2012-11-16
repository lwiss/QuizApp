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
import org.json.JSONException;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * 
 * @author crazybhy
 * 
 */
public class ShowQuestionsActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	private Solo solo;
	private static final int TIME = 1000;

	public ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		Thread.sleep(TIME);
	}

	public void testShowQuestion() throws JSONException {
		assertTrue(
				"Question is displayed",
				solo.searchText("What is the answer to life, the universe and everything?"));
		assertTrue("Correct answer is displayed", solo.searchText("42"));
		assertTrue("Incorrect answer is displayed",
				solo.searchText("24"));

		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}
	
	public void testFalseResponses() {
		solo.clickOnText("24");
		assertTrue("False answer showen as so",
				solo.searchText("24 \u2718"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}

	public void testTrueResponse() {
		solo.clickOnText("42");
		assertTrue("True answer showen as so",
				solo.searchText("42 \u2714"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertTrue("Next question button is enabled",
				nextQuestionButton.isEnabled());
	}

	public void testNoMoreEffects() {
		solo.clickOnText("42");
		solo.clickOnText("24");
		assertFalse("No effect showing false response",
				solo.searchText("\u2718"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertTrue("Next question button is still enabled", nextQuestionButton.isEnabled());
	}
	
	public void testClickButton() {
		solo.clickOnText("42");
		solo.clickOnText("Next question");
		assertTrue(
				"Question is displayed",
				solo.searchText("What is the answer to life, the universe and everything?"));
		assertTrue("Correct answer is displayed", solo.searchText("42"));
		assertTrue("Incorrect answer is displayed",
				solo.searchText("24"));

		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
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
			final int statusOk = 200;
			HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
					HttpVersion.HTTP_1_1, statusOk, "OK"));
			response.setEntity(new StringEntity("{"
							+ " \"question\": \"What is the answer to life, the universe and everything?\","
							+ " \"answers\": [ \"42\", \"24\" ],"
							+ " \"solutionIndex\": 0," + " \"id\": 4243,"
							+ " \"owner\": \"anounymous\","
							+ " \"tags\": [ \"h2g2\", \"trivia\" ]" + " }"));
			response.setHeader("Content-type", "application/json;charset=utf-8");
			return response;
		}
	}
	
}


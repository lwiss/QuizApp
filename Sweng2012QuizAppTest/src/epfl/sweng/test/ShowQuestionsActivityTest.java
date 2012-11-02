package epfl.sweng.test;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.json.JSONException;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * 
 * @author Eagles
 * 
 */
public class ShowQuestionsActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	private Solo solo;
	private String question;
	private List<String> answers;
	private int solution;
	private static final int TIME = 5000;

	public ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SharedPreferences setting = getActivity().getSharedPreferences(
				MainActivity.PREF_NAME, MainActivity.MODE_PRIVATE);
		SharedPreferences.Editor editor = setting.edit();
		editor.putString("SESSION_ID", "test");
		editor.commit();
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
		question = (String) ShowQuestionsActivity.getQuestion().getText();
		answers = ShowQuestionsActivity.getAnswers();
		solution = ShowQuestionsActivity.getSolution();
	}

	public void testShowQuestion() throws JSONException {
		assertTrue("Question is displayed", solo.searchText(question));
		for (int i = 0; i < answers.size(); i++) {
			assertTrue("Answer " + i + " is displayed",
					solo.searchText(answers.get(i)));
		}
		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}

	public void testFalseResponses() {
		for (int i = 0; i < answers.size(); i++) {
			if (i != solution) {
				solo.clickOnText(answers.get(i));
				assertTrue("False answer " + i + " showen as so",
						solo.searchText(answers.get(i) + " \u2718"));
				Button nextQuestionButton = solo.getButton("Next question");
				assertFalse("Next question button is disabled",
						nextQuestionButton.isEnabled());
			}
		}
	}

	public void testTrueResponse() {
		solo.clickOnText(answers.get(solution));
		assertTrue("True answer " + solution + " showen as so",
				solo.searchText(answers.get(solution) + " \u2714"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertTrue("Next question button is enabled",
				nextQuestionButton.isEnabled());
	}

	public void testNoMoreEffects() {
		solo.clickOnText(answers.get(solution));
		for (int i = 0; i < answers.size(); i++) {
			solo.clickOnText(answers.get(i));
			// Button nextQuestionButton = solo.getButton("Next question");
			// assertTrue("Next question button is still enabled",
			// nextQuestionButton.isEnabled());
		}
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
			//final int statusFalse = 201;
			HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
					null, statusOk, null));
			response.setEntity(new StringEntity("{"
						+ " \"question\": \"What is the answer to life, the universe and everything?\","
						+ " \"answers\": [ \"42\", \"24\" ],"
						+ " \"solutionIndex\": 0,"
						+ " \"tags\": [ \"h2g2\", \"trivia\" ]" + " }"));
			response.setHeader("Content-type", "application/json");
			return response;
		}
	}
}

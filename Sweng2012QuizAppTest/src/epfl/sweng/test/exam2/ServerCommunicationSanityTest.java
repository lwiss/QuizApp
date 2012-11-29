package epfl.sweng.test.exam2;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;
import epfl.sweng.test.ShowQuestionsActivityTest.MockHttpRequestExecutor;
import android.test.AndroidTestCase;
/**
 * 
 * @author crazybhy
 *
 */
public class ServerCommunicationSanityTest extends AndroidTestCase {
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
	}
	
	public void testInterfaceImplementation() {
		String className = questionSearch.getClass().getName();
		assertEquals("epfl.sweng.servercomm.search.DefaultQuestionSearchCommunication",
				className);
	}
	
	public void testSearchByOwner() throws CommunicationException {
		List<QuizQuestion> questions = 
				questionSearch.getQuestionsByOwner("joe");
		assertTrue(questions.size()==2);
	}
	
	public void testSearchByTag() throws CommunicationException {
		List<QuizQuestion> questions =
				questionSearch.getQuestionsByTag("tag");
		assertTrue(questions.size()==2);
	}
	
	public void testSearchByInvalidOwner() throws CommunicationException {
		List<QuizQuestion> questions;
		try {
			questions = 
					questionSearch.getQuestionsByOwner("j o e");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			assertTrue(e.getLocalizedMessage().equals("Invalid Characters"));
		}
		try {
			questions = 
					questionSearch.getQuestionsByOwner("");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals("Too long or Empty"));
		}
		try {
			questions = 
					questionSearch.getQuestionsByOwner("abdcdefghijklmnopqrstuvwxyz");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals("Too long or Empty"));
		}
	}
	
	public void testSearchByInvalidTag() throws CommunicationException {
		List<QuizQuestion> questions;
		try {
			questions = 
					questionSearch.getQuestionsByTag("t a g");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			assertTrue(e.getLocalizedMessage().equals("Invalid Characters"));
		}
		try {
			questions = 
					questionSearch.getQuestionsByOwner("");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals("Too long or Empty"));
		}
		try {
			questions = 
					questionSearch.getQuestionsByOwner("abdcdefghijklmnopqrstuvwxyz");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals("Too long or Empty"));
		}
	}
	
	/**
	 * 
	 * @author crazybhy
	 *
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
			final int statusNotFound = 404;
			final int statusError = 303;
			HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
					HttpVersion.HTTP_1_1, statusOk, "OK"));
			response.setHeader("Content-type", "application/json;charset=utf-8");
			response.setEntity(new StringEntity("[{"
					+ " \"question\": \"What is the answer to life, the universe and everything?\","
					+ " \"answers\": [ \"42\", \"24\" ],"
					+ " \"solutionIndex\": 0,"
					+ " \"id\": 4243,"
					+ " \"owner\": \"joe\","
					+ " \"tags\": [ \"tag\"]"
					+ " },"
					+ "{"
					+ " \"question\": \"What is NOT the answer to life, the universe and everything?\","
					+ " \"answers\": [ \"42\", \"24\" ],"
					+ " \"solutionIndex\": 1,"
					+ " \"id\": 4342,"
					+ " \"owner\": \"joe\","
					+ " \"tags\": [ \"tag\"]" + " }]"));
			return response;
		}
	}
	
}

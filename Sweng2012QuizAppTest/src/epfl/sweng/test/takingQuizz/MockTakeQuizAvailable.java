package epfl.sweng.test.takingQuizz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class is responsable to give quizz title and quizz questions to the
 * client
 * 
 * @author MohamedBenArbia
 * 
 */
public class MockTakeQuizAvailable extends DefaultHttpClient {

	private static final String GET_LIST_QUIZZES_URI = "/quizzes";
	private static final String GET_QUIZZ_QUESTION_URI = "/quizzes/2";
	private static final double SCORE = 1.25;
	private final static int OK_STATUS_CODE = 200;
	private final static int QUIZZ_ID = 2;

	@Override
	protected HttpRequestExecutor createRequestExecutor() {
		return new MockHttpRequestExecutor();
	}

	/**
	 * 
	 * @author MohamedBenArbia
	 * 
	 */

	private class MockHttpRequestExecutor extends HttpRequestExecutor {
		@Override
		public HttpResponse execute(final HttpRequest request,
				final HttpClientConnection conn, final HttpContext context)
			throws IOException, HttpException {
			String question1 = "How much is 2 + 2 ?";
			String question2 = "How much is 1 + 1 ?";
			String question3 = "How is the best Team ?";
			List<String> response1 = new ArrayList<String>();
			response1.add("5, for very large values of 2");
			response1.add("4, if you're out of inspiration");
			response1.add("10, for some carefully chosen base");
			List<String> response2 = new ArrayList<String>();
			response2.add("2");
			response2.add("10");
			response2.add("11");
			response2
					.add("It all depends on the semantics of the '+' operator");
			List<String> response3 = new ArrayList<String>();
			response3.add("Eagles");
			response3.add("Basters");
			response3.add("Cats");
			String uri = request.getRequestLine().getUri();
			Log.d("URI ", uri);
			HttpResponse response = null;
			/**
			 * send the available quizz easy quizz
			 */
			if (uri.equals(GET_LIST_QUIZZES_URI)) {
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, OK_STATUS_CODE, "OK"));
				JSONArray jsonArray = new JSONArray();
				JSONObject quizz1 = new JSONObject();
				try {
					quizz1.put("id", QUIZZ_ID);
					quizz1.put("title", "easy quizz");
					jsonArray.put(0, quizz1);
				} catch (JSONException e) {

				}
				response.setEntity(new StringEntity(jsonArray.toString()));
			}
			/**
			 * sends the questions of the easy quizz
			 */
			if (uri.equals(GET_QUIZZ_QUESTION_URI)) {
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, OK_STATUS_CODE, "OK"));
				JSONObject quizz = new JSONObject();
				JSONArray questions = new JSONArray();
				JSONObject quizzquestion1 = new JSONObject();
				JSONObject quizzquestion2 = new JSONObject();
				JSONObject quizzquestion3 = new JSONObject();
				try {
					quizzquestion1.put("question", question1);
					quizzquestion1.put("answers", new JSONArray(response1));
					quizzquestion2.put("question", question2);
					quizzquestion2.put("answers", new JSONArray(response2));
					quizzquestion3.put("question", question3);
					quizzquestion3.put("answers", new JSONArray(response3));

					questions.put(0, quizzquestion1);
					questions.put(1, quizzquestion2);
					questions.put(2, quizzquestion3);

					quizz.put("id", QUIZZ_ID);
					quizz.put("title", "easy quizz");
					quizz.put("questions", questions);

				} catch (JSONException e) {

				}
				response.setEntity(new StringEntity(quizz.toString()));

			}

			if (request.getRequestLine().getMethod().equals("POST")) {
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, OK_STATUS_CODE, "OK"));
				JSONObject json = new JSONObject();
				try {
					json.put("score", SCORE);
				} catch (JSONException e) {
					response.setEntity(new StringEntity(json.toString()));

				}
			}

			return response;
		}
	}

}

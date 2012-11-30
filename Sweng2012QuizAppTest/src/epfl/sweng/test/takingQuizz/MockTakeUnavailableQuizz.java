package epfl.sweng.test.takingQuizz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 * 
 * @author MohamedBenArbia
 * 
 */
public class MockTakeUnavailableQuizz extends DefaultHttpClient {

	private static final String GET_QUIZZ_QUESTION_URI = "/quizzes/2";
	private final static int OK_STATUS_CODE = 200;
	private final static int NOT_FOUND_STATUS_CODE = 404;
	private final static int UNAUTHORIZED_STATUS_CODE = 401;
	private final static int CASE_3 = 3;
	private final static int CASE_4 = 4;
	private final static String EROOR_MESSAGE = "ERROR";
	private final static int NUMBER_OF_TESTS = 5;
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
			String question1 = "How much is 2 + 2 ?jansdjkfnaskjdfnaksjdbfkasbdfk"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj"
					+ "ajsdbfjasbdfjabsdjfbajsdbfjabsdjfbasjdfbjasbdfjabsdfjbasfasdasd"
					+ "asdasdasdasdasfasdfasdfasdfasdfasdfasdfansdjfbajskdbfkajsbdfkjasbdfj";
			List<String> response1 = new ArrayList<String>();
			response1.add("false question");

			String question2 = "question2 malformed response";
			List<String> response2 = new ArrayList<String>();
			response2.add(question1);
			String uri = request.getRequestLine().getUri();
			Log.d("URI ", uri);
			HttpResponse response = null;
			Random random = new Random();
			int i = random.nextInt(NUMBER_OF_TESTS);
			
			/**
			 * sends the questions of the easy quizz
			 */
			if (uri.equals(GET_QUIZZ_QUESTION_URI)) {
                Log.d("Method", request.getRequestLine().getMethod());
				try {
					switch (i) {

						case 0:
							response = new BasicHttpResponse(new BasicStatusLine(
									HttpVersion.HTTP_1_1, OK_STATUS_CODE, "OK"));
							JSONObject json = new JSONObject();
							json.put("id", -1);
							response.setEntity(new StringEntity(json.toString()));
	
							break;
	
						case 1:
							response = new BasicHttpResponse(new BasicStatusLine(
									HttpVersion.HTTP_1_1, NOT_FOUND_STATUS_CODE,
									"NOT_FOUND"));
							response.setEntity(new StringEntity(EROOR_MESSAGE));
							break;
						case 2:
							response = new BasicHttpResponse(new BasicStatusLine(
									HttpVersion.HTTP_1_1, UNAUTHORIZED_STATUS_CODE,
									"UNAUTHORIZED"));
							response.setEntity(new StringEntity(EROOR_MESSAGE));
							break;
						case CASE_3:
							response = new BasicHttpResponse(new BasicStatusLine(
									HttpVersion.HTTP_1_1, OK_STATUS_CODE, "OK"));
							JSONObject jsonQuestion = new JSONObject();
							jsonQuestion.put("id", 1);
							jsonQuestion.put("title", question1);
							response.setEntity(new StringEntity(jsonQuestion
									.toString()));
							break;
						case CASE_4:
							response = new BasicHttpResponse(new BasicStatusLine(
									HttpVersion.HTTP_1_1, OK_STATUS_CODE, "OK"));
							JSONObject quizz = new JSONObject();
							JSONArray questions = new JSONArray();
							JSONObject quizzquestion2 = new JSONObject();
							quizzquestion2.put("question", question2);
							quizzquestion2.put("answers", new JSONArray(response2));
							questions.put(0, quizzquestion2);
							quizz.put("id", QUIZZ_ID);
							quizz.put("title", "easy quizz");
							quizz.put("questions", questions);
							response.setEntity(new StringEntity(quizz.toString()));
							break;
						default:
							break;

					}

				} catch (JSONException e) {

				}

			}
		

			return response;
		}
	}
}

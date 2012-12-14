package epfl.sweng.test.showquestionsactivity;

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
import org.json.JSONObject;

import android.util.Log;

/**
 * To use this, call SwengHttpClientFactory.setInstance(new MockHttpClient()) in
 * your testing code. Remember that the app always has to use
 * SwengHttpClientFactory.getInstance() if it needs an HttpClient.
 */
public class MockHttpClient extends DefaultHttpClient {
	@Override
	protected HttpRequestExecutor createRequestExecutor() {
		return new MockHttpRequestExecutor();
	}

	/**
	 * 
	 * @author wissem
	 * 
	 */
	private class MockHttpRequestExecutor extends HttpRequestExecutor {
		@Override
		public HttpResponse execute(final HttpRequest request,
				final HttpClientConnection conn, final HttpContext context)
			throws IOException, HttpException {
			final int statusOk = 200;
			String uri = request.getRequestLine().getUri();
			String method = request.getRequestLine().getMethod();

			if (method.equals("POST")
					&& uri.equals("/quizquestions/4243/rating")) {
				Log.d("POST", "I?MA HERE");
				HttpResponse response = new BasicHttpResponse(
						new BasicStatusLine(HttpVersion.HTTP_1_1, statusOk,
								"OK"));
				return response;
			}

			else if (method.equals("POST") && uri.equals("/quizquestions")) {
				HttpResponse response = new BasicHttpResponse(
						new BasicStatusLine(HttpVersion.HTTP_1_1, statusOk,
								"OK"));
				response.setEntity(new StringEntity("{"
						+ " \"question\": \"2+2=?\","
						+ " \"answers\": [ \"3\", \"4\" ],"
						+ " \"solutionIndex\": 1," + " \"id\": 4243,"
						+ " \"owner\": \"anounymous\","
						+ " \"tags\": [ \"math\" ]" + " }"));
				response.setHeader("Content-type",
						"application/json;charset=utf-8");
				return response;

			} else if (uri.equals("/quizquestions/random")) {
				HttpResponse response = new BasicHttpResponse(
						new BasicStatusLine(HttpVersion.HTTP_1_1, statusOk,
								"OK"));
				response.setEntity(new StringEntity(
						"{"
								+ " \"question\": \"What is the answer to life, the universe and everything?\","
								+ " \"answers\": [ \"42\", \"24\" ],"
								+ " \"solutionIndex\": 0," + " \"id\": 4243,"
								+ " \"owner\": \"anounymous\","
								+ " \"tags\": [ \"h2g2\", \"trivia\" ]" + " }"));
				response.setHeader("Content-type",
						"application/json;charset=utf-8");
				return response;
			} else if (uri.equals("/quizquestions/4243/ratings")) {
				HttpResponse response = new BasicHttpResponse(
						new BasicStatusLine(HttpVersion.HTTP_1_1, statusOk,
								"OK"));
				response.setEntity(new StringEntity("{" + " \"likeCount\": 10,"
						+ " \"dislikeCount\": 3," + " \"incorrectCount\": 2"
						+ " }"));
				response.setHeader("Content-type",
						"application/json;charset=utf-8");
				return response;

			} else if (uri.equals("/quizquestions/4243/rating")) {
				HttpResponse response = new BasicHttpResponse(
						new BasicStatusLine(HttpVersion.HTTP_1_1, statusOk,
								"OK"));
				response.setEntity(new StringEntity("{"
						+ " \"verdict\": \"like\"" + " }"));
				response.setHeader("Content-type",
						"application/json;charset=utf-8");
				return response;

			}

			return null;
		}
	}
}

package epfl.sweng.test.ShowAvailableQuizzes;

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
import org.json.JSONArray;

import android.util.Log;

/**
 * This class will send an empty list of available quizz
 * 
 * @author MohamedBenArbia
 * 
 */
public class MockNoquizzesAvailable extends DefaultHttpClient {
	private static final String GET_LIST_QUIZZES_URI = "/quizzes";
	private final static int OK_STATUS_CODE = 200;

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
			String uri = request.getRequestLine().getUri();
			Log.d("URI ", uri);
			HttpResponse response = null;
			if (uri.equals(GET_LIST_QUIZZES_URI)) {
				response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, OK_STATUS_CODE, "OK"));
				JSONArray jsonArray = new JSONArray();
				response.setEntity(new StringEntity(jsonArray.toString()));

			}

			return response;
		}
	}

}

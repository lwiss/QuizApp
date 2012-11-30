package epfl.sweng.test.ShowAvailableQuizzes;

import java.io.IOException;
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

import android.util.Log;

/**
 * This class is used to send error status or null response to the client
 * 
 * @author MohamedBenArbia
 * 
 */
public class MockErrorCommunication extends DefaultHttpClient {
	private static final String GET_LIST_QUIZZES_URI = "/quizzes";
	private final static int NOT_FOUND_STATUS = 404;
	private final static int NOT_AUTORIZED_STATUS = 401;
	private final static int NUMBER_POSSIBLE_FALSE_ANSWER = 2;

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
			HttpResponse response = null;
			String uri = request.getRequestLine().getUri();
			if (uri.equals(GET_LIST_QUIZZES_URI)) {
				Random random = new Random();
				Log.d("Error Communication Mock", "I'am here !!!");
				int i = random.nextInt(NUMBER_POSSIBLE_FALSE_ANSWER);
				switch (i) {
					case 0:
						response = new BasicHttpResponse(new BasicStatusLine(
								HttpVersion.HTTP_1_1, NOT_AUTORIZED_STATUS,
								"Unauthorized"));
						response.setEntity(new StringEntity("ERROR"));
						break;
	
					case 1:
						response = new BasicHttpResponse(
								new BasicStatusLine(HttpVersion.HTTP_1_1,
										NOT_FOUND_STATUS, "Not Found"));
						response.setEntity(new StringEntity("ERROR"));
						break;
	
					default:
						break;
				}
			}
			return response;
		}
	}

}

package epfl.sweng.test;

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

/**
 * 
 * @author wissem
 * this class is responsible for giving some uncorrect responses
 *
 */
public class MockHttpClientUncR extends DefaultHttpClient {

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
				final HttpClientConnection conn, final HttpContext context) throws IOException, HttpException {
			final int statusOk = 200;
			final int emptyBodyStatus= 204;
			String uri=request.getRequestLine().getUri();
			
			if (uri.equals("/quizquestions/random")) {  
				HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusOk, "OK"));
				Random r = new Random();
				int i = r.nextInt(2);
				switch (i) {
					case 0:
						response.setEntity(new StringEntity(
								"{"
										+ " \"question\": \"What is the answer to life, the universe and everything?\","
										+ " \"answers\": [ \"42\", \"24\" ],"
										+ " \"solutionIndex\": 0," + " \"id\": 4243,"
										+ " \"owner\": \"anounymous\","
										+ " \"tags\": [ \"h2g2\", \"trivia\" ]" + " }"));
						break;
					case 1: 
						response.setEntity(new StringEntity(
							"{"
									+ " \"question\": \"What is the answer to life, the universe and everything?\","
									+ " \"answers\": [ \"42\", \"24\" ],"
									+ " \"solutionIndex\": 0," + " \"id\": 4244,"
									+ " \"owner\": \"anounymous\","
									+ " \"tags\": [ \"h2g2\", \"trivia\" ]" + " }"));
	
					default:
						break;
				}
				
				response.setHeader("Content-type", "application/json;charset=utf-8");
				return response;
			} else if (uri.equals("/quizquestions/4243/ratings")) { // unexpected data
				HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusOk, "OK"));
				response.setEntity(new StringEntity(
						"{"
								+ " \"likeCount\": -1,"
								+ " \"dislikeCount\": -2,"
								+ " \"incorrectCount\": -3"
								+ " }"));
				response.setHeader("Content-type", "application/json;charset=utf-8");
				return response;
				
			} else if (uri.equals("/quizquestions/4244/ratings")) { // malformed data
				HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, statusOk, "OK"));
				response.setEntity(new StringEntity(
						"{"
								+ " \"likeCount\": a,"
								+ " \"dislikeCount\": b,"
								+ " \"incorrectCount\": c"
								+ " }"));
				response.setHeader("Content-type", "application/json;charset=utf-8");
				return response;
			}  else {
				HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, emptyBodyStatus, "OK"));
				return response;
			} 
			
			
		}
	}


}

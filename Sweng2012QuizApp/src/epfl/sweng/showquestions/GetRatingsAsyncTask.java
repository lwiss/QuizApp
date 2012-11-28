package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.servercomm.SwengHttpClientFactory;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * 
 * @author crazybhy
 * 
 */
public class GetRatingsAsyncTask extends
		AsyncTask<Object, String, String> {
	private ShowQuestionsActivity activity;
	private final static String URL_SERVER = "https://sweng-quiz.appspot.com/quizquestions";
	private static final int OK_STATUS = 200;
	private static final int NO_CONTENT_STATUS = 204;
	private static final int NOT_FOUND_STATUS = 404;
	private static final int UNAUTHORIZED_STATUS = 401;
	private final static String LIKE_TEXT = "You like the question";
	private final static String DISLIKE_TEXT = "You dislike the question";
	private final static String INCORRECR_QUESTION_TEXT = "You think the question is incorrect";
	private final static String NO_RATED_QUESTION = "You have not rated this question";
	private int likeCount = -1;
	private int dislikeCount = -1;
	private int incorrectCount = -1;
	private String verdict = null;
	private String error = null;

	@Override
	protected String doInBackground(Object... arg0) {
		activity = (ShowQuestionsActivity) arg0[0];
		int questionId = activity.getQuizQuestion().getId();
		getRatings(questionId);
		getRating(questionId);
		return null;
	}

	private void getRatings(int id) {
		HttpResponse httpResponse = getRequest(URL_SERVER + "/" + id
				+ "/ratings");
		if (httpResponse != null
				&& httpResponse.getStatusLine().getStatusCode() == OK_STATUS) {
			try {
				String response = EntityUtils
						.toString(httpResponse.getEntity());
				JSONObject json = new JSONObject(response);
				likeCount = json.getInt("likeCount");
				dislikeCount = json.getInt("dislikeCount");
				incorrectCount = json.getInt("incorrectCount");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void getRating(int id) {
		HttpResponse httpResponse = getRequest(URL_SERVER + "/" + id
				+ "/rating");
		if (httpResponse != null) {
			switch (httpResponse.getStatusLine().getStatusCode()) {
				case OK_STATUS:
					try {
						String response = EntityUtils.toString(httpResponse
								.getEntity());
						JSONObject json = new JSONObject(response);
						verdict = json.getString("verdict");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case NO_CONTENT_STATUS:
					verdict = "You have not rated this question";
					break;
				case NOT_FOUND_STATUS:
				case UNAUTHORIZED_STATUS:
					try {
						String response = EntityUtils.toString(httpResponse
								.getEntity());
						JSONObject json = new JSONObject(response);
						error = json.getString("message");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				default:
			}
		}
	}

	private HttpResponse getRequest(String url) {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		String sessionId = activity.getSessionId();
		httpGet.setHeader("Authorization", "Tequila " + sessionId);
		try {
			httpResponse = SwengHttpClientFactory.getInstance()
					.execute(httpGet);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return httpResponse;
	}

	protected void onPostExecute(String param) {
		if (likeCount != -1 && dislikeCount != -1 && incorrectCount != -1) {
			activity.getLike().setText("Like (" + likeCount + ")");
			activity.getDislike().setText("Dislike (" + dislikeCount + ")");
			activity.getIncorrect().setText("Incorrect (" + incorrectCount + ")");
		} else {
			Toast.makeText(activity,
					"There was an error retrieving the ratings",
					Toast.LENGTH_LONG).show();
		}

		if (verdict != null) {
			if (verdict.equals("like")) {
				activity.getUserRating().setText(LIKE_TEXT);
			} else if (verdict.equals("dislike")) {
				activity.getUserRating().setText(DISLIKE_TEXT);
			} else if (verdict.equals("incorrect")) {
				activity.getUserRating().setText(INCORRECR_QUESTION_TEXT);
			} else if (verdict.equals(NO_RATED_QUESTION)) {
				activity.getUserRating().setText(NO_RATED_QUESTION);
			}
		} else if (error != null) {
			Toast.makeText(activity,
					"There was an error retrieving the ratings",
					Toast.LENGTH_LONG).show();
			activity.getUserRating().setText(error);
		} else {
			Toast.makeText(activity,
					"There was an error retrieving the ratings",
					Toast.LENGTH_LONG).show();
		}
	}
	
}
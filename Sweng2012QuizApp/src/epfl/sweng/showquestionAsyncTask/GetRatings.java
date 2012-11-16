package epfl.sweng.showquestionAsyncTask;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import epfl.sweng.R;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 * This AsyncTask is responsable for getting the Ratings of a questions and
 * display information in ShowQuestion Activity
 * 
 * @author MohamedBenArbia
 * 
 */
public class GetRatings extends
		AsyncTask<ShowQuestionsActivity, String, String> {
	private ShowQuestionsActivity activity;
	private static final int OK_STATUS = 200;
	private static final int NO_CONTENT_STATUS = 204;
	private static final int NOT_FOUND_STATUS = 404;
	private static final int UNAUTHORIZED_STATUS = 401;
	private final static String LIKE_TEXT = "You like the question";
	private final static String DISLIKE_TEXT = "You dislike the question";
	private final static String INCORRECR_QUESTION_TEXT = "You think the question is incorrect";
	private final static String NO_RATED_QUESTION = "You have not rated this question";
	private final static String URL_SERVER = "https://sweng-quiz.appspot.com/quizquestions";
	private int likeCount = -1;
	private int dislikeCount = -1;
	private int incorrectCount = -1;
	private String verdict = null;
	private String errorMessage = null;

	private HttpResponse getRequest(String url) {
		Log.d("GET RATINGS ASYNC TASK", "getRequest() method");
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		String sessionId = activity.getSessionId();
		Log.d("GET RATINGS ASYNC TASK", "sessionID : " + sessionId);
		httpGet.setHeader("Authorization", "Tequila " + sessionId);

		try {

			httpResponse = SwengHttpClientFactory.getInstance()
					.execute(httpGet);
			Log.d("GET RATINGS ASYNC TASK", "RESPONSE STATUS "
					+ httpResponse.getStatusLine().getStatusCode());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return httpResponse;
	}

	private void getAllRatings(int idQuizQuestion) {

		HttpResponse httpResponse = getRequest(URL_SERVER + "/"
				+ idQuizQuestion + "/ratings");

		String response = "";
		try {
			if (httpResponse != null) {
				if (httpResponse.getStatusLine().getStatusCode() == OK_STATUS) {
					response = EntityUtils.toString(httpResponse.getEntity());
					Log.d("response", response);
					JSONObject json = new JSONObject(response);
					likeCount = json.getInt("likeCount");
					dislikeCount = json.getInt("dislikeCount");
					incorrectCount = json.getInt("incorrectCount");
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void getUserRating(int idQuizQuestion) {

		HttpResponse httpResponse = getRequest(URL_SERVER + "/"
				+ String.valueOf(idQuizQuestion) + "/rating");
		String response = "";

		if (httpResponse != null) {
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			try {

				if (statusCode == OK_STATUS) {
					response = EntityUtils.toString(httpResponse.getEntity());
					JSONObject json = new JSONObject(response);
					verdict = json.getString("verdict");
				} else if (statusCode == NO_CONTENT_STATUS) {
					verdict = "You have not rated this question";
				} else if (statusCode == NOT_FOUND_STATUS
						|| statusCode == UNAUTHORIZED_STATUS) {
					response = EntityUtils.toString(httpResponse.getEntity());
					JSONObject json = new JSONObject(response);
					errorMessage = json.getString("message");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	protected String doInBackground(ShowQuestionsActivity... params) {
		activity = (ShowQuestionsActivity) params[0];
		Log.d("GET RATINGS ASYNC TASK", "IN DO IN BACKGROUD");
		int questionId = activity.getQuizQuestion().getId();
		Log.d("GET RATINGS ASYNC TASK", "Question ID : " + questionId);
		getAllRatings(questionId);
		getUserRating(questionId);
		return null;
	}

	@Override
	protected void onPostExecute(String param) {
		Button likeButton = (Button) activity.findViewById(R.id.likeButton);
		Button dislikeButton = (Button) activity
				.findViewById(R.id.DislikeButton);
		Button incorrectButton = (Button) activity
				.findViewById(R.id.IncorrectButton);
		if (likeCount != -1 && dislikeCount != -1 && incorrectCount != -1) {
			likeButton.setText("Like (" + likeCount + ")");
			dislikeButton.setText("Dislike (" + dislikeCount + ")");
			incorrectButton.setText("Incorrect (" + incorrectCount + ")");
		} else {
			Toast.makeText(activity,
					"There was an error retrieving the ratings",
					Toast.LENGTH_LONG).show();
		}

		if (verdict != null) {
			if (verdict.equals("like")) {
				activity.setUserRate(LIKE_TEXT);
			} else if (verdict.equals("dislike")) {
				activity.setUserRate(DISLIKE_TEXT);
			} else if (verdict.equals("incorrect")) {
				activity.setUserRate(INCORRECR_QUESTION_TEXT);
			} else if (verdict.equals(NO_RATED_QUESTION)) {
				activity.setUserRate(NO_RATED_QUESTION);
			}
		} else if (errorMessage != null) {
			Toast.makeText(activity,
					"There was an error retrieving the ratings",
					Toast.LENGTH_LONG).show();
			activity.setUserRate(errorMessage);
		} else {
			Toast.makeText(activity,
					"There was an error retrieving the ratings",
					Toast.LENGTH_LONG).show();
		}

	}
}

package epfl.sweng.showquestionAsyncTask;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.R;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * This AsyncTask is responsable for get the Ratings of a questions and display
 * information in Showquestion Activity
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
	private final static String URL_SERVER = "https://sweng-quiz.appspot.com/quizquestions";
	private int likeCount;
	private int dislikeCount;
	private int incorrectCount;
	private String verdict;
	private String errorMessage;

	private HttpResponse getRequest(String url) {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		try {
			httpResponse = SwengHttpClientFactory.getInstance()
					.execute(httpGet);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return httpResponse;
	}

	private void getAllRatings(int idQuizQuestion) {

		HttpResponse httpResponse = getRequest(URL_SERVER + "/"
				+ String.valueOf(idQuizQuestion) + "/ratings");
		String response = "";
		try {
			response = EntityUtils.toString(httpResponse.getEntity());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Log.d("response", response);
			JSONObject json = new JSONObject(response);
			likeCount = json.getInt("likeCount");
			dislikeCount = json.getInt("dislikeCount");
			incorrectCount = json.getInt("incorrectCount");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getUserRating(int idQuizQuestion) {

		HttpResponse httpResponse = getRequest(URL_SERVER + "/"
				+ String.valueOf(idQuizQuestion) + "/rating");
		String response = "";

		try {
			response = EntityUtils.toString(httpResponse.getEntity());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		try {

			if (statusCode == OK_STATUS) {
				JSONObject json = new JSONObject(response);
				verdict = json.getString("verdict");
			} else if (statusCode == NO_CONTENT_STATUS) {
				verdict = "";
			} else if (statusCode == NOT_FOUND_STATUS
					|| statusCode == UNAUTHORIZED_STATUS) {
				JSONObject json = new JSONObject(response);
				errorMessage = json.getString("message");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected String doInBackground(ShowQuestionsActivity... params) {
		// TODO Auto-generated method stub
		activity = (ShowQuestionsActivity) params[0];
		int questionId = activity.getQuizQuestion().getId();
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
		TextView userRateText = (TextView) activity
				.findViewById(R.id.userCurrentRating);
		likeButton.setText("Like (" + likeCount + ")");
		dislikeButton.setText("Dislike (" + dislikeCount + ")");
		incorrectButton.setText("Incorrect (" + incorrectCount + ")");

		if (verdict != null) {
			userRateText.setText(verdict);
		} else {
			userRateText.setText(errorMessage);
		}

	}
}

package epfl.sweng.showquestionAsyncTask;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * This class will perform communication in order to post the user's rating of
 * the question to the server
 * 
 * @author MohamedBenArbia
 * 
 */
public class PostRating extends AsyncTask<Object, Integer, HttpResponse> {
	private ShowQuestionsActivity activity;
	private String errorMeassage;
	private String userRate;
	private String sessionId;
	private static final int OK_STATUS = 200;
	private static final int CREATED_STATUS = 201;
	private static final int NOT_FOUND_STATUS = 404;
	private static final int UNAUTHORIZED_STATUS = 401;
	private final static String URL_SERVER = "https://sweng-quiz.appspot.com/quizquestions";

	private HttpResponse postUserRating(int questionID, String userrate) {
		HttpResponse response = null;
		JSONObject json = new JSONObject();
		try {
			json.put("verdict", userrate);
			HttpPost post = new HttpPost(URL_SERVER + "/" + questionID
					+ "/rating");
			post.setHeader("Authorization", "Tequila " + sessionId);
			post.setEntity(new StringEntity(json.toString()));
			response = SwengHttpClientFactory.getInstance().execute(post);
			Log.d("SERVER RESPONSE", " http response " + response);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected HttpResponse doInBackground(Object... params) {
		// TODO Auto-generated method stub
		activity = (ShowQuestionsActivity) params[0];
		userRate = (String) params[1];
		Log.d("User Rate", userRate);
		sessionId = activity.getSessionId();
		int questionId = activity.getQuizQuestion().getId();
		Log.d("QuestionID", "question id :" + questionId);
		return postUserRating(questionId, userRate);

	}

	@Override
	protected void onPostExecute(HttpResponse response) {
		if (response != null) {
			int status = response.getStatusLine().getStatusCode();
			Log.d("SERVER RESPONSE", "response status" + status);
			if (status == OK_STATUS) {
				Toast.makeText(activity, "Your rate has been updated",
						Toast.LENGTH_SHORT).show();

			} else if (status == CREATED_STATUS) {
				Toast.makeText(activity, "Your rate has been registred",
						Toast.LENGTH_SHORT).show();

			} else if (status == NOT_FOUND_STATUS
					|| status == UNAUTHORIZED_STATUS) {
				try {
					errorMeassage = EntityUtils.toString(response.getEntity());
					Log.d("SERVER", errorMeassage);
					Toast.makeText(activity,
							"There was an error setting the rating",
							Toast.LENGTH_SHORT).show();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(activity,
						"There was an error setting the rating",
						Toast.LENGTH_SHORT).show();
			}
			try {
				response.getEntity().getContent().close();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			new GetRatings().execute(activity);

		} else {
			Toast.makeText(activity, "There was an error setting the rating",
					Toast.LENGTH_SHORT).show();
		}
	}

}

package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
public class PostRatingAsyncTask extends
		AsyncTask<Object, String, HttpResponse> {
	private ShowQuestionsActivity activity;
	private final static String URL_SERVER = "https://sweng-quiz.appspot.com/quizquestions";
	private static final int OK_STATUS = 200;
	private static final int CREATED_STATUS = 201;
	private static final int NOT_FOUND_STATUS = 404;
	private static final int UNAUTHORIZED_STATUS = 401;

	@Override
	protected HttpResponse doInBackground(Object... params) {
		activity = (ShowQuestionsActivity) params[0];
		String rate = (String) params[1];
		int questionId = activity.getQuizQuestion().getId();
		return postUserRating(questionId, rate);
	}

	private HttpResponse postUserRating(int questionID, String rate) {
		HttpResponse response = null;
		try {
			JSONObject json = new JSONObject();
			json.put("verdict", rate);
			String sessionId = activity.getSessionId();
			HttpPost post = new HttpPost(URL_SERVER + "/" + questionID
					+ "/rating");
			post.setHeader("Authorization", "Tequila " + sessionId);
			post.setEntity(new StringEntity(json.toString()));
			response = SwengHttpClientFactory.getInstance().execute(post);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	protected void onPostExecute(HttpResponse response) {
		if (response != null) {
			int status = response.getStatusLine().getStatusCode();
			if (status == OK_STATUS) {
				Toast.makeText(activity, "Your rate has been updated",
						Toast.LENGTH_SHORT).show();
			} else if (status == CREATED_STATUS) {
				Toast.makeText(activity, "Your rate has been registred",
						Toast.LENGTH_SHORT).show();
			} else if (status == NOT_FOUND_STATUS
					|| status == UNAUTHORIZED_STATUS) {
				Toast.makeText(activity,
						"There was an error setting the rating",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity,
						"There was an error setting the rating",
						Toast.LENGTH_SHORT).show();
			}
			try {
				response.getEntity().getContent().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			new GetRatingsAsyncTask().execute(activity);
		} else {
			Toast.makeText(activity, "There was an error setting the rating",
					Toast.LENGTH_SHORT).show();
		}
	}

}

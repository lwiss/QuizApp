package epfl.sweng.quizzes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * This class is responsable to hand in the answers of the quizz
 * 
 * @author MohamedBenArbia
 * 
 */
public class HandInQuizzAsyncTask extends AsyncTask<Object, String, String> {
	private ShowQuizActivity activity;
	private static final String SERVER_URL = "https://sweng-quiz.appspot.com/quizzes/";
	private static final String ERROR_MESSAGE = "An error occurred while handing in your answers";
	private static final int OK_STATUS = 200;
	private int quizId;
	private double score;
	private int[] chosenAnswers;
	private String sessionId;
	private boolean communicationError = false;

	private void postQuizzAnswers() {
		HttpPost post = new HttpPost(SERVER_URL + quizId + "/submission");
		post.setHeader("Authorization", "Tequila " + sessionId);
		JSONObject json = new JSONObject();
		JSONArray answerArray = createJSONArray(chosenAnswers);
		try {
			json.put("choices", answerArray);
			post.setEntity(new StringEntity(json.toString()));
		} catch (JSONException e) {

		} catch (UnsupportedEncodingException e) {

		}
		HttpResponse response = null;

		try {
			response = SwengHttpClientFactory.getInstance().execute(post);
			if (response != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				String responseBody = EntityUtils
						.toString(response.getEntity());
				if (statusCode == OK_STATUS) {
					JSONObject result = new JSONObject(responseBody);
					score = result.getDouble("score");
				} else {
					communicationError = true;
				}
			} else {
				communicationError = true;
			}
		} catch (ClientProtocolException e) {
			communicationError = true;
		} catch (IOException e) {
			communicationError = true;
		} catch (JSONException e) {
			communicationError = true;
		}

	}

	private JSONArray createJSONArray(int[] answers) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < answers.length; i++) {
			if (answers[i] == -1) {
				jsonArray.put(JSONObject.NULL);
			} else {
				jsonArray.put(answers[i]);
			}
		}
		return jsonArray;
	}

	@Override
	protected String doInBackground(Object... params) {
		activity = (ShowQuizActivity) params[0];
		chosenAnswers = (int[]) params[1];
		quizId = activity.getQuizId();
		activity = (ShowQuizActivity) params[0];
		SharedPreferences preference = activity.getSharedPreferences(
				MainActivity.PREF_NAME, Activity.MODE_PRIVATE);
		sessionId = preference.getString("SESSION_ID", null);
		postQuizzAnswers();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPostExecute(String param) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		if (!communicationError) {
			DecimalFormat df = new DecimalFormat("0.00");
			builder.setMessage("Your score is " + df.format(score));
		} else {
			builder.setMessage(ERROR_MESSAGE);
		}
		AlertDialog dialog = builder.create();
		dialog.show();

	}

}

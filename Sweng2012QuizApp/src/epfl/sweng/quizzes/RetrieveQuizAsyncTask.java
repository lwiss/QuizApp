package epfl.sweng.quizzes;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.R;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author wissem(wissem.allouchi@epfl.ch) This class implements the server
 *         communication needed to retrieve a quiz and updates the
 *         ShowQuizActivity UI fields.
 * 
 * 
 */
public class RetrieveQuizAsyncTask extends
		AsyncTask<ShowQuizActivity, String, String> {
	private ShowQuizActivity activity;
	private String sessionId;
	private static final int OK_STATUS = 200;
	private static final int UNAUTHENTICATED_STATUS = 401;
	private static final int NOT_FOUND_STATUS = 404;
	private static final String SERVER_URL = "https://sweng-quiz.appspot.com/quizzes/";
	static final String COMMUNICATION_ERROR_MESSAGE = "An error occurred while loading the quiz.";
	/**
	 * If An error occurs during the communication, this variable is set to true
	 */
	private boolean communicationError = false;
	private String message;

	/**
	 * this method performs a GET request to the server in order to get the quiz
	 * question. If an error occurs while constructing the array then a null
	 * reference will be returned.
	 * 
	 * @param id
	 *            : the id of the quiz to retrieve
	 * @return a {@link JSONArray} that contains the questions description(the
	 *         question text and the list of answers)
	 */
	public JSONArray retrieveQuizQuestions(int id) {
		HttpGet httpGet = new HttpGet(SERVER_URL + id);
		httpGet.setHeader("Authorization", "Tequila " + sessionId);
		HttpResponse response = null;
		int statusCode = -1;
		String responseBody = "";
		JSONObject jsonQuiz = null;
		JSONArray jsonQuizArray = null;
		try {
			response = SwengHttpClientFactory.getInstance().execute(httpGet);
			if (response != null) {
				statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == OK_STATUS) {
					responseBody = EntityUtils.toString(response.getEntity());
					jsonQuiz = new JSONObject(responseBody);
					jsonQuizArray = jsonQuiz.getJSONArray("questions");
					if (jsonQuizArray.length() == 0) {
						communicationError = true;
						return null;
					}
				} else if (statusCode == NOT_FOUND_STATUS
						|| statusCode == UNAUTHENTICATED_STATUS) {
					responseBody = EntityUtils.toString(response.getEntity());
					jsonQuiz = new JSONObject(responseBody);
					message = jsonQuiz.getString("message");
				}

			} else {
				communicationError = true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			communicationError = true;
		} catch (IOException e) {
			e.printStackTrace();
			communicationError = true;
		} catch (JSONException e) {
			e.printStackTrace();
			communicationError = true;
		}

		return jsonQuizArray;
	}

	/**
	 * this method fills the
	 * 
	 * @param array
	 * @return
	 */
	public ArrayList<QuizQuestion> fillQuestionsList(JSONArray array) {
		ArrayList<QuizQuestion> qQuestion = new ArrayList<QuizQuestion>();
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				try {
					String question = array.getJSONObject(i).getString(
							"question");
					JSONArray jsonAnswers = array.getJSONObject(i)
							.getJSONArray("answers");
					ArrayList<String> answers = new ArrayList<String>();
					for (int j = 0; j < jsonAnswers.length(); j++) {
						answers.add(jsonAnswers.getString(j));
					}
					if (QuizQuestion.questionIsOK(question)
							&& QuizQuestion.answersAreOk(answers)) {
						qQuestion.add(new QuizQuestion(question, answers, 0,
								null, 0, null));
					} else {
						qQuestion.add(null);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					communicationError = true;
				}

			}
		}
		Log.d("liste of questions", qQuestion.toString());
		return qQuestion;
	}

	@Override
	protected String doInBackground(ShowQuizActivity... params) {
		activity = (ShowQuizActivity) params[0];
		SharedPreferences preference = activity.getSharedPreferences(
				MainActivity.PREF_NAME, Activity.MODE_PRIVATE);
		sessionId = preference.getString("SESSION_ID", null);
		JSONArray quiz = retrieveQuizQuestions(activity.getQuizId());
		activity.setQuizQuestionList(fillQuestionsList(quiz));
		activity.setNumberOfQuestions(activity.getQuizQuestionList().size());
		int[] chosenAnswers = new int[activity.getNumberOfQuestions()];
		for (int i = 0; i < chosenAnswers.length; i++) {
			chosenAnswers[i] = -1;
		}
		activity.setChosenAns(chosenAnswers);
		return null;
	}

	@Override
	protected void onPostExecute(String param) {

		if (message != null) {
			// then show the COMMUNICATION_ERROR_MESSAGE
			// and disable buttons
			TextView text = (TextView) activity.findViewById(R.id.questionText);
			text.setText(COMMUNICATION_ERROR_MESSAGE);
			activity.getQuestionText().setText(COMMUNICATION_ERROR_MESSAGE);
			Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			((Button) activity.findViewById(R.id.next)).setEnabled(false);
			((Button) activity.findViewById(R.id.previous)).setEnabled(false);
			((Button) activity.findViewById(R.id.handIn)).setEnabled(false);
		} else if (communicationError) {
			// then show the COMMUNICATION_ERROR_MESSAGE
			// and disable buttons
			TextView text = (TextView) activity.findViewById(R.id.questionText);
			text.setText(COMMUNICATION_ERROR_MESSAGE);
			((Button) activity.findViewById(R.id.next)).setEnabled(false);
			((Button) activity.findViewById(R.id.previous)).setEnabled(false);
			((Button) activity.findViewById(R.id.handIn)).setEnabled(false);
		} else {
			// show the first question
			activity.showQuestion(0);
		}

	}
}

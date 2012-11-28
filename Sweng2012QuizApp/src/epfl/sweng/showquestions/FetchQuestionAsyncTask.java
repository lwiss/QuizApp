package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;

import epfl.sweng.R;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author crazybhy
 *
 */
public class FetchQuestionAsyncTask extends
		AsyncTask<ShowQuestionsActivity, String, QuizQuestion> {
	private static final String URL = "https://sweng-quiz.appspot.com/quizquestions/random";
	private ShowQuestionsActivity activity;

	@Override
	protected QuizQuestion doInBackground(ShowQuestionsActivity... params) {
		activity = params[0];
		HttpGet httpGet = new HttpGet(URL);
		SharedPreferences preference = activity.getSharedPreferences(MainActivity.PREF_NAME, Activity.MODE_PRIVATE);
		activity.setSessionId(preference.getString("SESSION_ID", null));
		httpGet.setHeader("Authorization", "Tequila "+activity.getSessionId());
		ResponseHandler<String> handler = new BasicResponseHandler();
		String response = "";
		QuizQuestion quizQuestion = null;
		try {
			response = SwengHttpClientFactory.getInstance().execute(httpGet,
					handler);
			quizQuestion = new QuizQuestion(response);
		} catch (IOException e) {
			quizQuestion = new QuizQuestion(
					"There was an error retrieving the question",
					new ArrayList<String>(), -1, new HashSet<String>(), -1, null);
		} catch (JSONException e) {
			quizQuestion = new QuizQuestion(
					"There was an error retrieving the question",
					new ArrayList<String>(), -1, new HashSet<String>(), -1, null);
		}
		return quizQuestion;
	}
	
	@Override
	protected void onPostExecute(QuizQuestion quizQuestion) {
		activity.setQuizQuestion(quizQuestion);
		final TextView questionView = activity.getQuestionView();
		final ListView answersView = activity.getAnswersView();
		final Button nextQuestionButton = activity.getNextQuestionButton();
		final String question = quizQuestion.getQuestion();
		final List<String> answers = quizQuestion.getAnswers();
		final int solutionIndex = quizQuestion.getSolutionIndex();
		questionView.setText(question);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				activity, R.layout.answer_show, answers);
		answersView.setAdapter(adapter);
		answersView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView textView = (TextView) view;
				if (position == solutionIndex) {
					textView.setText(textView.getText() + " \u2714");
					nextQuestionButton.setEnabled(true);
					answersView.setEnabled(false);
				} else {
					if (textView.isEnabled()) {
						textView.setText(textView.getText() + " \u2718");
						textView.setEnabled(false);
					}
				}
			}
		});
		nextQuestionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.initializeActivity();
				nextQuestionButton.setEnabled(false);
				answersView.setEnabled(true);
			}
		});
	}

}

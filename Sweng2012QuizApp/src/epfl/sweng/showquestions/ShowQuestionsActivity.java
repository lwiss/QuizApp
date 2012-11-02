package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;

import epfl.sweng.R;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

/**
 * 
 * @author Eagles
 * 
 */
public class ShowQuestionsActivity extends Activity {
	private static TextView question;
	private ListView answersList;
	private static int solution;
	private static List<String> answers;
	private Button nextQuestionButton;
	private static final String URL = "https://sweng-quiz.appspot.com/quizquestions/random";

	public static TextView getQuestion() {
		return question;
	}

	public static int getSolution() {
		return solution;
	}

	public static List<String> getAnswers() {
		return answers;
	}

	private void nextQuestion() {
		new FetchQuestion().execute(URL);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);

		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMan.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			new FetchQuestion().execute(URL);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_show_questions, menu);
		return true;
	}

	/**
	 * 
	 * @author Eagles
	 * 
	 */
	private class FetchQuestion extends AsyncTask<String, String, QuizQuestion> {

		@Override
		protected QuizQuestion doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				return downloadContent(urls[0]);

			} catch (JSONException e) {
				System.err.println("ERROR! NULL JSONOBJECT");
				return null;
			}

		}

		private QuizQuestion downloadContent(String url) throws JSONException {

			HttpGet httpget = new HttpGet(url);
			SharedPreferences preference = getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);
			String sessionId = preference.getString("SESSION_ID", null);
			httpget.setHeader("Authorization", "Tequila "+sessionId);
			ResponseHandler<String> handler = new BasicResponseHandler();
			String request = "";
			try {
				request = SwengHttpClientFactory.getInstance().execute(httpget,
						handler);
			} catch (IOException e) {
				question.setText("There was an error retrieving the question");
			}
			QuizQuestion quizQuestion = new QuizQuestion(request);

			return quizQuestion;
		}

		@Override
		protected void onPostExecute(QuizQuestion quizquestion) {
		
			// ShowQuestionsActivity show = couple.getShowQuestionsActivity();
			question = (TextView) findViewById(R.id.question);
			String que = quizquestion.getQuestion();
			solution = quizquestion.getSolutionIndex();
			answers = quizquestion.getAnswers();
			question.setText(que);
			/**
			 * answers = new String[answersArray.length()]; for (int i = 0; i <
			 * answers.length; i++) { answers[i] = answersArray.getString(i); }
			 */

			ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
					ShowQuestionsActivity.this, R.layout.answer, answers);
			answersList = (ListView) findViewById(R.id.answersList);
			answersList.setAdapter(aAdapter);
			nextQuestionButton = (Button) findViewById(R.id.nextQuestion);
			answersList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView textView = (TextView) view;
					if (position == solution) {
						textView.setText(textView.getText() + " \u2714");
						nextQuestionButton.setEnabled(true);
						answersList.setEnabled(false);
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
					nextQuestion();
					nextQuestionButton.setEnabled(false);
					answersList.setEnabled(true);
				}
			});

		}

	}

}
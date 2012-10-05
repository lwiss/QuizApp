package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.R;
import epfl.sweng.R.layout;
import epfl.sweng.R.menu;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class ShowQuestionsActivity extends Activity {
	static public TextView question;
	private ListView answersList;
	static public int solution;
	static public String[] answers;
	private Button nextQuestionButton;
	private final String URL = "https://sweng-quiz.appspot.com/quizquestions/random";
	
	private void nextQuestion()
	{
		new DownloadJSONObject().execute(URL);
	}

	private Couple downloadContent(String url) throws JSONException {

		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String request = "";
		try {
			request = SwengHttpClientFactory.getInstance().execute(httpget,
					handler);
		} catch (IOException e) {
			question.setText("There was an error retrieving the question");
		}

		
		return new Couple(new JSONObject(request), this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMan.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			new DownloadJSONObject().execute(URL);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_show_questions, menu);
		return true;
	}

	private class DownloadJSONObject extends AsyncTask<String, String, Couple> {

		@Override
		protected Couple doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				return downloadContent(urls[0]);

			} catch (JSONException e) {
				System.err.println("ERROR! NULL JSONOBJECT");
				return null;
			}

		}

		@Override
		protected void onPostExecute(Couple couple) {
			try {

				JSONObject jsonObject = couple.getJsonObject();
				ShowQuestionsActivity show = couple.getShowQuestionsActivity();
				question = (TextView) findViewById(R.id.question);
				String que = jsonObject.getString("question");
				solution = jsonObject.getInt("solutionIndex");
				JSONArray answersArray = jsonObject.getJSONArray("answers");
				question.setText(que);

				answers = new String[answersArray.length()];
				for (int i = 0; i < answers.length; i++) {
					answers[i] = answersArray.getString(i);
				}

				ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(show,
						R.layout.answer, answers);
				answersList = (ListView) findViewById(R.id.answersList);
				answersList.setAdapter(aAdapter);
		        nextQuestionButton = (Button) findViewById(R.id.nextQuestion);
		        answersList.setOnItemClickListener(new OnItemClickListener() {
		        	public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
		        		TextView textView = (TextView) view;
		        		if (position==solution) {
		        			textView.setText(textView.getText()+" \u2714");
		        			nextQuestionButton.setEnabled(true);
		        			answersList.setEnabled(false);
		        		}
		        		else {
		        			if (textView.isEnabled()) {
		        				textView.setText(textView.getText()+" \u2718");
		        				textView.setEnabled(false);
		        			}
		        		}
		        	}
				});
		        nextQuestionButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						nextQuestion();
						nextQuestionButton.setEnabled(false);
						answersList.setEnabled(true);
					}
				});

			} catch (JSONException e) {
				System.err.println("ERREUR Lors de l'extraction des données");
			}

		}

	}

}
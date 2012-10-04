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

public class ShowQuestionsActivity extends Activity {

	private final String URL = "https://sweng-quiz.appspot.com/quizquestions/random";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);

		// check the Network Connection
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMan.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			System.out.println("il y a une conexion !");
			new DownloadJSONObject().execute(URL);
		}

	}

	private JSONObject downloadContent(String urlServer) throws JSONException {
		HttpGet httpget = new HttpGet(urlServer);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String question = "";
		try {
			question = SwengHttpClientFactory.getInstance().execute(httpget,
					handler);
		} catch (IOException e) {
			System.err.println("Enable to connect to the Server!");
		}

		return new JSONObject(question);

	}

	private class DownloadJSONObject extends
			AsyncTask<String, String, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				return downloadContent(urls[0]);

			} catch (JSONException e) {
				System.err.println("ERROR! NULL JSONOBJECT");
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			try {

				String quesion = jsonObject.getString("question");
				int solutionIndex = jsonObject.getInt("solutionIndex");
				JSONArray answersArray = jsonObject.getJSONArray("answers");
				System.out.println("la question est : " + quesion);
				System.out.println("les reponses sont " + answersArray);
				System.out.println("L'index de la solution est "
						+ solutionIndex);

			} catch (JSONException e) {
				System.err.println("ERREUR Lors de l'extraction des données");
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_show_questions, menu);
		return true;
	}

}

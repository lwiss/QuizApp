package epfl.sweng.quizzes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @author crazybhy
 *
 */
public class ListAvailableQuizzesAsyncTask extends
		AsyncTask<Object, String, String> {
	private static final String SERVER_URL = "https://sweng-quiz.appspot.com/quizzes";
	private static final int MAX_CHARACTER_NUMBER = 500;
	private static final int OK_STATUS = 200;
	private static final String NO_QUIZZES_AVAILABLE = "There are no quizzes available at the moment.";
	private static final String COMMUNICATION_ERROR_MESSAGE = "An error occurred while fetching quizzes.";
	private boolean noquizzAvailable = false;
	private boolean communicationError = false;
	private ShowAvailableQuizzesActivity activity;
	private String sessionId;
	private List<String> quizzesTitle = new ArrayList<String>();

	@Override
	protected String doInBackground(Object... params) {
		activity = (ShowAvailableQuizzesActivity) params[0];
		SharedPreferences preference = activity.getSharedPreferences(MainActivity.PREF_NAME, Activity.MODE_PRIVATE);
		sessionId = preference.getString("SESSION_ID", null);
		getQuizzesTitles();
		return null;
	}

	private void getQuizzesTitles() {
		HttpGet httpGet = new HttpGet(SERVER_URL);
		httpGet.setHeader("Authorization", "Tequila " + sessionId);
		HttpResponse response = null;
		int statusCode = -1;
		String responseBody = "";
		JSONArray json = null;
		try {
			response = SwengHttpClientFactory.getInstance().execute(httpGet);
			if (response != null) {
				statusCode = response.getStatusLine().getStatusCode();
				responseBody = EntityUtils.toString(response.getEntity());
				if (statusCode == OK_STATUS) {
					json = new JSONArray(responseBody);
					if (json.length() == 0) {
						noquizzAvailable = true;
					}
					getQuizzTitle(json);
				} else {
					communicationError = true;
				}
			} else {
				communicationError = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			communicationError = true;
		} catch (JSONException e) {
			e.printStackTrace();
			communicationError = true;
		}

	}
	
	private void getQuizzTitle(JSONArray json) throws JSONException {
		for (int i = 0; i < json.length(); i++) {
			JSONObject jsonObject = (JSONObject) json.get(i);
			String title = jsonObject.getString("title");
			int id = jsonObject.getInt("id");
			if (auditErrors(id, title) == 0) {
				quizzesTitle.add(title);
				ShowAvailableQuizzesActivity.getQuizzesIds().add(id);
			}

		}
	}
	
	private int auditErrors(int id, String title) {
		int i = 0;
		if (!verifyID(id)) {
			i++;
		}
		if (!verifyTitle(title)) {
			i++;
		}
		return i;
	}
	private boolean verifyID(int id) {
		if (id > 0) {
			return true;
		}
		return false;
	}
	private boolean verifyTitle(String title) {
		if (title.length() < MAX_CHARACTER_NUMBER) {
			return true;
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(String param) {

		if (communicationError) {
			activity.getText().setText(COMMUNICATION_ERROR_MESSAGE);

		} else if (noquizzAvailable) {
			activity.getText().setText(NO_QUIZZES_AVAILABLE);

		} else {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
					android.R.layout.simple_list_item_1, quizzesTitle);
			activity.getList().setAdapter(adapter);
			activity.getList().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ShowAvailableQuizzesActivity.setChosenQuizId(position);
					Intent intent = new Intent(activity,
							ShowQuizActivity.class);
					activity.startActivity(intent);
				}
			});

		}
	}
	
}

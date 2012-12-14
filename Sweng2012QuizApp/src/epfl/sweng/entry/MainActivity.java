package epfl.sweng.entry;

import epfl.sweng.R;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.servercomm.communication.ServerCommunicationProxy;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

/**
 * 
 * @author crazybhy
 * 
 */
public class MainActivity extends Activity {
	public final static String PREF_NAME = "user_session";
	private static boolean online = false;
	private static String sessionId;
	private static CheckBox checkBox;

	public static boolean isOnline() {
		return online;
	}

	public static void setOnline(boolean onLine) {
		MainActivity.online = onLine;
		checkBox.setChecked(!onLine);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences settings = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		if (settings.getString("SESSION_ID", null) == null) {
			Intent intent = new Intent(MainActivity.this,
					AuthenticationActivity.class);
			startActivity(intent);
		}
		MainActivity.setSessionId(settings.getString("SESSION_ID", null));
		checkBox = (CheckBox) findViewById(R.id.chekboxOffline);
		ServerCommunicationProxy.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void showQuestion(View v) {
		Intent intent = new Intent(MainActivity.this,
				ShowQuestionsActivity.class);
		startActivity(intent);
	}

	public void submitQuestion(View v) {
		Intent intent = new Intent(MainActivity.this,
				EditQuestionActivity.class);
		startActivity(intent);
	}

	public void takeQuiz(View v) {
		Intent intent = new Intent(MainActivity.this,
				ShowAvailableQuizzesActivity.class);
		startActivity(intent);
	}

	public void logout(View v) {
		SharedPreferences setting = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = setting.edit();
		editor.remove("SESSION_ID");
		editor.commit();
		Intent intent = new Intent(MainActivity.this,
				AuthenticationActivity.class);
		startActivity(intent);
	}

	/**
	 * This method is called when the chekbox is clicked
	 */
	public void onCheckBoxClick(View view) {

		boolean isCheked = ((CheckBox) view).isChecked();
		MainActivity.setOnline(!isCheked);

		if (!isCheked) {
			// ServerCommunicationProxy.getInstance().sendCachedContent();
			new SendCachAsyncTask().execute();
		}

	}

	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionID) {
		MainActivity.sessionId = sessionID;
	}

}

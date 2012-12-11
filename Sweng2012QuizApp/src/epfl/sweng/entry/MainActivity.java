package epfl.sweng.entry;

import epfl.sweng.R;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;

/**
 * 
 * @author crazybhy
 * 
 */
public class MainActivity extends Activity {
	public final static String PREF_NAME = "user_session";

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
	public void onCheckBoxClick() {

	}

}

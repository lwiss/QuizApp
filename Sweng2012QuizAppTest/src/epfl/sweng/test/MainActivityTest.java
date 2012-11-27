package epfl.sweng.test;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.entry.MainActivity;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

/**
 * 
 * @author crazybhy
 * 
 */
public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private static final int TIME = 1000;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SharedPreferences settings = getActivity().getSharedPreferences(
				MainActivity.PREF_NAME, MainActivity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("SESSION_ID", "sessionId");
		editor.commit();
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
	}

	public void testButtons() {
		solo.searchText("Submit quiz question");
		solo.searchText("Show a random question");
		solo.clickOnText("Submit quiz question");
		solo.searchText("EditQuestionActivity");
		solo.goBackToActivity("MainActivity");
		solo.clickOnText("Show a random question");
		solo.searchText("ShowQuestionsActivity");
		solo.goBackToActivity("MainActivity");
		solo.clickOnText("Log out");
		solo.searchButton("Log in using Tequila");
		solo.searchText("Log in using Tequila");
	}

}

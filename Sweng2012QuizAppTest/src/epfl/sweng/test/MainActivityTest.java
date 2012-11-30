package epfl.sweng.test;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.showquestions.ShowQuestionsActivity;
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

	public void testButton1() {
		assertTrue("Show question button exist", solo.searchText("Show a random question"));
		solo.clickOnText("Show a random question");
		solo.assertCurrentActivity("ShowQuestionsActivity", ShowQuestionsActivity.class);
		solo.goBackToActivity("MainActivity");
		solo.assertCurrentActivity("MainActivity", MainActivity.class);
	}
	
	public void testButton2() {
		assertTrue("Submit quiz button exist", solo.searchText("Submit quiz question"));
		solo.clickOnText("Submit quiz question");
		solo.assertCurrentActivity("EditQuestionActivity", EditQuestionActivity.class);
		solo.goBackToActivity("MainActivity");
		solo.assertCurrentActivity("MainActivity", MainActivity.class);
	}
	
	public void testButton3() {
		assertTrue("Take a quizz button exist", solo.searchText("Take a Quiz"));
		solo.clickOnText("Take a Quizz");
		solo.assertCurrentActivity("ShowAvailableQuizzesActivity", ShowAvailableQuizzesActivity.class);
		solo.goBackToActivity("MainActivity");
		solo.assertCurrentActivity("MainActivity", MainActivity.class);	
	}
	
	public void testLogOut() {
		assertTrue("Log out button exist", solo.searchText("Log out"));
		solo.clickOnText("Log out");
		assertTrue("Log in button exist", solo.searchText("Log in using Tequila"));
	}

}

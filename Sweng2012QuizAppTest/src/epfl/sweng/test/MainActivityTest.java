package epfl.sweng.test;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.entry.MainActivity;
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
	}
	
}

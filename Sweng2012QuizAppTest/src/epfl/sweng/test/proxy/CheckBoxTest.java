package epfl.sweng.test.proxy;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.cash.CacheManager;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.quizzes.ShowQuizActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.Rating;
import epfl.sweng.test.showquestionsactivity.MockHttpClient;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

/**
 * This class is responsabel of testing the state of the checkBox
 * 
 * @author MohamedBenArbia
 * 
 */
public class CheckBoxTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private final static int quizId = 4243;

	public CheckBoxTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testCacheOnline() throws InterruptedException {
		solo.clickOnCheckBox(0);
		assertTrue(!solo.isCheckBoxChecked(0));
		solo.clickOnButton(0);
		solo.waitForActivity(ShowQuizActivity.class.getName());
		solo.clickOnText("Like");
		QuizQuestion quizQuestion = CacheManager.getInstance()
				.getCachedQuizQuestion();
		Rating rating = CacheManager.getInstance().getRatingQuestion(
				quizQuestion);
		Log.d("Verdict", rating.getVerdict());
		assertTrue(quizQuestion.getId() == quizId);
		assertTrue(rating.getVerdict().equals("like"));
		solo.goBack();
		solo.clickOnButton(1);
		solo.enterText(0, "2+2=?");
		solo.enterText(1, "3");
		solo.enterText(2, "4");
		solo.clickOnText("\u2718", 2);
		final int tagNumber = 3;
		solo.enterText(tagNumber, "Math");
		solo.scrollDown();
		solo.clickOnText("Submit");
	}

	public void testTickedCheckBox() {
		assertTrue(solo.searchText("Offline mode"));

		/**
		 * MainActivity.setOnline(true); assertTrue(!solo.isCheckBoxChecked(0));
		 * 
		 * MainActivity.setOnline(false); assertTrue(solo.isCheckBoxChecked(0));
		 */
		solo.clickOnCheckBox(0);
		assertTrue(MainActivity.isOnline());

		solo.clickOnCheckBox(0);
		assertTrue(!MainActivity.isOnline());
	}
}

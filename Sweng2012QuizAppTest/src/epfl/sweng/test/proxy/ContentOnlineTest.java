package epfl.sweng.test.proxy;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.cash.CacheManager;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
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
public class ContentOnlineTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private final static int QUIZID = 4243;

	public ContentOnlineTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testCacheOnline() throws InterruptedException {
		assertTrue(!solo.isCheckBoxChecked(0));
		solo.clickOnButton(0);
		solo.waitForActivity(ShowQuizActivity.class.getName());
		solo.clickOnText("Like");
		QuizQuestion quizQuestion = CacheManager.getInstance()
				.getCachedQuizQuestion();
		Rating rating = CacheManager.getInstance().getRatingQuestion(
				quizQuestion);
		Log.d("Verdict", rating.getVerdict());
		assertTrue(quizQuestion.getId() == QUIZID);
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
		final int id = 42;
		solo.waitForText("Question succefully posted");
		quizQuestion = CacheManager.getInstance().getListCachedQuizQuestion()
				.get(id);
		Log.d("Question cached", quizQuestion.toString());
		assertTrue(quizQuestion.getQuestion().equals("2+2=?"));
		solo.goBack();
		solo.clickOnCheckBox(0);
		
	}

	
}

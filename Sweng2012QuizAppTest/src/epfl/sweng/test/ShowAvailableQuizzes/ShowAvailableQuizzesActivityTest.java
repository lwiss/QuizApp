package epfl.sweng.test.ShowAvailableQuizzes;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.test.ActivityInstrumentationTestCase2;

/**
 * This class will perform the test for the ShowAvailableQuizzesActivity
 * 
 * @author MohamedBenArbia
 * 
 */
public class ShowAvailableQuizzesActivityTest extends
		ActivityInstrumentationTestCase2<ShowAvailableQuizzesActivity> {
	private Solo solo;

	public ShowAvailableQuizzesActivityTest() {
		super(ShowAvailableQuizzesActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockValidQuizzesTitle());
		solo = new Solo(getInstrumentation(), getActivity());

	}

	public void testAllQuizzezTitlesAvailable() {
		assertTrue(solo.searchText("easy quizz"));
		assertTrue(solo.searchText("hard quizz"));

	}

}

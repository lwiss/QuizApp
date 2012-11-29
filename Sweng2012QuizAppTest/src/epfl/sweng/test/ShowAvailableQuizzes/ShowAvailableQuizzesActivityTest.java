package epfl.sweng.test.ShowAvailableQuizzes;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.entry.MainActivity;
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
		ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private static final String NO_QUIZZES_AVAILABLE = "There are no quizzes available at the moment.";
	private static final String ERROR_COMMUNICATION = "An error occurred while fetching quizzes.";

	public ShowAvailableQuizzesActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());

	}

	public void testTakequizzbutton() {
		assertTrue("Take a Quiz button", solo.searchButton("Take a Quiz"));
		solo.clickOnButton("Take a Quiz");
		solo.assertCurrentActivity("Show available quizzes activity",
				ShowAvailableQuizzesActivity.class);
		solo.goBack();
	}

	public void testAllQuizzezTitlesAvailable() {
		SwengHttpClientFactory.setInstance(new MockValidQuizzesTitle());
		solo.assertCurrentActivity("Main activity", MainActivity.class);
		solo.clickOnButton("Take a Quiz");
		solo.searchText("easy quizz");
		solo.searchText("hard quizz");
		solo.goBack();
	}

	public void testNoQuizzAvailableMesaage() {
		SwengHttpClientFactory.setInstance(new MockNoquizzesAvailable());
		solo.assertCurrentActivity("Main activity", MainActivity.class);
		solo.clickOnButton("Take a Quiz");
		assertTrue("Search about No quizzes available text ",
				solo.searchText(NO_QUIZZES_AVAILABLE));
		solo.goBack();

	}

	public void testErrorCommunicationMessage() throws Exception {
		SwengHttpClientFactory.setInstance(new MockErrorCommunication());
		solo.clickOnButton("Take a Quiz");
		assertTrue("Search about Error Communication Text ",
				solo.searchText(ERROR_COMMUNICATION));
		solo.goBack();

	}

	public void testMalformedJson() {
		SwengHttpClientFactory.setInstance(new MockMalformedJsonResponse());
		solo.assertCurrentActivity("Main activity", MainActivity.class);
		solo.clickOnButton("Take a Quiz");
		assertTrue(!solo.searchText("invalid id"));
		solo.goBack();

	}
}

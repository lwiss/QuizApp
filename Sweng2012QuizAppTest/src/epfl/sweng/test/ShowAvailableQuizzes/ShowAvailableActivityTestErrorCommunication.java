package epfl.sweng.test.ShowAvailableQuizzes;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.test.ActivityInstrumentationTestCase2;

public class ShowAvailableActivityTestErrorCommunication extends
		ActivityInstrumentationTestCase2<ShowAvailableQuizzesActivity> {
	private Solo solo;
	private static final String ERROR_COMMUNICATION = "An error occurred while fetching quizzes.";

	public ShowAvailableActivityTestErrorCommunication() {
		super(ShowAvailableQuizzesActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockErrorCommunication());
		solo = new Solo(getInstrumentation(), getActivity());

	}

	public void testErrorCommunicationMessage() throws Exception {
		assertTrue("Search about Error Communication Text ",
				solo.searchText(ERROR_COMMUNICATION));


	}

}

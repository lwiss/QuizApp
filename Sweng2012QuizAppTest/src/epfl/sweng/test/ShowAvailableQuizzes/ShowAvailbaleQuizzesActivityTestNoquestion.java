package epfl.sweng.test.ShowAvailableQuizzes;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.test.ActivityInstrumentationTestCase2;

public class ShowAvailbaleQuizzesActivityTestNoquestion extends
		ActivityInstrumentationTestCase2<ShowAvailableQuizzesActivity> {
	private Solo solo;
	private static final String NO_QUIZZES_AVAILABLE = "There are no quizzes available at the moment.";

	public ShowAvailbaleQuizzesActivityTestNoquestion() {
		super(ShowAvailableQuizzesActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockNoquizzesAvailable());
		solo = new Solo(getInstrumentation(), getActivity());

	}

	public void testNoQuizzAvailableMesaage() {
		assertTrue("Search about No quizzes available text ",
				solo.searchText(NO_QUIZZES_AVAILABLE));

	}

}

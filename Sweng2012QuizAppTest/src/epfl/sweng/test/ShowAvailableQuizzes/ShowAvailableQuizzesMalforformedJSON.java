package epfl.sweng.test.ShowAvailableQuizzes;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.test.ActivityInstrumentationTestCase2;

public class ShowAvailableQuizzesMalforformedJSON extends
		ActivityInstrumentationTestCase2<ShowAvailableQuizzesActivity> {
	private Solo solo;

	public ShowAvailableQuizzesMalforformedJSON() {
		super(ShowAvailableQuizzesActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockMalformedJsonResponse());
		solo = new Solo(getInstrumentation(), getActivity());

	}

	public void testMalformedJson() {
		assertTrue(!solo.searchText("invalid id"));
	}

}

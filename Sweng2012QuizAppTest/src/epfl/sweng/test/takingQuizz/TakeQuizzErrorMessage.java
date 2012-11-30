package epfl.sweng.test.takingQuizz;

import java.util.ArrayList;
import java.util.List;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.quizzes.ShowQuizActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.test.ActivityInstrumentationTestCase2;

/**
 * 
 * @author MohamedBenArbia
 * 
 */
public class TakeQuizzErrorMessage extends
		ActivityInstrumentationTestCase2<ShowQuizActivity> {
	private Solo solo;

	public TakeQuizzErrorMessage() {
		super(ShowQuizActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockTakeUnavailableQuizz());
		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		ShowAvailableQuizzesActivity.setQuizzesIds(list);
		solo = new Solo(getInstrumentation(), getActivity());

	}

	public void testZerrorMessage() {
		assertTrue(solo.searchText("An error occurred while loading the quiz."));

	}

}

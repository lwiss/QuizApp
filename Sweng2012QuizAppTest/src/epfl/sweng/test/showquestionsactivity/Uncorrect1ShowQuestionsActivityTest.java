package epfl.sweng.test.showquestionsactivity;

import java.util.List;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * 
 * @author crazybhy
 * 
 */
public class Uncorrect1ShowQuestionsActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	private Solo solo;
	private List<String> answers;
	private int solution;
	private static final int TIME = 1000;

	public Uncorrect1ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClientUncorrect1());
		Thread.sleep(TIME);
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
		answers = getActivity().getQuizQuestion().getAnswers();
		solution = getActivity().getQuizQuestion().getSolutionIndex();
	}

	public void testUncorrectAllRatingsResponse() throws Exception {
		solo.clickOnText(answers.get(solution));
		solo.clickOnText("Next question");
		assertTrue("uncorrect data",
				solo.waitForText("There was an error retrieving the ratings"));
		assertTrue("user did not rated the question yet",
				solo.searchText("You have not rated this question"));
	}
	
}


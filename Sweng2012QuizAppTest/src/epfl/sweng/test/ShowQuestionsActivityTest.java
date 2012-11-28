package epfl.sweng.test;

import java.util.List;

import org.json.JSONException;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * 
 * @author crazybhy
 * 
 */
public class ShowQuestionsActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	private Solo solo;
	private List<String> answers;
	private int solution;
	private static final int TIME = 1000;

	public ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClientCR());
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
		getActivity().getQuizQuestion().getQuestion();
		answers = getActivity().getQuizQuestion().getAnswers();
		solution = getActivity().getQuizQuestion().getSolutionIndex();
	}

	public void testShowQuestion() throws JSONException {
		assertTrue(
				"Question is displayed",
				solo.searchText("What is the answer to life, the universe and everything?"));
		assertTrue("Correct answer is displayed", solo.searchText("42"));
		assertTrue("Incorrect answer is displayed",
				solo.searchText("24"));

		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}
	
	public void testFalseResponses() {
		solo.clickOnText("24");
		assertTrue("False answer showen as so",
				solo.searchText("24 \u2718"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}

	public void testTrueResponse() {
		solo.clickOnText("42");
		assertTrue("True answer showen as so",
				solo.searchText("42 \u2714"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertTrue("Next question button is enabled",
				nextQuestionButton.isEnabled());
	}

	public void testNoMoreEffects() {
		solo.clickOnText("42");
		solo.clickOnText("24");
		assertFalse("No effect showing false response",
				solo.searchText("\u2718"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertTrue("Next question button is still enabled", nextQuestionButton.isEnabled());
	}
	
	public void testClickButton() {
		solo.clickOnText("42");
		solo.clickOnText("Next question");
		assertTrue(
				"Question is displayed",
				solo.searchText("What is the answer to life, the universe and everything?"));
		assertTrue("Correct answer is displayed", solo.searchText("42"));
		assertTrue("Incorrect answer is displayed",
				solo.searchText("24"));

		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}

	public void testUIElements() {
		assertTrue("Like button exists", solo.searchButton("Like"));

		assertTrue("Dislike button exists", solo.searchButton("Dislike"));
		assertTrue("Incorrect button exists", solo.searchButton("Incorrect"));
		
	}

	public void testUncorrectAllRatingsResponse() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClientUncR());
		solo.clickOnText(answers.get(solution));
		solo.clickOnText("Next question");
		assertTrue("uncorrect data",
				solo.waitForText("There was an error retrieving the ratings"));
		assertTrue("user did not rated the question yet",
				solo.searchText("You have not rated this question"));
	}

	public void testCorrectRatingsResponse() throws Exception {
		assertTrue("correct number of Like ", solo.searchText("Like (10"));
		assertTrue("correct number of Dislike", solo.searchText("Dislike (3"));
		assertTrue("correct number of Incorrect ",
				solo.searchText("Incorrect (2"));
		assertTrue("correct verdict recieved",
				solo.searchText("You like the question"));
		assertFalse("thee is no uncorrect verdict recieved ",
				solo.searchText("You dislike the question"));
		assertFalse("thee is no uncorrect verdict recieved ",
				solo.searchText("You think the question is incorrect"));
		assertFalse("thee is no uncorrect verdict recieved ",
				solo.searchText("You have not rated this question"));
	}

	public void testUncorrectPersonalRatingResponse() throws Exception {
		SwengHttpClientFactory
				.setInstance(new MockHttpClientUncorrectRatingRes());
		solo.clickOnText(answers.get(solution));
		solo.clickOnText("Next question");
		assertTrue("uncorrect data",
				solo.waitForText("There was an error retrieving the ratings"));

	}
	
}


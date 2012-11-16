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
 * @author Eagles
 * 
 */
public class ShowQuestionsActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	private Solo solo;
	private String question;
	private List<String> answers;
	private int solution;
	private static final int TIME = 5000;

	public ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockHttpClientCR());
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
		question = (String) ShowQuestionsActivity.getQuestion().getText();
		answers = ShowQuestionsActivity.getAnswers();
		solution = ShowQuestionsActivity.getSolution();
	}

	public void testShowQuestion() throws JSONException {
		assertTrue("Question is displayed", solo.searchText(question));
		for (int i = 0; i < answers.size(); i++) {
			assertTrue("Answer " + i + " is displayed",
					solo.searchText(answers.get(i)));
		}
		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}

	public void testFalseResponses() {
		for (int i = 0; i < answers.size(); i++) {
			if (i != solution) {
				solo.clickOnText(answers.get(i));
				assertTrue("False answer " + i + " showen as so",
						solo.searchText(answers.get(i) + " \u2718"));
				Button nextQuestionButton = solo.getButton("Next question");
				assertFalse("Next question button is disabled",
						nextQuestionButton.isEnabled());
			}
		}
	}

	public void testTrueResponse() {
		solo.clickOnText(answers.get(solution));
		assertTrue("True answer " + solution + " showen as so",
				solo.searchText(answers.get(solution) + " \u2714"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertTrue("Next question button is enabled",
				nextQuestionButton.isEnabled());
	}

	public void testNoMoreEffects() {
		solo.clickOnText(answers.get(solution));
		for (int i = 0; i < answers.size(); i++) {
			solo.clickOnText(answers.get(i));
		}
	}

	/**
	 * This test asserts that the Like, Dislike and Incorrect buttons are are
	 * existing and are clickable
	 */

	public void testUIElements() {
		assertTrue("Like button exists", solo.searchButton("Like"));

		assertTrue("Dislike button exists", solo.searchButton("Dislike"));
		assertTrue("Incorrect button exists", solo.searchButton("Incorrect"));
		assertTrue("Like button is clickable", solo.getButton("Incorrect")
				.isClickable());
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

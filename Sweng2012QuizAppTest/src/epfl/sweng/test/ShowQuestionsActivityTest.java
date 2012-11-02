package epfl.sweng.test;

import java.util.List;

import org.json.JSONException;

import com.jayway.android.robotium.solo.Solo;

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
			//Button nextQuestionButton = solo.getButton("Next question");
			//assertTrue("Next question button is still enabled",
			//		nextQuestionButton.isEnabled());
		}
	}
}

package epfl.sweng.test;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * 
 * @author crazybhy
 *
 */
public class EditQuestionActivityTest extends
		ActivityInstrumentationTestCase2<EditQuestionActivity> {
	private Solo solo;
	private static final int TIME = 1000;

	public EditQuestionActivityTest() {
		super(EditQuestionActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
	}
	
	public void testInvalidQuestion() {
		assertTrue("EditText for question available",
				solo.searchText("Type in the question's text body"));
		solo.clearEditText(0);
		solo.enterText(0, "    ");
		assertTrue("First EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(1);
		solo.enterText(1, "3");
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(2);
		solo.enterText(2, "4");
		solo.clickOnText("\u2718", 2);
		assertTrue("EditText for tags available",
				solo.searchText("Type in the question's tags"));
		final int tagNumber = 3;
		solo.clearEditText(tagNumber);
		solo.enterText(tagNumber, "history-geography countries");
		solo.scrollDown();
		Button submit = solo.getButton("Submit");
		assertFalse("Submit button is disabled", submit.isEnabled());
	}

	public void testInvalidTags() {
		assertTrue("EditText for question available",
				solo.searchText("Type in the question's text body"));
		solo.clearEditText(0);
		solo.enterText(0, "2+2=?");
		assertTrue("First EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(1);
		solo.enterText(1, "3");
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(2);
		solo.enterText(2, "4");
		solo.clickOnText("\u2718", 2);
		assertTrue("EditText for tags available",
				solo.searchText("Type in the question's tags"));
		final int tagNumber = 3;
		solo.clearEditText(tagNumber);
		solo.enterText(tagNumber, "historygeographycountries");
		solo.scrollDown();
		Button submit = solo.getButton("Submit");
		assertFalse("Submit button is disabled", submit.isEnabled());
	}

	public void testInvalidAnswers() {
		assertTrue("EditText for question available",
				solo.searchText("Type in the question's text body"));
		solo.clearEditText(0);
		solo.enterText(0, "2+2=?");
		assertTrue("First EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(1);
		solo.enterText(1, " ");
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(2);
		solo.enterText(2, "4");
		solo.clickOnText("\u2718", 2);
		assertTrue("EditText for tags available",
				solo.searchText("Type in the question's tags"));
		final int tagNumber = 3;
		solo.clearEditText(tagNumber);
		solo.enterText(tagNumber, "history-geography countries");
		solo.scrollDown();
		Button submit = solo.getButton("Submit");
		assertFalse("Submit button is disabled", submit.isEnabled());
	}

	public void testAsnwersNumber() {
		solo.clickOnText("\u002D");
		assertTrue("At least two answers",
				solo.searchText("Cannot remove: At least two anwers"));
		final int maxAdds = 8;
		for (int i = 1; i <= maxAdds; i++) {
			solo.clickOnText("\u002B");
		}
		solo.clickOnText("\u002B");
		assertTrue("No more than ten answers",
				solo.searchText("Cannot add more answers"));
	}

	public void testAllValid() {
		assertTrue("EditText for question available",
				solo.searchText("Type in the question's text body"));
		solo.clearEditText(0);
		solo.enterText(0, "2+2=?");
		assertTrue("First EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(1);
		solo.enterText(1, "3");
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(2);
		solo.enterText(2, "4");
		solo.clickOnText("\u2718", 2);
		final int tagNumber = 3;
		solo.clearEditText(tagNumber);
		solo.enterText(tagNumber, "history-geography countries");
		solo.scrollDown();
		solo.scrollDown();
		Button submit = solo.getButton("Submit");
		assertTrue("Submit button is enabled", submit.isEnabled());
		solo.clickOnText("Submit");
	}

	public void testManipulationAnswers() {
		solo.clickOnText("\u002B");
		assertTrue("EditText for question available",
				solo.searchText("Type in the question's text body"));
		solo.clearEditText(0);
		solo.enterText(0, "2+2=?");
		assertTrue("First EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(1);
		solo.enterText(1, "3");
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(2);
		solo.enterText(2, "4");
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		final int additionalAnswer = 3;
		solo.clearEditText(additionalAnswer);
		solo.enterText(additionalAnswer, "5");
		final int tagNumber = 4;
		solo.clearEditText(tagNumber);
		solo.enterText(tagNumber, "history-geography countries");
		solo.clickOnText("\u2718");
		solo.clickOnText("\u2714");
		assertFalse("No answer is setted as true", solo.searchText("\u2714"));
		solo.clickOnText("\u2718");
		solo.clickOnText("\u2718");
		assertFalse("Only one answer is setted as true",
				solo.searchText("\u2714", 2));
		solo.clickOnText("\u002D", 2);
		assertFalse("No answer is setted as true", solo.searchText("\u2714"));
		solo.scrollDown();
		Button submit = solo.getButton("Submit");
		assertTrue("Submit button is enabled", submit.isEnabled());
		solo.clickOnText("Submit");
		assertTrue(
				"Error diplayed",
				solo.searchText("You have to choose exactly one correct answer"));
	}

	public void testValidationAnswers() {
		solo.clickOnText("\u002B");
		assertTrue("EditText for question available",
				solo.searchText("Type in the question's text body"));
		solo.clearEditText(0);
		solo.enterText(0, "2+2=?");
		assertTrue("First EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(1);
		solo.enterText(1, "3");
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		solo.clearEditText(2);
		assertTrue("Second EditText for answers available",
				solo.searchText("Type in the answer"));
		final int additionalAnswer = 3;
		solo.clearEditText(additionalAnswer);
		solo.enterText(additionalAnswer, "5");
		final int tagNumber = 4;
		solo.clearEditText(tagNumber);
		solo.enterText(tagNumber, "history-geography countries");
		solo.scrollDown();
		Button submit = solo.getButton("Submit");
		assertFalse("Submit button is disabled", submit.isEnabled());
		solo.clickOnText("\u002D", 2);
		assertTrue("Submit button is enabled", submit.isEnabled());
	}

	public void testSubmitClick() {

	}

	public void testSubmitEnabled() {
		solo.scrollDown();
		Button submit = solo.getButton("Submit");
		assertFalse("Submit button is disabled", submit.isEnabled());
	}
}

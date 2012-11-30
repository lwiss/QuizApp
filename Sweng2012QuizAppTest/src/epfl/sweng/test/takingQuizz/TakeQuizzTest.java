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
public class TakeQuizzTest extends
		ActivityInstrumentationTestCase2<ShowAvailableQuizzesActivity> {
	private Solo solo;

	private String question1 = "How much is 2 + 2 ?";
	private String question2 = "How much is 1 + 1 ?";
	private String question3 = "How is the best Team ?";
	private List<String> response1 = new ArrayList<String>();
	private List<String> response2 = new ArrayList<String>();
	private List<String> response3 = new ArrayList<String>();

	public TakeQuizzTest() {
		super(ShowAvailableQuizzesActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		SwengHttpClientFactory.setInstance(new MockTakeQuizAvailable());
		response1.add("5, for very large values of 2");
		response1.add("4, if you're out of inspiration");
		response1.add("10, for some carefully chosen base");
		response2.add("2");
		response2.add("10");
		response2.add("11");
		response2.add("It all depends on the semantics of the '+' operator");
		response3.add("Eagles");
		response3.add("Basters");
		response3.add("Cats");

	}

	public void testZerrorMessage() {
		solo.clickOnText("easy quizz");
		SwengHttpClientFactory.setInstance(new MockTakeUnavailableQuizz());
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);
		assertTrue(solo.searchText("An error occurred while loading the quiz."));
		solo.goBack();
	}

	public void testFirstQuestionDisplayed() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);
		assertTrue(
				"Question displayed is the first question in the list returned by the server ",
				solo.searchText(question1));
		solo.goBack();

	}

	public void testNoquestionMarkedWithAsterisk() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);

		assertTrue(!solo.searchText(" \u2724"));
		solo.clickOnButton("Next question");
		assertTrue(!solo.searchText(" \u2724"));
		solo.clickOnButton("Next question");
		assertTrue(!solo.searchText(" \u2724"));

		solo.goBack();

	}

	public void testButtons() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);

		solo.searchButton("Next question");
		solo.searchButton("Hand in quiz");
		solo.searchButton("Previous question");
		solo.goBack();

	}

	public void testOederOfQuestions() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);

		assertTrue(solo.searchText(question1));
		solo.clickOnButton("Next question");
		assertTrue(solo.searchText(question2));
		solo.clickOnButton("Next question");
		assertTrue(solo.searchText(question3));
		solo.clickOnButton("Next question");
		assertTrue(solo.searchText(question1));
		solo.clickOnButton("Previous question");
		assertTrue(solo.searchText(question3));

		solo.goBack();

	}
	

	public void testListOfPossibleAnswers() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);
		assertTrue("response 1 of question1  displayed on text View",
				solo.searchText(response1.get(0)));
		assertTrue("response 2 of question1 displayed on text View",
				solo.searchText(response1.get(1)));
		assertTrue("response 3 of question1 displayed on text View",
				solo.searchText(response1.get(2)));
		solo.clickOnButton("Next question");
		assertTrue("response 1 of question2  displayed on text View",
				solo.searchText(response2.get(0)));
		assertTrue("response 2 of question2 displayed on text View",
				solo.searchText(response2.get(1)));
		assertTrue("response 3 of question2 displayed on text View",
				solo.searchText(response2.get(2)));
		solo.clickOnButton("Next question");
		assertTrue("response 1 of question3  displayed on text View",
				solo.searchText(response3.get(0)));
		assertTrue("response 2 of question3 displayed on text View",
				solo.searchText(response3.get(1)));
		assertTrue("response 3 of question3 displayed on text View",
				solo.searchText(response3.get(2)));
		solo.goBack();

	}

	public void testFindAsteriskSymbol() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);
		solo.clickOnText(response1.get(0));
		assertTrue(solo.searchText(response1.get(0) + " \u2724"));
		solo.clickOnText(response1.get(1));
		assertTrue(solo.searchText(response1.get(1) + " \u2724"));
		solo.clickOnText(response1.get(2));
		assertTrue(solo.searchText(response1.get(2) + " \u2724"));

		solo.clickOnButton("Next question");

		solo.clickOnText(response2.get(0));
		assertTrue(solo.searchText(response2.get(0) + " \u2724"));
		solo.clickOnText(response2.get(1));
		assertTrue(solo.searchText(response2.get(1) + " \u2724"));
		solo.clickOnText(response2.get(2));
		assertTrue(solo.searchText(response2.get(2) + " \u2724"));

		solo.clickOnButton("Next question");

		solo.clickOnText(response3.get(0));
		assertTrue(solo.searchText(response3.get(0) + " \u2724"));
		solo.clickOnText(response3.get(1));
		assertTrue(solo.searchText(response3.get(1) + " \u2724"));
		solo.clickOnText(response3.get(2));
		assertTrue(solo.searchText(response3.get(2) + " \u2724"));

		solo.goBack();
	}

	public void testFindAtMostOneAsteriskSymbol() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);
		solo.clickOnText(response1.get(0));
		assertTrue(!solo.searchText(" \u2724", 2));
		solo.clickOnText(response1.get(1));
		assertTrue(!solo.searchText(" \u2724", 2));
		solo.clickOnText(response1.get(2));
		assertTrue(!solo.searchText(" \u2724", 2));

		solo.clickOnButton("Next question");

		solo.clickOnText(response2.get(0));
		assertTrue(!solo.searchText(" \u2724", 2));
		solo.clickOnText(response2.get(1));
		assertTrue(!solo.searchText(" \u2724", 2));
		solo.clickOnText(response2.get(2));
		assertTrue(!solo.searchText(" \u2724", 2));

		solo.clickOnButton("Next question");

		solo.clickOnText(response3.get(0));
		assertTrue(!solo.searchText(" \u2724", 2));
		solo.clickOnText(response3.get(1));
		assertTrue(!solo.searchText(" \u2724", 2));
		solo.clickOnText(response3.get(2));
		assertTrue(!solo.searchText(" \u2724", 2));

		solo.goBack();

	}

	public void testRemoveAsteriskSymbol() {
		solo.clickOnText("easy quizz");
		solo.assertCurrentActivity("ShowQuizActivity", ShowQuizActivity.class);

		solo.clickOnText(response1.get(0));
		solo.clickOnText(response1.get(0));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnText(response1.get(1));
		solo.clickOnText(response1.get(1));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnText(response1.get(2));
		solo.clickOnText(response1.get(2));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnButton("Next question");

		solo.clickOnText(response2.get(0));
		solo.clickOnText(response2.get(0));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnText(response2.get(1));
		solo.clickOnText(response2.get(1));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnText(response2.get(2));
		solo.clickOnText(response2.get(2));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnButton("Next question");

		solo.clickOnText(response3.get(0));
		solo.clickOnText(response3.get(0));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnText(response3.get(1));
		solo.clickOnText(response3.get(1));
		assertTrue(!solo.searchText(" \u2724"));

		solo.clickOnText(response3.get(2));
		solo.clickOnText(response3.get(2));
		assertTrue(!solo.searchText(" \u2724"));

		solo.goBack();

	}

}

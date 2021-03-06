package epfl.sweng.test.handingQuizzAnswer;

import java.util.ArrayList;
import java.util.List;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.quizzes.ShowQuizActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.takingQuizz.MockTakeQuizAvailable;
import android.test.ActivityInstrumentationTestCase2;

/**
 * This class is responsible for the Hand In of the quizzes
 * 
 * @author MohamedBenArbia
 * 
 */
public class HandingInQuizzTest extends
		ActivityInstrumentationTestCase2<ShowQuizActivity> {
	private Solo solo;
	private List<String> response1 = new ArrayList<String>();
	private List<String> response2 = new ArrayList<String>();
	private List<String> response3 = new ArrayList<String>();

	public HandingInQuizzTest() {
		super(ShowQuizActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		SwengHttpClientFactory.setInstance(new MockTakeQuizAvailable());
		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		ShowAvailableQuizzesActivity.setQuizzesIds(list);
		solo = new Solo(getInstrumentation(), getActivity());
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

	public void testAlertDialog() {
		solo.clickOnText(response1.get(0));
		solo.clickOnButton("Next question");
		solo.clickOnText(response2.get(2));
		solo.clickOnButton("Next question");
		solo.clickOnText(response3.get(0));
		solo.clickOnButton("Hand in quiz");
		assertTrue(solo.searchText("Your score is 1.25"));
		assertTrue(solo.searchButton("OK"));
		solo.clickOnButton("OK");
		assertTrue(!solo.searchText("Your score is 1.25"));
		solo.assertCurrentActivity("Show quizz Activity",
				ShowQuizActivity.class);
		

	}

}

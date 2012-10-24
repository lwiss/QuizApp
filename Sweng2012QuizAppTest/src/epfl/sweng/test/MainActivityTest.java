package epfl.sweng.test;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.entry.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * 
 * @author Eagles
 * 
 */
public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private static final int TIME = 5000;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(TIME);
	}

	protected void testEditQuestionActivity() {
		solo.clickOnText("");
	}

}

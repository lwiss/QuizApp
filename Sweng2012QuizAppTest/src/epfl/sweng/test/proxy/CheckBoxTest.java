package epfl.sweng.test.proxy;


import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.entry.MainActivity;

/**
 * 
 * @author MohamedBenArbia
 * 
 */
public class CheckBoxTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;


	public CheckBoxTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
          
		solo = new Solo(getInstrumentation(), getActivity());
		
	}

	public void testATickedCheckBox() {
		/**
		assertTrue(solo.searchText("Offline mode"));
        assertTrue(MainActivity.isOnline());
	
		solo.clickOnCheckBox(0);
		solo.sleep(1);
		assertTrue(!MainActivity.isOnline());

		solo.clickOnCheckBox(0);
		solo.sleep(1);
		assertTrue(MainActivity.isOnline());
		*/
	}

}

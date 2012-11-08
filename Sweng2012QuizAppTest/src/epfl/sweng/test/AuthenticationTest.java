package epfl.sweng.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.AuthenticationActivity;
/**
 * 
 * @author crazybhy
 *
 */
public class AuthenticationTest extends
		ActivityInstrumentationTestCase2<AuthenticationActivity> {

	public AuthenticationTest() {
		super(AuthenticationActivity.class);
		// TODO Auto-generated constructor stub
	}

	public void testAuthentication() {
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.clickOnText("GASPAR Username");
		Button login = solo.getButton("Log in using Tequila");
		assertTrue("Buton enabled", login.isEnabled());
	}

}

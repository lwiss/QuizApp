package epfl.sweng.test;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.AuthenticationActivity;

public class AuthenticationTest extends
		ActivityInstrumentationTestCase2<AuthenticationActivity> {

	public AuthenticationTest() {
		super(AuthenticationActivity.class);
		// TODO Auto-generated constructor stub
	}

	public void testAuthentication() {
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.clickOnText("GASPAR Username");
		solo.clickOnText("Log in using Tequila");
	}

}

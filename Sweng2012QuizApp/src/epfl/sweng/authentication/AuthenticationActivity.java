package epfl.sweng.authentication;

import epfl.sweng.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author crazybhy
 * 
 */
public class AuthenticationActivity extends Activity {
	private EditText usernameEdit;
	private EditText passwordEdit;
	private Button loginButton;

	public EditText getUsernameEdit() {
		return usernameEdit;
	}

	public EditText getPassowrdEdit() {
		return passwordEdit;
	}

	public Button getLoginButton() {
		return loginButton;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		initializeActivity();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_authentication, menu);
		return true;
	}

	public void initializeActivity() {
		usernameEdit = (EditText) findViewById(R.id.username);
		passwordEdit = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login);
		usernameEdit.setText("");
		passwordEdit.setText("");
	}

	public void login(View v) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new LoginTequilaAsyncTask().execute(usernameEdit.getText()
					.toString(), passwordEdit.getText().toString(), this);
		}

	}

}

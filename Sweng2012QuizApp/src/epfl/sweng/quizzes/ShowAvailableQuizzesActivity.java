package epfl.sweng.quizzes;

import epfl.sweng.R;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

/**
 * 
 * @author crazybhy
 *
 */
public class ShowAvailableQuizzesActivity extends Activity {
	
	private ListView list;
	private TextView text;
	
	public ListView getList() {
		return list;
	}

	public TextView getText() {
		return text;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_available_quizzes);
		list = (ListView) findViewById(android.R.id.list);
		text = (TextView) findViewById(android.R.id.empty);
		new ListAvailableQuizzesAsyncTask().execute(this);
	}

}

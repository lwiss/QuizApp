package epfl.sweng.quizzes;

import java.util.ArrayList;
import java.util.List;

import epfl.sweng.R;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * 
 * @author crazybhy
 *
 */
public class ShowAvailableQuizzesActivity extends Activity {
	
	private ListView list;
	private TextView text;
	private static int chosenQuizId;
	private static List<Integer> quizzesIds= new ArrayList<Integer>();
	
	public static int getChosenQuizId() {
		return chosenQuizId;
	}

	public static List<Integer> getQuizzesIds() {
		return quizzesIds;
	}

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
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				chosenQuizId=position;
				Intent intent = new Intent(ShowAvailableQuizzesActivity.this,
						ShowQuizActivity.class);
				startActivity(intent);
			}
		});
		new ListAvailableQuizzesAsyncTask().execute(this);
	}

}

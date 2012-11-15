package epfl.sweng.showquestions;

import epfl.sweng.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author crazybhy
 *
 */
public class ShowQuestionsActivity extends Activity {
	private TextView questionView;
	private ListView answersView;
	private Button nextQuestionButton;
	
    public TextView getQuestionView() {
		return questionView;
	}

	public ListView getAnswersView() {
		return answersView;
	}

	public Button getNextQuestionButton() {
		return nextQuestionButton;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        
        questionView = (TextView) findViewById(R.id.question);
        answersView = (ListView) findViewById(R.id.answers);
        nextQuestionButton = (Button) findViewById(R.id.nextQuestion);
        
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        	inizialiseActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_questions, menu);
        return true;
    }
    
    public void inizialiseActivity() {
    	new FetchQuestionAsyncTask().execute(this);
    }
}

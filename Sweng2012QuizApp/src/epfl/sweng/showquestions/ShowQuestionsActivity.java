package epfl.sweng.showquestions;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
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
	private Button like;
	private Button dislike;
	private Button incorrect;
	private TextView userRating;
	private QuizQuestion quizQuestion;
	private String sessionId;
	
    public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}

	public void setQuizQuestion(QuizQuestion question) {
		quizQuestion = question;
	}

	public TextView getQuestionView() {
		return questionView;
	}

	public ListView getAnswersView() {
		return answersView;
	}

	public Button getNextQuestionButton() {
		return nextQuestionButton;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String id) {
		sessionId = id;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        
        questionView = (TextView) findViewById(R.id.question);
        answersView = (ListView) findViewById(R.id.answers);
        nextQuestionButton = (Button) findViewById(R.id.nextQuestion);
        like = (Button) findViewById(R.id.like);
        dislike = (Button) findViewById(R.id.dislike);
        incorrect = (Button) findViewById(R.id.incorrect);
        userRating = (TextView) findViewById(R.id.userRating);
        
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        	initializeActivity();
        }
    }

    public Button getLike() {
		return like;
	}

	public Button getDislike() {
		return dislike;
	}

	public Button getIncorrect() {
		return incorrect;
	}

	public TextView getUserRating() {
		return userRating;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_questions, menu);
        return true;
    }
    
    public void initializeActivity() {
    	new FetchQuestionAsyncTask().execute(this);
    	new GetRatingsAsyncTask().execute(this);
    }
    
	public void like(View view) {
		new PostRatingAsyncTask().execute(this, "like");
		new GetRatingsAsyncTask().execute(this);

	}

	public void dislike(View view) {
		new PostRatingAsyncTask().execute(this, "dislike");
		new GetRatingsAsyncTask().execute(this);

	}

	public void incorrect(View view) {
		new PostRatingAsyncTask().execute(this, "incorrect");
		new GetRatingsAsyncTask().execute(this);

	}
    
}

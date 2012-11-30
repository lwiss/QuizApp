package epfl.sweng.quizzes;

import java.util.ArrayList;

import epfl.sweng.R;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author wissem(wissem.allouchi@epfl.ch) This activity shows a particular
 *         question chosen from the available quizzes that are displayed by the
 *         ShowAvailableQuizzesActivity
 * 
 */
public class ShowQuizActivity extends Activity {
	private String sessionId;

	/**
	 * this index indicates which question is displayed at a particular moment,
	 * initially set to zero because when first started, this activity must show
	 * the first question in the list
	 */
	private int questionIndex = 0;
	private int numberOfQuestions = 0;
	private int quizId;
	private ArrayList<QuizQuestion> quizQuestionList = new ArrayList<QuizQuestion>();
	private LinearLayout ansList;
	private TextView questionText;
	/**
	 * 
	 */
	private int[] chosenAns;
	private static final char HEAVY_FOUR_BALLOON_SPOKED_ASTERISK = '\u2724';

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_quiz);
		SharedPreferences preference = getSharedPreferences(
				MainActivity.PREF_NAME, Activity.MODE_PRIVATE);
		sessionId = preference.getString("SESSION_ID", null);
		quizId = ShowAvailableQuizzesActivity.getQuizzesIds().get(
				ShowAvailableQuizzesActivity.getChosenQuizId());
		ansList = (LinearLayout) findViewById(R.id.answersList);
		questionText = (TextView) findViewById(R.id.questionText);
		new RetrieveQuizAsyncTask().execute(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_show_quiz, menu);
		return true;
	}

	/**
	 * shows the question at position index from the list quizQuestionsList
	 * 
	 * @param int index the index of the question to show
	 */
	public void showQuestion(int index) {
		QuizQuestion q = quizQuestionList.get(index);
		ansList.removeAllViews();
		if (q != null) {
			int userAns = chosenAns[index];
			for (int i = 0; i < q.getAnswers().size(); i++) {
				LinearLayout ans = (LinearLayout) LinearLayout.inflate(this,
						R.layout.answer, null);
				((TextView) ans.getChildAt(0)).setText(q.getAnswers().get(i));
				if (i == userAns) {
					String a = ((TextView) ans.getChildAt(0)).getText()
							.toString();
					((TextView) ans.getChildAt(0)).setText(a + " "
							+ HEAVY_FOUR_BALLOON_SPOKED_ASTERISK);
				}
				ansList.addView(ans);
			}
			questionText.setText(q.getQuestion());

		} else {
			questionText
					.setText(RetrieveQuizAsyncTask.COMMUNICATION_ERROR_MESSAGE);
		}

	}

	/**
	 * stores the user's answer for a given question in the chosenAnswers array
	 * and the refreshes the UI
	 * 
	 * @param v
	 *            the answer being clicked
	 */
	public void onAnswerClick(View v) {
		LinearLayout answersList = (LinearLayout) v.getParent();
		int userAnswer = answersList.indexOfChild(v);
		chosenAns[questionIndex] = userAnswer;
		ansList.removeAllViews();
		showQuestion(questionIndex);
	}

	public void next(View v) {
		questionIndex = (questionIndex + 1) % numberOfQuestions;
		showQuestion(questionIndex);
	}

	public void previous(View v) {
		if (questionIndex == 0) {
			questionIndex = numberOfQuestions - 1;
		} else {
			questionIndex--;
		}
		showQuestion(questionIndex);
	}

	public void handInQuiz(View v) {
		new HandInQuizzAsyncTask().execute(this, chosenAns);
	}

	public int getQuizId() {
		return quizId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setQuizQuestionList(ArrayList<QuizQuestion> qQuestionList) {
		this.quizQuestionList = qQuestionList;

	}

	public ArrayList<QuizQuestion> getQuizQuestionList() {
		return quizQuestionList;
	}

	public int[] getChosenAns() {
		return chosenAns;
	}

	public void setChosenAns(int[] chosenAnswers) {
		chosenAns = chosenAnswers;
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(int numOfQuestions) {
		numberOfQuestions = numOfQuestions;
	}

	public TextView getQuestionText() {
		return questionText;
	}

}

package epfl.sweng.editquestions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @author crazybhy
 * 
 */
public class EditQuestionActivity extends Activity {
	private EditText questionEdit;
	private LinearLayout answersLayout;
	private EditText tagsEdit;
	private Button addButton;
	private Button submitButton;
	private List<Boolean> answersState;
	private boolean questionState = false;
	private boolean tagState = true;
	private int solutionIndex = -1;
	private static final int MAX_POSSIBLE_ANSWERS = 10;
	private static final int MIN_POSSIBLE_ANSWERS = 2;
	private static final String HEAVY_BALLOT = "\u2718";
	private static final String HEAVY_CHECK_MARK = "\u2714";

	public EditText getQuestionEdit() {
		return questionEdit;
	}

	public LinearLayout getAnswersEdit() {
		return answersLayout;
	}

	public EditText getTagsEdit() {
		return tagsEdit;
	}

	public Button getAddButton() {
		return addButton;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public boolean getFinalAnswersState() {
		for (int i = 0; i < answersState.size(); i++) {
			if (!answersState.get(i)) {
				return false;
			}
		}
		return true;
	}

	public List<Boolean> getAnswersState() {
		return answersState;
	}

	public void setAnswersState(List<Boolean> state) {
		this.answersState = state;
	}

	public boolean getQuestionState() {
		return questionState;
	}

	public boolean getTagState() {
		return tagState;
	}

	public void setQuestionState(boolean state) {
		questionState = state;
	}

	public void setTagState(boolean state) {
		tagState = state;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeActivity();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_question, menu);
		return true;
	}

	public void initializeActivity() {
		setContentView(R.layout.activity_edit_question);

		questionEdit = (EditText) findViewById(R.id.question);
		answersLayout = (LinearLayout) findViewById(R.id.answers);
		tagsEdit = (EditText) findViewById(R.id.tags);
		addButton = (Button) findViewById(R.id.addAnswer);
		submitButton = (Button) findViewById(R.id.submit);

		answersState = new ArrayList<Boolean>();

		questionEdit.addTextChangedListener(new TextEditWatcher(this,
				questionEdit));
		tagsEdit.addTextChangedListener(new TextEditWatcher(this, tagsEdit));

		addAnswer(addButton);
		addAnswer(addButton);
	}

	public void addAnswer(View view) {
		if (answersLayout.getChildCount() < MAX_POSSIBLE_ANSWERS) {
			LinearLayout answerLayout = (LinearLayout) LinearLayout.inflate(
					this, R.layout.answer_edit, null);
			answerLayout.setBackgroundColor(Color.RED);
			answersState.add(false);
			submitButton.setEnabled(false);
			EditText reponse = (EditText) answerLayout.getChildAt(0);
			reponse.addTextChangedListener(new TextEditWatcher(this, reponse));
			answersLayout.addView(answerLayout);

		} else {
			Toast.makeText(this, "Cannot add more answers!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void removeAnswer(View view) {
		Button button = (Button) view;
		if (answersLayout.getChildCount() > MIN_POSSIBLE_ANSWERS) {
			if (solutionIndex == answersLayout
					.indexOfChild((LinearLayout) (button.getParent()))) {
				solutionIndex = -1;
			}
			answersState.remove(answersLayout
					.indexOfChild((LinearLayout) button.getParent()));
			answersLayout.removeView((LinearLayout) button.getParent());
			if (getQuestionState() && getTagState() && getFinalAnswersState()) {
				submitButton.setEnabled(true);
			}
		} else {
			Toast.makeText(this, "Cannot remove: At least two anwers",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void submit(View view) {
		if (solutionIndex == -1) {
			Toast.makeText(this, "You have to choose exactly one correct answer", Toast.LENGTH_SHORT).show();
		} else {
			String question = questionEdit.getText().toString();
			List<String> answers = new ArrayList<String>();
			for (int i = 0; i < answersLayout.getChildCount(); i++) {
				EditText answerText = (EditText) ((LinearLayout) answersLayout
						.getChildAt(i)).getChildAt(0);
				answers.add(answerText.getText().toString());
			}
			Set<String> tags = new HashSet<String>();
			String tagsString = tagsEdit.getText().toString();
			String[] tagsTab = tagsString.split("[^a-zA-Z0-9]");
			tags.toArray(tagsTab);
			QuizQuestion quizQuestion = new QuizQuestion(question, answers,
					solutionIndex, tags, -1, null);
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				new SubmitQuestionAsyncTask().execute(quizQuestion, this);
				initializeActivity();
			}
		}
	}

	public void markAs(View view) {
		Button button = (Button) view;
		CharSequence text = button.getText();
		LinearLayout correspondingAnswerField = (LinearLayout) button
				.getParent();
		if (text.equals(HEAVY_BALLOT)) {
			if (solutionIndex != -1) {
				((LinearLayout) answersLayout.getChildAt(solutionIndex)).setBackgroundColor(Color.RED);
				((Button) ((LinearLayout) answersLayout
						.getChildAt(solutionIndex)).getChildAt(1))
						.setText(HEAVY_BALLOT);
			}
			button.setText(HEAVY_CHECK_MARK);
			correspondingAnswerField.setBackgroundColor(Color.GREEN);
			solutionIndex = answersLayout.indexOfChild((LinearLayout) (button
					.getParent()));

		} else {
			button.setText(HEAVY_BALLOT);
			correspondingAnswerField.setBackgroundColor(Color.RED);
			solutionIndex = -1;
		}
	}
	
	public int auditErrors() {
		int nbErrors = 0;
		/*
		// Exercise 2:
		if (!correctQuestionEditText()) {
			nbErrors++;
		}
		if (!correctAnswersEditText()) {
			nbErrors++;
		}
		if (!correctTagsEditText()) {
			nbErrors++;
		}
		// Exercise 3:
		if (!existAddButton()) {
			nbErrors++;
		}
		if (!existSubmitButton()) {
			nbErrors++;
		}
		if (!existRemoveAnswerButtons()) {
			nbErrors++;
		}
		if (!existMarkAsAnswerButtons()) {
			nbErrors++;
		}
		// Exercise 4:
		if (!maxOneCorrectAnswers()) {
			nbErrors++;
		}
		// Exercise 5:
		if(!okSubmitButtonStatut())
		{
			nbErrors++;
		}
		*/
		return nbErrors;
	}
	/*
	// Priority 2.1:
	public boolean correctQuestionEditText() {
		EditText question = (EditText) findViewById(R.id.question);
		if (question != null
				&& question.getHint().equals("Type in the question's text body")
				&& question.getVisibility() == EditText.VISIBLE) {
			return true;
		}
		return false;
	}

	// Priority 2.2:
	public boolean correctAnswersEditText() {
		LinearLayout answersContainer = (LinearLayout) findViewById(R.id.answers);
		if (answersContainer != null && answersContainer.getChildCount() > 0) {
			for (int i = 0; i < answersContainer.getChildCount(); i++) {
				LinearLayout answerLine = (LinearLayout) answersContainer
						.getChildAt(i);
				EditText answerText = (EditText) answerLine.getChildAt(0);
				if (!answerText.getHint().equals("Type in the answer")
						|| answerText.getVisibility() != EditText.VISIBLE) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	// Priority 2.3:
	public boolean correctTagsEditText() {
		EditText tags = (EditText) findViewById(R.id.tags);
		if (tags != null && tags.getHint().equals("Type in the question's tags")
				&& tags.getVisibility() == EditText.VISIBLE) {
			return true;
		}
		return false;
	}

	// Priority 3.1
	public boolean existAddButton() {
		Button addButton = (Button) findViewById(R.id.addAnswer);
		if (addButton != null && addButton.getText().equals("\u002B")
				&& addButton.getVisibility() == Button.VISIBLE) {
			return true;
		}
		return false;
	}

	// Priority 3.2:
	public boolean existSubmitButton() {
		Button submitButton = (Button) findViewById(R.id.submit);
		if (submitButton != null && submitButton.getText().equals("Submit")
				&& submitButton.getVisibility() == Button.VISIBLE) {
			return true;
		}
		return false;
	}

	// Priority 3.3
	public boolean existRemoveAnswerButtons() {
		LinearLayout answersContainer = (LinearLayout) findViewById(R.id.answers);
		for (int i = 0; i < answersContainer.getChildCount(); i++) {
			LinearLayout answerLine = (LinearLayout) answersContainer
					.getChildAt(i);
			Button removeAnswerButton = (Button) answerLine.getChildAt(2);
			if (!removeAnswerButton.getText().equals("\u002D")
					|| removeAnswerButton.getVisibility() != Button.VISIBLE) {
				return false;
			}
		}
		return true;
	}

	// Priority 3.4
	public boolean existMarkAsAnswerButtons() {
		LinearLayout answersContainer = (LinearLayout) findViewById(R.id.answers);
		for (int i = 0; i < answersContainer.getChildCount(); i++) {
			LinearLayout answerLine = (LinearLayout) answersContainer
					.getChildAt(i);
			Button markAsAnswerButton = (Button) answerLine.getChildAt(1);
			if ((!markAsAnswerButton.getText().equals(HEAVY_BALLOT) && !markAsAnswerButton
					.getText().equals(HEAVY_CHECK_MARK))
					|| markAsAnswerButton.getVisibility() != Button.VISIBLE) {
				return false;
			}
		}
		return true;
	}
	// Priority 4
	public boolean maxOneCorrectAnswers() {
		LinearLayout answersContainer = (LinearLayout) findViewById(R.id.answers);
		int numberCorrectAnswers = 0;
		for (int i = 0; i < answersContainer.getChildCount(); i++) {
			LinearLayout answerLine = (LinearLayout) answersContainer
					.getChildAt(i);
			Button markAsAnswerButton = (Button) answerLine.getChildAt(1);
			if (markAsAnswerButton.getText().equals(HEAVY_CHECK_MARK))
				numberCorrectAnswers++;
			}
		if (numberCorrectAnswers<=1) {
			return true;
		}
		return false;
	}
	// Priority 5
	public boolean questionIsOK() {
		EditText question = (EditText) findViewById(R.id.question);
		if(!QuizQuestion.questionIsOK(question.getText().toString())) {
			return false;
		}
		LinearLayout answersContainer = (LinearLayout) findViewById(R.id.answers);
		if (answersContainer.getChildCount()<2 || answersContainer.getChildCount()>10)
		{
			return false;
		}
		for (int i = 0; i < answersContainer.getChildCount(); i++) {
			LinearLayout answerLine = (LinearLayout) answersContainer
					.getChildAt(i);
			EditText answerText = (EditText) answerLine.getChildAt(0);
			if (!QuizQuestion.answerIsOK(answerText.getText().toString())) {
				return false;
			}
		}
		if (solutionIndex==-1) {
			return false;
		}
		EditText tags = (EditText) findViewById(R.id.question);
		if (!QuizQuestion.tagsAreOK(tags.getText().toString())) {
			return false;
		}
		return true;
	}
	public boolean okSubmitButtonStatut() {
		Button submitButton = (Button) findViewById(R.id.submit);
		return (questionIsOK()==submitButton.isEnabled());
	}
	*/
}

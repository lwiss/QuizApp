package epfl.sweng.editquestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.R;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @author Eagles
 * 
 */
public class EditQuestionActivity extends Activity {

	/**
	 * a GroupView that contains all possible answers
	 */
	private LinearLayout l;
	/**
	 * Indicates how many answers are accepted
	 */
	private static final int MAX_POSSIBLE_ANSWERS = 10;
	private static final String HEAVY_BALLOT = "\u2718";
	private static final String HEAVY_CHECK_MARK = "\u2714";
	private static final String SERVER_URL = "http://sweng-quiz.appspot.com";
	private static List<Boolean> answersState;
	private static Boolean questionState = false;
	private static Boolean tagState = true;
	private static Button submit;
	private static final int MIN_ANSWERS_NUMBER = 2;

	public static Button getSubmit() {
		return submit;
	}

	public static void setSubmit(Button submitButton) {
		EditQuestionActivity.submit = submitButton;
	}

	public static Boolean getFinalAnswersState() {
		for (int i = 0; i < answersState.size(); i++) {
			if (!answersState.get(i)) {
				return false;
			}
		}
		return true;
	}

	public static void setQuestionState(Boolean questionstate) {
		EditQuestionActivity.questionState = questionstate;
	}

	public Button getCorrectAns() {
		return correctAns;
	}

	public void setCorrectAns(Button correctans) {
		this.correctAns = correctans;
	}

	public static void setAnswersState(List<Boolean> answersstate) {
		EditQuestionActivity.answersState = answersstate;
	}

	public static void setTagState(Boolean tagstate) {
		EditQuestionActivity.tagState = tagstate;
	}

	public static List<Boolean> getAnswersState() {
		return answersState;
	}

	public static Boolean getQuestionState() {
		return questionState;
	}

	public static Boolean getTagState() {
		return tagState;
	}

	private Button correctAns;
	private QuizQuestion quizzQuestion;
	private static final String TAG = "MyActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "On create");
		initialiseView();

		/**
		 * l = (LinearLayout) findViewById(R.id.answersContainer); LinearLayout
		 * l1 = (LinearLayout) LinearLayout.inflate(this, R.layout.answer_field,
		 * null); l1.setBackgroundColor(Color.RED); EditText reponse =
		 * (EditText) l1.getChildAt(0); reponse.addTextChangedListener(new
		 * TextEditWatcher(reponse)); answersState.add(false); l.addView(l1);
		 */

	}

	public QuizQuestion createQuizzQuestion() {
		EditText question = (EditText) findViewById(R.id.questionBody);
		String questionField = question.getText().toString();
		List<String> answers = new ArrayList<String>();
		for (int i = 0; i < l.getChildCount(); i++) {
			EditText answerText = (EditText) ((LinearLayout) l.getChildAt(i))
					.getChildAt(0);
			answers.add(answerText.getText().toString());

		}

		int solutionIndex = -1;
		if (correctAns != null) {
			solutionIndex = l.indexOfChild((LinearLayout) (correctAns
					.getParent()));
		}
		Set<String> tags = new HashSet<String>();
		EditText tag = (EditText) findViewById(R.id.questionTags);
		String tagsField = tag.getText().toString();
		String[] tagsTab = tagsField.split("[^a-zA-Z0-9]");
		tags.toArray(tagsTab);
		return new QuizQuestion(questionField, answers, solutionIndex, tags,
				-1, null);
	}

	public int auditErrors() {
		/*
		int errors = 0;
		LinearLayout layout = (LinearLayout) findViewById(R.id.answersContainer);
		EditText question = (EditText) findViewById(R.id.questionBody);
		EditText tags = (EditText) findViewById(R.id.questionTags);
		Button addButton = (Button) findViewById(R.id.addAnswerButton);
		Button submitButton = (Button) findViewById(R.id.submitButton);
		QuizQuestion quizzQues = createQuizzQuestion();
		if (!verifyQuestionwidget(question)) {
			errors++;

		}
		if (!verifyAnswersEditText(layout)) {
			errors++;

		}
		if (!verifyTagsWidget(tags)) {
			errors++;

		}

		if (!verifyButtons(addButton, "\u002B", Button.VISIBLE)) {
			errors++;

		}
		if (!verifyButtons(submitButton, "Submit", Button.VISIBLE)) {
			errors++;

		}
		if (!verifyAnswersButton(layout, "\u002D", "\u002D", Button.VISIBLE, 2)) {
			errors++;

		}
		if (!verifyAnswersButton(layout, HEAVY_CHECK_MARK, HEAVY_BALLOT,
				Button.VISIBLE, 1)) {
			errors++;

		}
		if (!verifyOneCorrectAnswers(layout)) {
			errors++;

		}
		if (quizzQues.auditErrors(0) > 0 && submitButton.isEnabled()) {
			errors++;

		}
		if (quizzQues.auditErrors(0) == 0 && !submitButton.isEnabled()) {
			errors++;

		}
		return errors;
*/
		return 0;
	}

	public boolean verifyOneCorrectAnswers(LinearLayout layout) {
		int numberOfAnswers = layout.getChildCount();
		int numberOfrightAnswers = 0;
		for (int i = 0; i < numberOfAnswers; i++) {
			Button button = (Button) ((LinearLayout) layout.getChildAt(i))
					.getChildAt(1);
			if (button.getText().equals(HEAVY_CHECK_MARK)) {
				numberOfrightAnswers++;
			}

		}

		if (numberOfrightAnswers > 1) {
			return false;
		}
		return true;

	}

	public boolean verifyButtons(Button button, String buttonText,
			int buttonvisibility) {
		if (button == null) {
			return false;
		}
		String text = (String) button.getText();
		int visibility = button.getVisibility();
		if (text.equals(buttonText) && visibility == buttonvisibility) {
			return true;
		}
		return false;
	}

	public boolean verifyQuestionwidget(EditText question) {

		if (question == null) {
			return false;
		}
		String questionHint = (String) question.getHint();
		int visibilityProperty = question.getVisibility();

		if (questionHint.equals("Type in the question's text body")
				&& visibilityProperty == EditText.VISIBLE) {
			return true;
		}
		return false;
	}

	public boolean verifyAnswersButton(LinearLayout linearLayout,
			String buttonText1, String buttonText2, int visibility,
			int indexOfbutton) {
		int numberOfAnswers = linearLayout.getChildCount();
		for (int i = 0; i < numberOfAnswers; i++) {
			Button button = (Button) ((LinearLayout) linearLayout.getChildAt(i))
					.getChildAt(indexOfbutton);
			if (!(button.getText().equals(buttonText1) || button.getText()
					.equals(buttonText2))
					|| button.getVisibility() != visibility) {
				return false;
			}

		}

		return true;

	}

	public boolean verifyAnswersEditText(LinearLayout linearLayout) {
		int numberOfAnswers = linearLayout.getChildCount();
		for (int i = 0; i < numberOfAnswers; i++) {
			EditText answerText = (EditText) ((LinearLayout) linearLayout
					.getChildAt(i)).getChildAt(0);
			if (!answerText.getHint().equals("Type in the answer")
					|| answerText.getVisibility() != EditText.VISIBLE) {
				return false;
			}

		}

		return true;

	}

	public boolean verifyTagsWidget(EditText tags) {
		if (tags == null) {
			return false;
		}
		String tagsHint = (String) tags.getHint();
		int visibilityProperty = tags.getVisibility();
		if (tagsHint.equals("Type in the question's tags")
				&& visibilityProperty == EditText.VISIBLE) {
			return true;
		}
		return false;
	}

	/**
	 * This method is called every time we want the initial
	 */

	public void initialiseView() {
		setContentView(R.layout.activity_edit_question);
		answersState = new ArrayList<Boolean>();
		EditText question = (EditText) findViewById(R.id.questionBody);
		question.addTextChangedListener(new TextEditWatcher(question));
		EditText questionTag = (EditText) findViewById(R.id.questionTags);
		questionTag.addTextChangedListener(new TextEditWatcher(questionTag));
		submit = (Button) findViewById(R.id.submitButton);
		// when first started, the activity contains one empty answer
		Button addButton = (Button) findViewById(R.id.addAnswerButton);
		addNewAnswerButton(addButton);
		addNewAnswerButton(addButton);
	}

	/**
	 * 
	 * @author MohamedBenArbia
	 * 
	 */

	private class SubmitQuestion extends
			AsyncTask<QuizQuestion, String, String> {

		@Override
		protected String doInBackground(QuizQuestion... params) {
			// TODO Auto-generated method stub
			return submit(params[0]);

		}

		@Override
		protected void onPostExecute(String response) {
			EditQuestionActivity.this.initialiseView();

		}

		public String submit(QuizQuestion question) {
			SharedPreferences preference = getSharedPreferences(
					MainActivity.PREF_NAME, MODE_PRIVATE);
			String sessionId = preference.getString("SESSION_ID", null);
			if (sessionId != null) {
				JSONObject json = new JSONObject();
				try {
					json.put("question", question.getQuestion());
					json.put("answers", new JSONArray(question.getAnswers()));
					json.put("solutionIndex", question.getSolutionIndex());
					json.put("tags", new JSONArray(question.getTags()));

				} catch (JSONException e) {
					System.err.println("Error while constructing JsonObject");
				}

				HttpPost post = new HttpPost(SERVER_URL + "/quizquestions/");
				String response = "";
				try {
					post.setEntity(new StringEntity(json.toString()));
					Header[] headers = new Header[2];
					headers[0] = new BasicHeader("Content-type",
							"application/json");
					headers[1] = new BasicHeader("Authorization", "Tequila "
							+ sessionId);
					post.setHeaders(headers);
					ResponseHandler<String> handler = new BasicResponseHandler();
					HttpClient client = epfl.sweng.servercomm.SwengHttpClientFactory
							.getInstance();
					response = client.execute(post, handler);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return response;
			}
			return null;

		}

	}

	/**
	 * adds a new possible answer
	 * 
	 * @param view
	 */
	public void addNewAnswerButton(View view) {
		l = (LinearLayout) findViewById(R.id.answersContainer);
		if (l.getChildCount() < MAX_POSSIBLE_ANSWERS) {
			LinearLayout l1 = (LinearLayout) LinearLayout.inflate(this,
					R.layout.answer_field, null);
			l1.setBackgroundColor(Color.RED);
			EditText reponse = (EditText) l1.getChildAt(0);
			answersState.add(false);
			submit.setEnabled(false);
			reponse.addTextChangedListener(new TextEditWatcher(reponse));
			l.addView(l1);

		} else {
			Toast.makeText(this, "Cannot add more answers!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * submits the question to the server
	 * 
	 * @param view
	 */
	public void submitButton(View view) {
		if (correctAns == null) {
			Toast.makeText(this,
					"You have to choose exactly one correct answer",
					Toast.LENGTH_SHORT).show();
		} else {
			EditText question = (EditText) findViewById(R.id.questionBody);
			String questionField = question.getText().toString();
			List<String> answers = new ArrayList<String>();
			for (int i = 0; i < l.getChildCount(); i++) {
				EditText answerText = (EditText) ((LinearLayout) l
						.getChildAt(i)).getChildAt(0);
				answers.add(answerText.getText().toString());

			}

			int solutionIndex = -1;
			if (correctAns != null) {
				solutionIndex = l.indexOfChild((LinearLayout) (correctAns
						.getParent()));
			}
			Set<String> tags = new HashSet<String>();
			EditText tag = (EditText) findViewById(R.id.questionTags);
			String tagsField = tag.getText().toString();
			String[] tagsTab = tagsField.split("[^a-zA-Z0-9]");
			tags.toArray(tagsTab);
			quizzQuestion = new QuizQuestion(questionField, answers,
					solutionIndex, tags, -1, null);
			ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = conMan.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected()) {
				new SubmitQuestion().execute(quizzQuestion);
			}
		}
	}

	/**
	 * removes the corresponding answer field from the answers list;
	 * 
	 * @param view
	 */
	public void removeButton(View view) {

		Button bView = (Button) view;
		if (l.getChildCount() > MIN_ANSWERS_NUMBER) {

			if (correctAns != null
					&& correctAns.getParent() == bView.getParent()) {
				correctAns = null;
			}
			answersState
					.remove(l.indexOfChild((LinearLayout) bView.getParent()));
			l.removeView((LinearLayout) bView.getParent());
			if (EditQuestionActivity.getQuestionState()
					&& EditQuestionActivity.getTagState()
					&& EditQuestionActivity.getFinalAnswersState()) {
				EditQuestionActivity.getSubmit().setEnabled(true);
			}

		} else {
			Toast.makeText(this, "Cannot remove: At least two anwers",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * marks the answer as correct or not.
	 * 
	 * @param view
	 */
	public void markAs(View view) {

		Button bView = (Button) view;
		CharSequence text = bView.getText();
		LinearLayout correspondingAnswerField = (LinearLayout) bView
				.getParent();
		if (text.equals(HEAVY_BALLOT)) {
			if (correctAns != null) {
				correctAns.setText(HEAVY_BALLOT);
				((LinearLayout) correctAns.getParent())
						.setBackgroundColor(Color.RED);
			}
			bView.setText(HEAVY_CHECK_MARK);
			correspondingAnswerField.setBackgroundColor(Color.GREEN);
			correctAns = bView;

		} else {
			bView.setText(HEAVY_BALLOT);
			correspondingAnswerField.setBackgroundColor(Color.RED);
			correctAns = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_question, menu);
		return true;
	}
}

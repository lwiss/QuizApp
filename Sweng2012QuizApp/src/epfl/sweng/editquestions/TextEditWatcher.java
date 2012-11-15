package epfl.sweng.editquestions;

import java.util.List;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 * @author MohamedBenArbia
 * 
 */
public class TextEditWatcher implements TextWatcher {
	private View view;
	private static final String TAG = "TextWatcher";

	public TextEditWatcher(View viewEditQuestion) {
		this.view = viewEditQuestion;
	}

	
	public void afterTextChanged(Editable s) {
		String text = s.toString();
		switch (view.getId()) {
			case R.id.questionBody:
				EditQuestionActivity.setQuestionState(QuizQuestion
						.questionIsOK(text));
				Log.d(TAG, "question modified");
				break;
			case R.id.questionTags:
				EditQuestionActivity.setTagState(QuizQuestion.tagsAreOK(text));
				Log.d(TAG, "tags modified");
				break;
			case R.id.response:
				List<Boolean> list = EditQuestionActivity.getAnswersState();
				LinearLayout l = (LinearLayout) view.getParent().getParent();
				list.set(l.indexOfChild((LinearLayout) view.getParent()),
						QuizQuestion.answerIsOK(text));
				EditQuestionActivity.setAnswersState(list);
				Log.d(TAG, "response modified");
				break;
	
			default:
				break;
		}
		if (EditQuestionActivity.getQuestionState()
				&& EditQuestionActivity.getTagState()
				&& EditQuestionActivity.getFinalAnswersState()) {
			EditQuestionActivity.getSubmit().setEnabled(true);
		} else {
			EditQuestionActivity.getSubmit().setEnabled(false);
		}
	}

	
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

}

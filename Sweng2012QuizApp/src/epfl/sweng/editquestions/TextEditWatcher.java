package epfl.sweng.editquestions;

import java.util.List;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 * @author crazybhy
 *
 */
public class TextEditWatcher implements TextWatcher {
	private View view;
	private EditQuestionActivity activity;

	public TextEditWatcher(EditQuestionActivity activityEditQuestion, View viewEditQuestion) {
		activity = activityEditQuestion;
		view = viewEditQuestion;
	}
	public void afterTextChanged(Editable s) {
		String text = s.toString();
		switch (view.getId()) {
			case R.id.question:
				activity.setQuestionState(QuizQuestion.questionIsOK(text));
				break;
			case R.id.tags:
				activity.setTagState(QuizQuestion.tagsAreOK(text));
				break;
			case R.id.response:
				List<Boolean> list = activity.getAnswersState();
				LinearLayout answers = (LinearLayout) view.getParent().getParent();
				list.set(answers.indexOfChild((LinearLayout) view.getParent()),
						QuizQuestion.answerIsOK(text));
				activity.setAnswersState(list);
				break;
			default:
				break;
		}
		if (activity.getQuestionState()
				&& activity.getTagState()
				&& activity.getFinalAnswersState()) {
			activity.getSubmitButton().setEnabled(true);
		} else {
			activity.getSubmitButton().setEnabled(false);
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}

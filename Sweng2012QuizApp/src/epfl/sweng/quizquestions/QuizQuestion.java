package epfl.sweng.quizquestions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Eagles
 * 
 */
public class QuizQuestion {
	private String question;
	private List<String> answers;
	private int solutionIndex;
	private Set<String> tags;
	private String id;
	private String owner;
	private static final int MAX_QUESTION_LENGTH = 500;
	private static final int MAX_ANSWER_LENGTH = 500;
	private static final int MAX_TAG_LENGTH = 20;
	private static final int MAX_OWNER_LENGTH = 20;
	private static final int MAX_ANSWERS_NUMBER = 10;
	private static final int MIN_ANSWERS_NUMBER = 2;

	public String getQuestion() {
		return question;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public int getSolutionIndex() {
		return solutionIndex;
	}

	public Set<String> getTags() {
		return tags;
	}

	public QuizQuestion(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);

		question = jsonObject.getString("question");

		JSONArray answersArray = jsonObject.getJSONArray("answers");
		answers = new ArrayList<String>();
		for (int i = 0; i < answersArray.length(); i++) {
			answers.add(answersArray.getString(i));
		}

		solutionIndex = jsonObject.getInt("solutionIndex");

		JSONArray tagsArray = jsonObject.getJSONArray("tags");
		tags = new HashSet<String>();
		for (int i = 0; i < tagsArray.length(); i++) {
			tags.add(tagsArray.getString(i));
		}

		id = jsonObject.getString("id");

		owner = jsonObject.getString("owner");
	}

	/**
	 * The constructor for quiz questions defined by the user
	 * 
	 * @param text
	 *            The body of the question, as input by the user
	 * @param answersList
	 *            The list of possible answers of the question, as input by the
	 *            user
	 * @param solution
	 *            The index identifying the correct answer, as input by the user
	 * @param tagsSet
	 *            The set of tags of the question, as input by the user
	 * @param questionId
	 *            The id of the question
	 * @param questionOwner
	 *            The owner of the question
	 */
	public QuizQuestion(String text, List<String> answersList, int solution,
			Set<String> tagsSet, String questionId, String questionOwner) {
		question = text;
		answers = answersList;
		solutionIndex = solution;
		tags = tagsSet;
		id = questionId;
		owner = questionOwner;
	}

	public int auditErrors(int depth) {
		int errors = 0;
		if (depth < 0) {
			return errors;
		}
		// Checking question is neither empty neither too long
		if (!questionIsOK(question)) {
			errors++;
		}
		// Checking number of answers
		if (!numberOfAnswersIsOK(answers)) {
			errors++;
		}
		// Checking answers are neither empty neither too long
		for (String answer : answers) {
			if (!answerIsOK(answer)) {
				errors++;
			}
		}
		// Checking indexSolution is valid
		if (!indexSolutionIsOK(solutionIndex, answers)) {
			errors++;
		}
		// Checking tags are not too long and are alphanumeric
		Iterator<String> iterator = tags.iterator();
		while (iterator.hasNext()) {
			String tag = iterator.next();
			if (!tagIsOK(tag)) {
				errors++;
			}
		}
		// Checking owner is not too long and is alphanumeric
		if (!ownerIsOK(owner)) {
			errors++;
		}
		// Checking id is an Integer >0
		if (!idIsOK(id)) {
			errors++;
		}
		return errors;
	}

	static public boolean questionIsOK(String text) {
		if (text.trim().length() == 0) {
			return false;
		}
		if (text.length() > MAX_QUESTION_LENGTH) {
			return false;
		}
		return true;
	}

	static public boolean numberOfAnswersIsOK(List<String> answers) {
		if (answers.size() < MIN_ANSWERS_NUMBER
				|| answers.size() > MAX_ANSWERS_NUMBER) {
			return false;
		}
		return true;
	}

	static public boolean answerIsOK(String answer) {
		if (answer.trim().length() == 0) {
			return false;
		}
		if (answer.length() > MAX_ANSWER_LENGTH) {
			return false;
		}
		return true;
	}

	static public boolean indexSolutionIsOK(int solutionIndex,
			List<String> answers) {
		if (solutionIndex < 0 || solutionIndex >= answers.size()) {
			return false;
		}
		return true;
	}

	static public boolean tagsAreOK(String tags) {
		String[] tagsArray = tags.split("[^a-zA-Z0-9]");
		for (int i = 0; i < tagsArray.length; i++) {
			if (!tagIsOK(tagsArray[i])) {
				return false;
			}
		}
		return true;
	}

	static public boolean tagIsOK(String tag) {
		if (tag.length() > MAX_TAG_LENGTH) {
			return false;
		}
		for (int i = 0; i < tag.length(); i++) {
			if (!Character.isLetterOrDigit(tag.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	static public boolean ownerIsOK(String owner) {
		if (owner.length() > MAX_OWNER_LENGTH) {
			return false;
		}
		for (int i = 0; i < owner.length(); i++) {
			if (!Character.isLetterOrDigit(owner.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	static public boolean idIsOK(String id) {
		if (Integer.getInteger(id) > 0) {
			return true;
		}
		return false;
	}
}

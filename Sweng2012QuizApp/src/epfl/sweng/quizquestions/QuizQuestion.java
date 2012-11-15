package epfl.sweng.quizquestions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author crazybhy
 * 
 */
public class QuizQuestion {
	private String question;
	private List<String> answers;
	private int solutionIndex;
	private Set<String> tags;
	private int id;
	private String owner;

	/**
	 * The constructor for quiz questions received as JSON strings from the
	 * Sweng2012QuizApp server, as in homework #1
	 * 
	 * @param json
	 *            The JSON string received from the Sweng2012QuizApp server, as
	 *            in homework #1
	 */
	public QuizQuestion(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		question = jsonObject.getString("question");
		answers = new ArrayList<String>();
		JSONArray answersArray = jsonObject.getJSONArray("answers");
		for (int i = 0; i < answersArray.length(); i++) {
			answers.add(answersArray.getString(i));
		}
		solutionIndex = jsonObject.getInt("solutionIndex");
		tags = new HashSet<String>();
		JSONArray tagsArray = jsonObject.getJSONArray("tags");
		for (int i = 0; i < tagsArray.length(); i++) {
			tags.add(tagsArray.getString(i));
		}
		id = jsonObject.getInt("id");
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
			Set<String> tagsSet, int questionId, String questionOwner) {
		question = text;
		answers = answersList;
		solutionIndex = solution;
		tags = tagsSet;
		id = questionId;
		owner = questionOwner;
	}

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

	public int getId() {
		return id;
	}

	public String getOwner() {
		return owner;
	}
}

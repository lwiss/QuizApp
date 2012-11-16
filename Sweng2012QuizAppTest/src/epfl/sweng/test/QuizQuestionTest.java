package epfl.sweng.test;

import org.json.JSONException;

import epfl.sweng.quizquestions.QuizQuestion;

import junit.framework.TestCase;

/**
 * 
 * @author crazybhy
 * 
 */
public class QuizQuestionTest extends TestCase {

	public static final String VALID_QUESTION_JSON = "{"
			+ "question: 'What is the answer to life, the universe and everything?', "
			+ "answers: ['42', '27']," + "solutionIndex: 0,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
/*
	public static final String LONG_QUESTION_JSON = "{"
			+ "question: 'What is the answer to life, the universe and everything?"
			+ "What is the answer to life, the universe and everything?"
			+ "What is the answer to life, the universe and everything?"
			+ "What is the answer to life, the universe and everything?', "
			+ "answers: ['42', '27']," + "solutionIndex: 0,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
	
	public static final String EMPTY_QUESTION_JSON = "{"
			+ "question: '   ', "
			+ "answers: ['42', '27']," + "solutionIndex: 0,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
	
	public static final String NO_ANSWERS_JSON = "{"
			+ "question: 'What is the answer to life, the universe and everything?', "
			+ "answers: []," + "solutionIndex: 0,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
	
	public static final String TOO_MUCH_ANSWERS_JSON = "{"
			+ "question: 'What is the answer to life, the universe and everything?', "
			+ "answers: ['42', '27', '47', '22', '24', '72', '22', '77', '1', '2', '3']," + "solutionIndex: 0,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
	
	public static final String TOO_LONG_ANSWER_JSON = "{"
			+ "question: 'What is the answer to life, the universe and everything?', "
			+ "answers: ['42', '27', "
			+ "'33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333"
			+ "33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333"
			+ "33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333"
			+ "33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333"
			+ "33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333'],"
			+ "solutionIndex: 0,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
	
	public static final String TOO_SMALL_SOLUTION_JSON = "{"
			+ "question: 'What is the answer to life, the universe and everything?', "
			+ "answers: ['42', '27']," + "solutionIndex: -1,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
	
	public static final String TOO_BIG_SOLUTION_JSON  = "{"
			+ "question: 'What is the answer to life, the universe and everything?', "
			+ "answers: ['42', '27']," + "solutionIndex: 2,"
			+ "tags : ['h2g2', 'trivia']," + "owner : 'anonymous',"
			+ "id : '123'" + "}";
	*/
	public void testQuestionOK() throws JSONException {
		String json = VALID_QUESTION_JSON;
		assertNotNull(new QuizQuestion(json));
	}
	
	public void testQuestionNotOk() throws JSONException {
/*		String json = LONG_QUESTION_JSON;
		QuizQuestion q = new QuizQuestion(json);
		//assertTrue(q.auditErrors(0)==1); */
	}
}

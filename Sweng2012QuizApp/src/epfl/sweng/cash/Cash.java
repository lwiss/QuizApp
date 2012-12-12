package epfl.sweng.cash;

import epfl.sweng.quizquestions.QuizQuestion;

/**
 * @author MohamedBenArbia
 * defines the methods that the casheManager has to implement
 *
 */
public interface Cash {
	
	/**
	 * This method is used to cache the quizzQuestion in online mode:
	 * the quizQuestion fetched from the server or posted by the user in modeOnline 
	 * @param quizQuestion
	 */
	void cacheOnlineQuizQuestion(QuizQuestion quizQuestion);

}

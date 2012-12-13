package epfl.sweng.cash;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.showquestions.Rating;
import epfl.sweng.showquestions.Rating.RateState;

/**
 * @author MohamedBenArbia defines the methods that the casheManager has to
 *         implement
 * 
 */

public interface Cache {

	/**
	 * This method is used to cache the quizzQuestion in online mode: the
	 * quizQuestion is fetched from the server or posted by the user in
	 * modeOnline
	 * 
	 * @param quizQuestion
	 */
	void cacheOnlineQuizQuestion(QuizQuestion quizQuestion);

	void cacheOnlineRatings(Rating rating);

	/**
	 * This method cache the userRating of a question: save the rating in two
	 * different lists: 
	 * 	-list of all ratings: in a way that the rating of the
	 * question will be updated(simulate a successful response from the) the
	 *  -list of user ratings ; in a way that we can send the user's rate when we
	 * go online if two ratings have the same id , always save the newest one
	 * !!!
	 * 
	 * return rate state , if the rate already exists and has been updated ,
	 * return updated status else return created status
	 */
	RateState cacheUserRating(Rating rating);

	/**
	 * This method is used to cache the quizQuestion that the user has posted in
	 * offline Mode
	 * 
	 * @param auizQuestion
	 */

	void cacheQuizQuestionToSubmit(QuizQuestion quizQuestion);

}

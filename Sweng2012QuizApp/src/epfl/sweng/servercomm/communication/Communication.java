package epfl.sweng.servercomm.communication;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.showquestions.Rating;

/**
 * This interface defines the methods that will be used by the
 * serverCommunication Class and the Proxy
 * 
 * @author MohamedBenArbia
 * 
 */
public interface Communication {

	/**
	 * This method is responsable of returning the quizzQuestion fetched from the server
	 * @return
	 */
	QuizQuestion getQuizQuestion(String sessionId) throws CommunicationException;
	/**
	 * post a quizz question that the user submit 
	 * @param quizQuestion
	 */
	void postQuestion(QuizQuestion quizQuestion);
	/**
	 * return the rating of a quizz question 
	 * @return
	 */
	Rating getRatings();
	/**
	 * post a rating of a quiz question 
	 * @param rating
	 */
	void postRating(Rating rating);

}

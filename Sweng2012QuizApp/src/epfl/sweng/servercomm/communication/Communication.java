package epfl.sweng.servercomm.communication;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.showquestions.Rating;
import epfl.sweng.showquestions.Rating.RateState;

/**
 * This interface defines the methods that will be used by the
 * serverCommunication Class and the Proxy
 * 
 * @author MohamedBenArbia
 * 
 */
public interface Communication {

	/**
	 * This method is responsable of returning the quizzQuestion fetched from
	 * the server
	 * 
	 * @return
	 */
	QuizQuestion getQuizQuestion()
		throws CommunicationException;

	/**
	 * post a quizz question that the user submit
	 * 
	 * @param quizQuestion
	 */
	boolean postQuestion(QuizQuestion quizQuestion) throws CommunicationException;

	/**
	 * return the rating of a quizz question
	 * 
	 * @return
	 */
	Rating getRatings(QuizQuestion quizQuestion) throws CommunicationException;

	/**
	 * post a rating of a quiz question
	 * 
	 * @param rating
	 */
	RateState postRating(String verdict, QuizQuestion quizQuestion) throws CommunicationException;

}

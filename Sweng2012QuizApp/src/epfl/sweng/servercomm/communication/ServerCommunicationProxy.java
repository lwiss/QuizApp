package epfl.sweng.servercomm.communication;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.showquestions.Rating;

/**
 * This class represents the proxy that the application is using to do the
 * communication with the sweng server. This class has a unique instance. this
 * class represents the proxy Object of the proxy pattern
 * 
 * @author MohamedBenArbia
 * 
 */
public final class ServerCommunicationProxy implements Communication {
	private ServerCommunication serverCommunication;
	private static ServerCommunicationProxy proxy;

	private ServerCommunicationProxy() {

	}

	public static ServerCommunicationProxy getInstance() {
		if (proxy == null) {
			return new ServerCommunicationProxy();
		}
		return proxy;
	}

	public QuizQuestion getQuizQuestion() {
		// TODO Auto-generated method stub
	
		return null;
	}

	public void postQuestion(QuizQuestion quizQuestion) {
		// TODO Auto-generated method stub

	}

	public Rating getRatings() {
		// TODO Auto-generated method stub
		return null;
	}

	public void postRating(Rating rating) {
		// TODO Auto-generated method stub

	}

}

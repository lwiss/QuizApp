package epfl.sweng.servercomm.communication;

import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;
import epfl.sweng.cash.CacheManager;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
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
	private Communication serverCommunication;
	private CacheManager caheManager;
	private static ServerCommunicationProxy proxy;


	private ServerCommunicationProxy() {
		caheManager = CacheManager.getInstance();
		serverCommunication = ServerCommunication.getInstance();
	}

	public static ServerCommunicationProxy getInstance() {
		if (proxy == null) {
			proxy = new ServerCommunicationProxy();
		}
		return proxy;
	}

	public QuizQuestion getQuizQuestion(String sessionId) {
		Log.d("getQuestion Proxy method", "here in proxy ");
		QuizQuestion quizQuestion = null;
		if (MainActivity.isOnline()) {
			Log.d("Online", "here in proxy");
			// then call getQuizQuestion method of servercommunication class to
			// do the communication with the server and cache the quizz question with the Cache
			// Manager.
			// If an error is generated an exception is thrown and we pass in
			// offline Mode
			try {
				quizQuestion = serverCommunication.getQuizQuestion(sessionId);
				caheManager.cacheOnlineQuizQuestion(quizQuestion);
			} catch (CommunicationException e) {
				quizQuestion = new QuizQuestion(
						"There was an error retrieving the question",
						new ArrayList<String>(), -1, new HashSet<String>(), -1,
						null);
				MainActivity.setOnline(false);
			}
		}
		return quizQuestion; 
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

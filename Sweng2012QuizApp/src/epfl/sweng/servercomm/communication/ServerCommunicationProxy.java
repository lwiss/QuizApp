package epfl.sweng.servercomm.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.R;
import android.util.Log;
import android.util.SparseArray;
import epfl.sweng.cash.CacheManager;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.showquestions.Rating;
import epfl.sweng.showquestions.Rating.RateState;

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

	/**
	 * This method is called by the fetchQuestionAsyncTask in order to get a
	 * quizzQuestion
	 * 
	 * If the state is online : call getQuizQuestion method of
	 * servercommunication class to do the communication with the server and
	 * cache the quizz question with the Cache Manager. If an error
	 * communication is generated , we catch the errorCommunication Exception
	 * and we pass in offline Mode offline Mode
	 * 
	 * Offline: return the cached Quizzquestion
	 */
	public QuizQuestion getQuizQuestion() {
		Log.d("getQuestion Proxy method", "here in proxy ");
		QuizQuestion quizQuestion = null;
		if (MainActivity.isOnline()) {
			Log.d("Online", "here in proxy");

			try {
				quizQuestion = serverCommunication.getQuizQuestion();
				caheManager.cacheOnlineQuizQuestion(quizQuestion);
				Log.d(" CACHED QUESTIONS ",
						caheManager.showCahcedQuizQuestion());
			} catch (CommunicationException e) {
				quizQuestion = new QuizQuestion(
						"There was an error retrieving the question",
						new ArrayList<String>(), -1, new HashSet<String>(), -1,
						null);
				MainActivity.setOnline(false);
			}
		} else {
			quizQuestion = caheManager.getCachedQuizQuestion();
			// display the cahced information to the user using a method defined
			// in CacheManager
		}
		return quizQuestion;
	}

	/**
	 * This method is used to post a question of a user
	 */

	public boolean postQuestion(QuizQuestion quizQuestion) {
		// TODO Auto-generated method stub
		boolean questionPosted = false;
		if (MainActivity.isOnline()) {
			try {
				questionPosted = serverCommunication.postQuestion(quizQuestion);
				Log.d("CACHED QUESTIONS  ",
						caheManager.showCahcedQuizQuestion());
			} catch (CommunicationException e) {
				MainActivity.setOnline(false);
				questionPosted = false;
			}
		} else {
			caheManager.cacheQuizQuestionToSubmit(quizQuestion);
			Log.d("QUESTION TO SUBMIT ", caheManager.showQuizQuestionToSubmit());
			questionPosted = true;
			// cache the posted question
		}

		return questionPosted;
	}

	public Rating getRatings(QuizQuestion quizQuestion) {
		Rating rating = null;
		if (MainActivity.isOnline()) {
			try {
				rating = serverCommunication.getRatings(quizQuestion);
				caheManager.cacheOnlineRatings(rating);
				Log.d("CACHED RATINGS", caheManager.showCachedRaings());
			} catch (CommunicationException e) {
				rating = null;
				MainActivity.setOnline(false);
				
			}
		} else {
			rating = caheManager.getRatingQuestion(quizQuestion);
			Log.d("rating", "" + rating);
			Log.d("Rating in OFFLINE MODE", rating.toString());
			// return the cached informati
		}
		return rating;
	}

	public RateState postRating(String verdict, QuizQuestion quizQuestion) {

		RateState rateState = null;
		if (MainActivity.isOnline()) {

			try {
				rateState = serverCommunication.postRating(verdict,
						quizQuestion);

				// do not have to cahe because getRatings will be called
				// and the rating will be cached
			} catch (CommunicationException e) {
				// do not have to cahce the rating and after send it when a
				// communication error happens : Seen in Piazza!
				rateState = RateState.NOTFOUND;
				MainActivity.setOnline(false);

			}

		} else {
			// treat the rating of the user as if it was in online mode:
			// change the rate of the user correspending to the id of the
			// question !
			// Use the method updateRating defined in Rating
			// store it using the cacheManager

			// this method will save the user rating in a list in order to send
			// it after to the
			// to the server and will save it in list_of_all_ratings in the way
			// that if there
			// is a question of the same id , it update the state of that
			// question !!

			rateState = caheManager.cacheUserRating(new Rating(verdict,
					quizQuestion));
		}
		return rateState;

	}

	public void sendCachedContent() {
		HashMap<QuizQuestion, Rating> listOfQuizQuestionToSubmit = caheManager
				.getListOfQuizQuestionTosubmit();
		List<Rating> listOfUserRating = caheManager
				.getListOfUserRatingToSubmit();
		SparseArray<QuizQuestion> listOfAllQuizzQuestionCached = caheManager
				.getListOfAllCachedQuizzQuestion();

		for (QuizQuestion quizQuestion : listOfQuizQuestionToSubmit.keySet()) {
			postQuestion(quizQuestion);
			listOfQuizQuestionToSubmit.remove(quizQuestion);
			/**
			 * try { Log.d("Quizz Question To submit", quizQuestion.toString());
			 * serverCommunication.postQuestion(quizQuestion);
			 * listOfQuizQuestionToSubmit.remove(quizQuestion); } catch
			 * (CommunicationException e) { // this case only happens if we have
			 * 500 status or an // IOException MainActivity.setOnline(false); }
			 */
		}

		for (Rating rating : listOfUserRating) {
			postRating(rating.getVerdict(), rating.getQuizQuestion());
			listOfUserRating.remove(rating);
			/**
			 * try { serverCommunication.postRating(rating.getVerdict(),
			 * rating.getQuizQuestion()); listOfUserRating.remove(rating); }
			 * catch (CommunicationException e) { // this case only happens if
			 * we have 500 status or an // IOException
			 * MainActivity.setOnline(false); }
			 */
		}

		// get the latest ratings of all quizQuestion

		int length = listOfAllQuizzQuestionCached.size();
		for (int j = 0; j < length; j++) {
			try {

				QuizQuestion quizQuestion = listOfAllQuizzQuestionCached
						.valueAt(j);
				caheManager.cacheOnlineRatings(serverCommunication
						.getRatings(quizQuestion));
			} catch (CommunicationException e) {
				MainActivity.setOnline(false);
			}

		}

	}

}

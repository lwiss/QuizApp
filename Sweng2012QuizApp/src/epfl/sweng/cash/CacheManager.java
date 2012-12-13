package epfl.sweng.cash;

import java.util.HashMap;

import android.util.Log;
import android.util.SparseArray;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.showquestions.Rating;
import epfl.sweng.showquestions.Rating.RateState;

/**
 * This class is responsable of caching the information through the proxy this
 * class is a singleton factory
 * 
 * @author MohamedBenArbia
 * 
 */
public final class CacheManager implements Cache {

	/**
	 * an instance of ChaceManager
	 */
	private static CacheManager cacheManager;
	/**
	 * list of QuizQuestions : it stores the questions that user has already
	 * submitted to, or fetched from, the server in during the online mode.
	 */
	private SparseArray<QuizQuestion> onlineCachedQuizQuestionList;
	/**
	 * list of Ratings : it stores ratings of corresponding question with id
	 * that matches the key value of the Rating object
	 */
	private SparseArray<Rating> onlineCachedRatings;
	/**
	 * this data structure stores both the submitted questions
	 * and their corresponding ratings
	 */
	private HashMap<QuizQuestion, Rating> offlineCachedQuizQuestionVsRatings;

	private CacheManager() {
		onlineCachedQuizQuestionList = new SparseArray<QuizQuestion>();
		onlineCachedRatings = new SparseArray<Rating>();
		offlineCachedQuizQuestionVsRatings= new HashMap<QuizQuestion, Rating>();
	}

	/**
	 * this method return the unique instance of the CacheManager type if it
	 * already exists or instantiates a new CacheManager object
	 * 
	 * @return the unique instance of the CacheManager class
	 */
	public static CacheManager getInstance() {
		if (cacheManager == null) {
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}

	public void cacheOnlineQuizQuestion(QuizQuestion quizQuestion) {
		if (quizQuestion != null) {
			int qId = quizQuestion.getId();
			if (onlineCachedQuizQuestionList.get(qId) != null) {
				Log.d("CACHING INFO", "This question already exists");
				onlineCachedQuizQuestionList.put(qId, quizQuestion);
			} else {
				onlineCachedQuizQuestionList.put(qId, quizQuestion);
			}
		} else {
			Log.d("CACHING FAIL", "(question) you want to cache a null value!!");
		}
	}

	public void cacheOnlineRatings(Rating rating) {
		if (rating!=null) {
			int qId = rating.getQuestionId();
			onlineCachedRatings.put(qId, rating);
		} else {
			Log.d("CACHING FAIL", "(rating) you want to cache a null value!!");
		}
	}

	public void cacheQuizQuestionToSubmit(QuizQuestion quizQuestion) {
		Rating r = new Rating(0, 0, 0, null, -1);
		offlineCachedQuizQuestionVsRatings.put(quizQuestion, r);
	}

	public RateState cacheUserRating(Rating rating) {
		
		
		
		return null;
	}

}

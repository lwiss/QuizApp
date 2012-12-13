package epfl.sweng.cash;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.showquestions.Rating;
import epfl.sweng.showquestions.Rating.RateState;

/**
 * This class is responsable of caching the information through the proxy
 * 
 * @author MohamedBenArbia
 * 
 */
public final class CacheManager implements Cash {

	private static CacheManager cacheManager;

	private CacheManager() {

	}

	public RateState cacheUserRating(Rating rating) {
		return null;
	}

	public static CacheManager getInstance() {
		if (cacheManager == null) {
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}

	public void cacheOnlineQuizQuestion(QuizQuestion quizQuestion) {

	}

	public void cacheOnlineRatings(Rating rating) {
		// TODO Auto-generated method stub

	}

	public void cacheQuizQuestionToSubmit(QuizQuestion quizQuestion) {
		// TODO Auto-generated method stub

	}

}

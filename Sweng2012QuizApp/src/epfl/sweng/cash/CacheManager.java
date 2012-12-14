package epfl.sweng.cash;

import java.util.List;

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

	private List<QuizQuestion> listOfQuizQuestionTosubmit;
	public List<QuizQuestion> getListOfQuizQuestionTosubmit() {
		return listOfQuizQuestionTosubmit;
	}

	public List<Rating> getListOfUserRatingToSubmit() {
		return listOfUserRatingToSubmit;
	}

	private List<Rating> listOfUserRatingToSubmit;

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

	public QuizQuestion getCachedQuizQuestion() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rating getRatingQuestion(QuizQuestion quizQuestion) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<QuizQuestion> getListOfAllCachedQuizzQuestion() {
		// TODO Auto-generated method stub
		return null;
	}

}

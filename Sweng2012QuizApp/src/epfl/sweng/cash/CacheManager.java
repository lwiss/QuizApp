package epfl.sweng.cash;

import java.util.ArrayList;
import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.showquestions.Rating;

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
	 * list of QuizQuestions : it stores the questions that user has already submitted to, 
	 * or fetched from, the server in during the online mode. 
	 */
	private static List<QuizQuestion> onlineCachedQuizQuestionList;
	private static List<Rating> onlineCachedRatings;

	private CacheManager() {
		onlineCachedQuizQuestionList = new ArrayList<QuizQuestion>();
		onlineCachedRatings = new ArrayList<Rating>();
	}

	/**
	 * this method return the unique instance of the CacheManager type
	 * if it already exists or instantiates a new CacheManager object   
	 * @return the unique instance of the CacheManager class
	 */
	public static CacheManager getInstance() {
		if (cacheManager == null) {
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}

	public void cacheOnlineQuizQuestion(QuizQuestion quizQuestion) {

	}

}

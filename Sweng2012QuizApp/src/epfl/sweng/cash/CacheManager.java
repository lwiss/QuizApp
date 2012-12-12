package epfl.sweng.cash;

import epfl.sweng.quizquestions.QuizQuestion;

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

	public static CacheManager getInstance() {
		if (cacheManager == null) {
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}

	public void cacheOnlineQuizQuestion(QuizQuestion quizQuestion) {

	}

}

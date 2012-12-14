package epfl.sweng.cash;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	 * this data structure stores both the submitted questions and their
	 * corresponding ratings (a rating is a simulation of the server response)
	 */
	private HashMap<QuizQuestion, Rating> offlineCachedQuizQuestionVsRatings;

	/**
	 * the list that contains ratings
	 */
	private List<Rating> listOfUserRatingToSubmit;

	private CacheManager() {
		onlineCachedQuizQuestionList = new SparseArray<QuizQuestion>();
		onlineCachedRatings = new SparseArray<Rating>();
		offlineCachedQuizQuestionVsRatings = new HashMap<QuizQuestion, Rating>();
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
		if (rating != null) {
			int qId = rating.getQuizQuestion().getId();
			onlineCachedRatings.put(qId, rating);
		} else {
			Log.d("CACHING FAIL", "(rating) you want to cache a null value!!");
		}
	}

	public void cacheQuizQuestionToSubmit(QuizQuestion quizQuestion) {
		Rating r = new Rating(0, 0, 0, null, quizQuestion);
		offlineCachedQuizQuestionVsRatings.put(quizQuestion, r);
	}

	public RateState cacheUserRating(Rating rating) {
		RateState state;
		QuizQuestion q = rating.getQuizQuestion();
		// add the user rating to the list of tasks to be done
		listOfUserRatingToSubmit.add(rating);
		// update the rating corresponding to this question
		int qId = q.getId();
		if (qId == -1) { // this means that the question belongs to the
							// questions to be submitted
			Rating r = offlineCachedQuizQuestionVsRatings.get(q);
			if (r.getVerdict() == null) {
				state = RateState.REGISTRED;
			} else {
				state = RateState.UPDATED;
			}
			r.updateCounts(rating.getVerdict());
			r.setVerdict(rating.getVerdict());
		} else { // this means that the question belongs to the questions
					// fetched from the server (on online mode)
			Rating r = onlineCachedRatings
					.get(rating.getQuizQuestion().getId());
			if (r.getVerdict() == null) {
				state = RateState.REGISTRED;
			} else {
				state = RateState.UPDATED;
			}
			r.updateCounts(rating.getVerdict());
			r.setVerdict(rating.getVerdict());
		}

		return state;
	}

	public QuizQuestion getCachedQuizQuestion() {

		int i = new Random().nextInt(2);
		if (onlineCachedQuizQuestionList.size() == 0) {
			i=1;
		} else if (offlineCachedQuizQuestionVsRatings.keySet().toArray().length == 0) {
			i=0;
		}
		if (i == 0) { // the question will be selected from the
						// onlineCachedQuizQuestion List
			int j = onlineCachedQuizQuestionList.size();
			if (j != 0) {
				j = new Random().nextInt(j);
				return onlineCachedQuizQuestionList.valueAt(j);
			} else {
				return null;
			}

		} else { // the question will be selected from the
					// offlineCachedQuizQuestion List
			Object[] q = offlineCachedQuizQuestionVsRatings.keySet().toArray();
			int j = q.length;
			if (j != 0) {
				j = new Random().nextInt(j);
				return (QuizQuestion) q[j];
			} else {
				return null;
			}

		}
	}

	public Rating getRatingQuestion(QuizQuestion quizQuestion) {
		int qId = quizQuestion.getId();
		if (qId == -1) {
			return offlineCachedQuizQuestionVsRatings.get(quizQuestion);
		} else {
			return onlineCachedRatings.get(qId);
		}
	}

	public HashMap<QuizQuestion, Rating> getListOfQuizQuestionTosubmit() {
		return offlineCachedQuizQuestionVsRatings;
	}

	public SparseArray<QuizQuestion> getListOfAllCachedQuizzQuestion() {
		return onlineCachedQuizQuestionList;
	}

	public List<Rating> getListOfUserRatingToSubmit() {
		return listOfUserRatingToSubmit;
	}

}

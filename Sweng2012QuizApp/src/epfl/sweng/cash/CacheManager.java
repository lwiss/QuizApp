package epfl.sweng.cash;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Set;

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
		listOfUserRatingToSubmit = new ArrayList<Rating>();
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

	public SparseArray<QuizQuestion> getListCachedQuizQuestion() {
		SparseArray<QuizQuestion> mergedList = new SparseArray<QuizQuestion>();
		for (int i = 0; i < onlineCachedQuizQuestionList.size(); i++) {
			mergedList.put(onlineCachedQuizQuestionList.keyAt(i),
					onlineCachedQuizQuestionList.valueAt(i));
		}
		Set<QuizQuestion> set = offlineCachedQuizQuestionVsRatings.keySet();
		Iterator<QuizQuestion> iterator = set.iterator();
		while (iterator.hasNext()) {
			QuizQuestion qq = iterator.next();
			mergedList.put(qq.getId(), qq);
		}
		return mergedList;
	}

	public QuizQuestion getCachedQuizQuestion() {
		SparseArray<QuizQuestion> allCachedQuestion = getListCachedQuizQuestion();
		int i = allCachedQuestion.size();
		if (i != 0) {
			i = new Random().nextInt(i);
			return onlineCachedQuizQuestionList.valueAt(i);
		} else {
			return null;
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

	public String showCahcedQuizQuestion() {
		int length = onlineCachedQuizQuestionList.size();
		String a = "";
		for (int i = 0; i < length; i++) {
			a += " \n" + i + " : "
					+ onlineCachedQuizQuestionList.valueAt(i).toString();
		}
		return a;
	}

	public String showCachedRaings() {
		String a = "";
		int length = onlineCachedRatings.size();
		for (int i = 0; i < length; i++) {
			a += " \n" + i + " : " + onlineCachedRatings.valueAt(i).toString();
		}

		return a;
	}

	public String showQuizQuestionToSubmit() {
		String a = "";
		for (QuizQuestion q : offlineCachedQuizQuestionVsRatings.keySet()) {
			a += "\n" + q.toString();
		}
		return a;
	}

	public String showUserRating() {
		String a = "";
		return a;
	}

}

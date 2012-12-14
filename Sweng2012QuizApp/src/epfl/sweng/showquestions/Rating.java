package epfl.sweng.showquestions;

import epfl.sweng.quizquestions.QuizQuestion;

/**
 * This class describes a rating of a question
 * 
 * @author MohamedBenArbia
 * 
 */
public class Rating {

	private int likeCount;
	private int dislikeCount;
	private int incorrectCount;
	/**
	 * this attribute gives information about what the user has chosen as rate
	 * to this question. if he has not chosen a rate yet verdict will be set to
	 * null.
	 * 
	 */
	private String verdict;

	private QuizQuestion quizQuestion;

	/**
	 * 
	 * @author MohamedBenArbia This enum value represents a state of a rate
	 */
	public enum RateState {
		UPDATED, REGISTRED, NOTFOUND;
		@Override
		public String toString() {
			switch (this) {
				case UPDATED:
					return "UPDATED";
				case REGISTRED:
					return "REGISTRED";
				case NOTFOUND:
					return "NOTFOUND";
				default:
					return "NOTFOUND";
			}
		};
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likecount) {
		this.likeCount = likecount;
	}

	public int getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(int dislikecount) {
		this.dislikeCount = dislikecount;
	}

	public int getIncorrectCount() {
		return incorrectCount;
	}

	public void setIncorrectCount(int incorrectcount) {
		this.incorrectCount = incorrectcount;
	}

	public String getVerdict() {
		return verdict;
	}

	public void setVerdict(String userverdict) {
		this.verdict = userverdict;

	}

	public Rating(int likecount, int dislikecount, int incorrectcount,
			String userverdict, QuizQuestion quizquestion) {
		likeCount = likecount;
		dislikeCount = dislikecount;
		incorrectCount = incorrectcount;
		verdict = userverdict;
		this.quizQuestion = quizquestion;

	}

	public Rating(String userVerdict, QuizQuestion quizquestion) {
		verdict = userVerdict;
		this.quizQuestion = quizquestion;
	}

	public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}

	public void setQuizQuestion(QuizQuestion quizquestion) {
		this.quizQuestion = quizquestion;
	}

	public void updateCounts(String myVerdict) {
		if (myVerdict.equals(verdict)) {
			// this means that there is no change
		} else { // this means that the user has changed his mind

			if (myVerdict.equals("like")) {
				likeCount++;
				decrementCount(verdict);
			} else if (myVerdict.equals("dislike")) {
				dislikeCount++;
				decrementCount(verdict);
			} else {
				incorrectCount++;
				decrementCount(verdict);
			}
		}
	}

	private void decrementCount(String myVerdict) {
		if (myVerdict != null) {
			if (myVerdict.equals("like")) {
				likeCount--;
			} else if (myVerdict.equals("dislike")) {
				dislikeCount--;
			} else {
				incorrectCount--;
			}
		}

	}

	public String toString() {
		return " question: " + quizQuestion.getQuestion() + " #like: "
				+ likeCount + "#dislike " + dislikeCount + " #incorrect "
				+ incorrectCount + "  Verdict " + verdict;
	}
}

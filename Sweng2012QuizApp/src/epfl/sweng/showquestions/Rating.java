package epfl.sweng.showquestions;

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
	private String verdict;
	private int questionId;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionID) {
		this.questionId = questionID;
	}

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
			String userverdict, int questionID) {
		likeCount = likecount;
		dislikeCount = dislikecount;
		incorrectCount = incorrectcount;
		verdict = userverdict;
		questionId = questionID;
	}

	public Rating(String userVerdict, int questionID) {
		verdict = userVerdict;
		questionId = questionID;
	}

}

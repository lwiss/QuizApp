package epfl.sweng.servercomm.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.http.HttpResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import epfl.sweng.cash.CacheManager;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.showquestions.Rating;
import epfl.sweng.showquestions.Rating.RateState;

/**
 * This class implements method that are responsable of doing the communication
 * with the SwEng Server in different cases. This class represents the real
 * object of the proxy Design patttern
 * 
 * @author MohamedBenArbia
 * 
 */
public final class ServerCommunication implements Communication {
	private static final String GET_QUESTION_SERVER_URL = "https://sweng-quiz.appspot.com/quizquestions/random";
	private final static String RATING_URL_SERVER = "https://sweng-quiz.appspot.com/quizquestions";
	private final static String POST_RATING = "https://sweng-quiz.appspot.com/quizquestions";
	private final static String POST_QUESTION_URL = "https://sweng-quiz.appspot.com/quizquestions";
	private static final int OK_STATUS = 200;
	private static final int ERROR_STATUS = 500;
	private static final int CREATED_STATUS = 201;
	private static final int NO_CONTENT_STATUS = 204;
	private static final int NOT_FOUND_STATUS = 404;
	private static final int UNAUTHORIZED_STATUS = 401;
	private static ServerCommunication serverCommunication;

	public static ServerCommunication getInstance() {
		if (serverCommunication == null) {
			serverCommunication = new ServerCommunication();
		}
		return serverCommunication;
	}

	private ServerCommunication() {

	}

	/**
	 * This method is responsible of getting q quizzQuestion from the server IF
	 * an error communication occurs then a Communication Exception is thrown A
	 * communicationError is : An IoException or 500 status ! Otherwise return
	 * the errorQuizQuestion
	 */

	public QuizQuestion getQuizQuestion() throws CommunicationException {

		QuizQuestion errorQuizQuestion = new QuizQuestion(
				"There was an error retrieving the question",
				new ArrayList<String>(), -1, new HashSet<String>(), -1, null);
		HttpGet httpGet = new HttpGet(GET_QUESTION_SERVER_URL);
		httpGet.setHeader("Authorization",
				"Tequila " + MainActivity.getSessionId());
		HttpResponse httpResponse = null;
		QuizQuestion quizQuestion = null;
		try {
			httpResponse = SwengHttpClientFactory.getInstance()
					.execute(httpGet);
			if (httpResponse != null) {
				int status = httpResponse.getStatusLine().getStatusCode();
				if (status == OK_STATUS) {
					String responseEntity = EntityUtils.toString(httpResponse
							.getEntity());
					quizQuestion = new QuizQuestion(responseEntity);
					return quizQuestion;
				} else if (status == ERROR_STATUS) {
					throw new CommunicationException();
				} else {
					return errorQuizQuestion;
				}
			} else {
				return errorQuizQuestion;
			}

		} catch (IOException e) {
			throw new CommunicationException(e);
		} catch (JSONException e) {
			return errorQuizQuestion;
		}

	}

	/**
	 * This method is responsible for posting a quiz question
	 * 
	 * @throws CommunicationException
	 */
	public boolean postQuestion(QuizQuestion question)
		throws CommunicationException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			json.put("question", question.getQuestion());
			json.put("answers", new JSONArray(question.getAnswers()));
			json.put("solutionIndex", question.getSolutionIndex());
			json.put("tags", new JSONArray(question.getTags()));
		} catch (JSONException e) {
			return false;
		}
		HttpPost post = new HttpPost(POST_QUESTION_URL);

		try {
			post.setEntity(new StringEntity(json.toString()));
			post.setHeader(new BasicHeader("Content-type", "application/json"));
			post.setHeader("Authorization",
					"Tequila " + MainActivity.getSessionId());
			
			HttpResponse response = SwengHttpClientFactory.getInstance()
					.execute(post);
			if (response != null) {
				int status = response.getStatusLine().getStatusCode();
				if (status == CREATED_STATUS) {
					String responseEntity = EntityUtils.toString(response
							.getEntity());
					try {
						QuizQuestion quizQuestion = new QuizQuestion(
								responseEntity);
						CacheManager.getInstance().cacheOnlineQuizQuestion(
								quizQuestion);
						CacheManager.getInstance().cacheOnlineRatings(new Rating(0, 0, 0, null, quizQuestion));
						//CacheManager.getInstance().getListOfQuizQuestionTosubmit().remove(question);
					} catch (JSONException e) {
						return false;
					}
					return true;
				} else if (status == ERROR_STATUS) {
					throw new CommunicationException();
				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (IOException e) {
			throw new CommunicationException(e);
		}

	}

	/**
	 * this method is responsable of getting a rating of question from the
	 * server If an errorCommunication occurs, a CommunicationException is
	 * thrown
	 */

	public Rating getRatings(QuizQuestion quizQuestion)
		throws CommunicationException {
		int questionId = quizQuestion.getId();
		Rating rating = new Rating(-1, -1, -1, null, quizQuestion);
		getAllRatings(questionId, rating);
		getRating(questionId, rating);
		return rating;

	}

	private void getAllRatings(int id, Rating rating)
		throws CommunicationException {
		HttpResponse httpResponse = getRequest(RATING_URL_SERVER + "/" + id
				+ "/ratings");
		if (httpResponse != null) {
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status == OK_STATUS) {
				try {
					String response = EntityUtils.toString(httpResponse
							.getEntity());
					JSONObject json = new JSONObject(response);
					rating.setLikeCount(json.getInt("likeCount"));
					rating.setDislikeCount(json.getInt("dislikeCount"));
					rating.setIncorrectCount(json.getInt("incorrectCount"));
				} catch (IOException e) {
					throw new CommunicationException(e);
				} catch (JSONException e) {
					rating.setDislikeCount(-1);
					rating.setLikeCount(-1);
					rating.setIncorrectCount(-1);
				}
			} else if (status == ERROR_STATUS) {
				throw new CommunicationException();
			}
		} else {
			rating.setDislikeCount(-1);
			rating.setLikeCount(-1);
			rating.setIncorrectCount(-1);
		}
	}

	private void getRating(int id, Rating rating) throws CommunicationException {
		HttpResponse httpResponse = getRequest(RATING_URL_SERVER + "/" + id
				+ "/rating");
		if (httpResponse != null) {
			switch (httpResponse.getStatusLine().getStatusCode()) {
				case OK_STATUS:
					try {
						String response = EntityUtils.toString(httpResponse
								.getEntity());
						JSONObject json = new JSONObject(response);
						rating.setVerdict(json.getString("verdict"));
					} catch (IOException e) {
						throw new CommunicationException(e);
					} catch (JSONException e) {
						rating.setVerdict("");
					}
					break;
				case NO_CONTENT_STATUS:
					rating.setVerdict("You have not rated this question");
					break;
				case NOT_FOUND_STATUS:
					rating.setVerdict("");
				case UNAUTHORIZED_STATUS:
					try {
						String response = EntityUtils.toString(httpResponse
								.getEntity());
						JSONObject json = new JSONObject(response);
						rating.setVerdict(json.getString("message"));
						throw new CommunicationException();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				case ERROR_STATUS:
					throw new CommunicationException();
				default:
			}
		} else {
			rating.setVerdict("");
		}
	}

	private HttpResponse getRequest(String url) throws CommunicationException {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		String sessionId = MainActivity.getSessionId();
		httpGet.setHeader("Authorization", "Tequila " + sessionId);
		try {
			httpResponse = SwengHttpClientFactory.getInstance()
					.execute(httpGet);

		} catch (IOException e) {
			throw new CommunicationException();
		}
		return httpResponse;
	}

	/**
	 * This method is responsable of posting a rating of a user to a
	 * quizzQuestion
	 */
	public RateState postRating(String verdict, QuizQuestion quizQuestion)
		throws CommunicationException {
		int questionID = quizQuestion.getId();
		return postUserRating(questionID, verdict);

	}

	private RateState postUserRating(int questionID, String rate)
		throws CommunicationException {
		HttpResponse response = null;
		try {
			JSONObject json = new JSONObject();
			json.put("verdict", rate);

			HttpPost post = new HttpPost(POST_RATING + "/" + questionID
					+ "/rating");
			post.setHeader("Authorization",
					"Tequila " + MainActivity.getSessionId());
			post.setEntity(new StringEntity(json.toString()));
			response = SwengHttpClientFactory.getInstance().execute(post);
			if (response != null) {
				int status = response.getStatusLine().getStatusCode();
				response.getEntity().getContent().close();
				if (status == OK_STATUS) {
					return RateState.UPDATED;
				} else if (status == CREATED_STATUS) {
					return RateState.REGISTRED;
				} else if (status == ERROR_STATUS) {
					throw new CommunicationException();
				} else {
					return RateState.NOTFOUND;
				}

			} else {
				return RateState.NOTFOUND;
			}
		} catch (JSONException e) {
			return RateState.NOTFOUND;
		} catch (IOException e) {
			throw new CommunicationException(e);
		}

	}
}

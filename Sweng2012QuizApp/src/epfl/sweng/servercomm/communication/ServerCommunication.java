package epfl.sweng.servercomm.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.showquestions.Rating;

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
	private static final int OK_STATUS = 200;
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
	 * This method is responsible of getting q quizzQuestion from the server 
	 * IF an error communication occurs then a Communication Exception is generated
	 */

	public QuizQuestion getQuizQuestion(String sessionId)
		throws CommunicationException {
		HttpGet httpGet = new HttpGet(GET_QUESTION_SERVER_URL);
		httpGet.setHeader("Authorization", "Tequila " + sessionId);
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
				} else {
					Log.d("Server Response while fetching question", ""+status);
					throw new CommunicationException();
				}
			} else {
				throw new CommunicationException();
			}

		} catch (IOException e) {
			throw new CommunicationException(e);
		} catch (JSONException e) {
			throw new CommunicationException(e);
		}

		return quizQuestion;
	}

	public void postQuestion(QuizQuestion quizQuestion) {
		// TODO Auto-generated method stub

	}

	public Rating getRatings() {
		// TODO Auto-generated method stub
		return null;
	}

	public void postRating(Rating rating) {
		// TODO Auto-generated method stub

	}

}

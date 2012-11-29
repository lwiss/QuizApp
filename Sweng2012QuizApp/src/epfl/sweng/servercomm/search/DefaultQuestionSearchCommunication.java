package epfl.sweng.servercomm.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
/**
 * 
 * @author crazybhy
 *
 */
public class DefaultQuestionSearchCommunication implements
		QuestionSearchCommunication {

	public DefaultQuestionSearchCommunication() {
		
	}
	
	public static void validArgument(String argument) {
		final int maxLength = 20;
		if (argument==null || argument.length()==0 || argument.length()>maxLength) {
			throw new IllegalArgumentException("Too long or Empty");
		}
		for (int i=0; i<argument.length(); i++) {
			if (!Character.isLetterOrDigit(argument.charAt(i))) {
				throw new IllegalArgumentException("Invalid Characters");
			}
		}
	}
	
	public static HttpResponse sendRequest(String url) throws CommunicationException {
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = SwengHttpClientFactory.getInstance();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (IOException e) {
			throw new CommunicationException("Communication error with the server");
		}
		return httpResponse;
	}
	
	public static String getResponseBody(HttpResponse httpResponse) throws CommunicationException {
		String responseBody = null;
		try {
			responseBody = EntityUtils.toString(httpResponse.getEntity());
		} catch (IOException e) {
			throw new CommunicationException("Communication error with the server");
		}
		return responseBody;
	}
	
	public static JSONArray getTheJSONArray(String responseBody) throws CommunicationException {
		JSONArray tabOfAnswers = null;
		try {
			tabOfAnswers = new JSONArray(responseBody);
		} catch (JSONException e) {
			throw new CommunicationException("Communication error with the server");
		}
		return tabOfAnswers;
	}
	
	public static QuizQuestion getAQuestion(JSONArray tabOfAnswers, int index) throws CommunicationException {
		QuizQuestion aQuestion = null;
		try {
			aQuestion = new QuizQuestion(tabOfAnswers.getString(index));
		} catch (JSONException e) {
			throw new CommunicationException("Communication error with the server");
		}
		return aQuestion;
	}
	
	public List<QuizQuestion> getQuestionsByOwner(String owner)
		throws CommunicationException {
		validArgument(owner);
		List<QuizQuestion> result = new ArrayList<QuizQuestion>();
		HttpResponse httpResponse = sendRequest("https://sweng-quiz.appspot.com/quizquestions/ownedby/"+owner);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		final int notFound = 404;
		final int found = 200;
		if (statusCode!=notFound && statusCode!=found) {
			throw new CommunicationException("Communication error with the server: Bad status");
		}
		if (statusCode == notFound) {
			return result;
		}
		String responseBody = getResponseBody(httpResponse);
		JSONArray tabOfAnswers = getTheJSONArray(responseBody);
		if (tabOfAnswers.length()==0) {
			throw new CommunicationException("Communication error with the server");
		}
		for (int i=0; i<tabOfAnswers.length(); i++) {
			QuizQuestion aQuestion = getAQuestion(tabOfAnswers, i);
			if (aQuestion.auditErrors(0)!=0) {
				throw new CommunicationException("Question returned by the server is invalid");
			} else if (!aQuestion.getOwner().equals(owner)) {
				throw new CommunicationException("Question that do not match the search criteria");
			} else {
				result.add(aQuestion);
			}
		}
		return result;
	}

	public List<QuizQuestion> getQuestionsByTag(String tag)
		throws CommunicationException {
		validArgument(tag);
		List<QuizQuestion> result = new ArrayList<QuizQuestion>();
		HttpResponse httpResponse = sendRequest("https://sweng-quiz.appspot.com/quizquestions/tagged/"+tag);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		final int notFound = 404;
		final int found = 200;
		if (statusCode!=notFound && statusCode!=found) {
			throw new CommunicationException("Communication error with the server");
		}
		if (statusCode == notFound) {
			return result;
		}
		String responseBody = getResponseBody(httpResponse);
		JSONArray tabOfAnswers = getTheJSONArray(responseBody);
		if (tabOfAnswers.length()==0) {
			throw new CommunicationException("Communication error with the server");
		}
		for (int i=0; i<tabOfAnswers.length(); i++) {
			QuizQuestion aQuestion = getAQuestion(tabOfAnswers, i);
			if (aQuestion.auditErrors(0)!=0) {
				throw new CommunicationException("Question returned by the server is invalid");
			} else if (!aQuestion.getTags().contains(tag)) {
				throw new CommunicationException("Question that do not match the search criteria");
			} else {
				result.add(aQuestion);
			}
		}
		return result;
	}

}

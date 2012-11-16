package epfl.sweng.editquestions;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.os.AsyncTask;

/**
 * 
 * @author crazybhy
 *
 */
public class SubmitQuestionAsyncTask extends
		AsyncTask<QuizQuestion, String, String> {
	private static final String URL = "https://sweng-quiz.appspot.com/quizquestions/";

	@Override
	protected String doInBackground(QuizQuestion... params) {
		QuizQuestion question = params[0];
		JSONObject json = new JSONObject();
		try {
			json.put("question", question.getQuestion());
			json.put("answers", new JSONArray(question.getAnswers()));
			json.put("solutionIndex", question.getSolutionIndex());
			json.put("tags", new JSONArray(question.getTags()));
		} catch (JSONException e) {
			System.err.println("Error while constructing JsonObject");
		}
		HttpPost post = new HttpPost(URL);
		String response = "";
		try {
			post.setEntity(new StringEntity(json.toString()));
			post.setHeader(new BasicHeader("Content-type", "application/json"));
			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpClient client = SwengHttpClientFactory.getInstance();
			response = client.execute(post, handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
}

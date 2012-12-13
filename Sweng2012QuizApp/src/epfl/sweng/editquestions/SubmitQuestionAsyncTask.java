package epfl.sweng.editquestions;

import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.communication.ServerCommunicationProxy;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * 
 * @author crazybhy
 * 
 */
public class SubmitQuestionAsyncTask extends AsyncTask<Object, String, String> {
	private EditQuestionActivity activity;
	private boolean questionPosted = false;

	@Override
	protected String doInBackground(Object... params) {
		activity = (EditQuestionActivity) params[1];
		QuizQuestion question = (QuizQuestion) params[0];
		questionPosted = ServerCommunicationProxy.getInstance().postQuestion(
				question);
		return null;
		/**
		 * JSONObject json = new JSONObject(); try { json.put("question",
		 * question.getQuestion()); json.put("answers", new
		 * JSONArray(question.getAnswers())); json.put("solutionIndex",
		 * question.getSolutionIndex()); json.put("tags", new
		 * JSONArray(question.getTags())); } catch (JSONException e) {
		 * System.err.println("Error while constructing JsonObject"); } HttpPost
		 * post = new HttpPost(URL); String response = ""; try {
		 * post.setEntity(new StringEntity(json.toString())); post.setHeader(new
		 * BasicHeader("Content-type", "application/json"));
		 * post.setHeader("Authorization", "Tequila " + sessionId);
		 * ResponseHandler<String> handler = new BasicResponseHandler();
		 * HttpClient client = SwengHttpClientFactory.getInstance(); response =
		 * client.execute(post, handler); } catch (IOException e) {
		 * e.printStackTrace(); } return response; }
		 */
	}

	@Override
	protected void onPostExecute(String string) {
		if (questionPosted) {
			if (MainActivity.isOnline()) {
				Toast.makeText(activity, "Question succefully posted",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(activity, "Your question has been cached",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(activity, "Question not posted! An eroor occured",
					Toast.LENGTH_LONG).show();
		}

	}
}

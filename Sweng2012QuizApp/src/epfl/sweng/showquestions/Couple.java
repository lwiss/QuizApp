package epfl.sweng.showquestions;

import org.json.JSONObject;

public class Couple {
	private JSONObject jsonObject;
	private ShowQuestionsActivity showQuestionsActivity;

	public Couple(JSONObject jsonObject,
			ShowQuestionsActivity showQuestionsActivity) {
		this.jsonObject = jsonObject;
		this.showQuestionsActivity = showQuestionsActivity;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public ShowQuestionsActivity getShowQuestionsActivity() {
		return showQuestionsActivity;
	}
}
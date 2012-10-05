package epfl.sweng.showquestions;

import org.json.JSONObject;

/**
 * 
 * @author Eagles
 * 
 */
public class Couple {
	private JSONObject jsonObject;
	private ShowQuestionsActivity showQuestionsActivity;

	public Couple(JSONObject json,
			ShowQuestionsActivity show) {
		this.jsonObject = json;
		this.showQuestionsActivity = show;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public ShowQuestionsActivity getShowQuestionsActivity() {
		return showQuestionsActivity;
	}
}
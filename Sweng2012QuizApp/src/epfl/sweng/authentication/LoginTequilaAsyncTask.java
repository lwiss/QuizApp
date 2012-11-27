package epfl.sweng.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author crazybhy
 *
 */
public class LoginTequilaAsyncTask extends AsyncTask<Object, String, String> {
	private AuthenticationActivity activity;
	private static final String SWENG_SERVER_URL = "https://sweng-quiz.appspot.com/login"; 
	private static final String TEQUILA_SERVER_URL = "https://tequila.epfl.ch/cgi-bin/tequila/login";
	private static final int TEQUILA_SUCCES_STATUS = 302;
	private static final int SWENG_UNSUCCESSFUL_STATUS = 403;

	@Override
	protected String doInBackground(Object... arg0) {
		activity = (AuthenticationActivity) arg0[2];
		String session = null;
		String token = step1and2();
		int status = step3and4((String) arg0[0], (String) arg0[1], token);
		if (status==TEQUILA_SUCCES_STATUS) {
			session = step5and6(token);
		}
		return session;
	}
	
	protected String step1and2() {
		String token = "";
		int status = -1;
		try {
			HttpGet httpGet = new HttpGet(SWENG_SERVER_URL);
			HttpResponse response = SwengHttpClientFactory.getInstance().execute(httpGet);
			status = response.getStatusLine().getStatusCode();
			String responseString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(responseString);
			token = jsonObject.getString("token");
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Log.e("SERVER", "Replied with status code " +  status);
		}
		return token;
	}
	
	protected int step3and4(String user, String pass, String token) {
		List<NameValuePair> postBodyList= new ArrayList<NameValuePair>();
		postBodyList.add(new BasicNameValuePair("requestkey", token));
		postBodyList.add(new BasicNameValuePair("username", user));
		postBodyList.add(new BasicNameValuePair("password", pass));
		int status = -1;
		try {
			UrlEncodedFormEntity postBodyEntity = new UrlEncodedFormEntity(postBodyList);
			HttpPost httpPost = new HttpPost(TEQUILA_SERVER_URL);
			httpPost.setEntity(postBodyEntity);
			HttpResponse response = SwengHttpClientFactory.getInstance().execute(httpPost);
			status = response.getStatusLine().getStatusCode();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Log.e("SERVER", "Replied with status code " + status);
		}
		return status;
	}
	
	protected String step5and6(String token) {
		String session = "";
		int status = -1;
		try {
			HttpPost httpPost = new HttpPost(SWENG_SERVER_URL);
			JSONObject postRequestBody = new JSONObject();
			postRequestBody.put("token", token);
			StringEntity postRequestEntity = new StringEntity(postRequestBody.toString());
			postRequestEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(postRequestEntity);
			HttpResponse response = SwengHttpClientFactory.getInstance().execute(httpPost);
			status = response.getStatusLine().getStatusCode();
			String responseString = EntityUtils.toString(response.getEntity());
			JSONObject jsonResponse = new JSONObject(responseString);
			if (response.getStatusLine().getStatusCode()!=SWENG_UNSUCCESSFUL_STATUS) {
				try {
					session=jsonResponse.getString("session");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Log.e("SERVER", "Replied with status code " + status);
		}
		return session;
	}
	
	protected void onPostExecute(String session) {
		if (session!=null) {
			SharedPreferences settings = activity.getSharedPreferences(MainActivity.PREF_NAME, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("SESSION_ID", session);
			editor.commit();
			activity.finish();
		} else {
			Toast.makeText(activity, "Authentication failed", Toast.LENGTH_SHORT).show();
			activity.initializeActivity();
		}
	}
	
}

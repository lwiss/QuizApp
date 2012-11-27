package epfl.sweng.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
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
		String token ="";
		HttpGet httpGet = new HttpGet(SWENG_SERVER_URL);
		AbstractHttpClient httpClient = SwengHttpClientFactory.getInstance();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR1", "ClientProtocolException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR1", "IOException");
		}
		String responseString = null;
		try {
			responseString = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR2", "ParseException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR2", "IOException");
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(responseString);
			token = jsonObject.getString("token");
		} catch (JSONException e) {
			Log.d("JSON ERROR", "Error due to the Json");
		}
		return token;
	}
	
	protected int step3and4(String user, String pass, String token) {
		List<NameValuePair> postBodyList= new ArrayList<NameValuePair>();
		postBodyList.add(new BasicNameValuePair("requestkey", token));
		postBodyList.add(new BasicNameValuePair("username", user));
		postBodyList.add(new BasicNameValuePair("password", pass));
		UrlEncodedFormEntity postBodyEntity = null;
		try {
			postBodyEntity = new UrlEncodedFormEntity(postBodyList);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpPost httpPost = new HttpPost(TEQUILA_SERVER_URL);
		httpPost.setEntity(postBodyEntity);
		HttpResponse response = null;
		try {
			response= SwengHttpClientFactory.getInstance().execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.getStatusLine().getStatusCode();
	}
	
	protected String step5and6(String token) {
		HttpPost httpPost = new HttpPost(SWENG_SERVER_URL);
		JSONObject postRequestBody = new JSONObject();
		try {
			postRequestBody.put("token", token);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringEntity postRequestEntity = null;
		try {
			postRequestEntity = new StringEntity(postRequestBody.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postRequestEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpPost.setEntity(postRequestEntity);
		HttpResponse response = null;
		try {
			response= SwengHttpClientFactory.getInstance().execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String responseString = null;
		try {
			responseString = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(responseString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = "";
		try {
			message = jsonResponse.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String session="";
		if (!message.equals("The token you specified was invalid.") && !message.equals("")) {
			try {
				session=jsonResponse.getString("session");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

package epfl.sweng.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import epfl.sweng.R;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @author bhy
 *
 */
public class AuthenticationActivity extends Activity {
	private static String token=null;
	private static final String SWENG_SERVER_URL="https://sweng-quiz.appspot.com/login"; 
	private static final String TEQUILA_SERVER_URL="https://tequila.epfl.ch/cgi-bin/tequila/login";
	private EditText usernameTF;
	private EditText passwordTF;
	private static final int TEQUILA_SUCCESS_STATUS = 302;
	private static final int SERVER_UNSUCCESS_STATUS = 403;

    public void setUsernameTF(String username) {
		this.usernameTF.setText(username);
	}

	public void setPasswordTF(String password) {
		this.passwordTF.setText(password);
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String aToken) {
		AuthenticationActivity.token = aToken;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        usernameTF=(EditText) findViewById(R.id.username);
        passwordTF=(EditText) findViewById(R.id.password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_authentication, menu);
        return true;
    }
    
    
    
    public void loginMethod(View v) {
    	ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMan.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			new LoginTequila().execute(SWENG_SERVER_URL, TEQUILA_SERVER_URL, 
					usernameTF.getText().toString(), passwordTF.getText().toString(), this);
		}
    	
    }




	/**
     * This class ensures the authentication of the client... 
     * @author wissem
     *
     */
    private class LoginTequila extends AsyncTask<Object, String , String> {
    	
    
		protected String doInBackground(Object... params) {
			String sessionId = null; 
			int  status=-1;
			
			AuthenticationActivity.setToken(retrieveToken(SWENG_SERVER_URL));
			final int userParameterNumber=2;
			final int passwordParameterNumber=3;
			status = tequilaPostRequest(token,
					(String) params[userParameterNumber],
					(String) params[passwordParameterNumber],
					TEQUILA_SERVER_URL);
			if (status==TEQUILA_SUCCESS_STATUS) {
				//if the post request is successful (<==> the status code returned by the tequila server is 302)
				//then we execute the step 5 : we essue an http Post request to SwengApp server
				HttpPost swengPostRequest= new HttpPost(SWENG_SERVER_URL);
				try {
					JSONObject postRequestBody=new JSONObject();
					//we form the jsonObject that we will send to Sweng Server.
					postRequestBody.put("token", AuthenticationActivity.getToken());
					//we form the entity that we will send to the Sweng server, based on the jsonObject.
					StringEntity se = new StringEntity(postRequestBody.toString());
					//this manipulation is necessary,
						//because we have to notify the server that the post body is a json Object.
					//se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					
					//we set the request entity to the entity that we have already formed.
					swengPostRequest.setEntity(se);
					swengPostRequest.setHeader(HTTP.CONTENT_TYPE, "application/json");
					//here we send the post request to the Sweng Server:
					HttpResponse response = SwengHttpClientFactory.getInstance().execute(swengPostRequest);
					status = response.getStatusLine().getStatusCode();
					Log.d("status line", response.getStatusLine().toString());
					Log.e("SERVER3", "Replied with status code " +  status); //prints the response's status code 
					if (status!=SERVER_UNSUCCESS_STATUS) {
						//If the post request is successful (<==> the status code returned by the tequila server is 302)
						//then we execute the step 6 : we extract the sessionId of the server's response.
						String resBody = EntityUtils.toString(response.getEntity());
						JSONObject jsonResponse= new JSONObject(resBody);
					    sessionId=jsonResponse.getString("session");			
					} else {
						String resBody = EntityUtils.toString(response.getEntity());
						JSONObject jsonResponse= new JSONObject(resBody);
						String message=jsonResponse.getString("message");
						Log.e("SERVER", "Replied with status code " +  status); //prints the response's status code 
						Log.e("SERVER", "Replied with message " +  message); //prints the response's message			
					}
					
					
				} catch (UnsupportedEncodingException e) {
					Log.d("ERROR", "Error while constructing the post body");
				} catch (JSONException e) {
					Log.d("JSON ERROR", "Error due to the Json");
				} catch (ClientProtocolException e) {
					Log.d("CONNECTION ERROR", "Protocol error");
				} catch (IOException e) {
					Log.d("CONNECTION ERROR", "Error recieving the post server's response");
				}
			}
			else {
				
			}
				
			return sessionId;
			
			
		}
		/**
		 * The following method does Step1 and Step 2 of the authentication process :
		 * it issues a httpGet request to retrieve the authentication token that provides 
		 * the SwengApp server
		 * @param url: the url of the Sweng2012QuizApp server
		 * @return the token that is returned by the Sweng2012QuizApp server, and null if an error occurs
		 */
		private String retrieveToken(String url) {
			HttpGet request = new HttpGet(url);
			HttpResponse response=null;
			String aToken=null;
			try {
				response= SwengHttpClientFactory.getInstance().execute(request);
				String resBody = EntityUtils.toString(response.getEntity());
				JSONObject jsonResponse= new JSONObject(resBody);
				aToken=jsonResponse.getString("token");
			} catch (JSONException e) {
				Log.d("JSON ERROR", "Error due to the Json");
				return aToken;
			} catch (IOException e) {
				Log.d("CONNECTION ERROR", "Error retrieving the token");
				return aToken;
			} finally {
				Log.e("SERVER1", "Replied with status code " +  response.getStatusLine().getStatusCode());
			}
			return aToken;
			
		}
		/**
		 * This method issues an HttpPost request to the specified url  
		 * @param token: The token returned by the Sweng2012QuizApp server 
		 * @return the status of the server's response 
		 */
		private int tequilaPostRequest(String requestToken, String user, String pass, String url) {
			
			List<NameValuePair> postBodyList= new ArrayList<NameValuePair>();
			postBodyList.add(new BasicNameValuePair("requestkey", requestToken));
			postBodyList.add(new BasicNameValuePair("username", user));
			postBodyList.add(new BasicNameValuePair("password", pass));
			int statusCode=-1;
			try {
				UrlEncodedFormEntity postBodyField = new UrlEncodedFormEntity(postBodyList);
				HttpPost request = new HttpPost(url); 
				request.setEntity(postBodyField);
				HttpResponse response= SwengHttpClientFactory.getInstance().execute(request);
				statusCode= response.getStatusLine().getStatusCode();
			} catch (UnsupportedEncodingException e) {
				Log.d("ERROR", "Error while constructing the post body");
				return statusCode;
			} catch (ClientProtocolException e) {
				Log.d("CONNECTION ERROR", "Protocol error");
				return statusCode;
			} catch (IOException e) {
				Log.d("CONNECTION ERROR", "Error recieving the post server's response");
				return statusCode;
			} finally {
				Log.e("SERVER2", "Replied with status code " + statusCode);
			}
			
			
			return statusCode;
		}
		
		protected void onPostExecute(String sessionId) {
			if (sessionId!=null) {
				SharedPreferences settings = getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("SESSION_ID", sessionId);
				editor.commit();
				AuthenticationActivity.this.finish();
			} else {
				token= null; 
				Toast.makeText(AuthenticationActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
				AuthenticationActivity.this.setUsernameTF("");
				AuthenticationActivity.this.setPasswordTF("");
				
				
			}
		}
		
    	
    }
}
/**il manque encore l'etape 8 : le logout**/

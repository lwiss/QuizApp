package epfl.sweng.servercomm;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.RedirectHandler;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * This factory creates HttpClients. It also allows to inject custom HttpClients
 * for testing.
 */
public class SwengHttpClientFactory {

	private static AbstractHttpClient httpClient;
	
	public static synchronized AbstractHttpClient getInstance() {
		if (httpClient == null) {
			httpClient = create();
		}
		
		return httpClient;
	}

	public static synchronized void setInstance(AbstractHttpClient instance) {
		httpClient = instance;
	}

	final private static RedirectHandler redirectNoFollow = new RedirectHandler() {
		@Override
		public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
			return false;
		}

		@Override
		public URI getLocationURI(HttpResponse response, HttpContext context) throws org.apache.http.ProtocolException {
			return null;
		}
	};

	private static AbstractHttpClient create() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		HttpParams params = new BasicHttpParams();
		ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		AbstractHttpClient result = new DefaultHttpClient(connManager, params);
		result.setRedirectHandler(redirectNoFollow);
		return result;
	}

}

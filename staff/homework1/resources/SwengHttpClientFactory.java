package epfl.sweng.servercomm;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectHandler;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.HttpResponse;
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
	private static final int HTTP_PORT = 80;
	private final static int HTTPS_PORT = 443;


	public static synchronized AbstractHttpClient getInstance() {
		if (httpClient == null) {
			httpClient = create();
		}

		return httpClient;
	}

	public static synchronized void setInstance(AbstractHttpClient instance) {
		httpClient = instance;
	}

	final private static RedirectHandler REDIRECT_NO_FOLLOW = new RedirectHandler() {
		@Override
		public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
			return false;
		}

		@Override
		public URI getLocationURI(HttpResponse response, HttpContext context) throws org.apache.http.ProtocolException {
			return null;
		}
	};

	final private static CookieStore COOKIE_MONSTER = new CookieStore() {
		@Override
		public void addCookie(Cookie cookie) {
		}

		@Override
		public void clear() {
		}

		@Override
		public boolean clearExpired(Date date) {
			return true;
		}

		@Override
		public List<Cookie> getCookies() {
			return new ArrayList<Cookie>();
		}
	};

	private static AbstractHttpClient create() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), HTTPS_PORT));
		HttpParams params = new BasicHttpParams();
		ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		AbstractHttpClient result = new DefaultHttpClient(connManager, params);
		result.setRedirectHandler(REDIRECT_NO_FOLLOW);
		result.setCookieStore(COOKIE_MONSTER);
		return result;
	}

}

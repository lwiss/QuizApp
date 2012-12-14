package epfl.sweng.entry;

import epfl.sweng.servercomm.communication.ServerCommunicationProxy;
import android.os.AsyncTask;
/**
 * This class is responsable of sending the cached Content
 * @author MohamedBenArbia
 *
 */
public class SendCachAsyncTask extends AsyncTask<Object, Object, Object> {

	@Override
	protected Object doInBackground(Object... params) {
		ServerCommunicationProxy.getInstance().sendCachedContent();
		return null;
	}

}

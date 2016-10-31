package wei.learn.cef;


import org.cef.callback.CefQueryCallback;

import com.google.gson.Gson;

public class BrowserFunctionCallback {
	private CefQueryCallback cefQueryCallback;
	private static Gson gson = new Gson();

	BrowserFunctionCallback(CefQueryCallback cefQueryCallback) {
		this.cefQueryCallback = cefQueryCallback;
	}

	public void success(Object response) {
		if (response instanceof String) {
			this.cefQueryCallback.success((String) response);
		} else {
			this.cefQueryCallback.success(gson.toJson(response));
		}

	}

	public void failure(int error_code, String error_message) {
		this.cefQueryCallback.failure(error_code, error_message);
	}
}
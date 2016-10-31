package wei.learn.cef;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

public class BrowserFunctionRouterHandler extends
		CefMessageRouterHandlerAdapter {
	private Map<String, BrowserFunction> allFunctions = new HashMap();

	public void addBrowserFunction(BrowserFunction browserFunction) {
		if (browserFunction != null) {
			this.allFunctions.put(browserFunction.getName(), browserFunction);
		}

	}

	public void removeBrowserFunction(BrowserFunction browserFunction) {
		if (browserFunction != null) {
			this.allFunctions.remove(browserFunction.getName());
		}

	}

	public BrowserFunction getBrowserFunction(String name) {
		return (BrowserFunction) this.allFunctions.get(name);
	}

	public void clearBrowserFunction() {
		Iterator arg1 = this.allFunctions.values().iterator();

		while (arg1.hasNext()) {
			BrowserFunction function = (BrowserFunction) arg1.next();
			function.dispose(false);
		}

		this.allFunctions.clear();
	}

	public boolean onQuery(CefBrowser browser, long query_id, String request,
			boolean persistent, CefQueryCallback callback) {
		if (request != null) {
			String[] arguments = (String[]) null;
			int s = request.indexOf("?");
			String name = request;
			if (s > 0) {
				name = request.substring(0, s);
				arguments = request.substring(s + 1).split("&");
			}

			BrowserFunction function = (BrowserFunction) this.allFunctions
					.get(name);
			String[] argumentsF = arguments;
			if (function != null) {
				try {
					function.function(argumentsF, new BrowserFunctionCallback(
							callback));
				} catch (Exception arg12) {
					arg12.printStackTrace();
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public void onQueryCanceled(CefBrowser browser, long query_id) {
		super.onQueryCanceled(browser, query_id);
	}

	public Map<String, BrowserFunction> getAllFunctions() {
		return this.allFunctions;
	}
}
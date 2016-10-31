package wei.learn.cef;


import org.cef.browser.CefBrowser;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefRequest.ResourceType;
import org.cef.network.CefWebPluginInfo;
import org.eclipse.swt.browser.ProgressEvent;

final class RequestHandler extends CefRequestHandlerAdapter {
	private final CEF3Browser browser;

	public RequestHandler(CEF3Browser browser) {
		this.browser = browser;
	}

	public boolean onBeforeBrowse(CefBrowser browser, CefRequest request,
			boolean is_redirect) {
		if ("data:text/html,chromewebdata".equals(request.getURL())) {
			return true;
		} else if ("chrome-devtools://devtools/devtools.html".equals(request
				.getURL())) {
			return false;
		} else {
			boolean top = request.getResourceType() == ResourceType.RT_MAIN_FRAME;
			if (top && request.getURL() != null) {
				ProgressEvent progressEvent = new ProgressEvent(this.browser);
				progressEvent.current = 0;
				progressEvent.total = 100;
				this.browser.notifyListeners(progressEvent);
				LocationChangingEvent event = new LocationChangingEvent(
						this.browser);
				event.doit = true;
				event.location = request.getURL();
				event.top = request.getResourceType() == ResourceType.RT_MAIN_FRAME;
				this.browser.setLoadingURL(event.location);
				this.browser.notifyListeners(event);
				return !event.doit;
			} else {
				return false;
			}
		}
	}

	public boolean onBeforeResourceLoad(CefBrowser browser, CefRequest request) {
		return false;
	}

	public CefResourceHandler getResourceHandler(CefBrowser browser,
			CefRequest request) {
		return null;
	}

	public void onResourceRedirect(CefBrowser browser, String old_url,
			StringRef new_url) {
	}
}
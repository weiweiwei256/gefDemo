package wei.learn.cef;


import org.cef.browser.CefBrowser;
import org.cef.handler.CefLoadHandlerAdapter;
import org.eclipse.swt.browser.ProgressEvent;

final class LoadHandler extends CefLoadHandlerAdapter {
	public static final int TOTAL_PROGRESS = 100;
	private final CEF3Browser cef3Browser;
	private int loadStarted = 0;
	private int loadCompleted = 0;
	private String loadingURL;

	public LoadHandler(CEF3Browser browser) {
		this.cef3Browser = browser;
	}

	public void onLoadStart(CefBrowser browser, int frameIdentifer) {
		browser.setZoomLevel(Math.log(PlatformUtil.getDPIScale())
				/ Math.log(1.2D));
		if (!this.isDevTools(browser)) {
			if (frameIdentifer == 1) {
				this.loadingURL = browser.getURL();
			}

			++this.loadStarted;
		}
	}

	public void onLoadEnd(CefBrowser browser, int frameIdentifier,
			int httpStatusCode) {
		if (!this.isDevTools(browser)) {
			if (this.loadStarted > 0) {
				ProgressEvent url = new ProgressEvent(this.cef3Browser);
				url.total = 100;
				if (frameIdentifier == 1) {
					url.current = 100;
					this.loadStarted = this.loadCompleted = 0;
					this.cef3Browser.notifyListeners(url);
				} else if (++this.loadCompleted < this.loadStarted) {
					url.current = 100 * this.loadCompleted / this.loadStarted;
					this.cef3Browser.notifyListeners(url);
				}
			}

			if (frameIdentifier == 1) {
				this.cef3Browser.setLoadingURL((String) null);
				if ("data:text/html,chromewebdata".equals(browser.getURL())) {
					String arg4 = StringUtil.isBlank(this.loadingURL) ? this.cef3Browser
							.getUrl() : this.loadingURL;
					this.loadingURL = null;
					browser.loadString("<html><body><h1>无法访问页面" + arg4
							+ "</h1></body></html>", arg4);
				} else if (this.cef3Browser.getUrl() != null
						&& !this.cef3Browser.getUrl().equalsIgnoreCase(
								browser.getURL())) {
					this.loadingURL = null;
					browser.loadURL(this.cef3Browser.getUrl());
				}
			}

		}
	}

	public void onLoadError(CefBrowser browser, int frameIdentifer,
			ErrorCode errorCode, String errorText, String failedUrl) {
		if (!this.isDevTools(browser) && frameIdentifer == 1) {
			if (!ErrorCode.ERR_NONE.equals(errorText)) {
				this.cef3Browser.setLoadingURL((String) null);
			}

			ProgressEvent event = new ProgressEvent(this.cef3Browser);
			event.total = 100;
			event.current = 100;
			this.cef3Browser.notifyListeners(event);
		}

	}

	public void onLoadingStateChange(CefBrowser browser, boolean isLoading,
			boolean canGoBack, boolean canGoForward) {
	}

	protected boolean isDevTools(CefBrowser browser) {
		return "chrome-devtools://devtools/devtools.html".equals(browser
				.getURL());
	}
}
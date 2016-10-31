/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package wei.learn.cef;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefLifeSpanHandlerAdapter;

final class LifeSpanHandler extends CefLifeSpanHandlerAdapter {
	private final CEF3Browser browser;
	private boolean sentCloseEvent = false;

	public LifeSpanHandler(CEF3Browser browser) {
		this.browser = browser;
	}

	public boolean onBeforePopup(CefBrowser browser, String target_url,
			String target_frame_name) {
		if (this.browser.isSame(browser)) {
			OpenWindowEvent event = new OpenWindowEvent(this.browser);
			event.required = true;
			event.location = target_url;
			this.browser.notifyListeners(event);
		}

		return true;
	}

	public void onAfterCreated(CefBrowser cefBrowser) {
		this.browser.onCreated(cefBrowser);
	}

	public boolean runModal(CefBrowser cefBrowser) {
		return false;
	}

	public boolean doClose(CefBrowser cefBrowser) {
		CloseWindowEvent event = new CloseWindowEvent(this.browser);
		event.browser = this.browser;
		this.browser.notifyListeners(event);
		this.sentCloseEvent = true;
		return event.doit;
	}

	public void onBeforeClose(CefBrowser cefBrowser) {
		this.browser.onClose();
		if (!this.sentCloseEvent) {
			CloseWindowEvent event = new CloseWindowEvent(this.browser);
			event.browser = this.browser;
			this.browser.notifyListeners(event);
		}
	}
}
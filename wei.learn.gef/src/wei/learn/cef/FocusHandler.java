/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package wei.learn.cef;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefFocusHandlerAdapter;
import org.eclipse.swt.widgets.Event;

final class FocusHandler extends CefFocusHandlerAdapter {
	private final CEF3Browser browser;

	public FocusHandler(CEF3Browser browser) {
		this.browser = browser;
	}

	public void onTakeFocus(CefBrowser cefBrowser, final boolean next) {
		this.browser.runInUI(new Runnable() {
			public void run() {
				FocusHandler.this.browser.traverse(next ? 16 : 8);
			}
		});
	}

	public boolean onSetFocus(CefBrowser browser, FocusSource source) {
		if (browser != null && this.browser.getCefBroser() == browser) {
			Event event = new Event();
			event.widget = this.browser;
			return true;
		} else {
			return true;
		}
	}
}
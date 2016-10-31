/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package wei.learn.cef;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.TitleEvent;

import wei.learn.cef.listener.ConsoleMessageEvent;

final class DisplayHandler extends CefDisplayHandlerAdapter {
	private final CEF3Browser browser;

	public DisplayHandler(CEF3Browser browser) {
		this.browser = browser;
	}

	public void onAddressChange(CefBrowser browser, String url) {
		if (!"about:blank".equals(url)) {
			LocationEvent event = new LocationEvent(this.browser);
			event.location = url;
			event.top = !browser.isPopup();
			this.browser.notifyListeners(event);
		}
	}

	public void onTitleChange(CefBrowser cefBrowser, String title) {
		if (!"about:blank".equals(cefBrowser.getURL())) {
			TitleEvent event = new TitleEvent(this.browser);
			event.title = title;
			this.browser.notifyListeners(event);
		}
	}

	public boolean onConsoleMessage(CefBrowser cefBrowser, String message,
			String source, int line) {
		try {
			ConsoleMessageEvent e = new ConsoleMessageEvent(this.browser);
			e.message = message;
			e.source = source;
			e.line = line;
			this.browser.notifyListeners(e);
		} catch (Throwable arg5) {
		   arg5.printStackTrace();
		}

		return false;
	}
}
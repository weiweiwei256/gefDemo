/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package wei.learn.cef;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefKeyboardHandlerAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Event;

final class KeyboardHandler extends CefKeyboardHandlerAdapter implements
		FocusListener {
	private final CEF3Browser browser;
	private Stack<Integer> charCodes = new Stack();
	private Map<Integer, Integer> charMaps = new HashMap();

	public KeyboardHandler(CEF3Browser browser) {
		this.charMaps.put(Integer.valueOf(16), Integer.valueOf(131072));
		this.charMaps.put(Integer.valueOf(17), Integer.valueOf(262144));
		this.charMaps.put(Integer.valueOf(18), Integer.valueOf(65536));
		this.browser = browser;
		if (this.browser != null) {
			this.browser.addFocusListener(this);
		}

	}

	public boolean OnKeyEvent(CefBrowser cefBrowser, int type, int code,
			int modifiers, boolean isSystemKey, boolean isAfterJavaScript) {
		if (isAfterJavaScript) {
			return false;
		} else if (this.charMaps.get(Integer.valueOf(code)) != null) {
			System.out.println("type:" + type);
			if (type == 0) {
				this.charCodes.add((Integer) this.charMaps.get(Integer
						.valueOf(code)));
			} else if (type == 2) {
				this.charCodes.remove(this.charMaps.get(Integer.valueOf(code)));
			}

			return true;
		} else {
			Event event = new Event();
			event.widget = this.browser;
			event.character = 0;

			for (event.keyCode = code; !this.charCodes.isEmpty(); event.stateMask |= ((Integer) this.charCodes
					.pop()).intValue()) {
				;
			}

			switch (type) {
			case 0:
				System.out.println("KEYEVENT_RAWKEYDOWN " + code + " "
						+ modifiers + " " + isSystemKey);
				break;
			case 1:
				System.out.println("KEYEVENT_KEYDOWN " + code + " " + modifiers
						+ " " + isSystemKey);
				break;
			case 2:
				System.out.println("KEYEVENT_KEYUP " + code + " " + modifiers
						+ " " + isSystemKey);
				break;
			case 3:
				System.out.println("KEYEVENT_CHAR " + code + " " + modifiers
						+ " " + isSystemKey);
			}

			this.browser.notifyEventListeners(1, event);
			return !event.doit;
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		this.charCodes.clear();
	}
}
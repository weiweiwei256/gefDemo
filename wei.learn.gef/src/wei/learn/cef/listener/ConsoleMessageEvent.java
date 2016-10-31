package wei.learn.cef.listener;


import org.eclipse.swt.events.TypedEvent;

import wei.learn.cef.CEF3Browser;

public class ConsoleMessageEvent extends TypedEvent {
	private static final long serialVersionUID = 1L;
	public String message;
	public String source;
	public int line;

	public ConsoleMessageEvent(CEF3Browser browser) {
		super(browser);
	}
}
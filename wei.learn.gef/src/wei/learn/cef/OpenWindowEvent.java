package wei.learn.cef;


import org.eclipse.swt.browser.WindowEvent;

public class OpenWindowEvent extends WindowEvent {
	private static final long serialVersionUID = 1L;
	public CEF3Browser browser;
	public String location;

	public OpenWindowEvent(CEF3Browser browser) {
		super(browser);
	}
}
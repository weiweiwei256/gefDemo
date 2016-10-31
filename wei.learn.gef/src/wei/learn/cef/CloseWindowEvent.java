package wei.learn.cef;


import org.eclipse.swt.browser.WindowEvent;

public class CloseWindowEvent extends WindowEvent {
	private static final long serialVersionUID = 1L;
	public CEF3Browser browser;
	public boolean doit = true;

	public CloseWindowEvent(CEF3Browser browser) {
		super(browser);
	}
}
package wei.learn.cef;


import org.eclipse.swt.browser.LocationEvent;

public class LocationChangingEvent extends LocationEvent {
	private static final long serialVersionUID = 1L;

	public LocationChangingEvent(CEF3Browser browser) {
		super(browser);
	}
}
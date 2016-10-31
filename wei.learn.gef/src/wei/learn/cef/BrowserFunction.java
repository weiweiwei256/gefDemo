package wei.learn.cef;


import org.eclipse.swt.SWT;

public abstract class BrowserFunction {
	public static final Object NULL = new Object();
	private CEF3Browser browser;
	String name;
	String functionString;
	boolean isEvaluate;
	boolean isDisposed;

	public BrowserFunction(CEF3Browser browser, String name) {
		this(browser, name, true);
	}

	public BrowserFunction(CEF3Browser browser, String name, boolean create) {
		if (browser == null) {
			SWT.error(4);
		}

		if (name == null) {
			SWT.error(4);
		}

		if (browser.isDisposed()) {
			SWT.error(24);
		}

		browser.checkWidget();
		this.browser = browser;
		this.name = name;
		if (create) {
			browser.createFunction(this);
		}

	}

	public void dispose() {
		this.dispose(true);
	}

	void dispose(boolean remove) {
		if (remove) {
			this.browser.destroyFunction(this);
		}

		this.browser = null;
		this.name = null;
		this.functionString = null;
		this.isDisposed = true;
	}

	public abstract void function(Object[] arg0, BrowserFunctionCallback arg1)
			throws Exception;

	public CEF3Browser getBrowser() {
		this.browser.checkWidget();
		return this.browser;
	}

	public String getName() {
		this.browser.checkWidget();
		return this.name;
	}

	public boolean isDisposed() {
		return this.isDisposed;
	}
}
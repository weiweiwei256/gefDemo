package wei.learn.cef.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;

import wei.learn.cef.CEF3Browser;

public class TestJCEF extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			TestJCEF shell = new TestJCEF(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public TestJCEF(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 10, 449, 241);
		CEF3Browser browser = new CEF3Browser(composite, SWT.NONE);
		browser.setUrl("www.baidu.com");
	}

	/**
	 * Create contents of the shell.
	 */

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

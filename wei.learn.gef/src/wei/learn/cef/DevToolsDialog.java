/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package wei.learn.cef;

import java.awt.Frame;

import org.cef.browser.CefBrowser;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class DevToolsDialog {
	private CefBrowser devTools_;
	private CEF3Browser browser;
	private Shell shell;

	public DevToolsDialog(CEF3Browser browser) {
		this.browser = browser;
	}

	public void createContent() {
		if (this.browser != null && this.browser.getCefBroser() != null) {
			if (this.shell != null && !this.shell.isDisposed()) {
				this.shell.forceFocus();
			} else {
				this.devTools_ = this.browser.getCefBroser().getDevTools();
				this.shell = new Shell(this.browser.getShell(), 1264);
				this.shell.setLayout(new FillLayout());
				this.shell.setImage(Window.getDefaultImage());
				this.shell.setSize(800, 600);
				this.shell.setLocation(
						this.browser.getShell().getLocation().x + 20,
						this.browser.getShell().getLocation().y + 20);
				Composite comp = new Composite(this.shell, 16777216);
				Frame frame = SWT_AWT.new_Frame(comp);
				frame.add(this.devTools_.getUIComponent());
				this.shell.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						DevToolsDialog.this.devTools_.close();
						DevToolsDialog.this.devTools_ = null;
					}
				});
				this.browser.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						DevToolsDialog.this.shell.dispose();
					}
				});
			}
		}
	}

	public void open() {
		try {
			if (this.shell != null && !this.shell.isDisposed()) {
				this.shell.open();
			}
		} catch (Throwable arg1) {
			arg1.printStackTrace();
		}

	}

	public boolean isDisposed() {
		return this.shell == null || this.shell.isDisposed();
	}

	public boolean isVisible() {
		return this.shell != null && this.shell.isVisible();
	}

	public void setVisible(boolean isVisible) {
		if (this.shell != null) {
			this.shell.setVisible(isVisible);
		}

	}

	public boolean isMinimized() {
		return this.shell != null && this.shell.getMinimized();
	}

	public void setMinimized(boolean minimized) {
		if (this.shell != null) {
			this.shell.setMinimized(minimized);
		}

	}

	public void setFocus() {
		if (this.shell != null) {
			this.shell.setFocus();
		}

	}

	public Shell getShell() {
		return this.shell;
	}
}
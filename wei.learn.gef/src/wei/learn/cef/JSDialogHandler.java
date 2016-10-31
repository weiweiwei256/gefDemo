/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package wei.learn.cef;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefJSDialogCallback;
import org.cef.handler.CefJSDialogHandlerAdapter;
import org.cef.misc.BoolRef;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

final class JSDialogHandler extends CefJSDialogHandlerAdapter {
	private static final String TITLE = "JavaScript";
	private final CEF3Browser browser;

	public JSDialogHandler(CEF3Browser browser) {
		this.browser = browser;
	}

	public boolean onJSDialog(final CefBrowser browser, String origin_url,
			String accept_lang, final JSDialogType dialog_type,
			final String message_text, final String default_prompt_text,
			final CefJSDialogCallback callback, BoolRef suppress_message) {
		if (!System.getProperty("os.name").toLowerCase()
				.startsWith("windows xp")) {
			return false;
		} else {
			if (PlatformUI.getWorkbench() != null
					&& PlatformUI.getWorkbench().getDisplay() != null
					&& !PlatformUI.getWorkbench().getDisplay().isDisposed()) {
				suppress_message.set(true);
				PlatformUI.getWorkbench().getDisplay()
						.asyncExec(new Runnable() {
							public void run() {
								if (dialog_type == JSDialogType.JSDIALOGTYPE_ALERT) {
									JSDialogHandler.this.OnJSAlert(browser,
											message_text);
								} else if (dialog_type == JSDialogType.JSDIALOGTYPE_CONFIRM) {
									JSDialogHandler.this.OnJSConfirm(browser,
											message_text, callback);
								} else if (dialog_type == JSDialogType.JSDIALOGTYPE_PROMPT) {
									JSDialogHandler.this.OnJSPrompt(browser,
											message_text, default_prompt_text,
											callback);
								}

							}
						});
			}

			return false;
		}
	}

	public boolean OnJSAlert(CefBrowser cefBrowser, String message) {
		MessageBox mbox = new MessageBox(this.browser.getShell(), 40);
		mbox.setText("JavaScript");
		mbox.setMessage(message);
		mbox.open();
		return true;
	}

	public boolean OnJSConfirm(CefBrowser cefBrowser, String message,
			CefJSDialogCallback callback) {
		MessageBox mbox = new MessageBox(this.browser.getShell(), 292);
		mbox.setText("JavaScript");
		mbox.setMessage(message);
		callback.Continue(mbox.open() == 32 || mbox.open() == 64, (String) null);
		return true;
	}

	public boolean OnJSPrompt(CefBrowser cefBrowser, String message,
			String defaultValue, CefJSDialogCallback callback) {
		PromptDialog dlg = new PromptDialog(this.browser.getShell());
		dlg.setText("JavaScript");
		dlg.setMessage(message);
		dlg.setInitialValue(defaultValue);
		callback.Continue(dlg.open() == 32, dlg.getValue());
		return true;
	}
}
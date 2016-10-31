package wei.learn.cef;


import java.awt.EventQueue;
import java.awt.Frame;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefStringVisitor;
import org.cef.network.CefPostData;
import org.cef.network.CefPostDataElement;
import org.cef.network.CefRequest;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

import wei.learn.cef.listener.ConsoleMessageEvent;
import wei.learn.cef.listener.ConsoleMessageListener;

public final class CEF3Browser extends Composite {
	static final String URL_BLANK = "about:blank";
	private static final String URL_TEXT = "html:internal";
	private static final String URL_SCRIPT = "javascript:";
	private static final String ERROR_ID = "com.hbuilder.swt.cef3.error";
	private static boolean hasCreateBrowser = false;
	private Set<ShellListener> devToolShellListeners = new HashSet();
	private CefClient browserClient;
	private CefBrowser cefBrowser;
	private BrowserFunctionRouterHandler browserFunctionRouterHandler;
	private String loadURL;
	private String loadText;
	private CefRequest loadRequest;
	private static int remoteDebuggingPort = -1;
	private boolean enableDefaultMenu = true;
	private boolean enableDevTools;
	DevToolsDialog devToolsDialog = null;
	private String loadingURL = null;
	private ListenerList<CloseWindowListener> closeWindowListeners = new ListenerList(
			CloseWindowListener.class);
	private ListenerList<LocationListener> locationListeners = new ListenerList(
			LocationListener.class);
	private ListenerList<OpenWindowListener> openWindowListeners = new ListenerList(
			OpenWindowListener.class);
	private ListenerList<ProgressListener> progressListeners = new ListenerList(
			ProgressListener.class);
	private ListenerList<StatusTextListener> statusTextListeners = new ListenerList(
			StatusTextListener.class);
	private ListenerList<TitleListener> titleListeners = new ListenerList(
			TitleListener.class);
	private ListenerList<ConsoleMessageListener> consoleMessageListeners = new ListenerList(
			ConsoleMessageListener.class);
	private static boolean hasInited = false;

	public CEF3Browser(Composite parent, int style) {
		super(parent, 16777216);
		this.setBackground(this.getDisplay().getSystemColor(25));
		this.init();
	}

	protected void checkWidget() {
		super.checkWidget();
	}

	public int getRemoteDebuggingPort() {
		return remoteDebuggingPort;
	}

	boolean isSame(CefBrowser cefBrowser) {
		return this.cefBrowser == null || !this.cefBrowser.equals(cefBrowser);
	}

	private void createBrowser() {
		Frame frame = SWT_AWT.new_Frame(this);
		this.cefBrowser = this.browserClient.createBrowser(
				this.loadURL != null ? this.loadURL : "about:blank",
				OS.isLinux(), false);
		frame.add(this.cefBrowser.getUIComponent());
	}

	void createFunction(BrowserFunction function) {
		this.registerFunction(function);
	}

	void destroyFunction(BrowserFunction function) {
		this.deregisterFunction(function);
	}

	private void registerFunction(BrowserFunction function) {
		this.browserFunctionRouterHandler.addBrowserFunction(function);
	}

	private void deregisterFunction(BrowserFunction function) {
		this.browserFunctionRouterHandler.removeBrowserFunction(function);
	}

	private void ensureBrowser() {
		if (this.cefBrowser == null) {
			this.createBrowser();
		}

	}

	public static void initCEF() {
		String arg = "com.hbuilder.swt.cef3.error";
		synchronized ("com.hbuilder.swt.cef3.error") {
			if (!hasInited) {
				remoteDebuggingPort = findFreePort(9222);
		
				CefApp.getInstance(new String[] {
						"-remote-debugging-port=" + remoteDebuggingPort,
						"-log-severity=disable", "-lang=zh-CH", "-proxy-server" });
				hasInited = true;
			}

		}
	}

	private void init() {
//		initCEF();
		OSHelper.initJNIPath();
		this.browserClient = CefApp.getInstance().createClient();
		hasCreateBrowser = true;
		this.browserClient.addLifeSpanHandler(new LifeSpanHandler(this));
		this.browserClient.addDisplayHandler(new DisplayHandler(this));
		this.browserClient.addLoadHandler(new LoadHandler(this));
		this.browserClient.addFocusHandler(new FocusHandler(this));
		this.browserClient.addContextMenuHandler(new MenuHandler(this));
		this.browserClient.addJSDialogHandler(new JSDialogHandler(this));
		this.browserClient.addKeyboardHandler(new KeyboardHandler(this));
		this.browserClient.addRequestHandler(new RequestHandler(this));
		this.browserFunctionRouterHandler = new BrowserFunctionRouterHandler();
		CefMessageRouter msgRouter = CefMessageRouter
				.create(this.browserFunctionRouterHandler);
		this.browserClient.addMessageRouter(msgRouter);
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.type) {
				case 11:
//					try {
//						if (RemoteDebugClient.getInstence().isOpen()
//								&& (RemoteDebugClient.getInstence().getDom()
//										.isHighlightEnabled() || RemoteDebugClient
//										.getInstence().getDom()
//										.isInspectModeEnabled())) {
//							RemoteDebugClient.getInstence().getDom()
//									.hideHighlight((MessageListener) null);
//						}
//					} catch (Throwable arg2) {
//						arg2.printStackTrace();
//					}

					CEF3Browser.this.onResize(CEF3Browser.this.cefBrowser);
					break;
				case 12:
					CEF3Browser.this.onDispose();
				}

			}
		};
		this.addListener(12, listener);
		this.addListener(1, listener);
		this.addListener(11, listener);
		this.initHighlightConfig();
	}

	private void initHighlightConfig() {
		BrowserFunction arg9999 = new BrowserFunction(this, "hideHighlight") {
			public void function(Object[] arguments,
					BrowserFunctionCallback callback) throws Exception {
//				if (RemoteDebugClient.getInstence().getDom()
//						.isHighlightEnabled()) {
//					Job job = new Job("click hideHighlight Job") {
//						protected IStatus run(IProgressMonitor monitor) {
//							try {
//								RemoteDebugClient.getInstence().getDom()
//										.hideHighlight((MessageListener) null);
//							} catch (Throwable arg2) {
//								arg2.printStackTrace();
//							}
//
//							return Status.OK_STATUS;
//						}
//					};
//					job.setSystem(true);
//					job.schedule();
//				}

			}
		};
		this.addProgressListener(new ProgressListener() {
			public void completed(ProgressEvent event) {
			}

			public void changed(ProgressEvent event) {
				if (event.current == 0) {
					CEF3Browser.this
							.execute("document.addEventListener(\'mousedown\',function(){window.cefQuery({request: \'hideHighlight\'})}, true)");
				}

			}
		});
	}

	private void onDispose() {
		this.browserFunctionRouterHandler.clearBrowserFunction();
	}

	private void onResize(CefBrowser cefBrowser) {
	}

	void onCreated(CefBrowser cefBrowser) {
	}

	void onClose() {
	}

	void notifyEventListeners(final int eventType, final Event event) {
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (!CEF3Browser.this.isDisposed()) {
					CEF3Browser.this.notifyListeners(eventType, event);
				}
			}
		});
	}

	void runInUI(Runnable runnable) {
		if (!this.isDisposed()) {
			this.getDisplay().asyncExec(runnable);
		}
	}

	void notifyListeners(final TypedEvent event) {
		if (!this.isDisposed()) {
			this.getDisplay().asyncExec(new Runnable() {
				public void run() {
					CEF3Browser.this.notifyListenersInternal(event);
				}
			});
		}
	}

	private void notifyListenersInternal(TypedEvent event) {
		if (!this.isDisposed()) {
			event.display = this.getDisplay();
			int arg3;
			int arg4;
			if (event instanceof OpenWindowEvent) {
				OpenWindowEvent consoleMessageEvent = (OpenWindowEvent) event;
				OpenWindowListener[] arg5;
				arg4 = (arg5 = (OpenWindowListener[]) this.openWindowListeners
						.getListeners()).length;

				for (arg3 = 0; arg3 < arg4; ++arg3) {
					OpenWindowListener listener = arg5[arg3];
					listener.open(consoleMessageEvent);
				}
			} else if (event instanceof CloseWindowEvent) {
				CloseWindowEvent arg6 = (CloseWindowEvent) event;
				CloseWindowListener[] arg20;
				arg4 = (arg20 = (CloseWindowListener[]) this.closeWindowListeners
						.getListeners()).length;

				for (arg3 = 0; arg3 < arg4; ++arg3) {
					CloseWindowListener arg10 = arg20[arg3];
					arg10.close(arg6);
				}
			} else {
				LocationListener arg12;
				LocationListener[] arg19;
				if (event instanceof LocationChangingEvent) {
					LocationChangingEvent arg7 = (LocationChangingEvent) event;
					arg4 = (arg19 = (LocationListener[]) this.locationListeners
							.getListeners()).length;

					for (arg3 = 0; arg3 < arg4; ++arg3) {
						arg12 = arg19[arg3];
						arg12.changing(arg7);
					}
				} else if (event instanceof LocationEvent) {
					LocationEvent arg8 = (LocationEvent) event;
					arg4 = (arg19 = (LocationListener[]) this.locationListeners
							.getListeners()).length;

					for (arg3 = 0; arg3 < arg4; ++arg3) {
						arg12 = arg19[arg3];
						arg12.changed(arg8);
					}
				} else if (event instanceof TitleEvent) {
					TitleEvent arg9 = (TitleEvent) event;
					TitleListener[] arg21;
					arg4 = (arg21 = (TitleListener[]) this.titleListeners
							.getListeners()).length;

					for (arg3 = 0; arg3 < arg4; ++arg3) {
						TitleListener arg14 = arg21[arg3];
						arg14.changed(arg9);
					}
				} else if (event instanceof ProgressEvent) {
					ProgressEvent arg11 = (ProgressEvent) event;
					ProgressListener arg16;
					ProgressListener[] arg22;
					if (arg11.current == arg11.total) {
						arg4 = (arg22 = (ProgressListener[]) this.progressListeners
								.getListeners()).length;

						for (arg3 = 0; arg3 < arg4; ++arg3) {
							arg16 = arg22[arg3];
							arg16.completed(arg11);
						}
					} else {
						arg4 = (arg22 = (ProgressListener[]) this.progressListeners
								.getListeners()).length;

						for (arg3 = 0; arg3 < arg4; ++arg3) {
							arg16 = arg22[arg3];
							arg16.changed(arg11);
						}
					}
				} else if (event instanceof StatusTextEvent) {
					StatusTextEvent arg13 = (StatusTextEvent) event;
					StatusTextListener[] arg24;
					arg4 = (arg24 = (StatusTextListener[]) this.statusTextListeners
							.getListeners()).length;

					for (arg3 = 0; arg3 < arg4; ++arg3) {
						StatusTextListener arg17 = arg24[arg3];
						arg17.changed(arg13);
					}
				} else if (event instanceof ConsoleMessageEvent) {
					ConsoleMessageEvent arg15 = (ConsoleMessageEvent) event;
					ConsoleMessageListener[] arg23;
					arg4 = (arg23 = (ConsoleMessageListener[]) this.consoleMessageListeners
							.getListeners()).length;

					for (arg3 = 0; arg3 < arg4; ++arg3) {
						ConsoleMessageListener arg18 = arg23[arg3];
						arg18.onConsoleMessage(arg15);
					}
				}
			}

		}
	}

	private void loadURL(CefBrowser browser) {
		this.loadText = null;
		this.loadRequest = null;
		this.ensureBrowser();
		if (browser != null) {
			if (browser.isLoading()) {
				browser.stopLoad();
			}

			browser.loadURL(this.loadURL != null ? this.loadURL : "about:blank");
		}

	}

	public boolean isloading() {
		return this.cefBrowser != null ? this.cefBrowser.isLoading() : false;
	}

	private void loadRequest(CefBrowser cefBrowser) {
		this.loadText = null;
		this.loadURL = null;
		this.ensureBrowser();
		if (cefBrowser != null) {
			cefBrowser.stopLoad();
			cefBrowser.loadRequest(this.loadRequest);
			this.loadRequest = null;
		}

	}

	private void loadText(CefBrowser cefBrowser) {
		this.loadURL = null;
		this.loadRequest = null;
		this.ensureBrowser();
		if (cefBrowser != null) {
			cefBrowser.stopLoad();
			cefBrowser.loadString(this.loadText, "html:internal");
			this.loadText = null;
		}

	}

	public void addCloseWindowListener(CloseWindowListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.closeWindowListeners.add(listener);
	}

	public void addLocationListener(LocationListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.locationListeners.add(listener);
	}

	public void addOpenWindowListener(OpenWindowListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.openWindowListeners.add(listener);
	}

	public void addProgressListener(ProgressListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.progressListeners.add(listener);
	}

	public void addStatusTextListener(StatusTextListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.statusTextListeners.add(listener);
	}

	public void addConsoleMessageListener(ConsoleMessageListener listener) {
		this.consoleMessageListeners.add(listener);
	}

	public void addTitleListener(TitleListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.titleListeners.add(listener);
	}

	public boolean back() {
		this.checkWidget();
		if (this.cefBrowser != null && this.cefBrowser.canGoBack()) {
			this.cefBrowser.goBack();
			return true;
		} else {
			return false;
		}
	}

	public boolean execute(String script) {
		if (script == null) {
			SWT.error(4);
		}

		if (this.cefBrowser != null) {
			this.cefBrowser.executeJavaScript(script, "javascript:", 1);
			return true;
		} else {
			return false;
		}
	}

	public void dispose() {
		super.dispose();
		if (this.browserClient != null) {
			this.browserClient.dispose();
		}

	}

	public boolean forward() {
		this.checkWidget();
		if (this.cefBrowser != null && this.cefBrowser.canGoForward()) {
			this.cefBrowser.goForward();
			return true;
		} else {
			return false;
		}
	}

	public String getText() {
		this.checkWidget();
		if (this.cefBrowser != null) {
			CefStringVisitor visitor = new CefStringVisitor() {
				private StringBuilder builder = new StringBuilder();

				public void visit(String string) {
					this.builder.append(string);
				}

				public String toString() {
					return this.builder.toString();
				}
			};
			this.cefBrowser.getSource(visitor);
			return visitor.toString();
		} else {
			return "";
		}
	}

	public String getUrl() {
		if (this.cefBrowser != null) {
			String url = this.cefBrowser.getURL();
			return url != null && !url.isEmpty()
					&& !"data:text/html,chromewebdata".equals(url) ? url
					: this.loadURL;
		} else {
			return "";
		}
	}

	public boolean isBackEnabled() {
		this.checkWidget();
		return this.cefBrowser != null && this.cefBrowser.canGoBack();
	}

	public boolean isForwardEnabled() {
		this.checkWidget();
		return this.cefBrowser != null && this.cefBrowser.canGoForward();
	}

	public void refresh() {
		this.checkWidget();
		if (this.cefBrowser != null) {
			if ("html:internal".equals(this.cefBrowser.getURL())) {
				this.cefBrowser.loadString(this.getText(), "html:internal");
				return;
			}

			this.cefBrowser.reloadIgnoreCache();
		}

	}

	public void removeCloseWindowListener(CloseWindowListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.closeWindowListeners.remove(listener);
	}

	public void removeLocationListener(LocationListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.locationListeners.remove(listener);
	}

	public void removeOpenWindowListener(OpenWindowListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.openWindowListeners.remove(listener);
	}

	public void removeProgressListener(ProgressListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.progressListeners.remove(listener);
	}

	public void removeStatusTextListener(StatusTextListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.statusTextListeners.remove(listener);
	}

	public void removeTitleListener(TitleListener listener) {
		this.checkWidget();
		if (listener == null) {
			SWT.error(4);
		}

		this.titleListeners.remove(listener);
	}

	public void setJavascriptEnabled(boolean enabled) {
		this.checkWidget();
	}

	public boolean setText(String html) {
		this.checkWidget();
		return this.setText(html, true);
	}

	public boolean setText(String html, boolean trusted) {
		this.checkWidget();
		if (html == null) {
			SWT.error(4);
		}

		this.loadText = html;
		this.loadText(this.cefBrowser);
		return true;
	}

	public boolean setUrl(String url) {
		this.loadURL = url;
		this.loadURL(this.cefBrowser);
		return true;
	}

	public boolean setUrl(String url, String postData,
			HashMap<String, String> headerMap) {
		this.checkWidget();
		if (url == null) {
			SWT.error(4);
		}

		CefRequest request = CefRequest.create();
		request.setURL(url);
		if (postData != null) {
			CefPostData pdata = CefPostData.create();
			CefPostDataElement element = CefPostDataElement.create();
			if (postData.length() > 0) {
				try {
					byte[] bytes = postData.getBytes("UTF-8");
					element.setToBytes(bytes.length, bytes);
				} catch (UnsupportedEncodingException arg7) {
					;
				}
			} else {
				element.setToEmpty();
			}

			pdata.addElement(element);
			request.setPostData(pdata);
		}

		if (headerMap != null) {
			request.setHeaderMap(headerMap);
		}

		this.loadRequest = request;
		this.loadRequest(this.cefBrowser);
		return true;
	}

	public void stop() {
		this.checkWidget();
		if (this.cefBrowser != null) {
			this.cefBrowser.stopLoad();
		}

	}

	public boolean isDisposed() {
		return super.isDisposed();
	}

	public CefBrowser getCefBroser() {
		return this.cefBrowser;
	}

	public void setMenu(Menu menu) {
		this.enableDefaultMenu = menu == null;
		super.setMenu(menu);
	}

	public boolean enableDefaultMenu() {
		return this.enableDefaultMenu;
	}

	public void setEnableDefaultMenu(boolean enableDefaultMenu) {
		this.enableDefaultMenu = enableDefaultMenu;
	}

	public boolean enableDevTools() {
		return this.enableDevTools;
	}

	public void setEnableDevTools(boolean enable) {
		this.enableDevTools = enable;
	}

	public static void closeAll() {
		if (hasCreateBrowser) {
			CefApp.getInstance().dispose();
		}

		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					Thread.currentThread().interrupt();
				}
			});
		} catch (Throwable arg0) {
			arg0.printStackTrace();
		}

	}

	public void openDevTools() {
		if (this.devToolsDialog != null && !this.devToolsDialog.isDisposed()) {
			this.getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (!CEF3Browser.this.devToolsDialog.isVisible()) {
						CEF3Browser.this.devToolsDialog.setVisible(true);
					} else if (CEF3Browser.this.devToolsDialog.isMinimized()) {
						CEF3Browser.this.devToolsDialog.setMinimized(false);
					} else {
						CEF3Browser.this.devToolsDialog.setFocus();
					}

				}
			});
		} else {
			this.getDisplay().asyncExec(new Runnable() {
				public void run() {
					CEF3Browser.this.devToolsDialog = new DevToolsDialog(
							CEF3Browser.this);
					CEF3Browser.this.devToolsDialog.createContent();
					Iterator arg1 = CEF3Browser.this.devToolShellListeners
							.iterator();

					while (arg1.hasNext()) {
						ShellListener listener = (ShellListener) arg1.next();
						if (listener != null) {
							CEF3Browser.this.devToolsDialog.getShell()
									.addShellListener(listener);
						}
					}

					CEF3Browser.this.devToolsDialog.open();
				}
			});
		}
	}

	public void addDevToolsDialoglistener(ShellListener listener) {
		if (this.devToolsDialog != null && !this.devToolsDialog.isDisposed()) {
			this.devToolsDialog.getShell().addShellListener(listener);
		}

		if (listener != null) {
			this.devToolShellListeners.add(listener);
		}

	}

	public void removeDevToolsDialoglistener(ShellListener listener) {
		if (listener != null) {
			this.devToolShellListeners.remove(listener);
		}

	}

	public boolean isDevToolsDialogOpened() {
		return this.devToolsDialog != null && !this.devToolsDialog.isDisposed();
	}

	public static int findFreePort(int defaultPort) {
		ServerSocket socket = null;

		try {
			int arg2;
			if (defaultPort > 0) {
				try {
					socket = new ServerSocket(defaultPort);
					socket.setReuseAddress(true);
					arg2 = defaultPort;
					return arg2;
				} catch (IOException arg13) {
					if (socket != null) {
						try {
							socket.close();
						} catch (IOException arg12) {
							;
						}
					}
				}
			}

			socket = new ServerSocket(0, 0);
			socket.setReuseAddress(true);
			arg2 = socket.getLocalPort();
			return arg2;
		} catch (Throwable arg14) {
			return -1;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException arg11) {
					;
				}
			}

		}
	}

	public String getLoadingURL() {
		return this.loadingURL;
	}

	public void setLoadingURL(String loadingURL) {
		this.loadingURL = loadingURL;
	}
}
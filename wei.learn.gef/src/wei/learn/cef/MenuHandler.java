package wei.learn.cef;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefContextMenuParams;
import org.cef.callback.CefMenuModel;
import org.cef.handler.CefContextMenuHandlerAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;


final class MenuHandler extends CefContextMenuHandlerAdapter
{
  private static final String objectGroup = "dcloud.getAttribute";
  private final CEF3Browser browser;

  public MenuHandler(CEF3Browser browser)
  {
    this.browser = browser;
  }

  public void onBeforeContextMenu(CefBrowser cefBrowser, CefContextMenuParams params, CefMenuModel model)
  {
    if (!(this.browser.enableDefaultMenu())) {
      model.clear();
    } else {
      Map commandIds = new LinkedHashMap();
      Map commandEnables = new HashMap();
      int commandId;
      for (int i = 0; i < model.getCount(); ++i) {
        commandId = model.getCommandIdAt(i);
        switch (commandId)
        {
        case 111:
        case 112:
        case 113:
        case 114:
        case 115:
        case 116:
          commandIds.put(Integer.valueOf(commandId), model.getLabel(commandId));
          commandEnables.put(Integer.valueOf(commandId), Boolean.valueOf(model.isEnabled(commandId)));
        }
      }

      model.clear();
      if (!(commandIds.isEmpty())) {
    	  Iterator entries = commandIds.keySet().iterator();       
		while (entries.hasNext()) {
			commandId = ((Integer)entries.next()).intValue();
			model.addItem(commandId, (String) commandIds.get(Integer.valueOf(commandId)));
			model.setEnabled(commandId, ((Boolean) commandEnables.get(Integer.valueOf(commandId)))
					.booleanValue());
		}
        
        
        model.addSeparator();
      }
      model.addItem(100, "返回(&B)");
      model.addItem(101, "前进(&F)");
      model.addItem(102, "刷新(&R)");
      model.setEnabled(100, cefBrowser.canGoBack());
      model.setEnabled(101, cefBrowser.canGoForward());
      if ((Platform.inDevelopmentMode()) || (this.browser.enableDevTools())) {
        model.addSeparator();
        model.addItem(26500, "查找代码中对应dom节点(&J)");
        model.setEnabled(26500, (this.browser.devToolsDialog == null) || (this.browser.devToolsDialog.isDisposed()));
        model.addItem(28500, "打开Chrome控制台(&N)");
      }
    }
	final Event event = new Event();
	 final int x = params.getXCoord();
	final int y = params.getYCoord();
	event.x = x;
	event.y = y;
	this.browser.notifyEventListeners(35, event);
	if (event.doit && !this.browser.enableDefaultMenu()
			&& !this.browser.isDisposed()) {
		this.browser.runInUI(new Runnable() {
			public void run() {
				Menu menu = MenuHandler.this.browser.getMenu();
				if (menu != null && !menu.isDisposed()) {
					if (x != event.x || y != event.y) {
						menu.setLocation(event.x, event.y);
					}

					menu.setVisible(true);
				}

			}
		});
	}
  }

  public boolean onContextMenuCommand(CefBrowser cefBrowser, CefContextMenuParams params, int commandId, int eventFlags)
  {
	  switch (commandId) {
		case 26500:
//			final int x = params.getXCoord();
//			final int y = params.getYCoord();
//			Job job = new Job("right mouse inspect Node job") {
//				protected IStatus run(IProgressMonitor monitor) {
//					if (!RemoteDebugClient.getInstence().isOpen()) {
//						int port = MenuHandler.this.browser
//								.getRemoteDebuggingPort();
//
//						try {
//							if (port < 0
//									|| !RemoteDebugClient.getInstence()
//											.connect(port)) {
//								return Status.CANCEL_STATUS;
//							}
//						} catch (Throwable arg3) {
//							arg3.printStackTrace();
//							return Status.CANCEL_STATUS;
//						}
//					}
//
//					RemoteDebugClient
//							.getInstence()
//							.getRunTime()
//							.evaluate(
//									"document.elementFromPoint(" + x + ", " + y
//											+ ");", "dcloud.getAttribute",
//									new MessageListener() {
//										public void receive(JSONObject response) {
//											if (response.has("result")) {
//												try {
//													response = response
//															.getJSONObject("result");
//													if (!response
//															.has("objectId")
//															|| !response
//																	.has("className")) {
//														return;
//													}
//
//													final String e = response
//															.getString("objectId");
//													final String className = response
//															.getString("className");
//													RemoteDebugClient
//															.getInstence()
//															.getDom()
//															.highlightFromObjectId(
//																	e,
//																	new MessageListener() {
//																		public void receive(
//																				JSONObject response) {
//																			InspectNodeStructureListenerManager
//																					.getInstence()
//																					.notify(e,
//																							className,
//																							"dcloud.getAttribute");
//																		}
//																	});
//												} catch (JSONException arg3) {
//													arg3.printStackTrace();
//												}
//											}
//
//											System.out.println(response);
//										}
//									});
//					return Status.OK_STATUS;
//				}
//			};
//			job.setSystem(true);
//			job.schedule();
//			return true;
		case 28500:
			this.browser.openDevTools();
			return true;
		default:
			return false;
		}
	}
}
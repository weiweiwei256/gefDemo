package wei.learn.gef.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import wei.learn.gef.Application;
import wei.learn.gef.editor.JCEFEditor;
import wei.learn.gef.ui.JCEFEditorInput;

public class JCEFAction extends Action implements IWorkbenchAction,
		ISelectionListener {
	private IWorkbenchWindow window = null;
	public final static String ID = "wei.learn.gef.JCEFAction";

	public JCEFAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&JCEF");
		setToolTipText("Open cef editor.");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Application.PLUGIN_ID, "/icons/alt_window16.gif"));
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

	}

	@Override
	public void dispose() {
	}

	@Override
	public void run() {
			IWorkbenchPage page = window.getActivePage();
			try {
				page.openEditor(new JCEFEditorInput(), JCEFEditor.ID, true);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

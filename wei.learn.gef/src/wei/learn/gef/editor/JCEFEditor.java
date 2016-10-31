package wei.learn.gef.editor;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import wei.learn.cef.CEF3Browser;

public class JCEFEditor extends EditorPart {
	// Editor ID
	public static final String ID = "wei.learn.gef.JCEFEditor";

	public JCEFEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		try {
			String url = FileLocator.toFileURL(
					this.getClass().getResource("/welcome/html/ABIDE.html")).toString();
			parent.setLayout(new FillLayout());
			CEF3Browser browser = new CEF3Browser(parent, SWT.NONE);
			browser.setUrl(url);
			browser.forceFocus();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

package wei.learn.gef.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.ui.parts.GraphicalEditor;

public class DiagramEditor extends GraphicalEditor {

	// Editor ID
	public static final String ID = "wei.learn.gef.DiagramEditor";
	//an EditDomain is a "session" of editing which contains things like the CommandStack
	public DiagramEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected void initializeGraphicalViewer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

}

package wei.learn.gef.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;

import wei.learn.gef.editpart.PartFactory;
import wei.learn.gef.model.ContentsModel;
import wei.learn.gef.model.HelloModel;

public class DiagramEditor extends GraphicalEditor {

	// Editor ID
	public static final String ID = "wei.learn.gef.DiagramEditor";
	// an EditDomain is a "session" of editing which contains things like the
	// CommandStack
	GraphicalViewer viewer;

	public DiagramEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new PartFactory());
	}

	@Override
	protected void initializeGraphicalViewer() {
		//set the contents of this editor
		ContentsModel contents = new ContentsModel();
		HelloModel child1 = new HelloModel();
        child1.setConstraint(new Rectangle(0, 0, -1, -1));
        contents.addChild(child1);

        HelloModel child2 = new HelloModel();
        child2.setConstraint(new Rectangle(30, 30, -1, -1));
        contents.addChild(child2);
        HelloModel child3 = new HelloModel();
        child3.setConstraint(new Rectangle(10, 80, 80, 50));
        contents.addChild(child3);
		viewer.setContents(contents);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

}

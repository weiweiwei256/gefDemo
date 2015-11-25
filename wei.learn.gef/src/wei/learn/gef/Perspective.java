package wei.learn.gef;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		final String editorArea = layout.getEditorArea();
		IFolderLayout bottomFolder = layout.createFolder("bottom",
				IPageLayout.BOTTOM, 0.75f, editorArea);
		bottomFolder.addView(IPageLayout.ID_PROP_SHEET);
		layout.setEditorAreaVisible(true);
	}
}

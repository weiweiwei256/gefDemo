package wei.learn.gef.handle;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.swt.graphics.Image;

import wei.learn.gef.Activator;
import wei.learn.gef.util.ImagePaths;

public class HelloHandle extends AbstractHandle {

	private ImageFigure imageFigure;

	public HelloHandle(GraphicalEditPart editpart, int i) {
		setOwner(editpart);
		setLayoutManager(new StackLayout());

		init();
		add(imageFigure);
		setSize(imageFigure.getSize());

	}

	private void init() {
		String imagePaths = ImagePaths.ICON_INDEX_HANDLE;
		imagePaths=imagePaths.replaceFirst("index", "1");
		Image image = Activator.getImageDescriptor(imagePaths).createImage();
		 imageFigure = new ImageFigure(image);
		imageFigure.setSize(image.getBounds().width, image.getBounds().height);
		add(imageFigure);
	}

	@Override
	public void setLocator(Locator locator) {
		super.setLocator(locator);
	}

	@Override
	protected DragTracker createDragTracker() {
		return null;
	}

}

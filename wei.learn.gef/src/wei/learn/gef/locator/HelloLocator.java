package wei.learn.gef.locator;

import org.eclipse.draw2d.AbstractLocator;
import org.eclipse.draw2d.geometry.Point;

public class HelloLocator extends AbstractLocator{

	@Override
	protected Point getReferencePoint() {
		return new Point(100,0);
	}
}

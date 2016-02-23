package wei.learn.gef.handle;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.handles.ConnectionHandle;
import org.eclipse.gef.tools.ConnectionEndpointTracker;

public class HomunculeConnectionStartHandle extends ConnectionHandle {

	public HomunculeConnectionStartHandle(ConnectionEditPart host) {
		setOwner(host);
		setLocator(new ConnectionLocator(getConnection(),
				ConnectionLocator.SOURCE));
	}

	/**
	 * Creates and returns a new {@link ConnectionEndpointTracker}.
	 * 
	 * @return the new ConnectionEndpointTracker
	 */
	protected DragTracker createDragTracker() {
		if (isFixed())
			return null;
		ConnectionEndpointTracker tracker;
		tracker = new ConnectionEndpointTracker((ConnectionEditPart) getOwner());
		tracker.setCommandName(RequestConstants.REQ_RECONNECT_SOURCE);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}

	@Override
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds();
		r.shrink(1, 1);
		try {
			g.setBackgroundColor(ColorConstants.green);
			g.fillOval(r);
			g.setForegroundColor(ColorConstants.darkBlue);
			g.drawOval(r);
		} finally {
			// We don't really own rect 'r', so fix it.
			r.expand(1, 1);
		}
	}
}
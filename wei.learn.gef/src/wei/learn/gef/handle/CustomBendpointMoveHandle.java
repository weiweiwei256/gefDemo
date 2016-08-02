package wei.learn.gef.handle;

import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.handles.BendpointHandle;
import org.eclipse.gef.tools.ConnectionBendpointTracker;

import wei.learn.gef.helper.Utility;

/**
 * ÒÆ¶¯ÏßµÄHandle
 * 
 * @author Administrator
 *
 */
public class CustomBendpointMoveHandle extends BendpointHandle {
	public CustomBendpointMoveHandle(ConnectionEditPart owner, int index) {
		this(owner, index, index + 1);
	}

	public CustomBendpointMoveHandle(ConnectionEditPart owner, int index,
			int locatorIndex) {
		setOwner(owner);
		setIndex(index);
		setLocator(new MidpointLocator(getConnection(), locatorIndex));
		if (index % 2 == 0) {
			setCursor(Utility.BENDPOINT_VERTICAL_MOVE_CURSOR);
		} else {
			setCursor(Utility.BENDPOINT_HORIZONTAL_MOVE_CURSOR);
		}
		setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE, DEFAULT_HANDLE_SIZE));
	}

	protected DragTracker createDragTracker() {
		ConnectionBendpointTracker tracker;
		tracker = new ConnectionBendpointTracker(
				(ConnectionEditPart) getOwner(), getIndex());
		tracker.setType(RequestConstants.REQ_MOVE_BENDPOINT);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}
}

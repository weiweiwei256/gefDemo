package wei.learn.gef.handle;

import org.eclipse.draw2d.BendpointLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.handles.BendpointHandle;
import org.eclipse.gef.tools.ConnectionBendpointTracker;

import wei.learn.gef.helper.Utility;

/**
 * ¹Õ½ÇÒÆ¶¯handle
 * 
 * @author Administrator
 *
 */
public class CustomBendpointCreationHandle extends BendpointHandle {
	public static final String NORTHEAST = "northeast";
	public static final String NORTHWEST = "northwest";

	public CustomBendpointCreationHandle(ConnectionEditPart owner, int index) {
		this(owner, index, index);
	}

	public CustomBendpointCreationHandle(ConnectionEditPart owner, int index,
			int locatorIndex) {
		setOwner(owner);
		setIndex(index);
		setLocator(new BendpointLocator(getConnection(), locatorIndex));
		setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE+2, DEFAULT_HANDLE_SIZE+2));
	}

	public void setCursorDirection(String direction) {
		if (direction.equals(NORTHEAST)) {
			setCursor(Utility.BENDPOINT_NORTHEAST_MOVE_CURSOR);
		} else if (direction.equals(NORTHWEST)) {
			setCursor(Utility.BENDPOINT_NORTHWEST_MOVE_CURSOR);
		}else{
			setCursor(Utility.BENDPOINT_ALL_MOVE_CURSOR);
		}
	}

	protected DragTracker createDragTracker() {
		ConnectionBendpointTracker tracker;
		tracker = new ConnectionBendpointTracker(
				(ConnectionEditPart) getOwner(), getIndex());
		tracker.setType(RequestConstants.REQ_CREATE_BENDPOINT);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}
}

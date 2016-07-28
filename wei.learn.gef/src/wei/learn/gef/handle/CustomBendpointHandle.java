package wei.learn.gef.handle;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.handles.BendpointCreationHandle;

import wei.learn.gef.helper.Utility;

public class CustomBendpointHandle extends BendpointCreationHandle {

	public CustomBendpointHandle(ConnectionEditPart owner, int index,
			int locatorIndex) {
		super(owner, index, locatorIndex);
		if (index % 2 == 0) {
			setCursor(Utility.BENDPOINT_VERTICAL_MOVE_CURSOR);
		} else {
			setCursor(Utility.BENDPOINT_HORIZONTAL_MOVE_CURSOR);
		}
		setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE, DEFAULT_HANDLE_SIZE));
	}
}

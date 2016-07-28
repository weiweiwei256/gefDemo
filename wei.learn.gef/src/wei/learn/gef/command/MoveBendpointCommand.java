package wei.learn.gef.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import wei.learn.gef.model.AbstractConnectionModel;

public class MoveBendpointCommand extends Command {
	private AbstractConnectionModel conn;
	private Point[] newLocation, oldLocation;
	private int[] index;
	private int size;

	@Override
	public void execute() {
		oldLocation = new Point[size];
		for (int i = 0; i < size; i++) {
			oldLocation[i] = conn.getBendpoints().get(index[i]);
			conn.replaceBendpoint(index[i], newLocation[i]);
		}
	}

	public void setConnection(Object object) {
		conn = (AbstractConnectionModel) object;
	}

	public void setIndex(int[] i) {
		size = i.length;
		index = i;
	}

	public void setNewLocation(Point[] loc) {
		newLocation = loc;
	}

	@Override
	public void undo() {
		for (int i = 0; i < size; i++) {
		conn.replaceBendpoint(index[i], oldLocation[i]);
		}
	}
}

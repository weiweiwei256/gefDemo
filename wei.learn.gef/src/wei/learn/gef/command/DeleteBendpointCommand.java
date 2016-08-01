package wei.learn.gef.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import wei.learn.gef.model.AbstractConnectionModel;

public class DeleteBendpointCommand extends Command {
	private AbstractConnectionModel conn;
	private Point[] oldLocation;
	private int[] index;
	private int size;

	@Override
	public void execute() {
		oldLocation = new Point[size];
		for (int i = 0; i < size; i++) {
			oldLocation[i] = conn.getBendpoints().get(index[i]-i);
			conn.removeBendpoint(index[i]-i);
		}
	}

	public void setConnection(Object object) {
		conn = (AbstractConnectionModel) object;
	}

	public void setIndex(int[] deleteIndexs) {
		size = deleteIndexs.length;
		index = deleteIndexs;
	}

	@Override
	public void undo() {
		for (int i = 0; i < size; i++) {
		conn.addBendpoint(index[i], oldLocation[i]);
		}
	}
}

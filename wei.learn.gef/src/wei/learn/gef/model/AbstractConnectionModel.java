package wei.learn.gef.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

public class AbstractConnectionModel extends AbstractModel {
	private HelloModel source, target;
	// bending点的位置
	private PointList bendpoints = new PointList();
	// bending点位置改变的ID
	public static final String P_BEND_POINT = "_bend_point";

	private boolean isManual = false;

	public void attachSource() {
		if (!source.getModelSourceConnection().contains(this)) {
			source.addSourceConnection(this);
		}
	}

	public void attachTarget() {
		if (!target.getModelTargetConnection().contains(this)) {
			target.addTargetConnection(this);
		}
	}

	public void detachSource() {
		source.removeSourceConnection(this);
	}

	public void detachTarget() {
		target.removeTargetConnection(this);
	}

	public HelloModel getSource() {
		return source;
	}

	public void setSource(HelloModel model) {
		source = model;
	}

	public HelloModel getTarget() {
		return target;
	}

	public void setTarget(HelloModel model) {
		target = model;
	}

	public void addBendpoint(int index, Point point) {
		bendpoints.setPoint(point, index);
		firePropertyChange(P_BEND_POINT, null, null);
	}

	public void removeBendpoint(int index) {
		bendpoints.removePoint(index);
		firePropertyChange(P_BEND_POINT, null, null);
	}

	public void replaceBendpoint(int index, Point point) {
		bendpoints.setPoint(point, index);
		firePropertyChange(P_BEND_POINT, null, null);
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

	public void setBendpoints(PointList bendpoints) {
		this.bendpoints = bendpoints;
		firePropertyChange(P_BEND_POINT, null, null);
	}

	public PointList getBendpoints() {
		return bendpoints;
	}
}

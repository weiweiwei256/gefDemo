package wei.learn.gef.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import wei.learn.gef.command.DeleteBendpointCommand;
import wei.learn.gef.command.MoveBendpointCommand;
import wei.learn.gef.handle.CustomBendpointHandle;
import wei.learn.gef.model.AbstractConnectionModel;

// ide: BendpointPolicy
public class CustomBendpointEditPolicy extends BendpointEditPolicy {

	private static final int OFFSET = 20;

	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		MoveBendpointCommand moveCom = new MoveBendpointCommand();
		Point mousePoint = request.getLocation();
		int reqIndex = request.getIndex();
		getConnection().translateToRelative(mousePoint);
		List<Point> points = ((AbstractConnectionModel) getHost().getModel())
				.getBendpoints(); // 原有布线结果
		List<Point> constraint = (List<Point>) getConnection()
				.getRoutingConstraint();
		// 准备数据
		Point[] relatePoints = new Point[4];
		Point fBorderPoint = (reqIndex == 0 ? getConnection().getPoints()
				.getFirstPoint() : constraint.get(reqIndex - 1)); // forward_Border_Point
		Point fPoint = constraint.get(reqIndex);
		Point nPoint = constraint.get(reqIndex + 1);
		Point nBorderPoint = reqIndex == constraint.size() - 1 ? getConnection()
				.getPoints().getLastPoint() : constraint.get(reqIndex + 2);
		relatePoints[0] = fBorderPoint;
		relatePoints[1] = fPoint;
		relatePoints[2] = nPoint;
		relatePoints[3] = nBorderPoint;
		Map<Integer, Point> changePoints = changeRelatePoints(mousePoint,
				relatePoints, reqIndex);
		// 修改线
		if (!changePoints.isEmpty()) {
			Point[] newLocs = new Point[changePoints.size()];
			int[] indexs = new int[changePoints.size()];
			int j = 0;
			for (Entry<Integer, Point> entry : changePoints.entrySet()) {
				indexs[j] = entry.getKey();
				newLocs[j] = entry.getValue();
				j++;
			}
			moveCom.setConnection(getHost().getModel());
			moveCom.setNewLocation(newLocs);
			moveCom.setIndex(indexs);
		}

		return moveCom;
	}

	/**
	 * @param mousePoint
	 *            请求点
	 * @param relatePoints
	 *            四个点依次是 移动点的前前点,前点,后点,后后点
	 * @param fPointIndex
	 *            前点 位于bendpoint的index,
	 * @return
	 */
	private Map<Integer, Point> changeRelatePoints(Point mousePoint,
			Point[] relatePoints, int fPointIndex) {
		Map<Integer, Point> changePoints = new HashMap<Integer, Point>();
		// 将要保存的值
		Point nForwardPoint = new Point();
		Point nNextPoint = new Point();
		if (relatePoints[1].x == relatePoints[2].x) // 检测是否是左右拖动
		{
			// y不变 设置x
			nForwardPoint.y = relatePoints[1].y;
			nNextPoint.y = relatePoints[2].y;
			nForwardPoint.x = mousePoint.x;
			nNextPoint.y = mousePoint.y;

		}

		if (relatePoints[1].y == relatePoints[2].y)// 检测是否是上下拖动
		{
			// x不变 设置y
			nForwardPoint.x = relatePoints[1].x;
			nForwardPoint.y = mousePoint.y;
			nNextPoint.y = mousePoint.y;
		}
		changePoints.put(fPointIndex, nForwardPoint);
		changePoints.put(fPointIndex + 1, nNextPoint);
		return changePoints;
	}

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		DeleteBendpointCommand command = new DeleteBendpointCommand();
		command.setConnection(getHost().getModel());
		command.setIndex(request.getIndex());
		return command;
	}

	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		return null;
	}

	@Override
	protected List createSelectionHandles() {
		List list = new ArrayList();
		ConnectionEditPart connEP = (ConnectionEditPart) getHost();
		PointList points = getConnection().getPoints();
		int bendPointIndex = 0;

		for (int i = 1; i < points.size() - 2; i++) {
			// Put a create handle on the middle of every segment
			list.add(new CustomBendpointHandle(connEP, bendPointIndex, i));
			bendPointIndex++;
		}

		return list;
	}

	@Override
	protected void showCreateBendpointFeedback(BendpointRequest request) {
		Point p = new Point(request.getLocation());
		int reqIndex = request.getIndex();
		List<Point> constraint;
		getConnection().translateToRelative(p);
		saveOriginalConstraint();
		constraint = (List<Point>) getConnection().getRoutingConstraint();
		// 准备数据
		Point[] relatePoints = new Point[4];
		Point fBorderPoint = (reqIndex == 0 ? getConnection().getPoints()
				.getFirstPoint() : constraint.get(reqIndex - 1)); // forward_Border_Point
		Point fPoint = constraint.get(reqIndex);
		Point nPoint = constraint.get(reqIndex + 1);
		Point nBorderPoint = reqIndex == constraint.size() - 2? getConnection()
				.getPoints().getLastPoint() : constraint.get(reqIndex + 2);
		relatePoints[0] = fBorderPoint;
		relatePoints[1] = fPoint;
		relatePoints[2] = nPoint;
		relatePoints[3] = nBorderPoint;

		Map<Integer, Point> changePoints = changeRelatePoints(p, relatePoints,
				reqIndex);
		for (Entry<Integer, Point> entry : changePoints.entrySet()) {
			constraint.set(entry.getKey(),
					new AbsoluteBendpoint(entry.getValue()));
		}
		getConnection().setRoutingConstraint(constraint);
	}
}

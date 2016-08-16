package wei.learn.gef.router;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import wei.learn.gef.editpart.CustomAbstractConnectionEditPart;
import wei.learn.gef.helper.Utility;
import wei.learn.gef.model.LineConnectionModel;
import wei.learn.gef.ui.DiagramEditor;

public class MyBendpointConnectionRouter extends AbstractRouter {
	private Map<Connection, Object> constraints = new HashMap<Connection, Object>(
			11);
	/**
	 * Gets the constraint for the given {@link Connection}.
	 * 
	 * @param connection
	 *            The connection whose constraint we are retrieving
	 * @return The constraint
	 */
	public Object getConstraint(Connection connection) {
		return constraints.get(connection);
	}

	/**
	 * Removes the given connection from the map of constraints.
	 * 
	 * @param connection
	 *            The connection to remove
	 */
	public void remove(Connection connection) {
		constraints.remove(connection);
	}

	/**
	 * Routes the {@link Connection}. Expects the constraint to be a List of
	 * {@link org.eclipse.draw2d.Bendpoint Bendpoints}.
	 * 
	 * @param conn
	 *            The connection to route
	 */
	@SuppressWarnings("unchecked")
	public void route(Connection conn) {
		PointList points = conn.getPoints();
		Point oldStart = null;
		Point oldEnd = null;
		for (int i = 0; i < points.size(); i++) {
			System.err.println("point " + i + ":" + points.getPoint(i));

		}
		if (!(points.getFirstPoint().equals(new Point(0, 0)) && points
				.getLastPoint().equals(new Point(100, 100)))) {
			oldStart = points.getFirstPoint();
			oldEnd = points.getLastPoint();
		}
		points.removeAllPoints();

		List<Point> bendpoints = (List<Point>) getConstraint(conn);
		if (bendpoints == null)
			bendpoints = Collections.EMPTY_LIST;

		Point ref1, ref2;

		if (bendpoints.isEmpty()) {
			ref1 = conn.getTargetAnchor().getReferencePoint();
			ref2 = conn.getSourceAnchor().getReferencePoint();
		} else {
			ref1 = new Point((Point) (bendpoints.get(0)));
			conn.translateToAbsolute(ref1);
			ref2 = new Point((Point) (bendpoints.get(bendpoints.size() - 1)));
			conn.translateToAbsolute(ref2);
		}
		// 准备新终点
		Point newStart = new Point();
		newStart.setLocation(conn.getSourceAnchor().getLocation(ref1));
		conn.translateToRelative(newStart);
		Point newEnd = new Point();
		newEnd.setLocation(conn.getTargetAnchor().getLocation(ref2));
		conn.translateToRelative(newEnd);

		// 处理逻辑
		points.addPoint(newStart);
		if (oldStart != null && !oldStart.equals(newStart)) // 出现改变起始点的情况 需要特殊处理
		{
			if (bendpoints.isEmpty()) // 当前连线只有两个点 竖直一条直线
			{
				// 自己在平均高度创建拐点
				int averageY = (newStart.y + newEnd.y) / 2;
				points.addPoint(newStart.x, averageY);
				points.addPoint(newEnd.x, averageY);
			} else { // 存在拐点的情况
				// 修改拐点位置
				Point nextPoint = (Point) bendpoints.get(0);
				bendpoints.set(0, new AbsoluteBendpoint(new Point(newStart.x,
						nextPoint.y)));
			}
			synchronizedLineModelData(conn, bendpoints);
		}

		for (int i = 0; i < bendpoints.size(); i++) {
			Bendpoint bp = (Bendpoint) bendpoints.get(i);
			points.addPoint(bp.getLocation());
		}
		// 为了处理 竖直线拖动 起始图源的情况 很恶心 但是没啥其他的办法
		if (oldStart != null && !oldStart.equals(newStart)
				&& bendpoints.isEmpty()) {
			int averageY = (newStart.y + newEnd.y) / 2;
			bendpoints.add(new AbsoluteBendpoint(newStart.x, averageY));
			bendpoints.add(new AbsoluteBendpoint(newEnd.x, averageY));
			synchronizedLineModelData(conn, bendpoints);
		}
		if (oldEnd != null && !oldEnd.equals(newEnd)) // 出现改变终点的情况 需要特殊处理
		{
			if (bendpoints.isEmpty()) // 当前连线只有两个点 竖直一条直线
			{
				// 自己在平均高度创建拐点
				int averageY = (newStart.y + newEnd.y) / 2;
				points.addPoint(newStart.x, averageY);
				points.addPoint(newEnd.x, averageY);
				bendpoints.add(new AbsoluteBendpoint(newStart.x, averageY));
				bendpoints.add(new AbsoluteBendpoint(newEnd.x, averageY));
			} else { // 存在拐点的情况
				// 修改拐点位置
				Point fPoint = (Point) bendpoints.get(bendpoints.size() - 1);
				Point newfPoint = new AbsoluteBendpoint(newEnd.x, fPoint.y);
				bendpoints.set(bendpoints.size() - 1, newfPoint);
				points.setPoint(newfPoint, points.size() - 1);
			}
			synchronizedLineModelData(conn, bendpoints);
		}
		points.addPoint(newEnd);
		conn.setPoints(points);
	}

	/**
	 * 暗渡陈仓 当拖动线两端图源时,通过router的机制计算线中bendpoints应进行的修改.
	 * 这里直接将改变赋值给wireModel中的bendpoint 实现数据和显示的同步 虽然直接修改model数据不提倡,但这个确实很方便快捷
	 * 而且用户也不是很关注wireModel的数据
	 * 
	 * @param conn
	 * @param bendpoints
	 */
	private void synchronizedLineModelData(Connection conn,
			List<Point> bendpoints) {
		DiagramEditor editor = (DiagramEditor) Utility.getCurrentEditor();
		CustomAbstractConnectionEditPart lineEditPart = (CustomAbstractConnectionEditPart) editor
				.getGraphicalViewer().getVisualPartMap().get(conn);
		LineConnectionModel lineModel = (LineConnectionModel) lineEditPart
				.getModel();
		lineModel.setBendpoints(bendpoints);
	}

	/**
	 * Sets the constraint for the given {@link Connection}.
	 * 
	 * @param connection
	 *            The connection whose constraint we are setting
	 * @param constraint
	 *            The constraint
	 */
	public void setConstraint(Connection connection, Object constraint) {
		constraints.put(connection, constraint);
	}

}

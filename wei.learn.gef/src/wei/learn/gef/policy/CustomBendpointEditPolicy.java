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
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.swt.internal.win32.BP_PAINTPARAMS;

import wei.learn.gef.command.DeleteBendpointCommand;
import wei.learn.gef.command.MoveBendpointCommand;
import wei.learn.gef.command.MultiCommand;
import wei.learn.gef.handle.CustomBendpointHandle;
import wei.learn.gef.model.AbstractConnectionModel;

// ide: BendpointPolicy
/**
 * 由于已有的连线模式是水平竖直的 所以要对已有的简单的bendpoint的创建移动的方式重新构建
 * 
 * @author Administrator
 *
 */
public class CustomBendpointEditPolicy extends BendpointEditPolicy {

	private static final int CheckDeleteLimit = 10;

	private boolean deleteBendpoint = false; // 当前是否是删除状态
	private boolean testDelete = false; // 检测到删除状态
	private int[] deleteIndexs = null;
	private int changeIndex;
	private Point orginPoint;
	private Bendpoint changePoint;
	// 这里用于临时存储数据. 方便在 Movefeedback和deletefeedback来回切换
	private Point fPoint;
	private Point nPoint;
	private Point fborderPoint;
	private Point nborderPoint;

	@Override
	public Command getCommand(Request request) {
		if (REQ_MOVE_BENDPOINT.equals(request.getType())) {
			return getMoveBendpointCommand((BendpointRequest) request);
		}
		if (REQ_CREATE_BENDPOINT.equals(request.getType())) {
			if (deleteBendpoint)
				return getDeleteBendpointCommand((BendpointRequest) request);
			return getCreateBendpointCommand((BendpointRequest) request);
		}
		return null;
	}

	/*
	 * 重写CreateBendpointCommand 本来用于创建Bendpoint的命令改为:移动bendpoint两端的bendpoints
	 */
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
		Point fPoint = constraint.get(reqIndex);
		Point nPoint = constraint.get(reqIndex + 1);
		Map<Integer, Point> changePoints = changeRelatePoints(mousePoint,
				fPoint, nPoint, reqIndex);
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

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		MultiCommand multiCmd = new MultiCommand();
		List<Command> cmds = new ArrayList<Command>();
		multiCmd.setCommands(cmds);
		// 移动相关点
		MoveBendpointCommand moveCmd = new MoveBendpointCommand();
		moveCmd.setConnection(getHost().getModel());
		moveCmd.setIndex(new int[] { changeIndex });
		moveCmd.setNewLocation(new Point[] { (Point) changePoint });
		cmds.add(moveCmd);
		// 删除重合点
		DeleteBendpointCommand delCmd = new DeleteBendpointCommand();
		delCmd.setConnection(getHost().getModel());
		delCmd.setIndex(deleteIndexs);
		cmds.add(delCmd);
		return multiCmd;
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
		Point mousePoint = new Point(request.getLocation());
		int reqIndex = request.getIndex();
		List<Point> bendpoints = (List<Point>) getConnection()
				.getRoutingConstraint();
		testDelete = false;
		// 数据准备
		if (!deleteBendpoint) {
			// 准备数据
			fPoint = bendpoints.get(reqIndex);
			nPoint = bendpoints.get(reqIndex + 1);
			fborderPoint = null; // 前方边界
			nborderPoint = null; // 后方边界
			deleteIndexs = new int[2];
			// 出现线对齐后,需要修改的数据
			if (reqIndex > 0) {
				fborderPoint = bendpoints.get(reqIndex - 1);
			}
			if (reqIndex < bendpoints.size() - 2) {
				nborderPoint = bendpoints.get(reqIndex + 2);
			}
		}

		orginPoint = null;
		changePoint = null;
		if (fPoint.y == nPoint.y) // 竖直移动
		{
			if (fborderPoint != null) {// 存在前方边界
				if (Math.abs(mousePoint.y - fborderPoint.y) < CheckDeleteLimit) { // 触发delete
					testDelete = true;
					if (!deleteBendpoint) {
						deleteIndexs[0] = bendpoints.indexOf(fborderPoint);
						deleteIndexs[1] = bendpoints.indexOf(fPoint);
					}
					orginPoint = nPoint;
					changePoint = new AbsoluteBendpoint(nPoint.x,
							fborderPoint.y);
				}
			}
			if (nborderPoint != null) {
				if (Math.abs(mousePoint.y - nborderPoint.y) < CheckDeleteLimit) { // 触发delete
					testDelete = true;
					if (!deleteBendpoint) {
						deleteIndexs[0] = bendpoints.indexOf(nPoint);
						deleteIndexs[1] = bendpoints.indexOf(nborderPoint);
					}
					orginPoint = fPoint;
					changePoint = new AbsoluteBendpoint(fPoint.x,
							nborderPoint.y);
				}
			}
		} else if (fPoint.x == nPoint.x) { // 水平移动
			if (fborderPoint != null) {// 存在前方边界
				if (Math.abs(mousePoint.x - fborderPoint.x) < CheckDeleteLimit) { // 触发delete
					testDelete = true;
					if (!deleteBendpoint) {
						deleteIndexs[0] = bendpoints.indexOf(fborderPoint);
						deleteIndexs[1] = bendpoints.indexOf(fPoint);
					}
					orginPoint = nPoint;
					changePoint = new AbsoluteBendpoint(fborderPoint.x,
							nPoint.y);
				}
			}
			if (nborderPoint != null) {
				if (Math.abs(mousePoint.x - nborderPoint.x) < CheckDeleteLimit) { // 触发delete
					testDelete = true;
					if (!deleteBendpoint) {
						deleteIndexs[0] = bendpoints.indexOf(nPoint);
						deleteIndexs[1] = bendpoints.indexOf(nborderPoint);
					}
					orginPoint = fPoint;
					changePoint = new AbsoluteBendpoint(nborderPoint.x,
							fPoint.y);
				}
			}
		}
		if (testDelete) {
			System.err.println("test delete");
			if (!deleteBendpoint) {
				System.err.println("deleteBendpoint is true");
				deleteBendpoint = true;
				saveOriginalConstraint();
				changeIndex = bendpoints.indexOf(orginPoint);
				List<Point> newBendpoints = (List<Point>) getConnection()
						.getRoutingConstraint();
				newBendpoints.set(changeIndex, (AbsoluteBendpoint) changePoint);
				for (int i = 0; i < deleteIndexs.length; i++) {
					newBendpoints.remove(deleteIndexs[i] - i);
				}
				getConnection().setRoutingConstraint(newBendpoints);
			}
			return;
		}

		if (deleteBendpoint) {
			System.err.println("deleteBendpoint is false");
			deleteBendpoint = false;
			restoreOriginalConstraint();
		}

		getConnection().translateToRelative(mousePoint);
		saveOriginalConstraint();
		List<Point> originBendpoints = (List<Point>) getConnection()
				.getRoutingConstraint();
		Map<Integer, Point> changePoints = changeRelatePoints(mousePoint,
				fPoint, nPoint, reqIndex);
		for (Entry<Integer, Point> entry : changePoints.entrySet()) {
			originBendpoints.set(entry.getKey(),
					new AbsoluteBendpoint(entry.getValue()));
		}
		getConnection().setRoutingConstraint(originBendpoints);
	}

	@Override
	protected void showDeleteBendpointFeedback(BendpointRequest request) {
		saveOriginalConstraint();
	}

	private Map<Integer, Point> changeRelatePoints(Point mousePoint,
			Point forwardPoint, Point nextPoint, int fPointIndex) {
		Map<Integer, Point> changePoints = new HashMap<Integer, Point>();
		// 将要保存的值
		Point nForwardPoint = new Point();
		Point nNextPoint = new Point();
		if (forwardPoint.x == nextPoint.x) // 检测是否是左右拖动
		{
			// y不变 设置x
			nForwardPoint.y = forwardPoint.y;
			nForwardPoint.x = mousePoint.x;
			nNextPoint.y = nextPoint.y;
			nNextPoint.x = mousePoint.x;

		}

		if (forwardPoint.y == nextPoint.y)// 检测是否是上下拖动
		{
			// x不变 设置y
			nForwardPoint.x = forwardPoint.x;
			nForwardPoint.y = mousePoint.y;
			nNextPoint.x = nextPoint.x;
			nNextPoint.y = mousePoint.y;
		}
		changePoints.put(fPointIndex, nForwardPoint);
		changePoints.put(fPointIndex + 1, nNextPoint);
		return changePoints;
	}
}

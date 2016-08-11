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
import org.eclipse.gef.handles.BendpointHandle;
import org.eclipse.gef.requests.BendpointRequest;

import wei.learn.gef.command.DeleteBendpointCommand;
import wei.learn.gef.command.MoveBendpointCommand;
import wei.learn.gef.command.MultiCommand;
import wei.learn.gef.handle.CustomBendpointMoveHandle;

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
	private int changeIndex = -1;
	private Point orginPoint;
	private Bendpoint changePoint;
	// 这里用于临时存储数据. 方便在 Movefeedback和deletefeedback来回切换
	private Point fPoint;
	private Point nPoint;
	private Point fborderPoint;
	private Point nborderPoint;

	private void resetData() {
		deleteBendpoint = false;
		testDelete = false;
		deleteIndexs = null;
		changeIndex = -1;

	}

	public Command getCommand(Request request) {
		if (REQ_MOVE_BENDPOINT.equals(request.getType())) {
			if (deleteBendpoint)
				return getDeleteBendpointCommand((BendpointRequest) request);
			return getMoveBendpointCommand((BendpointRequest) request);
		}
		return null;
	}

	@Override
	protected List<BendpointHandle> createSelectionHandles() {
		List<BendpointHandle> list = new ArrayList<BendpointHandle>();
		ConnectionEditPart connEP = (ConnectionEditPart) getHost();
		PointList points = getConnection().getPoints();
		for (int i = 1; i < points.size() - 2; i++) { // 从第一个点到倒数第二个点的遍历
			list.add(new CustomBendpointMoveHandle(connEP, i - 1, i));
		}
		return list;
	}

	/*
	 * 移动bendpoint两端的bendpoints
	 */
	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendpointCommand moveCom = new MoveBendpointCommand();
		Point mouseLocation = request.getLocation();
		getConnection().translateToRelative(mouseLocation);
		// 准备数据
		int reqIndex = request.getIndex();
		List<Point> constraint = getBendpoints();
		Point fPoint = constraint.get(reqIndex);
		Point nPoint = constraint.get(reqIndex + 1);
		Map<Integer, Point> changePoints = handlebendpointMove(mouseLocation,
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
	protected void eraseConnectionFeedback(BendpointRequest request) {
		super.eraseConnectionFeedback(request);
		resetData();
	}

	@Override
	protected void showMoveBendpointFeedback(BendpointRequest request) {
		Point mouseLocation = new Point(request.getLocation());
		int reqIndex = request.getIndex();
		List<Point> bendpoints = getBendpoints();
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
				if (Math.abs(mouseLocation.y - fborderPoint.y) < CheckDeleteLimit) { // 触发delete
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
				if (Math.abs(mouseLocation.y - nborderPoint.y) < CheckDeleteLimit) { // 触发delete
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
				if (Math.abs(mouseLocation.x - fborderPoint.x) < CheckDeleteLimit) { // 触发delete
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
				if (Math.abs(mouseLocation.x - nborderPoint.x) < CheckDeleteLimit) { // 触发delete
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
				List<Point> newBendpoints = getBendpoints();
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
		getConnection().translateToRelative(mouseLocation);
		saveOriginalConstraint();
		List<Point> originBendpoints = getBendpoints();
		Map<Integer, Point> changePoints = handlebendpointMove(mouseLocation,
				fPoint, nPoint, reqIndex);
		for (Entry<Integer, Point> entry : changePoints.entrySet()) {
			originBendpoints.set(entry.getKey(),
					new AbsoluteBendpoint(entry.getValue()));
		}
		getConnection().setRoutingConstraint(originBendpoints);
	}

	private Map<Integer, Point> handlebendpointMove(Point mouseLocation,
			Point forwardPoint, Point nextPoint, int fPointIndex) {
		Map<Integer, Point> changePoints = new HashMap<Integer, Point>();
		// 将要保存的值
		Point nForwardPoint = new Point();
		Point nNextPoint = new Point();
		if (forwardPoint.x == nextPoint.x) // 检测是否是左右拖动
		{
			// y不变 设置x
			nForwardPoint.y = forwardPoint.y;
			nForwardPoint.x = mouseLocation.x;
			nNextPoint.y = nextPoint.y;
			nNextPoint.x = mouseLocation.x;

		}

		if (forwardPoint.y == nextPoint.y)// 检测是否是上下拖动
		{
			// x不变 设置y
			nForwardPoint.x = forwardPoint.x;
			nForwardPoint.y = mouseLocation.y;
			nNextPoint.x = nextPoint.x;
			nNextPoint.y = mouseLocation.y;
		}
		changePoints.put(fPointIndex, nForwardPoint);
		changePoints.put(fPointIndex + 1, nNextPoint);
		return changePoints;
	}

	@SuppressWarnings("unchecked")
	private List<Point> getBendpoints() {
		return (List<Point>) getConnection().getRoutingConstraint();
	}

	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		return null;
	}
}

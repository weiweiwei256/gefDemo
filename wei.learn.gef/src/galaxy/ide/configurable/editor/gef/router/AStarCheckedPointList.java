package galaxy.ide.configurable.editor.gef.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

@SuppressWarnings("serial")
// 和CheckedPointList2完全一样 但是这个类是为AStar布线提供的 未来可能有个性化修改 所以单独存在一份
public class AStarCheckedPointList extends PointList {

	// 检测为重合的距离
	private int CHECK_DISTANCE = 10;
	// X方向重合 X方向偏转的距离
	private int LINE_OFFSET_X = 1;
	// Y方向重合和偏转距离
	private int LINE_OFFSET_Y = 1;
	// 机制开关
	private boolean on_off = true;
	// 顺路开关
	private boolean on_the_way = false;

	/****** shoulder *******************************************************/
	// 肩膀开关
	private boolean shoulder_on_off = false;
	// shoulder x偏移量
	private int SHOULDER_OFFSET_X = 3;
	// shoulder y偏移量
	private int SHOULDER_OFFSET_Y = 3;
	/****** bridge *******************************************************/
	// bridge 开关
	private Connection conn;
	// 其他的线
	private List<Connection> othersConnections = new ArrayList<Connection>();
	// 记录终点 为顺路提供服务 顺路开关关闭就无需关注了
	private Point newEndPoint;
	// 候选添加的点
	private Point caditatePoint;
	// 已经添加到List中的最后的点 这个点是可以改变的
	private Point lastPoint;

	// 储存每条线的终点和共终点点且顺路的线
	// 这是为了解决当改变终点图源位置时,共线的新旧线会相互干扰出现错误和平行线
	private HashMap<Connection, Point> connectionEndPoint = new HashMap<Connection, Point>();
	private HashMap<Point, ArrayList<Connection>> connectionsWithSameEndPoint = new HashMap<Point, ArrayList<Connection>>();
	private boolean changeEndPoints = false;

	public void setConn(Connection conn) {
		this.conn = conn;
		refreshConnections();
	}

	public void setOnOffMergeLine(boolean on_off_MergeLine) {
		on_the_way = on_off_MergeLine;
	}

	public void setEndPoint(Point endPoint) {
		// 只是更新到全局变量 并没有保存到connectionEndPoint中
		this.newEndPoint = endPoint;
		// 如果之前已经保存过
		if (connectionEndPoint.get(conn) != null) {
			// 保存之前的终点
			Point oldEndPoint = connectionEndPoint.get(conn);
			if (oldEndPoint.equals(endPoint)) // 和已有的存储相同,无需更新和修改.
			{
				// 不是改变终点的操作
				return;
			} else {
				changeEndPoints = true;
			}

		} else // 如果之前没有保存过,创建第一次的数据.
		{
			connectionEndPoint.put(conn, endPoint);
		}

		// 根据是否改变了终点而分别处理 填充othersConnections
		othersConnections.clear();
		if (changeEndPoints == false) // 正常执行
		{
			refreshConnections();
		} else // 处理终点改变问题
		{
			// 获取原有终点的ConnectionList
			Point oldEndPoint = connectionEndPoint.get(conn);
			ArrayList<Connection> connectionList = connectionsWithSameEndPoint
					.get(oldEndPoint);
			refreshConnectionsWithoutSameEndPointConnection(connectionList);
			// 为了其他的重合线做准备 因为 自己在无干扰情况下绘制完毕了 就得让其他的重合线检测到自己.
			if (connectionList != null) {
				connectionList.remove(conn);
			}
			// 移除无用的终点
			if (connectionList == null) {
				connectionsWithSameEndPoint.remove(oldEndPoint);
			}
			// 保存新的终点
			connectionEndPoint.put(conn, endPoint);
			changeEndPoints = false;
		}
	}

	@SuppressWarnings("unchecked")
	private void refreshConnectionsWithoutSameEndPointConnection(
			ArrayList<Connection> connectionList) {
		List<Connection> allConnections = conn.getParent().getChildren();
		for (int i = 0; i < allConnections.size(); i++) {
			// 解决当改变终点图源位置时,共线的新旧线会相互干扰错误跨点和平行线问题
			// 核心思路是,在其中一条重合线试图改变终点时,其他的重合线就从otherConnections中去除
			if (connectionList == null) // connectionList==null
			{
				if (allConnections.get(i) != conn) {
					othersConnections.add(allConnections.get(i));
				}
			}
			if (connectionList != null
					&& !connectionList.contains(allConnections.get(i))) {
				othersConnections.add(allConnections.get(i));
			}

		}
	}
	@SuppressWarnings("unchecked")
	// 刷新其他线的数据
	private void refreshConnections() {
		othersConnections.clear();
		List<Connection> allConnections = conn.getParent().getChildren();
		for (int i = 0; i < allConnections.size(); i++) {
			// 由于不能跟自己比较位置 所以移除自己
			if (allConnections.get(i) != conn) {
				othersConnections.add(allConnections.get(i));
			}
		}
	}

	// 添加候选点
	public void addCandidatePoint(Point point) {
		addCandidatePoint(point.x, point.y);
	}

	public void addCandidatePoint(int x, int y) {
		caditatePoint = new Point(x, y);
		// 如果这是增加的第一或者第二个点,就不用检测了
		// 如果这是增加的第一条线就不用了
		if (othersConnections.size() == 0 || size() < 2) {
			addPoint(caditatePoint);
		} else
		// 需要校验
		{
			// 取出最后一个
			lastPoint = getLastPoint();
			removePoint(size() - 1);
			if (on_off) {
				if (lastPoint.x == caditatePoint.x) {
					checkCaditateX();
				} else if (lastPoint.y == caditatePoint.y) {
					checkCaditateY();
				}
			}
			addCheckedPoints();
		}
	}

	// 最后点和候选点通过了检验 添加到list中
	private void addCheckedPoints() {
		addPoint(lastPoint);
		addPoint(caditatePoint);
	}

	// 检测准备添加的线的点是否和已有线重叠 X方向
	private void checkCaditateX() {
		// 由于线只有水平竖直两种情况 所以可以区分添加线的类型
		// 遍历已有的线
		for (int i = 0; i < othersConnections.size(); i++) {
			Connection conn = othersConnections.get(i);
			// 获取线的所有的点
			PointList pointList = conn.getPoints();
			// 遍历线的所有的点
			// -1的原因是避免溢出 由于线是水平竖直的 所以相邻点必然是X相同,或是Y相同 我们关注的是两个点的X和检测的点的X相同
			// 一个点命中无意义 所以无需检测最后一个
			for (int j = 0; j < pointList.size() - 1; j++) {
				// 由于只有x相同才可能存在重叠 更准确的说是两个相邻的点的x相同
				if (Math.abs(pointList.getPoint(j).x - lastPoint.x) <= CHECK_DISTANCE
						&& pointList.getPoint(j).x == pointList.getPoint(j + 1).x)// 这个判断是为了当线与桥重合时,不用来判断为重合
				{
					// 更加详细的判断
					// 排序
					int y1 = Math.min(pointList.getPoint(j).y,
							pointList.getPoint(j + 1).y);
					int y2 = Math.max(pointList.getPoint(j).y,
							pointList.getPoint(j + 1).y);
					int y3 = Math.min(lastPoint.y, caditatePoint.y);
					int y4 = Math.max(lastPoint.y, caditatePoint.y);
					// 竖直位置相同但不重叠
					if (y1 > y4 || y2 < y3) {
						break;
					} else
					// 重叠调整位置
					{
						// 检测是否顺路 如果顺路就停止遍历 并且调整到已有路线的位置
						if (on_the_way) {
							if (newEndPoint.equals(pointList.getLastPoint().x,
									pointList.getLastPoint().y)) {
								lastPoint.x = pointList.getPoint(j).x;
								caditatePoint.x = pointList.getPoint(j).x;
								recondSameEndConnections(conn);
								return;
							}
						}

						// 延续已有的方向 偏移
						if (!caditatePoint.equals(newEndPoint)) {
							Point frontLastPoint = getLastPoint();
							// x增大的趋势
							if (frontLastPoint.x < lastPoint.x) {
								// 右移
								lastPoint.x += LINE_OFFSET_X;
								caditatePoint.x += LINE_OFFSET_X;
							} else {
								// 左移
								lastPoint.x -= LINE_OFFSET_X;
								caditatePoint.x -= LINE_OFFSET_X;
							}
							// 递归检测新位置 是否有重叠
							checkCaditateX();
						}else
						{
							return;
						}
					}

				}
			}
		}
	}

	public void recondSameEndConnections(Connection conn) {
		ArrayList<Connection> connectionList;
		if (connectionsWithSameEndPoint.get(newEndPoint) == null) {
			connectionList = new ArrayList<Connection>();
			connectionList.add(this.conn);
			connectionList.add(conn);
		} else {
			connectionList = connectionsWithSameEndPoint.get(newEndPoint);
			if (!connectionList.contains(this.conn)) {
				connectionList.add(this.conn);
			}
			if (!connectionList.contains(conn)) {
				connectionList.add(conn);
			}
		}
		connectionsWithSameEndPoint.put(newEndPoint, connectionList);
	}

	// 检测准备添加的线的点是否和已有线重叠 Y方向
	private void checkCaditateY() {
		// 遍历已有的线
		for (int i = 0; i < othersConnections.size(); i++) {
			Connection conn = othersConnections.get(i);
			// 判断已有的线是否存在重叠
			PointList pointList = conn.getPoints();
			// 遍历线的所有的点
			for (int j = 0; j < pointList.size() - 1; j++) {
				// 由于只有x相同才可能存在重叠 更准确的说是两个相邻的点的x相同
				if (Math.abs(pointList.getPoint(j).y - lastPoint.y) <= CHECK_DISTANCE
						&& pointList.getPoint(j).y == pointList.getPoint(j + 1).y) {
					// 更加详细的判断
					// 排序
					int x1 = Math.min(pointList.getPoint(j).x,
							pointList.getPoint(j + 1).x);
					int x2 = Math.max(pointList.getPoint(j).x,
							pointList.getPoint(j + 1).x);
					int x3 = Math.min(lastPoint.x, caditatePoint.x);
					int x4 = Math.max(lastPoint.x, caditatePoint.x);
					// 竖直位置相同但不重叠
					if (x1 > x4 || x2 < x3) {
						break;
					} else
					// 重叠调整位置
					{
						// 检测是否顺路 如果顺路就停止遍历 并且调整到已有路线的位置
						if (on_the_way) {
							if (newEndPoint.equals(pointList.getLastPoint().x,
									pointList.getLastPoint().y)) {
								lastPoint.y = pointList.getPoint(j).y;
								caditatePoint.y = pointList.getPoint(j).y;
								recondSameEndConnections(conn);
								return;
							}
						}
						// 延续已有的方向 偏移
						Point frontLastPoint = getLastPoint();
						if (frontLastPoint.y < lastPoint.y) {
							// 下移
							lastPoint.y += LINE_OFFSET_Y;
							caditatePoint.y += LINE_OFFSET_Y;
						} else {
							// 上移
							lastPoint.y -= LINE_OFFSET_Y;
							caditatePoint.y -= LINE_OFFSET_Y;
						}
						checkCaditateY();
					}

				}
			}
		}
	}

	// shoulder的意思是最后到达终点前避免直角弯 而且提供一个过渡
	public void addShoulder() {
		if (shoulder_on_off == false) {
			return;
		}
		// 如果是直上直下则不用
		if (size() > 2) {
			// 取出后三个点 删除后两个点
			int size = size();
			Point lastPoint = getPoint(size - 1);
			Point middlePoint = getPoint(size - 2);
			Point frontPoint = getPoint(size - 3);
			// 只有竖直进入的情况
			if (lastPoint.x == middlePoint.x
					&& Math.abs(frontPoint.x - middlePoint.x) >= SHOULDER_OFFSET_X) // 安全监测
			{
				Point middleFrontPoint = middlePoint.getCopy();
				if (frontPoint.x < middlePoint.x) // "7"形状
				{
					// 左移动两个像素
					middleFrontPoint.x = middlePoint.x - SHOULDER_OFFSET_X;
				} else {
					// 右移动两个像素
					middleFrontPoint.x = middlePoint.x + SHOULDER_OFFSET_X;
				}
				// 移除原有拐点
				removePoint(size - 2);
				// 添加中前点
				insertPoint(middleFrontPoint, size - 2);
				Point middleNextPoint = middlePoint.getCopy();
				middleNextPoint.y = middlePoint.y + SHOULDER_OFFSET_Y;
				// 添加中后点
				insertPoint(middleNextPoint, size - 1);
			}
		}
	}

}

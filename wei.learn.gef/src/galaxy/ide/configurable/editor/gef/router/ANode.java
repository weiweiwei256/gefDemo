package galaxy.ide.configurable.editor.gef.router;

import org.eclipse.draw2d.geometry.Point;

/**
 * @author caiyu
 * @date 2014-5-12
 */
public class ANode implements Comparable<ANode> {
    private ANode parent;
    public int g;
    public int h;

    public final int xIndex;
    public final int yIndex;

    private ANodeLevel level = ANodeLevel.EASY;

    public ANodeLevel getLevel() {
        return level;
    }

    public void setLevel(ANodeLevel level) {
        this.level = level;
    }

    public ANode() {
        xIndex = 0;
        yIndex = 0;
    }

    public ANode(int xIndex, int yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    /**
     * 
     * @param p
     *            当前Point
     * @param startPoint
     *            起始Point
     * @param GRID_WIDTH
     *            格子宽度
     */
    public ANode(Point p, Point startPoint, final int GRID_WIDTH) {
        this.xIndex = ANodePreReader.calculateIndex(p.x, startPoint.x,
                GRID_WIDTH);
        this.yIndex = ANodePreReader.calculateIndex(p.y, startPoint.y,
                GRID_WIDTH)-2;
    }

    public Point getPoint(final Point startPoint, final int D) {
        return new Point(startPoint.x + D * xIndex, startPoint.y + D * yIndex);
    }

    public void setParent(ANode parent) {
        this.parent = parent;
    }

    public int f() {
        return g + h;
    }

    public ANode getParent() {
        return parent;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof ANode)
            return ((ANode) o).xIndex == this.xIndex
                    && ((ANode) o).yIndex == this.yIndex;
        return false;
    }

    public int hashCode() {
        return xIndex * 17 + yIndex * 11 + 13;
    }

    @Override
    public int compareTo(ANode o) {
        if(f()!=o.f())
        {
            return f() - o.f();
        }else   //返回 h较小的  
        {
            return this.h-o.h;
        }
//        return f() - o.f();
    }

    public String toString() {
        return g + ":" + h + "\n";
    }
}

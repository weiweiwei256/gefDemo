package wei.learn.gef.model;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class HelloModel extends AbstractModel {
	private String text = "Hello world";
	private Rectangle constraint;
	public static final String P_CONSTRAINT = "_constraint";
	// 添加字符串的ID 这样改变图形的文本时可通知其Editpart
	public static final String P_TEXT = "_text";
	// connection

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		firePropertyChange(P_TEXT, null, text);
	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
		firePropertyChange(P_CONSTRAINT, null, constraint);
	}

	// 其实属性视图中用tableview来显示属性,第一列是属性名称,第2列是属性值.
	// IPropertyDescriptor[]数组顾名思义就是用来设置属性名称的.这里我们只提供一个属性
	// 并命名为greeeting
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] { new TextPropertyDescriptor(
				P_TEXT, "Greeting") };
		return descriptors;
	}

	// 使用属性的ID来获得该属性在属性视图的值
	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(P_TEXT)) {
			return text;
		}
		return null;
	}

	// 判断属性视图中的属性值是否改变,如果没有指定属性值则返回false
	@Override
	public boolean isPropertySet(Object id) {
		if (id.equals(P_TEXT)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(P_TEXT)) {
			setText((String) value);
		}
	}
}

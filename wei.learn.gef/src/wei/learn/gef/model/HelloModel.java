package wei.learn.gef.model;

import org.eclipse.draw2d.geometry.Rectangle;

public class HelloModel extends AbstractModel{
	private String text="Hello world";
	private Rectangle constraint;
	public static final String P_CONSTRAINT="_constraint";
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
		firePropertyChange(P_CONSTRAINT, null, constraint);
	}
	
}

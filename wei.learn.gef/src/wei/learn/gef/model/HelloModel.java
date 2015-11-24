package wei.learn.gef.model;

import org.eclipse.draw2d.geometry.Rectangle;

public class HelloModel {
	private String text="Hello world";
	private Rectangle constraint;

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
	}
	
}

package wei.learn.gef.model;

import java.util.ArrayList;
import java.util.List;

public class ContentsModel {
	private List<Object> children = new ArrayList<Object>();

	public void addChild(Object child) {
		children.add(child);
	}

	public List<Object> getChildren() {
		return children;
	}
}

package wei.learn.gef.model;


public class AbstractConnectionModel {
	private HelloModel source, target;

	public void attachSource() {
		if (!source.getModelSourceConnection().contains(this)) {
			source.addSourceConnection(this);
		}
	}

	public void attachTarget() {
		if (!target.getModelTargetConnection().contains(this)) {
			target.addTargetConnection(this);
		}
	}

	public void detachSource() {
		source.removeSourceConnection(this);
	}

	public void detachTarget() {
		target.removeTargetConnection(this);
	}

	public HelloModel getSource() {
		return source;
	}

	public void setSource(HelloModel model) {
		source = model;
	}

	public HelloModel getTarget() {
		return target;
	}

	public void setTarget(HelloModel model) {
		target = model;
	}
}

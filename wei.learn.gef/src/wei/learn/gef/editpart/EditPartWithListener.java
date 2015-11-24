package wei.learn.gef.editpart;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import wei.learn.gef.model.AbstractModel;

public abstract class EditPartWithListener extends AbstractGraphicalEditPart
		implements PropertyChangeListener {
	@Override
	public void activate() {
		super.activate();
		((AbstractModel) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((AbstractModel) getModel()).removePropertyChangeListener(this);
	}
}

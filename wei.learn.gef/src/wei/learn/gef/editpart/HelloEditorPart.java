package wei.learn.gef.editpart;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

import wei.learn.gef.model.HelloModel;
import wei.learn.gef.policy.CustomComponentEditPolicy;

public class HelloEditorPart extends EditPartWithListener {

	@Override
	protected IFigure createFigure() {
		HelloModel model = (HelloModel) getModel();

		Label label = new Label();
		label.setText(model.getText());

		label.setBorder(new CompoundBorder(new LineBorder(),
				new MarginBorder(3)));
		label.setBackgroundColor(ColorConstants.orange);
		label.setOpaque(true);
		return label;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new CustomComponentEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(HelloModel.P_CONSTRAINT)) {
			refreshVisuals();
		}
	}

	@Override
	protected void refreshVisuals() {
		Rectangle constraint = ((HelloModel) getModel()).getConstraint();
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), constraint);
	}
}

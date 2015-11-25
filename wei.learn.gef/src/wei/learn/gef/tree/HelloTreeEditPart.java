package wei.learn.gef.tree;


import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import wei.learn.gef.model.HelloModel;
import wei.learn.gef.policy.CustomComponentEditPolicy;

public class HelloTreeEditPart extends CustomTreeEditPart {
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new CustomComponentEditPolicy());
	}
	// override
	protected void refreshVisuals() {
		HelloModel model = (HelloModel) getModel();
		// the text of HelloModel is set up as the text of a text item
		setWidgetText(model.getText());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(HelloModel.P_TEXT))
			refreshVisuals();
	}
}

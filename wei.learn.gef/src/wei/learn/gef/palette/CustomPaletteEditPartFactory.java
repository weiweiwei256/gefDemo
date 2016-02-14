package wei.learn.gef.palette;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;

public class CustomPaletteEditPartFactory extends PaletteEditPartFactory {
	@Override
	protected EditPart createDrawerEditPart(EditPart parentEditPart,
			Object model) {
		return new DrawerEditPart((PaletteDrawer) model);
	}
}

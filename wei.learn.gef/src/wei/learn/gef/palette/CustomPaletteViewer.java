package wei.learn.gef.palette;

import org.eclipse.gef.ui.palette.PaletteViewer;

public class CustomPaletteViewer extends PaletteViewer {
	public CustomPaletteViewer() {
		super();
		setEditPartFactory(new CustomPaletteEditPartFactory());
	}
}

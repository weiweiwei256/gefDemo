package wei.learn.gef.palette;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.swt.widgets.Composite;

public class CustomPaletteViewerProvider extends PaletteViewerProvider {

	public CustomPaletteViewerProvider(EditDomain graphicalViewerDomain) {
		super(graphicalViewerDomain);
		// TODO Auto-generated constructor stub
	}
    public PaletteViewer createPaletteViewer(Composite parent) {  
        PaletteViewer pViewer = new CustomPaletteViewer();  
        pViewer.createControl(parent);  
        configurePaletteViewer(pViewer);  
        hookPaletteViewer(pViewer);  
        return pViewer;  
    }  
}

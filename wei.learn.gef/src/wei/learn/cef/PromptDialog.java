package wei.learn.cef;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

final class PromptDialog extends Dialog {
	private String message;
	private String value;

	public PromptDialog(Shell parent) {
		super(parent);
	}

	public PromptDialog(Shell parent, int style) {
		super(parent, style);
	}

	public int open() {
		final Shell shell = new Shell(this.getParent(), 67680);
		shell.setText(this.getText());
		shell.setLayout(new GridLayout());
		Label label = new Label(shell, 64);
		label.setText(this.message);
		GridData data = new GridData();
		Monitor monitor = this.getParent().getMonitor();
		int maxWidth = monitor.getBounds().width * 2 / 3;
		int width = label.computeSize(-1, -1).x;
		data.widthHint = Math.min(width, maxWidth);
		data.horizontalAlignment = 4;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData(data);
		final Text valueText = new Text(shell, 2048);
		if (this.value != null) {
			valueText.setText(this.value);
		}

		data = new GridData();
		width = valueText.computeSize(-1, -1).x;
		if (width > maxWidth) {
			data.widthHint = maxWidth;
		}

		data.horizontalAlignment = 4;
		data.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(data);
		Composite buttonContainer = new Composite(shell, 0);
		data = new GridData();
		data.horizontalAlignment = 2;
		buttonContainer.setLayoutData(data);
		buttonContainer.setLayout(new GridLayout(2, true));
		final Button okButton = new Button(buttonContainer, 8);
		okButton.setText(SWT.getMessage("SWT_OK"));
		okButton.setLayoutData(new GridData(768));
		Button cancelButton = new Button(buttonContainer, 8);
		cancelButton.setText(SWT.getMessage("SWT_Cancel"));
		cancelButton.setLayoutData(new GridData(768));
		final int[] result = new int[] { 256 };
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				PromptDialog.this.value = valueText.getText();
				result[0] = event.widget == okButton ? 32 : 256;
				shell.close();
			}
		};
		okButton.addListener(13, listener);
		cancelButton.addListener(13, listener);
		shell.pack();
		Rectangle bounds = this.getParent().getBounds();
		Rectangle rect = shell.getBounds();
		shell.setLocation(bounds.x + (bounds.width - rect.width) / 2, bounds.y
				+ (bounds.height - rect.height) / 2);
		shell.open();
		Display display = this.getParent().getDisplay();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return result[0];
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setInitialValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
package wei.learn.gef.ui;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import wei.learn.gef.Application;
import wei.learn.gef.editpart.PartFactory;
import wei.learn.gef.helper.IImageKeys;
import wei.learn.gef.model.ArrowConnectionModel;
import wei.learn.gef.model.ContentsModel;
import wei.learn.gef.model.HelloModel;
import wei.learn.gef.model.LineConnectionModel;

public class DiagramEditor extends GraphicalEditorWithPalette {

	// Editor ID
	public static final String ID = "wei.learn.gef.DiagramEditor";
	// an EditDomain is a "session" of editing which contains things like the
	// CommandStack
	GraphicalViewer viewer;

	public DiagramEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		viewer = getGraphicalViewer();
		// 具有缩放能力的ScalableRootEditPart
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer.setRootEditPart(rootEditPart);
		ZoomManager manager = rootEditPart.getZoomManager();
		// 放大比例数组
		double[] zoomLevels = new double[] {
				// 缩放比例是从25％－2000％
				0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
		manager.setZoomLevels(zoomLevels); // 添加放大比例
		// 设置非百分比缩放
		ArrayList<String> zoomContributions = new ArrayList<String>();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);
		// 注册放大Action
		IAction action = new ZoomInAction(manager);
		getActionRegistry().registerAction(action);
		// 注册缩小Action
		action = new ZoomOutAction(manager);
		getActionRegistry().registerAction(action);

		viewer.setEditPartFactory(new PartFactory());
		// 创建键盘句柄keyHander
		KeyHandler keyHandler = new KeyHandler();

		// 按DEL键时执行删除Action
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(GEFActionConstants.DELETE));
		// 按F2键时执行直接编辑Action
		keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry()
				.getAction(GEFActionConstants.DIRECT_EDIT));
		getGraphicalViewer().setKeyHandler(keyHandler);
	}

	@Override
	protected void initializeGraphicalViewer() {
		// set the contents of this editor
		ContentsModel contents = new ContentsModel();
		HelloModel child1 = new HelloModel();
		child1.setConstraint(new Rectangle(0, 0, -1, -1));
		contents.addChild(child1);

		HelloModel child2 = new HelloModel();
		child2.setConstraint(new Rectangle(30, 30, -1, -1));
		contents.addChild(child2);
		HelloModel child3 = new HelloModel();
		child3.setConstraint(new Rectangle(10, 80, 80, 50));
		contents.addChild(child3);
		viewer.setContents(contents);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		// 1.0 创建一个palette的route
		PaletteRoot root = new PaletteRoot();
		// 2.0 创建一个工具组用于防止常规Tools
		PaletteGroup toolGroup = new PaletteGroup("工具");

		// 3.0 创建一个GEF提供的"selection"工具 并将其放到toolGroup中
		ToolEntry tool = new SelectionToolEntry();
		toolGroup.add(tool);

		// 3.1 该(选择)工具为缺省被选择的工具
		root.setDefaultEntry(tool);

		// 4.0 创建一个GEF提供的 "Marquee多选"工具并将其放到toolGroup中
		tool = new MarqueeToolEntry();
		toolGroup.add(tool);

		// 5.0 创建一个Drawer(抽屉)放置绘图工具,该抽屉名称为"画图"
		PaletteDrawer drawer = new PaletteDrawer("画图");

		// 指定"创建HelloModel模型"工具所对应的图标
		ImageDescriptor descriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons//alt_window16.gif");

		// 6.0 创建"创建HelloModel模型"工具
		CreationToolEntry creationToolEntry = new CreationToolEntry(
				"绘制HelloModel", "创建HelloModel模型", new SimpleFactory(
						HelloModel.class), descriptor, descriptor);
		drawer.add(creationToolEntry);

		// 连线
		PaletteDrawer connectionDrawer = new PaletteDrawer("连线");
		ImageDescriptor newConnectionDescriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						IImageKeys.jiantou);
		ConnectionCreationToolEntry connCreationEntry = new ConnectionCreationToolEntry(
				"简单链接", "创建简单连接", new SimpleFactory(LineConnectionModel.class),
				newConnectionDescriptor, newConnectionDescriptor);
		connectionDrawer.add(connCreationEntry);

		// 箭头连线
		PaletteDrawer ArrowConnectionDrawer = new PaletteDrawer("箭头连线");
		ImageDescriptor newArrowConnectionDescriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						IImageKeys.model);
		ConnectionCreationToolEntry arrowConnCreationEntry = new ConnectionCreationToolEntry(
				"箭头链接", "创建箭头连接",
				new SimpleFactory(ArrowConnectionModel.class),
				newArrowConnectionDescriptor, newArrowConnectionDescriptor);
		ArrowConnectionDrawer.add(arrowConnCreationEntry);
		//
		// //汇聚节点
		// PaletteDrawer mergeLineDrawer = new PaletteDrawer("汇聚节点");
		// ImageDescriptor mergeLineDescriptor = AbstractUIPlugin
		// .imageDescriptorFromPlugin(Application.PLUGIN_ID,
		// IImageKeys.mergeLine);
		// CreationToolEntry mergeLineCreationEntry = new CreationToolEntry(
		// "汇聚节点", "汇聚节点",
		// new SimpleFactory(mergeLineModel.class),
		// mergeLineDescriptor, mergeLineDescriptor);
		// mergeLineDrawer.add(mergeLineCreationEntry);

		// 7.0 最后将创建的两组工具加到root上
		root.add(toolGroup);
		root.add(drawer);
		root.add(connectionDrawer);
		root.add(ArrowConnectionDrawer);
		// root.add(mergeLineDrawer);
		return root;
	}

	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();

		// 创建并注册一个DirectEditAction
		IAction action = new DirectEditAction((IWorkbenchPart) this);
		registry.registerAction(action);

		// 当一个action需要由选择对象更新时,需要注册其ID ??
		getSelectionActions().add(action.getId());
	
	}

	public Object getAdapter(Class type) {
		if (type == ZoomManager.class) {
			return ((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();
		}
		return super.getAdapter(type);
	}
}

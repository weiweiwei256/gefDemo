package wei.learn.gef.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import wei.learn.gef.Application;
import wei.learn.gef.editpart.PartFactory;
import wei.learn.gef.helper.IImageKeys;
import wei.learn.gef.helper.Utility;
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

	@SuppressWarnings("deprecation")
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new PartFactory());
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart(){
			@Override
			public DragTracker getDragTracker(Request req) {
				MarqueeDragTracker dt=	new MarqueeDragTracker();
				dt.setMarqueeBehavior(MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS);
				return dt;
			}
		};
		viewer.setRootEditPart(rootEditPart);
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
		child1.setConstraint(new Rectangle(100, 80, -1, -1));
		child1.setText("child1");
		contents.addChild(child1);
		HelloModel child2 = new HelloModel();
		child2.setConstraint(new Rectangle(100, 160, -1, -1));
		child2.setText("child2");
		contents.addChild(child2);
		HelloModel child3 = new HelloModel();
		child3.setConstraint(new Rectangle(400, 400, 80, 50));
		child3.setText("child3");
		contents.addChild(child3);
		viewer.setContents(contents);
		// Connection层布线策略
		ConnectionLayer connLayer = (ConnectionLayer) LayerManager.Helper.find(
				getGraphicalViewer().getContents()).getLayer(
				LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(Utility.getRouter(this));
		//增加第一条线
		LineConnectionModel line1 = new LineConnectionModel();
		List<Point> points1 = new ArrayList<Point>();
		points1.add(new Point(439, 100));
		points1.add(new Point(125, 100));
		line1.setBendpoints(points1);
		line1.setSource(child1);
		line1.setTarget(child2);
		line1.attachSource();
		line1.attachTarget();

		//增加第二条线
		LineConnectionModel line2 = new LineConnectionModel();
		List<Point> points2 = new ArrayList<Point>();
		points2.add(new Point(125, 200));
		points2.add(new Point(239, 200));
		points2.add(new Point(239, 290));
		points2.add(new Point(439, 290));
		line2.setBendpoints(points2);
		line2.setSource(child2);
		line2.setTarget(child3);
		line2.attachSource();
		line2.attachTarget();
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
		tool = new MarqueeToolEntry(); //修改区域选择tool支持选中关联连线
		tool.setToolProperty(
				MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR,
				MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS);
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

	@SuppressWarnings("unchecked")
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

	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}
}

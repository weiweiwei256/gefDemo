package galaxy.ide.configurable.editor.gef.router;

/**
 * @author caiyu
 * @date 2014-5-15
 */
public interface RouterStyle
{
    /**
     * 描点测试
     */
    int TEST = 1 << 1;
    /**
     * 简化共线点
     */
    int FLOYD_SIMPLIFY = 1 << 2;
    /**
     * 弗洛伊德平滑处理, 简化共线点并且使节点平滑
     */
    int FLOYD = 1 << 3;
    /**
     * 寻路只参考四个方向
     */
    int FOUR_DIR = 1 << 4;

    /**
     * 通过不同颜色展示预读的区域以及难度
     */
    int SHOW_POOL = 1 << 5;
    
	/**
	 * 在控制台显示布线信息
	 */
	int CONSOLE_INFO = 1 << 6;

    int NONE = 0;

}

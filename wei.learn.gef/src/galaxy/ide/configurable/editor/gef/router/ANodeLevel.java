package galaxy.ide.configurable.editor.gef.router;

/**
 * ½ÚµãµÈ¼¶£¬REÖ±½Ç±ß£¬BEÐ±½Ç±ß
 * 
 * @author caiyu
 * @date 2014-5-15
 */
public enum ANodeLevel {
    EASY(10, 14), NORMAL(20, 28), HARD(300,520), DEAD(2000, 2800);
    /**
     * Ö±½Ç±ß
     */
    public final int RE;
    /**
     * Ð±½Ç±ß
     */
    public final int BE;

    ANodeLevel(int RE, int BE) {
        this.RE = RE;
        this.BE = BE;
    }
}

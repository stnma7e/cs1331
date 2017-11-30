/**
 * Stores all the moves during a chess game.
 *
 * @author sdelmerico3
 * @version 0.0.1
 */
public class Move {
    private Ply whitePly;
    private Ply blackPly;

    /**
     * Creates a Moves class with one white player's move and one black player's
     * move.
     *
     * @param whitePly the white move
     * @param blackPly the black move
     */
    Move(Ply whitePly, Ply blackPly) {
        this.whitePly = whitePly;
        this.blackPly = blackPly;
    }

    /**
     * @return The white move.
     */
    Ply getWhite() {
        return whitePly;
    }

    /**
     * @return The black move.
     */
    Ply getBlack() {
        return blackPly;
    }
}

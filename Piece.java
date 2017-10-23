/**
 * Represents a piece on a chess board.
 *
 * @author sdelmerico3
 */
public abstract class Piece {
    protected Color color;

    /**
     * Creates a Piece with a specified color.
     */
    public Piece(Color c) {
        this.color = c;
    }

    /**
     * Returns the color of the piece.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns the algebraic notation name of the piece.
     */
    public abstract String algebraicName();

    /**
     * Returns the FEN notation name of the piece.
     */
    public abstract String fenName();

    /**
     * Returns all the possible moves that the specific piece
     * type can make from the specified square.
     */
    public abstract Square[] movesFrom(Square square);
}

import java.util.Optional;

/**
 * Represents a single player's (of any color) move.
 *
 * @author sdelmerico3
 * @version 0.0.1
 */
public class Ply {
    private Piece  piece;
    private Square from;
    private Square to;
    private Optional<String> comment;

    /**
     * Creates a Ply move with the piece that moved, the square it moved to and
     * from, and an optional comment on the move.
     *
     * @param piece The piece that moved
     * @param from The square that the piece moved from
     * @param to The square that the piece moved to
     * @param comment An optional comment that describes the comment
     */
    Ply(Piece piece, Square from, Square to, Optional<String> comment) {
        this.piece   = piece;
        this.from    = from;
        this.to      = to;
        this.comment = comment;
    }

    /**
     * Returns the piece given in the Ply.
     */
    Piece getPiece() {
        return piece;
    }

    /**
     * @return The square the Piece moved from: given in the Ply.
     */
    Square getFrom() {
        return from;
    }

    /**
     * @return The square the Piece moved to: given in the Ply.
     */
    Square getTo() {
        return to;
    }

    /**
     * @return The comment given in the Ply.
     */
    Optional<String> getComment() {
        return comment;
    }
}

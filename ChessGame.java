import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Stores all the moves during a chess game.
 *
 * @author sdelmerico3
 * @version 0.0.1
 */
public class ChessGame {
    private List<Move> moves;

    /**
     * Creates a ChessGame with a starting List of moves.
     *
     * @param moves The list of moves that will be used to define the game.
     */
    ChessGame(List<Move> moves) {
        this.moves = moves;
    }

    /**
     * @return The nth Move in the ChessGame.
     */
    Move getMove(int n) {
        if (n < moves.size()) {
            return moves.get(n);
        } else {
            return null;
        }
    }

    /**
     * @return All the moves that have occured in the ChessGame.
     */
    List<Move> getMoves() {
        return this.moves;
    }

    /**
     * @return All the moves that are validated by the filter.
     *
     * @param filter The filter that will be applied to the moves List.
     */
    List<Move> filter(Predicate<Move> filter) {
        return moves.stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * @return All moves that contain a valid comment.
     */
    List<Move> getMovesWithComment() {
        return filter(move -> {
                return move.getWhite().getComment().isPresent()
                    || move.getBlack().getComment().isPresent();
            });
    }

    /**
     * @return All moves that don't contain a valid comment.
     */
    List<Move> getMovesWithoutComment() {
        return filter(new Predicate<Move>() {
                public boolean test(Move move) {
                    return !(move.getWhite().getComment().isPresent()
                        || move.getBlack().getComment().isPresent());
                }
        });
    }

    /**
     * @param p is the Piece whose type will be used to filter the moves List.
     *
     * @return All moves that are made by a certain type of piece.
     */
    List<Move> getMovesWithPiece(Piece p) {
        return filter(new GetMovesWithPiece(p));
    }

    /**
     * Used to filter a ChessGame moves list by returning all moves with that
     * are made by a certain type of piece.
     *
     * @author sdelmerico3
     */
    private static class GetMovesWithPiece implements Predicate<Move> {
        private Piece p;

        /**
         * Creates a class of GetMovesWithPiece with the passed Piece.
         *
         * @param p Piece whose type is to be used.
         */
        GetMovesWithPiece(Piece p) {
            this.p = p;
        }

        @Override
        public boolean test(Move move) {
            String n = p.algebraicName();
            return move.getWhite().getPiece().algebraicName() == n
                    || move.getBlack().getPiece().algebraicName() == n;
        }
    }
}

public class Queen extends Piece {
    public Queen(Color c) {
        super(c);
    }

    @Override
    public String algebraicName() {
        return "Q";
    }

    @Override
    public String fenName() {
        return this.color == Color.WHITE ? "Q" : "q";
    }

    @Override
    public Square[] movesFrom(Square square) {
        Square[] squares = new Square[46];

        Rook r = new Rook(this.color);
        Square[] rookMoves = r.movesFrom(square);
        for (int i = 0; i < rookMoves.length; i++) {
            squares[i] = rookMoves[i];
        }

        Bishop b = new Bishop(this.color);
        Square[] bishopMoves = b.movesFrom(square);
        for (int i = 0; i < bishopMoves.length; i++) {
            squares[i + rookMoves.length] = bishopMoves[i];
        }

        return squares;
    }
}

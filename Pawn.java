public class Pawn extends Piece {
    public Pawn(Color c) {
        super(c);
    }

    @Override
    public String algebraicName() {
        return "";
    }

    @Override
    public String fenName() {
        return this.color == Color.WHITE ? "P" : "p";
    }

    @Override
    public Square[] movesFrom(Square square) {
        int rank = square.getRank();

        if (this.color == Color.WHITE) {
            if (square.getRank() == '8') {
                return new Square[0];
            }

            rank += 1;
        } else {
            if (square.getRank() == '1') {
                return new Square[0];
            }

            rank -= 1;
        }

        return new Square[] {new Square(square.getFile(), rank)};
    }
}

public class Rook extends Piece {
    public Rook(Color c) {
        super(c);
    }

    @Override
    public String algebraicName() {
        return "R";
    }

    @Override
    public String fenName() {
        return this.color == Color.WHITE ? "R" : "r";
    }

    @Override
    public Square[] movesFrom(Square square) {
        Square[] squares = new Square[16];
        for (char i = 0; i < 8; i++) {
            squares[i] = new Square(square.getFile(), '1' + i);
        }
        for (char i = 0; i < 8; i++) {
            squares[i + 8] = new Square('a' + i, square.getRank());
        }

        return squares;
    }
}

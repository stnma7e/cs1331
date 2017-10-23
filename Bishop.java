public class Bishop extends Piece {
    public Bishop(Color c) {
        super(c);
    }

    @Override
    public String algebraicName() {
        return "B";
    }

    @Override
    public String fenName() {
        return this.color == Color.WHITE ? "B" : "b";
    }

    @Override
    public Square[] movesFrom(Square square) {
        Square[] squares = new Square[30];

        for (int i = -7; i <= 7; i++) {
            int file = square.getFile() + i;
            int rank = square.getRank() + i;

            if (file < 'a' || file > 'h' || rank < '1'
                || rank > '8') {
                continue;
            }

            squares[i + 7] = new Square(file, rank);
        }

        for (int i = -7; i <= 7; i++) {
            int file = square.getFile() + i;
            int rank = square.getRank() - i;

            if (file < 'a' || file > 'h' || rank < '1'
                || rank > '8') {
                continue;
            }

            squares[i + 7 + 15] = new Square(file, rank);
        }

        return squares;
    }
}

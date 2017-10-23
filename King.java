public class King extends Piece {
    public King(Color c) {
        super(c);
    }

    @Override
    public String algebraicName() {
        return "K";
    }

    @Override
    public String fenName() {
        return this.color == Color.WHITE ? "K" : "k";
    }

    @Override
    public Square[] movesFrom(Square square) {
        Square[] squares = new Square[9];

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int file = square.getFile() + i;
                int rank = square.getRank() + j;

                if (file < 'a' || file > 'h' || rank < '1'
                    || rank > '8') {
                    continue;
                }

                squares[(1 - i) * 3 + (1 - j)] =
                    new Square(file, rank);
            }
        }

        return squares;
    }
}

public class Knight extends Piece {
    public Knight(Color c) {
        super(c);
    }

    @Override
    public String algebraicName() {
        return "N";
    }

    @Override
    public String fenName() {
        return this.color == Color.WHITE ? "N" : "n";
    }

    @Override
    public Square[] movesFrom(Square square) {
        Square[] squares = new Square[8];

        // going forward
        if (square.getRank() + 2 <= '8') {
            if (square.getFile() + 1 <= 'h') {
                squares[0] = new Square(square.getFile() + 1
                    , square.getRank() + 2);
            }
            if (square.getFile() - 1 >= 'a') {
                squares[1] = new Square(square.getFile() - 1
                    , square.getRank() + 2);
            }
        }

        // going backward
        if (square.getRank() - 2 >= '1') {
            if (square.getFile() + 1 <= 'h') {
                squares[2] = new Square(square.getFile() + 1
                    , square.getRank() - 2);
            }
            if (square.getFile() - 1 >= 'a') {
                squares[3] = new Square(square.getFile() - 1
                    , square.getRank() - 2);
            }
        }

        // going right
        if (square.getFile() + 2 <= 'h') {
            if (square.getRank() + 1 <= '8') {
                squares[4] = new Square(square.getFile() + 2
                    , square.getRank() + 1);
            }
            if (square.getRank() - 1 >= '1') {
                squares[5] = new Square(square.getFile() + 2
                    , square.getRank() - 1);
            }
        }

        // going left
        if (square.getFile() - 2 >= 'a') {
            if (square.getRank() + 1 <= '8') {
                squares[6] = new Square(square.getFile() - 2
                    , square.getRank() + 1);
            }
            if (square.getRank() - 1 >= '1') {
                squares[7] = new Square(square.getFile() - 2
                    , square.getRank() - 1);
            }
        }

        return squares;
    }
}

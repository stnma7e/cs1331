public class Main {
    public static void main(String[] args) {
        Piece knight = new Knight(Color.BLACK);
        assert knight.algebraicName().equals("N");
        assert knight.fenName().equals("n");
        Square[] attackedSquares = knight.movesFrom(new Square("f6"));
        for (Square i : attackedSquares) {
            System.out.println(i.toString());
        }

        System.out.println();

        Piece p = new Queen(Color.BLACK);
        Square[] squares = p.movesFrom(new Square("e4"));
        for (Square i : squares) {
            if (i == null) continue;
            System.out.println(i.toString());
        }

        // test that attackedSquares contains e8, g8, etc.
        Square a1 = new Square("a1");
        Square otherA1 = new Square('a', '1');
        Square h8 = new Square("h8");
        assert a1.equals(otherA1);
        assert !a1.equals(h8);

        System.out.println("HERE");
    }
}

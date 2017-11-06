import java.util.ArrayList;
import java.util.Arrays;

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
        try {
            new Square("a1");
        } catch (InvalidSquareException e) {
            System.out.println("InvalidSquareException for valid square: " + e.getMessage());
        }
        try {
            String invalidSquare = "a9";
            new Square(invalidSquare);
            System.out.println("No InvalidSquareException for invalid square: " + invalidSquare);
        } catch (InvalidSquareException e) {
            // Success
        }
        Square s = new Square("f7");
        assert 'f' == s.getFile();
        assert '7' == s.getRank();
        Square s2 = new Square('e', '4');
        assert "e4" == s2.toString();

        SquareSet ss = new SquareSet();
        ss.add(s);
        ss.addAll(Arrays.asList(new Square[] {s, s2, null}));

        for (Square s1 : ss) {
            System.out.println(s1.toString());
        }
    }
}

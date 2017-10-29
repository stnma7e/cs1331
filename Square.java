/**
 * Represents a square on a chess board.
 *
 * @author sdelmerico3
 */
public class Square {
    private char file;
    private char rank;

    /**
     * Creates a Square with specified rank and file.
     */
    public Square(char file, char rank) {
        if (rank < '1' || rank > '8'
            || file < 'a' || file > 'h') {
            char[] badSquare = new char[] {file, rank};
            throw new InvalidSquareException(new String(badSquare));
        }

        this.file = file;
        this.rank = rank;
    }

    /**
     * Creates an Square with a rank and file specified by a
     * String with the format "<file><rank>".
     */
    public Square(String name) {
        this(name.charAt(0), name.charAt(1));
    }

    /**
     * Helper constructor for use with character arithmetic
     * that defaults to integer types.
     */
    public Square(int file, int rank) {
        this((char) file, (char) rank);
    }

    public char getRank() {
        return this.rank;
    }

    public char getFile() {
        return this.file;
    }

    @Override
    public String toString() {
        char[] ret = {file, rank};
        return new String(ret);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Square)) {
            return false;
        }

        if (this.file != ((Square) obj).file
            || this.rank != ((Square) obj).rank) {
            return false;
        }

        return true;
    }
}

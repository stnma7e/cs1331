/**
 * Thrown when an attempt is made to create a square that would not exist on a
 * chess board. It should be an unchecked exception because it is likely a logic
 * error if an invalid square is created during the course of the program.
 * However, if Squares are created directly from external input (e.g. PGN file),
 * then the validity of the source would not be verified, and if the input
 * contained an error, then it would be propagted via this unchecked exception.
 * In the case of user input, this exception should be checked. Otherwise, it
 * implies a programmatic error, and may remained unchecked.
 *
 * @author sdelmerico3
 */
public class InvalidSquareException extends RuntimeException {
    /**
     * Creates an InvalidSquareException with the String name of the invalid
     * square that was to be created.
     */
    public InvalidSquareException(String msg) {
        super(msg);
    }
}

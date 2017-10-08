import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PgnReader {

    /**
     * Find the tagName tag pair in a PGN game and return its value.
     *
     * @see http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm
     *
     * @param tagName the name of the tag whose value you want
     * @param game a `String` containing the PGN text of a chess game
     * @return the value in the named tag pair
     */
    public static String tagValue(String tagName, String game) {
        Pattern tagPattern = Pattern.compile("(\\[\\s*[^\\]]+\\s*\\])");
        Matcher tagMatcher = tagPattern.matcher(game);
        while (tagMatcher.find()) {
            String currentTagNamePatternString = "(?<=(\\[))\\s*[\\S]+(?=( ?))";
            Pattern currentTagNamePattern =
                Pattern.compile(currentTagNamePatternString);
            Matcher currentTagNameMatcher =
                currentTagNamePattern.matcher(tagMatcher.group());
            if (currentTagNameMatcher.find()) {
                if (tagName.equalsIgnoreCase(currentTagNameMatcher.group())) {
                    String[] tagParts = tagMatcher.group()
                        .split(currentTagNamePatternString);
                    String valuePart = "";
                    for (int i = 1; i < tagParts.length; i++) {
                        valuePart += tagParts[i];
                    }
                    Pattern currentTagValuePattern =
                        Pattern.compile("(?<=(\"?))[^\"]+(?=(\"?\\]))");
                    Matcher currentTagValueMatcher =
                        currentTagValuePattern.matcher(valuePart);
                    currentTagValueMatcher.find();
                    return currentTagValueMatcher.group();
                }
            }
        }
        return "NOT GIVEN";
    }

    private static int arrayLength(char[][] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i][0] == (char) 0) {
                return i;
            }
        }

        return 0;
    }

    private static void arrayPush(char[][] array, char[] item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i][0] == (char) 0) {
                array[i] = item;
                return;
            }
        }
    }

    private static void arrayDelete(char[][] array, char[] item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item) {
                array[i] = new char[array[i].length];
                arrayRepair(array);
                return;
            }
        }

//        System.out.println("WARNING: nothing deleted");
    }

    private static void arrayRepair(char[][] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i][0] == (char) 0) {
                for (int j = i + 1; j < array.length; j++) {
                    if (array[j][0] != (char) 0) {
                        array[i] = array[j];
                        array[j] = new char[array[i].length];
                        break;
                    }
                }
                if (array[i] == null) {
                    return;
                }
            }
        }
    }

    private static String arrayToString(char[][] array) {
        String s = new String();
        for (int i = 0; i < array.length; i++) {
            if (array[i][0] == (char) 0) {
                return s;
            }

            s += Arrays.toString(array[i]);
        }

        return s;
    }

    private static char[][][] newGameBoard(boolean white) {
        char[][] gameBoard = new char[16][3];
        for (int i = 0; i < gameBoard.length; i++) {
            char[] piece = new char[3];
            if (white) {
                piece[2] = '1';
            } else {
                piece[2] = '8';
            }

            piece[1] = (char) ((i % 8) + 97);

            switch (i) {
            case 0:
            case 7:
                piece[0] = 'R';
                break;
            case 1:
            case 6:
                piece[0] = 'N';
                break;
            case 2:
            case 5:
                piece[0] = 'B';
                break;
            case 3:
                piece[0] = 'Q';
                break;
            case 4:
                piece[0] = 'K';
                break;
            default:
                piece[0] = 'p';
                if (white) {
                    piece[2] = '2';
                } else {
                    piece[2] = '7';
                }
                break;
            }

            gameBoard[i] = piece;
        }

        char[][][] gamePieces = new char[6][10][3];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (gameBoard[j][0] == PIECE_TYPES[i]) {
                    arrayPush(gamePieces[i], gameBoard[j]);
                }
            }
        }

        return gamePieces;
    }

    private static final int FILE = 1;
    private static final int RANK = 2;
    private static final int ROOK   = 0;
    private static final int KNIGHT = 1;
    private static final int BISHOP = 2;
    private static final int KING   = 3;
    private static final int QUEEN  = 4;
    private static final int PAWN   = 5;
    private static final char[] PIECE_TYPES = {'R', 'N', 'B', 'K', 'Q', 'p'};
    private static char[][][][] pieces = {newGameBoard(true),
        newGameBoard(false)};

    private static char[] findPieceAt(char[][][] gameBoard,
        char file, char rank) {
        for (int type = 0; type < gameBoard.length; type++) {
            for (int piece = 0; piece < gameBoard[type].length; piece++) {
                if (gameBoard[type][piece][RANK] != rank) {
                    continue;
                }
                if (gameBoard[type][piece][FILE] != file) {
                    continue;
                }
                return gameBoard[type][piece];
            }
        }

//        System.out.println("ERROR: no such piece found in gameBoard at "
//           + file + " " + rank);
//        for (int j = 0; j < pieces.length; j++) {
//            for (int i = 0; i < pieces[j].length; i++) {
//                System.out.println(PIECE_TYPES[i]
//                   + arrayToString(pieces[j][i]));
//            }
  //          System.out.println();
//        }
        return new char[0];
    }

    /**
     * Play out the moves in game and return a String with the game's
     * final position in Forsyth-Edwards Notation (FEN).
     *
     * @see http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1
     *
     * @param game a `Strring` containing a PGN-formatted chess game or opening
     * @return the game's final position in FEN.
     */
    public static String finalPosition(String game)  {
        Pattern moveNumberPattern =
            Pattern.compile("([0-9]+\\.\\s*)(?=.*[a-zA-Z])([A-Za-z0-9\\-\\+\\#"
            + "\\=]+)(?=\\s*)\\s*((?=.*[a-zA-Z])([A-Za-z0-9\\-\\+\\#\\=]+))?");
        Matcher moveNumberMatcher = moveNumberPattern.matcher(game);
        while (moveNumberMatcher.find()) {
            String[] currentMove = {moveNumberMatcher.group(2),
                moveNumberMatcher.group(4)};
            String movePatternString = "";
//            System.out.println(currentMove[0] + " " + currentMove[1]);
            for (int color = 0; color < currentMove.length; color++) {
                int enemyColor = color == 0 ? 1 : 0;
//                System.out.print((color == 0 ? "WHITE" : "BLACK") + ": ");
                if (currentMove[color] == null) {
                    continue;
                }

                char pieceType, targetRank, targetFile;
                boolean capture, castle = false;

                Pattern moveDescriptionPattern =
                    Pattern.compile("([KNBKQa-h])(x)?([a-z])?([0-9])?");
                Matcher moveDescriptionMatcher =
                    moveDescriptionPattern.matcher(currentMove[color]);
                if (moveDescriptionMatcher.find()) {
                    pieceType = moveDescriptionMatcher.group(1).charAt(0);
                    targetRank = moveDescriptionMatcher.group(4).charAt(0);
                    targetFile = moveDescriptionMatcher.group(3) != null
                        ? moveDescriptionMatcher.group(3).charAt(0)
                        : pieceType;
                        // if it's a pawn then pieceType is the same
                    capture = moveDescriptionMatcher.group(2) != null
                        ? moveDescriptionMatcher.group(2).charAt(0) == 'x'
                        : false;
                } else {
                    // check for castle
                    moveDescriptionPattern =
                        Pattern.compile("O\\s*\\-\\s*O(\\s*\\-\\s*O)?");
                    moveDescriptionMatcher =
                        moveDescriptionPattern.matcher(currentMove[color]);
                    if (!moveDescriptionMatcher.find()) {
//                        System.out.println(
//                           "ERROR: regex FAILED again (no castle): "
//                          + Arrays.toString(currentMove));
                        continue;
                    }

                    castle = true;
                    pieceType = 'K';
                    targetRank = color == 0 ? '1' : '8';
                    capture = false;
                    if (moveDescriptionMatcher.group(1) == null) {
                        // then this was a (O-O) king side castle
                        targetFile = 'g';
                    } else {
                        targetFile = 'c';
                    }

                }

        //        System.out.println("type: " + pieceType + " file: "
    //                + targetFile + " rank: " + targetRank + " capture: "
//                    + capture);

                /***********************************
                * PAWNS
                ***********************************/
                if (Character.isLowerCase(pieceType)) {
                    char[][] pawns      = pieces[color][5];
                    char[][] enemyPawns = pieces[enemyColor][5];
                    // we're dealing with a pawn piece
                    for (int j = 0; j < pawns.length; j++) {
                        if (pawns[j][FILE] == pieceType) {
                            char[] currentPiece = pawns[j];

                            if (currentMove[color].length() == 2) {
                                /*
                                * then this is a single move by a pawn
                                */
                                int distanceFromPieceToMove = Math.abs(
                                    targetRank - currentPiece[RANK]);
//                                System.out.println(distanceFromPieceToMove);
                                if (distanceFromPieceToMove <= 1
                                    || ((currentPiece[RANK] == '2'
                                        || currentPiece[RANK] == '7')
                                    && distanceFromPieceToMove <= 2)) {
                                    // if this pawn is only one space away,
                                    // or this pawn is on its first move,
                                    // and the move is two spaces away,
                                    // then this is the only pawn that can
                                    // move to that space
       //                             System.out.println("PAWN move");
                                    currentPiece[RANK] = targetRank;
//                                } else {
//                                    System.out.println("ERROR: illegal move "
//                                       + Arrays.toString(currentPiece));
                                }
                            } else if (capture) {
                                int distanceFromPieceToMove = Math.abs(
                                    (int) targetRank
                                        - (int) currentPiece[RANK]);
                                if (distanceFromPieceToMove > 1) {
                                    continue;
                                }

      //                          System.out.print("PAWN take ");
                                currentPiece[FILE] = targetFile;
                                currentPiece[RANK] = targetRank;
                                char[] takenPiece = findPieceAt(
                                    pieces[enemyColor], targetFile, targetRank);
                                // check for en passant
                                if (takenPiece.length == 0) {
                                    // pawn has moved to empty space diagonally
                                    char enPassantRank = (char)
                                        (targetRank - (color == 0 ? 1 : (-1)));
                                    takenPiece = findPieceAt(pieces[enemyColor],
                                        targetFile,  enPassantRank);

                                }
                                int typeIndex = new String(PIECE_TYPES)
                                    .indexOf(takenPiece[0]);
                                arrayDelete(pieces[enemyColor][typeIndex],
                                    takenPiece);
                            } else {
                                // dont check for promotion if nothing else
                                // worked; we wouldn't know what piece to promo
                                continue;
                            }


                            Pattern pawnPromotionPattern =
                                Pattern.compile("(?<==)\\S");
                            Matcher pawnPromotionMatcher =
                                pawnPromotionPattern.matcher(
                                    currentMove[color]);
                            if (pawnPromotionMatcher.find()) {
//                           System.out.println("PAWN promotion " +
//                                Arrays.toString(currentPiece));
                                String newType = pawnPromotionMatcher.group();
                                int typeIndex = new String(PIECE_TYPES)
                                    .indexOf(newType);
                                currentPiece[0] = newType.charAt(0);
                                arrayPush(pieces[color][typeIndex],
                                    currentPiece);
                                arrayDelete(pawns, currentPiece);
                            }

                            break;


                        }

//                       if (j == pawns.length - 1) {
//                            System.out.println("ERROR: no such pawn! "
//                              + Arrays.toString(pawns[j]));
//                        }
                    }
                } else {
                /***********************************
                * NON PAWNS
                ***********************************/

                    switch (pieceType) {
                    case 'B':
    //                    System.out.println("BISHOP move");
                        char[][] bishops = pieces[color][BISHOP];
                        for (int i = 0; i < bishops.length; i++) {
                            int changeInFile = Math.abs(targetFile
                                - bishops[i][FILE]);
                            int changeInRank = Math.abs(targetRank
                                - bishops[i][RANK]);
                            if (changeInFile != changeInRank) {
                                continue;
                            }

                            bishops[i][FILE] = targetFile;
                            bishops[i][RANK] = targetRank;

                            if (capture) {
                                char[] takenPiece = findPieceAt(
                                    pieces[enemyColor], targetFile, targetRank);
                                int typeIndex = new String(PIECE_TYPES)
                                    .indexOf(takenPiece[0]);
                                arrayDelete(pieces[enemyColor][typeIndex],
                                    takenPiece);
                            }

//                            if (i == bishops.length - 1) {
//                               System.out.println("ERROR: no such bishop! "
//                                  + Arrays.toString(bishops[i]));
//                         }
                        }
                        break;
                    case 'R':
   //                     System.out.println("ROOK move");
                        char[][] rooks = pieces[color][ROOK];
                        for (int i = 0; i < rooks.length; i++) {
                            if (rooks[i][FILE] == targetFile) {
                                int distanceFromPieceToMove = Math.abs(
                                    targetFile - rooks[i][FILE]);
                                boolean blockingPieces = false;
                                for (char j = 0; j < distanceFromPieceToMove;
                                    j++) {
                                    char[] takenPiece = findPieceAt(
                                        pieces[enemyColor],
                                        targetFile,
                                        (char) (rooks[i][RANK] + j));
                                    if (takenPiece != null) {
                                        // this rook cannot make the move
                                        blockingPieces = true;
                                        break;
                                    }
                                }

                                if (!blockingPieces) {
                                    rooks[i][FILE] = targetFile;
                                    rooks[i][RANK] = targetRank;
                                    break;

                                }
                            } else if (rooks[i][RANK] == targetRank) {
                                int distanceFromPieceToMove = Math.abs(
                                    targetRank - rooks[i][RANK]);
                                boolean blockingPieces = false;
                                for (char j = 0; j < distanceFromPieceToMove;
                                    j++) {
                                    char[] takenPiece = findPieceAt(
                                        pieces[enemyColor], targetFile,
                                        (char) (rooks[i][FILE] + j));
                                    if (takenPiece != null) {
                                        // this rook cannot make the move
                                        blockingPieces = true;
                                        break;
                                    }
                                }

                                if (!blockingPieces) {
                                    rooks[i][FILE] = targetFile;
                                    rooks[i][RANK] = targetRank;
                                    break;

                                }
                            } else {
                                continue;
                            }

                            if (capture) {
                                char[] takenPiece = findPieceAt(
                                    pieces[enemyColor], targetFile, targetRank);
                                int typeIndex = new String(PIECE_TYPES)
                                    .indexOf(takenPiece[0]);
                                arrayDelete(pieces[enemyColor][typeIndex],
                                    takenPiece);
                            }

//                            if (i == rooks.length - 1) {
//                                System.out.println("ERROR: no such rook! "
//                                    + Arrays.toString(rooks[i]));
//                            }
                        }
                        break;
                    case 'N':
  //                      System.out.println("KNIGHT move");
                        char[][] knights = pieces[color][KNIGHT];
                        for (int i = 0; i < knights.length; i++) {
                            int changeInFile = Math.abs(targetFile
                                - knights[i][FILE]);
                            int changeInRank = Math.abs(targetRank
                                - knights[i][RANK]);
                            if (!((changeInFile == 1 && changeInRank == 2)
                                || (changeInFile == 2 && changeInRank == 1))) {
                                continue;
                            }

                            knights[i][FILE] = targetFile;
                            knights[i][RANK] = targetRank;

                            if (capture) {
                                char[] takenPiece = findPieceAt(
                                    pieces[enemyColor], targetFile, targetRank);
                                int typeIndex = new String(PIECE_TYPES)
                                    .indexOf(takenPiece[0]);
                                arrayDelete(pieces[enemyColor][typeIndex],
                                    takenPiece);
                            }
                        }
                        break;
                    case 'Q':
 //                       System.out.println("QUEEN move");
                        char[][] queens = pieces[color][QUEEN];
                        for (int i = 0; i < queens.length; i++) {
                            if (queens[i][0] == (char) 0) {
                                continue;
                            }

                            queens[i][FILE] = targetFile;
                            queens[i][RANK] = targetRank;

                            if (capture) {
                                char[] takenPiece = findPieceAt(
                                    pieces[enemyColor], targetFile, targetRank);
                                int typeIndex = new String(PIECE_TYPES)
                                    .indexOf(takenPiece[0]);
                                arrayDelete(pieces[enemyColor][typeIndex],
                                    takenPiece);
                            }
                        }
                        break;
                    case 'K':
//                        System.out.println("KING move");
                        char[][] kings = pieces[color][KING];
                        for (int i = 0; i < kings.length; i++) {
                            if (kings[i][0] == (char) 0) {
                                continue;
                            }
                            kings[i][FILE] = targetFile;
                            kings[i][RANK] = targetRank;

                            if (capture) {
                                char[] takenPiece = findPieceAt(
                                    pieces[enemyColor], targetFile, targetRank);
                                int typeIndex = new String(PIECE_TYPES)
                                    .indexOf(takenPiece[0]);
                                arrayDelete(pieces[enemyColor][typeIndex],
                                    takenPiece);
                            }

                            if (castle) {
                                boolean kingSide =  targetFile == 'g';
                                char[] castlingRook = findPieceAt(pieces[color],
                                    kingSide ? 'h' : 'a', targetRank);
                                castlingRook[FILE] = kingSide ? 'f' : 'd';
                            }
                        }
                        break;
                    default:
                    }

                }
            }
        }

        char[][][] gameBoard = new char[8][8][4];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                for (int k = 0; k < pieces[i][j].length; k++) {
                    if (pieces[i][j][k][0] == (char) 0) {
                        continue;
                    }
                    int rank = pieces[i][j][k][RANK] - '1';
                    int file = pieces[i][j][k][FILE] - 'a';
                    gameBoard[rank][file] =
                        (new String(pieces[i][j][k])
                        + (i == 0 ? "W" : "B")).toCharArray();
                }
            }
        }

        String fen = new String();
        for (int i = gameBoard.length - 1; i >= 0; i--) {
            int empty = 0;
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j][0] == (char) 0) {
                    empty += 1;
                    if (j == gameBoard.length - 1) {
                        fen += empty;
                    }
                } else {
                    if (empty > 0) {
                        fen += empty;
                        empty = 0;
                    }
                    char pieceType = gameBoard[i][j][0];
                    fen += gameBoard[i][j][3] == 'W'
                        ? Character.toUpperCase(pieceType)
                        : Character.toLowerCase(pieceType);
                }
            }

            if (i != 0) {
                fen += "/";
            }
        }

        return fen;
    }

    /**
     * Reads the file named by path and returns its content as a String.
     *
     * @param path the relative or abolute path of the file to read
     * @return a String containing the content of the file
     */
    public static String fileContent(String path) {
        Path file = Paths.get(path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Add the \n that's removed by readline()
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.exit(1);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String game = fileContent(args[0]);
        System.out.format("Event: %s%n", tagValue("Event", game));
        System.out.format("Site: %s%n", tagValue("Site", game));
        System.out.format("Date: %s%n", tagValue("Date", game));
        System.out.format("Round: %s%n", tagValue("Round", game));
        System.out.format("White: %s%n", tagValue("White", game));
        System.out.format("Black: %s%n", tagValue("Black", game));
        System.out.format("Result: %s%n", tagValue("Result", game));
        System.out.println("Final Position:");
        System.out.println(finalPosition(game));

//        for (int j = 0; j < pieces.length; j++) {
//            for (int i = 0; i < pieces[j].length; i++) {
//                System.out.println(PIECE_TYPES[i]
//                    + arrayToString(pieces[j][i]));
//            }
//            System.out.println();
//        }
    }
}

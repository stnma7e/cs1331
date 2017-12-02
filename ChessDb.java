import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * Holds all the metadata for PGN chess games to be used by ChessGui.
 *
 * @author sdelmerico3
 * @version 0.0.1
 */
public class ChessDb {

    private List<ChessGame> games;

    /**
     * Creates a ChessDb with two default games: Morphy Isouard and Tal Fischer.
     * Then it searches all subfolders of its current working directory to fin
     * PGN files, and it adds those to its game lists.
     */
    public ChessDb() {
        games = new ArrayList<>();
        games.add(morphyIsouard());
        games.add(talFischer());

        ArrayList<Path> files = new ArrayList();
        ArrayList<String> fileData = new ArrayList();
        try {
            Stream<Path> paths = Files.walk(Paths.get("."));
            paths.filter(Files::isRegularFile)
                 .filter(path -> {
                         String extension = "";
                         String fileName = path.toString();

                         int i = fileName.lastIndexOf('.');
                         if (i > 0) {
                             extension = fileName.substring(i + 1);
                         }

                         if (extension.equals("pgn")) {
                             return true;
                         }

                         return false;
                     }
                 ).forEach(path -> {
                         files.add(path);
                         System.out.println(path);
                     }
                );

            for (Path path : files) {
                byte[] encoded = Files.readAllBytes(Paths.get(path.toString()));
                fileData.add(new String(encoded, StandardCharsets.UTF_8));
            }
        } catch (Exception err) {
            String fuckCheckstyle = "";
        }

        PgnReader pgnReader = new PgnReader();
        for (String game : fileData) {
            ChessGame newGame = new ChessGame(
                pgnReader.tagValue("Event", game),
                pgnReader.tagValue("Site", game),
                pgnReader.tagValue("Date", game),
                pgnReader.tagValue("White", game),
                pgnReader.tagValue("Black", game),
                pgnReader.tagValue("Result", game)
            );

            Pattern moveNumberPattern =
                Pattern.compile("([0-9]+\\.\\s*)(?=.*[a-zA-Z])"
                + "([A-Za-z0-9\\-\\+\\#"
                + "\\=]+)(?=\\s*)\\s*((?=.*[a-zA-Z])"
                + "([A-Za-z0-9\\-\\+\\#\\=]+))?");
            Matcher moveNumberMatcher = moveNumberPattern.matcher(game);
            while (moveNumberMatcher.find()) {
                String[] currentMove = {moveNumberMatcher.group(2),
                    moveNumberMatcher.group(4)};
                newGame.addMove(currentMove[0] + " " + currentMove[1]);
            }
            games.add(newGame);
        }
    }

    /**
     * @return a list of all the games the ChessDb has metadata for.
     */
    public List<ChessGame> getGames() {
        return games;
    }

    private ChessGame morphyIsouard() {
        ChessGame game = new ChessGame(
            "A Night at the Opera",
            "Paris Opera House",
            "1958.01.01",
            "Morphy, Paul",
            "Comte Isouard de Vauvenargues and Karl II, Duke of Brunswick",
            "1-0"
        );
        game.addMove("e4 e5");
        game.addMove("Nf3 d6");
        game.addMove("d4 Bg4");
        game.addMove("dxe5 Bxf3");
        game.addMove("Qxf3 dxe5");
        game.addMove("Bc4 Nf6");
        game.addMove("Qb3 Qe7");
        game.addMove("Nc3 c6");
        game.addMove("Bg5 b5");
        game.addMove("Nxb5 cxb5");
        game.addMove("Bxb5+ Nbd7");
        game.addMove("O-O-O Rd8");
        game.addMove("Rxd7 Rxd7");
        game.addMove("Rd1 Qe6");
        game.addMove("Bxd7+ Nxd7");
        game.addMove("Qb8+ Nxb8");
        game.addMove("Rd8#");
        return game;
    }

    private ChessGame talFischer() {
        ChessGame game = new ChessGame(
            "Bled-Zagreb-Belgrade Candidates",
            "Bled, Zagreb & Belgrade YUG",
            "1959.10.11",
            "Tal, Mikhail",
            "Fischer, Robert James",
            "1-0"
        );
        game.addMove("d4 Nf6");
        game.addMove("c4 g6");
        game.addMove("Nc3 Bg7");
        game.addMove("e4 d6");
        game.addMove("Be2 O-O");
        game.addMove("Nf3 e5");
        game.addMove("d5 Nbd7");
        game.addMove("Bg5 h6");
        game.addMove("Bh4 a6");
        game.addMove("O-O Qe8");
        game.addMove("Nd2 Nh7");
        game.addMove("b4 Bf6");
        game.addMove("Bxf6 Nhxf6");
        game.addMove("Nb3 Qe7");
        game.addMove("Qd2 Kh7");
        game.addMove("Qe3 Ng8");
        game.addMove("c5 f5");
        game.addMove("exf5 gxf5");
        game.addMove("f4 exf4");
        game.addMove("Qxf4 dxc5");
        game.addMove("Bd3 cxb4");
        game.addMove("Rae1 Qf6");
        game.addMove("Re6 Qxc3");
        game.addMove("Bxf5+ Rxf5");
        game.addMove("Qxf5+ Kh8");
        game.addMove("Rf3 Qb2");
        game.addMove("Re8 Nf6");
        game.addMove("Qxf6+ Qxf6");
        game.addMove("Rxf6 Kg7");
        game.addMove("Rff8 Ne7");
        game.addMove("Na5 h5");
        game.addMove("h4 Rb8");
        game.addMove("Nc4 b5");
        game.addMove("Ne5 1-0");
        return game;

    }
}

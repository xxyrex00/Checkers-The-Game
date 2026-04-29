import game.GameController;
import game.GameMode;
import game.GameState;
import move.Move;
import player.BotPlayer;
import pieces.Piece;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== International Checkers (10x10) ===\n");

        GameMode selectedMode = selectGameMode();

        GameController game = new GameController(selectedMode);
        game.startGame();

        if (selectedMode == GameMode.PVP) {
            runPlayerVsPlayer(game);
        } else {
            runPlayerVsBot(game);
        }

        System.out.println("\nFinal state: " + game.getGameState());
    }

    private static GameMode selectGameMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select Game Mode:");
        System.out.println("  1. Player vs Player");
        System.out.println("  2. Player vs Bot");
        System.out.print("Enter your choice (1 or 2): ");

        while (true) {
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    System.out.println("\nMode selected: Player vs Player\n");
                    return GameMode.PVP;
                case "2":
                    System.out.println("\nMode selected: Player vs Bot\n");
                    return GameMode.PVE;
                default:
                    System.out.print("Invalid choice. Please enter 1 or 2: ");
            }
        }
    }

    private static void runPlayerVsPlayer(GameController game) {
        Scanner scanner = new Scanner(System.in);

        while (game.getGameState() == GameState.ONGOING) {
            if (game.getForcedPiece() != null) {
                System.out.println("*** Forced continuation: you must capture again with the piece at " +
                        game.getForcedPiece().getPosition() + " ***");
            }
            System.out.println("\n" + game.getCurrentPlayer().getName() + " (" +
                    game.getCurrentPlayer().getColor() + ") – enter your move (e.g. 5,0-4,1): ");

            String line = scanner.nextLine().trim();
            Move move = parseMove(line);
            if (move == null) {
                System.out.println("Could not parse move. Use format: row,col-row,col");
                continue;
            }
            game.applyMove(move);
        }
    }

    private static void runPlayerVsBot(GameController game) {
        Scanner scanner = new Scanner(System.in);
        BotPlayer bot = (BotPlayer) game.getPlayerBlack(); // reuse the bot from GameController

        while (game.getGameState() == GameState.ONGOING) {
            if (game.getCurrentPlayer().getColor().equals("BLACK")) {
                // Bot's turn
                Move move = null;
                Piece forcedPiece = game.getForcedPiece();
                if (forcedPiece != null) {
                    List<Move> forcedCaps = bot.getMoveGenerator().getCaptureMovesForPiece(game.getBoard(), forcedPiece);
                    if (!forcedCaps.isEmpty()) {
                        move = forcedCaps.get(bot.getRandom().nextInt(forcedCaps.size()));
                        System.out.println("Bot forced capture with piece at " + forcedPiece.getPosition());
                    }
                } else {
                    move = bot.makeMove(game.getBoard());
                }

                if (move == null) {
                    System.out.println("Bot has no moves. Game over.");
                    break;
                }
                System.out.println("\nBot (BLACK) plays: " + move);
                game.applyMove(move);
            } else {
                if (game.getForcedPiece() != null) {
                    System.out.println("*** Forced continuation: you must capture again with the piece at " +
                            game.getForcedPiece().getPosition() + " ***");
                }
                System.out.println("\nPlayer 1 (WHITE) – enter your move (e.g. 5,0-4,1): ");
                String line = scanner.nextLine().trim();
                Move move = parseMove(line);
                if (move == null) {
                    System.out.println("Could not parse move. Use format: row,col-row,col");
                    continue;
                }
                game.applyMove(move);
            }
        }
    }

    private static Move parseMove(String input) {
        try {
            String[] parts = input.split("-");
            if (parts.length != 2) return null;
            String[] from = parts[0].split(",");
            String[] to = parts[1].split(",");
            if (from.length != 2 || to.length != 2) return null;
            int fromRow = Integer.parseInt(from[0].trim());
            int fromCol = Integer.parseInt(from[1].trim());
            int toRow = Integer.parseInt(to[0].trim());
            int toCol = Integer.parseInt(to[1].trim());
            board.Position start = new board.Position(fromRow, fromCol);
            board.Position end = new board.Position(toRow, toCol);
            return new move.Move(start, end);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
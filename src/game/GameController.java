package game;

import board.Board;
import board.Position;
import board.Tile;
import move.Move;
import pieces.King;
import pieces.Man;
import pieces.Piece;
import player.Player;
import player.HumanPlayer;
import player.BotPlayer;

public class GameController {

    private Board board;
    private Player currentPlayer;
    private Player playerWhite;
    private Player playerBlack;
    private GameState gameState;
    private GameMode gameMode;
    private RuleEngine ruleEngine;
    private MoveValidator moveValidator;

    private Piece forcedPiece;

    public GameController(GameMode gameMode) {
        this.gameMode = gameMode;
        this.ruleEngine = new RuleEngine();
        this.moveValidator = new MoveValidator(ruleEngine);
        this.board = new Board();
        this.gameState = GameState.ONGOING;
        this.forcedPiece = null;

        playerWhite = new HumanPlayer("Player 1", "WHITE");
        if (gameMode == GameMode.PVP) {
            playerBlack = new HumanPlayer("Player 2", "BLACK");
        } else {
            playerBlack = new BotPlayer("Bot", "BLACK");
        }
        currentPlayer = playerWhite;
    }

    public void startGame() {
        gameState = GameState.ONGOING;
        System.out.println("Game started! Mode: " + gameMode);
        board.printBoard();
    }

    public void switchTurn() {
        currentPlayer = (currentPlayer == playerWhite) ? playerBlack : playerWhite;
        forcedPiece = null;
    }

    public void applyMove(Move attemptedMove) {
        if (forcedPiece != null && !attemptedMove.getStart().equals(forcedPiece.getPosition())) {
            System.out.println("Invalid: You must continue capturing with the same piece at " + forcedPiece.getPosition());
            return;
        }

        Move fullMove = moveValidator.validateMove(board, attemptedMove, currentPlayer.getColor());
        if (fullMove == null) {
            System.out.println("Invalid move: " + attemptedMove);
            return;
        }

        Piece movingPiece = board.getTile(fullMove.getStart()).getPiece();
        board.movePiece(fullMove);

        Piece pieceAtEnd = board.getTile(fullMove.getEnd()).getPiece();
        pieceAtEnd = promoteIfEligible(pieceAtEnd);

        board.printBoard();

        if (ruleEngine.checkWinCondition(board, currentPlayer.getColor())) {
            gameState = currentPlayer.getColor().equals("WHITE") ? GameState.WHITE_WIN : GameState.BLACK_WIN;
            System.out.println(currentPlayer.getName() + " wins!");
            return;
        }

        if (fullMove.isCaptureMove() && ruleEngine.hasMoreCaptures(board, pieceAtEnd)) {
            forcedPiece = pieceAtEnd;
            System.out.println("Multi-capture! " + currentPlayer.getName() + " must continue capturing with same piece.");
            return;
        }

        forcedPiece = null;
        switchTurn();
    }

    private Piece promoteIfEligible(Piece piece) {
        if (piece instanceof Man) {
            Man man = (Man) piece;
            if (man.shouldPromote()) {
                Position pos = man.getPosition();
                King king = new King(man.getColor(), pos);
                board.getTile(pos).setPiece(king);
                System.out.println("Promoted to King at " + pos);
                return king;
            }
        }
        return piece;
    }

    public void resetGame() {
        this.board = new Board();
        this.gameState = GameState.ONGOING;
        currentPlayer = playerWhite;
        forcedPiece = null;
        System.out.println("Game reset.");
    }

    // Getters
    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public GameState getGameState() { return gameState; }
    public GameMode getGameMode() { return gameMode; }
    public RuleEngine getRuleEngine() { return ruleEngine; }
    public MoveValidator getMoveValidator() { return moveValidator; }
    public Piece getForcedPiece() { return forcedPiece; }
    public Player getPlayerBlack() { return playerBlack; }   // added for Main to reuse bot
}
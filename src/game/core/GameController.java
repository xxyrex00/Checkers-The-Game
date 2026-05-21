package game.core;

import board.Board;
import board.Position;
import game.GameMode;
import game.GameState;
import game.rules.MoveValidator;
import game.rules.RuleEngine;
import move.Move;
import pieces.Color;
import pieces.King;
import pieces.Man;
import pieces.Piece;
import player.Player;
import player.BotPlayer;

public class GameController {

    private Board board;
    private Player currentPlayer;
    private Player playerWhite;
    private Player playerBlack;
    private GameState gameState;
    private RuleEngine ruleEngine;
    private MoveValidator moveValidator;

    private Piece forcedPiece;

    public GameController(GameMode gameMode) {
        this.ruleEngine = new RuleEngine();
        this.moveValidator = new MoveValidator(ruleEngine);
        this.board = new Board();
        this.gameState = GameState.ONGOING;
        this.forcedPiece = null;

        playerWhite = new Player("Player 1", Color.WHITE);
        if (gameMode == GameMode.PVP) {
            playerBlack = new Player("Player 2", Color.BLACK);
        } else {
            playerBlack = new BotPlayer("Bot", Color.BLACK, ruleEngine);
        }
        currentPlayer = playerWhite;
    }

    public void startGame() {
        gameState = GameState.ONGOING;
    }

    public void switchTurn() {
        currentPlayer = (currentPlayer == playerWhite) ? playerBlack : playerWhite;
        forcedPiece = null;
    }

    public void applyMove(Move attemptedMove) {
        if (attemptedMove == null) {
            return;
        }
        if (forcedPiece != null && !attemptedMove.getStart().equals(forcedPiece.getPosition())) {
            return;
        }

        Move fullMove = moveValidator.validateMove(board, attemptedMove, currentPlayer.getColor());
        if (fullMove == null) {
            return;
        }

        board.movePiece(fullMove);
        Piece pieceAtEnd = board.getTile(fullMove.getEnd()).getPiece();

        pieceAtEnd = promoteIfEligible(pieceAtEnd);

        if (fullMove.isCaptureMove() && ruleEngine.hasMoreCaptures(board, pieceAtEnd)) {
            forcedPiece = pieceAtEnd;
            return;
        }

        if (ruleEngine.checkWinCondition(board, currentPlayer.getColor())) {
            gameState = currentPlayer.getColor() == Color.WHITE ? GameState.WHITE_WIN : GameState.BLACK_WIN;
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
    }

    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public GameState getGameState() { return gameState; }
    public RuleEngine getRuleEngine() { return ruleEngine; }
    public Piece getForcedPiece() { return forcedPiece; }
    public Player getPlayerBlack() { return playerBlack; }
}

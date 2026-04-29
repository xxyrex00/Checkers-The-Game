package game;

import board.Board;
import move.Move;
import move.MoveGenerator;
import pieces.Piece;

import java.util.List;

public class RuleEngine {

    private MoveGenerator moveGenerator;

    public RuleEngine() {
        this.moveGenerator = new MoveGenerator();
    }

    public List<Move> getValidMoves(Board board, String playerColor) {
        List<Move> captures = moveGenerator.getCaptureMoves(board, playerColor);
        if (!captures.isEmpty()) {
            return captures;
        }
        return moveGenerator.getAllMoves(board, playerColor);
    }

    public boolean hasMoreCaptures(Board board, Piece piece) {
        List<Move> moreCaps = moveGenerator.getCaptureMovesForPiece(board, piece);
        return !moreCaps.isEmpty();
    }

    public boolean checkWinCondition(Board board, String playerColor) {
        List<Piece> opponentPieces = board.getPiecesOf(getOpponentColor(playerColor));
        if (opponentPieces.isEmpty()) return true;

        List<Move> opponentMoves = getValidMoves(board, getOpponentColor(playerColor));
        return opponentMoves.isEmpty();
    }

    public String getOpponentColor(String color) {
        return color.equals("WHITE") ? "BLACK" : "WHITE";
    }
}
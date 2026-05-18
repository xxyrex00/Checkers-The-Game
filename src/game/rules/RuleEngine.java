package game.rules;

import board.Board;
import move.Move;
import move.MoveGenerator;
import pieces.Color;
import pieces.Piece;

import java.util.List;

public class RuleEngine {

    private MoveGenerator moveGenerator;

    public RuleEngine() {
        this.moveGenerator = new MoveGenerator();
    }

    public List<Move> getValidMoves(Board board, Color playerColor) {
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

    public boolean checkWinCondition(Board board, Color currentPlayerColor) {
        Color opponentColor = currentPlayerColor.opponent();
        List<Piece> opponentPieces = board.getPiecesOf(opponentColor);
        if (opponentPieces.isEmpty()) return true;

        List<Move> opponentMoves = getValidMoves(board, opponentColor);
        return opponentMoves.isEmpty();
    }

    public Color getOpponentColor(Color color) {
        return color.opponent();
    }
}

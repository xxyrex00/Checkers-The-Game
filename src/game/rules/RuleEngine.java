package game.rules;

import board.Board;
import board.Position;
import move.Move;
import move.MoveGenerator;
import pieces.Color;
import pieces.Piece;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public boolean hasMoreCaptures(Board board, Piece piece, List<Position> alreadyCaptured) {
        Set<Position> excluded = new HashSet<>(alreadyCaptured);
        List<Move> moreCaps = piece.getCaptureMoves(board, excluded);
        return !moreCaps.isEmpty();
    }

    public boolean hasMoreCaptures(Board board, Piece piece) {
        return hasMoreCaptures(board, piece, Collections.emptyList());
    }

    public boolean checkWinCondition(Board board, Color currentPlayerColor) {
        Color opponentColor = currentPlayerColor.opponent();
        List<Piece> opponentPieces = board.getPiecesOf(opponentColor);
        if (opponentPieces.isEmpty()) return true;

        List<Move> opponentMoves = getValidMoves(board, opponentColor);
        return opponentMoves.isEmpty();
    }
}

package game;

import board.Board;
import move.Move;

import java.util.List;

public class MoveValidator {

    private RuleEngine ruleEngine;

    public MoveValidator(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    public Move validateMove(Board board, Move attemptedMove, String playerColor) {
        if (attemptedMove == null) return null;
        if (!board.isWithinBounds(attemptedMove.getStart()) || !board.isWithinBounds(attemptedMove.getEnd()))
            return null;

        if (!board.getTile(attemptedMove.getStart()).isOccupied()) return null;
        if (!board.getTile(attemptedMove.getStart()).getPiece().getColor().equals(playerColor))
            return null;

        List<Move> validMoves = ruleEngine.getValidMoves(board, playerColor);
        for (Move valid : validMoves) {
            if (valid.getStart().equals(attemptedMove.getStart()) && valid.getEnd().equals(attemptedMove.getEnd())) {
                return valid;
            }
        }
        return null;
    }
}
package game;

import java.util.List;

public class RuleEngine {

    // Method to get valid moves (enforcing rules)
    // - If capture moves exist → return only capture moves
    // - Else → return normal moves

    public List<Move> getValidMoves(Board board, String playerColor) {
        // TODO: use MoveGenerator
        return null;
    }

    // Method to enforce multi-capture
    // - After a capture, check if another capture is possible

    public boolean hasMoreCaptures(Board board, Piece piece) {
        // TODO: implement
        return false;
    }

    // Method to check win condition
    // - No pieces left OR no valid moves

    public boolean checkWinCondition(Board board, String playerColor) {
        // TODO: implement
        return false;
    }
}

package player;

import board.Board;
import move.Move;
import pieces.Color;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, Color color) {
        super(name, color);
    }

    @Override
    public Move makeMove(Board board) {
        // Input is handled by the GUI/console layer — should never be called directly
        throw new UnsupportedOperationException(
            "HumanPlayer.makeMove() should not be called directly; input is handled by the game loop.");
    }
}

package player;

import board.Board;
import move.Move;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, String color) {
        super(name, color);
    }

    @Override
    public Move makeMove(Board board) {
        // Input is handled by the GUI/console layer
        return null;
    }
}
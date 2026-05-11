package player;

import board.Board;
import move.Move;
import pieces.Color;

public abstract class Player {

    private String name;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public abstract Move makeMove(Board board);
}

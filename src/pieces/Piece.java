package pieces;

import board.Board;
import board.Position;
import move.Move;

import java.util.List;

public abstract class Piece {

    private Color color;
    private Position position;

    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // Returns all possible non-capture moves
    public abstract List<Move> getPossibleMoves(Board board);

    // Returns all capture moves
    public abstract List<Move> getCaptureMoves(Board board);
}

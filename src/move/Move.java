package move;

import board.Position;

import java.util.ArrayList;
import java.util.List;

public class Move {

    private Position start;
    private Position end;
    private List<Position> capturedPieces;

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
        this.capturedPieces = new ArrayList<>();
    }

    public Move(Position start, Position end, List<Position> capturedPieces) {
        this.start = start;
        this.end = end;
        this.capturedPieces = capturedPieces != null ? capturedPieces : new ArrayList<>();
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public List<Position> getCapturedPieces() {
        return capturedPieces;
    }

    public boolean isCaptureMove() {
        return !capturedPieces.isEmpty();
    }

    @Override
    public String toString() {
        return start + " -> " + end + (isCaptureMove() ? " [captures: " + capturedPieces + "]" : "");
    }
}
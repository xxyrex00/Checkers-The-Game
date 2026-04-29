package board;

import pieces.Piece;

public class Tile {

    private Position position;
    private Piece piece;
    private boolean isPlayable;

    public Tile(Position position, boolean isPlayable) {
        this.position = position;
        this.isPlayable = isPlayable;
        this.piece = null;
    }

    public boolean isOccupied() {
        return piece != null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isPlayable() {
        return isPlayable;
    }
}

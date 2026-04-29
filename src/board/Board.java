package board;

import pieces.Man;
import pieces.Piece;
import move.Move;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Tile[][] tiles;
    public static final int SIZE = 10;

    public Board() {
        tiles = new Tile[SIZE][SIZE];
        initializeTiles();
        placeInitialPieces();
    }

    // Initialize 10x10 board, mark playable (dark) squares
    private void initializeTiles() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Position pos = new Position(row, col);
                // Dark squares: sum of row+col is odd (international checkers convention)
                boolean isPlayable = (row + col) % 2 != 0;
                tiles[row][col] = new Tile(pos, isPlayable);
            }
        }
    }

    // Place initial pieces: BLACK on rows 0-3, WHITE on rows 6-9 (dark squares only)
    private void placeInitialPieces() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!tiles[row][col].isPlayable()) continue;
                if (row <= 3) {
                    Man man = new Man("BLACK", new Position(row, col));
                    tiles[row][col].setPiece(man);
                } else if (row >= 6) {
                    Man man = new Man("WHITE", new Position(row, col));
                    tiles[row][col].setPiece(man);
                }
            }
        }
    }

    public Tile getTile(Position pos) {
        return tiles[pos.getRow()][pos.getCol()];
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    // Move a piece from start to end using a Move object
    public void movePiece(Move move) {
        Position start = move.getStart();
        Position end = move.getEnd();

        Tile startTile = getTile(start);
        Tile endTile = getTile(end);

        Piece piece = startTile.getPiece();
        startTile.removePiece();
        piece.setPosition(end);
        endTile.setPiece(piece);

        // Remove captured pieces
        for (Position captured : move.getCapturedPieces()) {
            getTile(captured).removePiece();
        }
    }

    public boolean isWithinBounds(Position pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    // Return all pieces belonging to a given player color
    public List<Piece> getPiecesOf(String color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Tile tile = tiles[row][col];
                if (tile.isOccupied() && tile.getPiece().getColor().equals(color)) {
                    pieces.add(tile.getPiece());
                }
            }
        }
        return pieces;
    }

    // Print board to console for testing
    public void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int row = 0; row < SIZE; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < SIZE; col++) {
                Tile tile = tiles[row][col];
                if (!tile.isPlayable()) {
                    System.out.print(". ");
                } else if (!tile.isOccupied()) {
                    System.out.print("_ ");
                } else {
                    Piece p = tile.getPiece();
                    String symbol = p.getColor().equals("WHITE") ? "W" : "B";
                    if (p instanceof pieces.King) symbol = symbol.toLowerCase();
                    System.out.print(symbol + " ");
                }
            }
            System.out.println();
        }
    }
}

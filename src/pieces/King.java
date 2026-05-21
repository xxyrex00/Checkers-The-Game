package pieces;

import board.Board;
import board.Position;
import board.Tile;
import move.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] d : directions) {
            int row = getPosition().getRow() + d[0];
            int col = getPosition().getCol() + d[1];
            while (true) {
                Position dest = new Position(row, col);
                if (!board.isWithinBounds(dest)) break;
                Tile tile = board.getTile(dest);
                if (tile.isOccupied()) break; // blocked
                moves.add(new Move(getPosition(), dest));
                row += d[0];
                col += d[1];
            }
        }
        return moves;
    }

    @Override
    public List<Move> getCaptureMoves(Board board) {
        List<Move> captures = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] d : directions) {
            int row = getPosition().getRow() + d[0];
            int col = getPosition().getCol() + d[1];
            Position enemyPos = null;

            while (true) {
                Position curr = new Position(row, col);
                if (!board.isWithinBounds(curr)) break;
                Tile tile = board.getTile(curr);

                if (tile.isOccupied()) {
                    if (tile.getPiece().getColor() == getColor()) break;
                    if (enemyPos != null) break;
                    enemyPos = curr;
                } else if (enemyPos != null) {
                    List<Position> captured = new ArrayList<>();
                    captured.add(enemyPos);
                    captures.add(new Move(getPosition(), curr, captured));
                }
                row += d[0];
                col += d[1];
            }
        }
        return captures;
    }
}


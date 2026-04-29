package pieces;

import board.Board;
import board.Position;
import move.Move;

import java.util.ArrayList;
import java.util.List;

public class Man extends Piece {

    public Man(String color, Position position) {
        super(color, position);
    }

    // Forward diagonal movement (WHITE moves up = decreasing row, BLACK moves down = increasing row)
    // International rules allow backward captures, but forward-only normal moves
    @Override
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        int direction = getColor().equals("WHITE") ? -1 : 1;

        int[][] forwardDiagonals = {{direction, -1}, {direction, 1}};
        for (int[] d : forwardDiagonals) {
            Position dest = new Position(row + d[0], col + d[1]);
            if (board.isWithinBounds(dest) && !board.getTile(dest).isOccupied()) {
                moves.add(new Move(getPosition(), dest));
            }
        }
        return moves;
    }

    // International rules: captures allowed in all 4 diagonal directions
    @Override
    public List<Move> getCaptureMoves(Board board) {
        List<Move> captures = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        int[][] allDiagonals = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] d : allDiagonals) {
            Position enemyPos = new Position(row + d[0], col + d[1]);
            Position landPos = new Position(row + 2 * d[0], col + 2 * d[1]);

            if (!board.isWithinBounds(enemyPos) || !board.isWithinBounds(landPos)) continue;
            if (!board.getTile(enemyPos).isOccupied()) continue;
            if (board.getTile(enemyPos).getPiece().getColor().equals(getColor())) continue;
            if (board.getTile(landPos).isOccupied()) continue;

            List<Position> captured = new ArrayList<>();
            captured.add(enemyPos);
            captures.add(new Move(getPosition(), landPos, captured));
        }
        return captures;
    }

    // Check if this Man should be promoted (reaches the opposite back row)
    public boolean shouldPromote() {
        int row = getPosition().getRow();
        return (getColor().equals("WHITE") && row == 0) ||
               (getColor().equals("BLACK") && row == Board.SIZE - 1);
    }
}

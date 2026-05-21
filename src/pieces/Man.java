package pieces;

import board.Board;
import board.Position;
import move.Move;

import java.util.ArrayList;
import java.util.List;

public class Man extends Piece {

    public Man(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        int direction = getColor() == Color.WHITE ? -1 : 1;

        int[][] forwardDiagonals = {{direction, -1}, {direction, 1}};
        for (int[] d : forwardDiagonals) {
            Position dest = new Position(row + d[0], col + d[1]);
            if (board.isWithinBounds(dest) && !board.getTile(dest).isOccupied()) {
                moves.add(new Move(getPosition(), dest));
            }
        }
        return moves;
    }

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
            if (board.getTile(enemyPos).getPiece().getColor() == getColor()) continue;
            if (board.getTile(landPos).isOccupied()) continue;

            List<Position> captured = new ArrayList<>();
            captured.add(enemyPos);
            captures.add(new Move(getPosition(), landPos, captured));
        }
        return captures;
    }

    public boolean shouldPromote() {
        int row = getPosition().getRow();
        return (getColor() == Color.WHITE && row == 0) ||
               (getColor() == Color.BLACK && row == Board.SIZE - 1);
    }
}


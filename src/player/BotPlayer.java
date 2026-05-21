package player;

import board.Board;
import move.Move;
import move.MoveGenerator;
import pieces.Color;

import java.util.List;
import java.util.Random;

public class BotPlayer extends Player {

    private MoveGenerator moveGenerator;
    private Random random;

    public BotPlayer(String name, Color color) {
        super(name, color);
        this.moveGenerator = new MoveGenerator();
        this.random = new Random();
    }

    @Override
    public Move makeMove(Board board) {
        List<Move> captures = moveGenerator.getCaptureMoves(board, getColor());
        if (!captures.isEmpty()) {
            return pickRandom(captures);
        }

        List<Move> allMoves = moveGenerator.getAllMoves(board, getColor());
        if (!allMoves.isEmpty()) {
            return pickRandom(allMoves);
        }
        return null;
    }

    public Move makeForcedCapture(Board board, pieces.Piece forcedPiece) {
        List<Move> forcedCaps = forcedPiece.getCaptureMoves(board);
        if (!forcedCaps.isEmpty()) {
            return pickRandom(forcedCaps);
        }
        return null;
    }

    private Move pickRandom(List<Move> moves) {
        return moves.get(random.nextInt(moves.size()));
    }
}

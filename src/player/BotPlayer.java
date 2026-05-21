package player;

import board.Board;
import move.Move;
import game.rules.RuleEngine;
import pieces.Color;

import java.util.List;
import java.util.Random;

public class BotPlayer extends Player {

    private RuleEngine ruleEngine;
    private Random random;

    public BotPlayer(String name, Color color, RuleEngine ruleEngine) {
        super(name, color);
        this.ruleEngine = ruleEngine;
        this.random = new Random();
    }

    public Move makeMove(Board board) {
        List<Move> moves = ruleEngine.getValidMoves(board, getColor());
        if (!moves.isEmpty()) {
            return pickRandom(moves);
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

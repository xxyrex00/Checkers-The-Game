package move;

import board.Board;
import pieces.Color;
import pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    public List<Move> getAllMoves(Board board, Color playerColor) {
        List<Move> allMoves = new ArrayList<>();
        List<Piece> pieces = board.getPiecesOf(playerColor);
        for (Piece piece : pieces) {
            allMoves.addAll(piece.getPossibleMoves(board));
           
        }
        return allMoves;
    }

    public List<Move> getCaptureMoves(Board board, Color playerColor) {
        List<Move> captureMoves = new ArrayList<>();
        List<Piece> pieces = board.getPiecesOf(playerColor);
        for (Piece piece : pieces) {
            captureMoves.addAll(piece.getCaptureMoves(board));
        }
        return captureMoves;
    }

    public List<Move> getCaptureMovesForPiece(Board board, Piece piece) {
        return piece.getCaptureMoves(board);
    }
}
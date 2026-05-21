package gui.panels;

import board.Board;
import board.Position;
import board.Tile;
import gui.UIConstants;
import move.Move;
import pieces.King;
import pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {

    public static final int TILE_SIZE = 68;
    private static final int BOARD_PX = Board.SIZE * TILE_SIZE;

    private static final Color LIGHT_TILE        = UIConstants.TILE_LIGHT;
    private static final Color DARK_TILE         = UIConstants.TILE_DARK;
    private static final Color FORCED_RING       = UIConstants.RING_FORCED;
    private static final Color WHITE_PIECE_SHADE = UIConstants.PIECE_WHITE_SHADE;
    private static final Color BLACK_PIECE_SHADE = UIConstants.PIECE_BLACK_SHADE;
    private static final Color CROWN_COLOR       = UIConstants.PIECE_CROWN;

    private Board board;
    private Position selectedPos;
    private List<Move> validMovesForSelected = new ArrayList<>();
    private Position forcedPiecePos;
    private List<Position> lastMovePath = new ArrayList<>();

    private ClickListener clickListener;

    public interface ClickListener {
        void onTileClicked(Position pos);
    }

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(BOARD_PX, BOARD_PX));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    int col = e.getX() / TILE_SIZE;
                    int row = e.getY() / TILE_SIZE;
                    if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
                        clickListener.onTileClicked(new Position(row, col));
                    }
                }
            }
        });
    }

    public void setClickListener(ClickListener l) { this.clickListener = l; }

    public void setBoard(Board b) { this.board = b; }

    public void setSelectedPos(Position p) { this.selectedPos = p; }

    public void setValidMovesForSelected(List<Move> moves) {
        this.validMovesForSelected = moves != null ? moves : new ArrayList<>();
    }

    public void setForcedPiecePos(Position p) { this.forcedPiecePos = p; }

    public void setLastMovePath(Position from, Position to) {
        lastMovePath.clear();
        if (from != null) lastMovePath.add(from);
        if (to != null)   lastMovePath.add(to);
    }

    public void refresh() { repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTiles(g2);
        drawLastMoveHighlight(g2);
        drawSelectedHighlight(g2);
        drawValidMoveDots(g2);
        drawForcedRing(g2);
        drawPieces(g2);
        drawCoordinates(g2);
    }

    private void drawTiles(Graphics2D g2) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                boolean isDark = (row + col) % 2 != 0;
                g2.setColor(isDark ? DARK_TILE : LIGHT_TILE);
                g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawLastMoveHighlight(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
        for (Position p : lastMovePath) {
            g2.setColor(UIConstants.HIGHLIGHT_YELLOW);
            g2.fillRect(p.getCol() * TILE_SIZE, p.getRow() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawSelectedHighlight(Graphics2D g2) {
        if (selectedPos == null) return;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
        g2.setColor(UIConstants.HIGHLIGHT_GREEN);
        g2.fillRect(selectedPos.getCol() * TILE_SIZE, selectedPos.getRow() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawValidMoveDots(Graphics2D g2) {
        for (Move m : validMovesForSelected) {
            Position end = m.getEnd();
            int cx = end.getCol() * TILE_SIZE + TILE_SIZE / 2;
            int cy = end.getRow() * TILE_SIZE + TILE_SIZE / 2;

            if (m.isCaptureMove()) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
                g2.setColor(UIConstants.X_CAPTURE);
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int r = TILE_SIZE / 4;
                g2.drawLine(cx - r, cy - r, cx + r, cy + r);
                g2.drawLine(cx + r, cy - r, cx - r, cy + r);
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
                g2.setColor(new Color(0x222222));
                int dotR = TILE_SIZE / 4;
                g2.fill(new Ellipse2D.Float(cx - dotR, cy - dotR, dotR * 2, dotR * 2));
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawForcedRing(Graphics2D g2) {
        if (forcedPiecePos == null) return;
        int x = forcedPiecePos.getCol() * TILE_SIZE;
        int y = forcedPiecePos.getRow() * TILE_SIZE;
        g2.setColor(FORCED_RING);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawPieces(Graphics2D g2) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Tile tile = board.getTile(new Position(row, col));
                if (!tile.isOccupied()) continue;
                drawPiece(g2, tile.getPiece(), row, col);
            }
        }
    }

    private void drawPiece(Graphics2D g2, Piece p, int row, int col) {
        boolean isWhite = p.getColor() == pieces.Color.WHITE;
        boolean isKing  = p instanceof King;

        int margin = 7;
        int x = col * TILE_SIZE + margin;
        int y = row * TILE_SIZE + margin;
        int size = TILE_SIZE - margin * 2;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Float(x + 3, y + 4, size, size));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        Color shade = isWhite ? WHITE_PIECE_SHADE : BLACK_PIECE_SHADE;
        GradientPaint gp = new GradientPaint(
            x, y, isWhite ? Color.WHITE : new Color(0x4A3020),
            x + size, y + size, shade
        );
        g2.setPaint(gp);
        g2.fill(new Ellipse2D.Float(x, y, size, size));

        g2.setPaint(null);
        g2.setColor(isWhite ? new Color(0xDDDDDD) : new Color(0x553322));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new Ellipse2D.Float(x + 1, y + 1, size - 2, size - 2));
        g2.setStroke(new BasicStroke(1f));

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Float(x + size * 0.2f, y + size * 0.1f, size * 0.4f, size * 0.3f));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        if (isKing) {
            drawCrown(g2, x, y, size, isWhite);
        }
    }

    private void drawCrown(Graphics2D g2, int x, int y, int size, boolean isWhite) {
        int cx = x + size / 2;
        int cy = y + size / 2;
        int cr = size / 4;

        g2.setColor(CROWN_COLOR);
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(new Ellipse2D.Float(cx - cr, cy - cr, cr * 2, cr * 2));

        g2.fill(new Ellipse2D.Float(cx - cr / 2f, cy - cr / 2f, cr, cr));
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawCoordinates(Graphics2D g2) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 10));
        for (int i = 0; i < Board.SIZE; i++) {
            boolean rowDark = (i + 1) % 2 != 0;
            g2.setColor(rowDark ? LIGHT_TILE : DARK_TILE);
            g2.drawString(String.valueOf(i), 2, i * TILE_SIZE + 12);

            boolean colDark = (Board.SIZE - 1 + i) % 2 != 0;
            g2.setColor(colDark ? LIGHT_TILE : DARK_TILE);
            g2.drawString(String.valueOf(i), i * TILE_SIZE + 2, BOARD_PX - 3);
        }
    }
}

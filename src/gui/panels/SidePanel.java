package gui.panels;

import board.Board;
import game.GameState;
import game.core.GameController;
import gui.UIConstants;
import move.Move;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidePanel extends JPanel {

    private static final java.awt.Color BG          = UIConstants.BG_DARK;
    private static final java.awt.Color CARD_BG     = UIConstants.BG_CARD;
    private static final java.awt.Color ACCENT      = UIConstants.ACCENT;
    private static final java.awt.Color TEXT_LIGHT  = UIConstants.TEXT_LIGHT;
    private static final java.awt.Color TEXT_DIM    = UIConstants.TEXT_DIM;
    private static final java.awt.Color WIN_COLOR   = UIConstants.WIN_HIGHLIGHT;
    private static final java.awt.Color TURN_GLOW   = UIConstants.TURN_GLOW;

    private JLabel statusLabel;
    private JLabel whiteCountLabel;
    private JLabel blackCountLabel;
    private JPanel whiteTurnDot;
    private JPanel blackTurnDot;
    private JTextArea moveLog;
    private JLabel messageLabel;
    private JButton resetButton;
    private JButton changeModeButton;
    private int moveNumber = 1;

    public SidePanel() {
        setBackground(BG);
        setPreferredSize(new Dimension(220, BoardPanel.TILE_SIZE * Board.SIZE));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(16, 12, 16, 12));
        buildUI();
    }

    private void buildUI() {
        JLabel title = new JLabel("CHECKERS");
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(ACCENT);
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);

        JLabel subtitle = new JLabel("International Rules · 10×10");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitle.setForeground(TEXT_DIM);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        add(subtitle);

        add(Box.createVerticalStrut(16));
        add(makeSeparator());
        add(Box.createVerticalStrut(14));

        statusLabel = new JLabel("WHITE'S TURN");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        statusLabel.setForeground(TEXT_LIGHT);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(statusLabel);

        add(Box.createVerticalStrut(14));
        add(makeSeparator());
        add(Box.createVerticalStrut(14));

        add(makePlayerCard(true));
        add(Box.createVerticalStrut(8));
        add(makePlayerCard(false));

        add(Box.createVerticalStrut(14));
        add(makeSeparator());
        add(Box.createVerticalStrut(10));

        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        messageLabel.setForeground(new java.awt.Color(0xFF7744));
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel);

        add(Box.createVerticalStrut(10));
        add(makeSeparator());
        add(Box.createVerticalStrut(10));

        JLabel historyTitle = new JLabel("MOVE HISTORY");
        historyTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        historyTitle.setForeground(TEXT_DIM);
        historyTitle.setAlignmentX(CENTER_ALIGNMENT);
        add(historyTitle);
        add(Box.createVerticalStrut(6));

        moveLog = new JTextArea();
        moveLog.setEditable(false);
        moveLog.setBackground(CARD_BG);
        moveLog.setForeground(TEXT_LIGHT);
        moveLog.setFont(new Font("Monospaced", Font.PLAIN, 10));
        moveLog.setBorder(new EmptyBorder(6, 6, 6, 6));

        JScrollPane scroll = new JScrollPane(moveLog);
        scroll.setPreferredSize(new Dimension(196, 160));
        scroll.setMaximumSize(new Dimension(196, 160));
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT.darker(), 1));
        scroll.setBackground(CARD_BG);
        scroll.getVerticalScrollBar().setBackground(CARD_BG);
        scroll.setAlignmentX(CENTER_ALIGNMENT);
        add(scroll);

        add(Box.createVerticalStrut(14));
        add(makeSeparator());
        add(Box.createVerticalStrut(10));

        resetButton = new JButton("NEW GAME");
        resetButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        resetButton.setForeground(TEXT_LIGHT);
        resetButton.setBackground(new java.awt.Color(0x6B3C1A));
        resetButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT, 1),
            new EmptyBorder(7, 14, 7, 14)
        ));
        resetButton.setFocusPainted(false);
        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetButton.setAlignmentX(CENTER_ALIGNMENT);
        resetButton.setMaximumSize(new Dimension(160, 36));
        add(resetButton);

        add(Box.createVerticalStrut(8));

        JButton changeModeButton = new JButton("CHANGE MODE");
        changeModeButton.setFont(new Font("SansSerif", Font.BOLD, 11));
        changeModeButton.setForeground(TEXT_LIGHT);
        changeModeButton.setBackground(new java.awt.Color(0x4A3A2A));
        changeModeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT.darker(), 1),
            new EmptyBorder(6, 12, 6, 12)
        ));
        changeModeButton.setFocusPainted(false);
        changeModeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changeModeButton.setAlignmentX(CENTER_ALIGNMENT);
        changeModeButton.setMaximumSize(new Dimension(160, 32));
        add(changeModeButton);
        this.changeModeButton = changeModeButton;
    }

    private JPanel makePlayerCard(boolean isWhite) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new java.awt.Color(0x3A2A22), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JPanel icon = new PieceIcon(isWhite);
        icon.setPreferredSize(new Dimension(36, 36));
        icon.setBackground(CARD_BG);
        card.add(icon, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(CARD_BG);

        String name = isWhite ? "WHITE" : "BLACK";
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setForeground(TEXT_LIGHT);
        info.add(nameLabel);

        JLabel countLabel = new JLabel("20 pieces");
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        countLabel.setForeground(TEXT_DIM);
        info.add(countLabel);
        if (isWhite) whiteCountLabel = countLabel;
        else blackCountLabel = countLabel;

        card.add(info, BorderLayout.CENTER);

        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(2, 8, 12, 12);
            }
        };
        dot.setPreferredSize(new Dimension(16, 28));
        dot.setBackground(CARD_BG);
        if (isWhite) whiteTurnDot = dot;
        else blackTurnDot = dot;

        card.add(dot, BorderLayout.EAST);
        return card;
    }

    private Component makeSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new java.awt.Color(0x3A2A22));
        sep.setBackground(BG);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    public void update(GameController game) {
        GameState state = game.getGameState();
        java.util.List<pieces.Piece> whitePieces = game.getBoard().getPiecesOf(pieces.Color.WHITE);
        java.util.List<pieces.Piece> blackPieces = game.getBoard().getPiecesOf(pieces.Color.BLACK);

        whiteCountLabel.setText(whitePieces.size() + " piece" + (whitePieces.size() != 1 ? "s" : ""));
        blackCountLabel.setText(blackPieces.size() + " piece" + (blackPieces.size() != 1 ? "s" : ""));

        if (state == GameState.ONGOING) {
            boolean whitesTurn = game.getCurrentPlayer().getColor() == pieces.Color.WHITE;
            statusLabel.setText(whitesTurn ? "WHITE'S TURN" : "BLACK'S TURN");
            statusLabel.setForeground(TEXT_LIGHT);
            whiteTurnDot.setBackground(whitesTurn ? TURN_GLOW : CARD_BG);
            blackTurnDot.setBackground(whitesTurn ? CARD_BG : TURN_GLOW);
        } else if (state == GameState.WHITE_WIN) {
            statusLabel.setText("WHITE WINS! 🏆");
            statusLabel.setForeground(WIN_COLOR);
            whiteTurnDot.setBackground(WIN_COLOR);
            blackTurnDot.setBackground(CARD_BG);
        } else {
            statusLabel.setText("BLACK WINS! 🏆");
            statusLabel.setForeground(WIN_COLOR);
            blackTurnDot.setBackground(WIN_COLOR);
            whiteTurnDot.setBackground(CARD_BG);
        }

        whiteTurnDot.repaint();
        blackTurnDot.repaint();
    }

    public void setMessage(String msg) {
        messageLabel.setText(msg == null || msg.isEmpty() ? " " : msg);
    }

    public void logMove(String playerColor, Move move) {
        String from = move.getStart().toString();
        String to   = move.getEnd().toString();
        String cap  = move.isCaptureMove() ? " ×" + move.getCapturedPieces().size() : "";
        moveLog.append(moveNumber + ". " + playerColor.charAt(0) + " " + from + "→" + to + cap + "\n");
        moveNumber++;
        moveLog.setCaretPosition(moveLog.getDocument().getLength());
    }

    public void resetLog() {
        moveLog.setText("");
        moveNumber = 1;
    }

    public JButton getResetButton() { return resetButton; }
    public JButton getChangeModeButton() { return changeModeButton; }

    private static class PieceIcon extends JPanel {
        private final boolean isWhite;
        PieceIcon(boolean isWhite) {
            this.isWhite = isWhite;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int m = 3, s = getWidth() - m * 2;
            g2.setColor(new java.awt.Color(0, 0, 0, 60));
            g2.fillOval(m + 2, m + 3, s, s);
            GradientPaint gp = new GradientPaint(m, m,
                isWhite ? java.awt.Color.WHITE : new java.awt.Color(0x4A3020),
                m + s, m + s,
                isWhite ? new java.awt.Color(0xC8C0B0) : new java.awt.Color(0x1A0A00));
            g2.setPaint(gp);
            g2.fillOval(m, m, s, s);
            g2.setPaint(null);
            g2.setColor(isWhite ? new java.awt.Color(0xDDDDDD) : new java.awt.Color(0x553322));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(m + 1, m + 1, s - 2, s - 2);
        }
    }
}

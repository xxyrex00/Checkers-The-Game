package gui;

import board.Position;
import game.GameMode;
import game.GameState;
import game.core.GameController;
import gui.dialogs.ModeDialog;
import gui.panels.BoardPanel;
import gui.panels.SidePanel;
import move.Move;
import player.BotPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CheckersWindow extends JFrame {

    private GameController game;
    private BoardPanel boardPanel;
    private SidePanel sidePanel;
    private GameMode mode;

    private Position selectedPos = null;

    public CheckersWindow(GameMode mode) {
        this.mode = mode;
        initGame(mode);
        buildUI();
        setupInteraction();
        refreshAll();
    }

    private void initGame(GameMode m) {
        game = new GameController(m);
        game.startGame();
    }

    private void buildUI() {
        setTitle("International Checkers");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new java.awt.Color(0x1A1210));

        boardPanel = new BoardPanel(game.getBoard());
        sidePanel  = new SidePanel();

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new java.awt.Color(0x1A1210));
        root.add(boardPanel, BorderLayout.CENTER);
        root.add(sidePanel,  BorderLayout.EAST);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        sidePanel.getResetButton().addActionListener(e -> resetGame());
        sidePanel.getChangeModeButton().addActionListener(e -> changeMode());
    }

    private void setupInteraction() {
        boardPanel.setClickListener(pos -> {
            if (game.getGameState() != GameState.ONGOING) return;

            if (mode == GameMode.PVE &&
                    game.getCurrentPlayer().getColor() == pieces.Color.BLACK) return;

            handleHumanClick(pos);
        });
    }

    private void handleHumanClick(Position pos) {
        Position forcedPos = game.getForcedPiece() != null
            ? game.getForcedPiece().getPosition() : null;

        if (forcedPos != null) {
            selectedPos = forcedPos;
            boardPanel.setSelectedPos(selectedPos);
            boardPanel.setValidMovesForSelected(getValidMovesFromPos(selectedPos));
        }

        if (selectedPos == null) {
            if (game.getBoard().getTile(pos).isOccupied() &&
                    game.getBoard().getTile(pos).getPiece().getColor()
                        == game.getCurrentPlayer().getColor()) {
                selectedPos = pos;
                boardPanel.setSelectedPos(selectedPos);
                List<Move> validForPiece = getValidMovesFromPos(pos);
                boardPanel.setValidMovesForSelected(validForPiece);
                sidePanel.setMessage(validForPiece.isEmpty() ? "No legal moves for this piece." : " ");
            }
        } else if (pos.equals(selectedPos)) {
            deselect();
        } else if (game.getBoard().getTile(pos).isOccupied() &&
                game.getBoard().getTile(pos).getPiece().getColor()
                    == game.getCurrentPlayer().getColor() && forcedPos == null) {
            selectedPos = pos;
            boardPanel.setSelectedPos(pos);
            boardPanel.setValidMovesForSelected(getValidMovesFromPos(pos));
            sidePanel.setMessage(" ");
        } else {
            Move attempt = new Move(selectedPos, pos);
            Position from = selectedPos;

            // Capture the moving player's color BEFORE applyMove switches the turn
            pieces.Color movingColor = game.getCurrentPlayer().getColor();

            // Only log the move if applyMove actually accepted it
            boolean accepted = game.applyMove(attempt);

            if (accepted) {
                String colorName = movingColor == pieces.Color.WHITE ? "WHITE" : "BLACK";
                sidePanel.logMove(colorName, new Move(from, pos));
            }

            boardPanel.setLastMovePath(from, pos);
            deselect();
            refreshAll();

            if (game.getForcedPiece() != null) {
                selectedPos = game.getForcedPiece().getPosition();
                boardPanel.setSelectedPos(selectedPos);
                boardPanel.setValidMovesForSelected(getValidMovesFromPos(selectedPos));
                boardPanel.setForcedPiecePos(selectedPos);
                sidePanel.setMessage("⚡ Must capture again with same piece!");
                boardPanel.refresh();
                return;
            }

            if (game.getGameState() == GameState.ONGOING &&
                    mode == GameMode.PVE &&
                    game.getCurrentPlayer().getColor() == pieces.Color.BLACK) {
                scheduleBotMove();
            }
        }

        boardPanel.refresh();
    }

    private void scheduleBotMove() {
        sidePanel.setMessage("🤖 Bot is thinking...");
        Timer delay = new Timer(600, e -> executeBotStep());
        delay.setRepeats(false);
        delay.start();
    }

    private void executeBotStep() {
        if (game.getGameState() != GameState.ONGOING) return;
        if (game.getCurrentPlayer().getColor() != pieces.Color.BLACK) return;

        BotPlayer bot = (BotPlayer) game.getPlayerBlack();
        Move move;
        Position from;

        if (game.getForcedPiece() != null) {
            from = game.getForcedPiece().getPosition();
            move = bot.makeForcedCapture(game.getBoard(), game.getForcedPiece());
        } else {
            move = bot.makeMove(game.getBoard());
            from = move != null ? move.getStart() : null;
        }

        if (move == null) {
            sidePanel.setMessage(" ");
            return;
        }

        boardPanel.setLastMovePath(from, move.getEnd());
        game.applyMove(move);
        sidePanel.logMove("BLACK", move);
        refreshAll();

        if (game.getForcedPiece() != null) {
            // More captures required — schedule the next step so the UI repaints
            // between each jump instead of freezing on the EDT
            Timer next = new Timer(400, e -> executeBotStep());
            next.setRepeats(false);
            next.start();
        } else {
            sidePanel.setMessage(" ");
        }
    }

    private List<Move> getValidMovesFromPos(Position pos) {
        List<Move> all = game.getRuleEngine().getValidMoves(game.getBoard(),
            game.getCurrentPlayer().getColor());
        return all.stream()
            .filter(m -> m.getStart().equals(pos))
            .collect(Collectors.toList());
    }

    private void deselect() {
        selectedPos = null;
        boardPanel.setSelectedPos(null);
        boardPanel.setValidMovesForSelected(null);
        boardPanel.setForcedPiecePos(null);
        sidePanel.setMessage(" ");
    }

    private void refreshAll() {
        boardPanel.setBoard(game.getBoard());
        boardPanel.setForcedPiecePos(game.getForcedPiece() != null
            ? game.getForcedPiece().getPosition() : null);
        sidePanel.update(game);
        boardPanel.refresh();

        if (game.getGameState() == GameState.WHITE_WIN) {
            sidePanel.setMessage("🏆 White wins the game!");
        } else if (game.getGameState() == GameState.BLACK_WIN) {
            sidePanel.setMessage("🏆 Black wins the game!");
        }
    }

    private void resetGame() {
        deselect();
        game.resetGame();
        boardPanel.setLastMovePath(null, null);
        sidePanel.resetLog();
        sidePanel.setMessage(" ");
        refreshAll();
    }

    private void changeMode() {
        dispose();
        ModeDialog dialog = new ModeDialog(null);
        dialog.setVisible(true);
        GameMode chosen = dialog.getChosen();
        if (chosen != null) {
            new CheckersWindow(chosen);
        }
    }
}

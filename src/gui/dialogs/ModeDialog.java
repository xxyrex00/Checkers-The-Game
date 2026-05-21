package gui.dialogs;

import game.GameMode;
import gui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModeDialog extends JDialog {

    private static final Color BG       = UIConstants.BG_DARK;
    private static final Color CARD_BG  = UIConstants.BG_CARD;
    private static final Color ACCENT   = UIConstants.ACCENT;
    private static final Color TEXT     = UIConstants.TEXT_LIGHT;
    private static final Color TEXT_DIM = UIConstants.TEXT_DIM;

    private GameMode chosen = null;

    public ModeDialog(Frame parent) {
        super(parent, "International Checkers", true);
        setUndecorated(false);
        setResizable(false);
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel root = new JPanel();
        root.setBackground(BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(32, 40, 32, 40));

        JLabel title = styledLabel("INTERNATIONAL CHECKERS", 24, Font.BOLD, ACCENT);
        title.setAlignmentX(CENTER_ALIGNMENT);
        root.add(title);

        JLabel sub = styledLabel("10×10 · Official Rules", 12, Font.PLAIN, TEXT_DIM);
        sub.setAlignmentX(CENTER_ALIGNMENT);
        root.add(sub);

        root.add(Box.createVerticalStrut(32));

        JLabel choose = styledLabel("SELECT GAME MODE", 13, Font.BOLD, TEXT);
        choose.setAlignmentX(CENTER_ALIGNMENT);
        root.add(choose);

        root.add(Box.createVerticalStrut(16));

        JPanel pvpCard = makeCard(
            "⚔  Player vs Player",
            "Two humans take turns on the same machine.",
            GameMode.PVP
        );
        pvpCard.setAlignmentX(CENTER_ALIGNMENT);
        root.add(pvpCard);

        root.add(Box.createVerticalStrut(12));

        JPanel pveCard = makeCard(
            "🤖  Player vs Bot",
            "Play against a random-move computer opponent.",
            GameMode.PVE
        );
        pveCard.setAlignmentX(CENTER_ALIGNMENT);
        root.add(pveCard);

        setContentPane(root);
        getContentPane().setBackground(BG);
    }

    private JPanel makeCard(String title, String desc, GameMode mode) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT.darker(), 1),
            new EmptyBorder(14, 20, 14, 20)
        ));
        card.setMaximumSize(new Dimension(320, 80));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = styledLabel(title, 14, Font.BOLD, TEXT);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(titleLabel);

        JLabel descLabel = styledLabel(desc, 11, Font.PLAIN, TEXT_DIM);
        descLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(descLabel);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                chosen = mode;
                dispose();
            }
            @Override public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(0x3D2B20));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT, 1),
                    new EmptyBorder(14, 20, 14, 20)
                ));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BG);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT.darker(), 1),
                    new EmptyBorder(14, 20, 14, 20)
                ));
            }
        });

        return card;
    }

    private JLabel styledLabel(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", style, size));
        l.setForeground(color);
        return l;
    }

    public GameMode getChosen() { return chosen; }
}

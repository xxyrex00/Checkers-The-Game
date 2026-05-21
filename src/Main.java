import game.GameMode;
import gui.CheckersWindow;
import gui.dialogs.ModeDialog;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("Panel.background",     gui.UIConstants.BG_DARK);
                UIManager.put("OptionPane.background", gui.UIConstants.BG_DARK);
                UIManager.put("Button.background",    gui.UIConstants.BG_BUTTON);
                UIManager.put("Button.foreground",    gui.UIConstants.BUTTON_TEXT);
            } catch (Exception e) {
                System.err.println("Warning: Could not set look-and-feel: " + e.getMessage());
            }

            ModeDialog dialog = new ModeDialog(null);
            dialog.setVisible(true);

            GameMode chosen = dialog.getChosen();
            if (chosen == null) {
                System.exit(0);
            }

            new CheckersWindow(chosen);
        });
    }
}

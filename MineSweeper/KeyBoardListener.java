import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
public class KeyBoardListener {
    public KeyBoardListener(GameControllerGUI controller, Grille grille, GridPanel gridPanel, DemineurGUI demineurGUI) {
        
        // implémentation du space click
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                Point point = pointerInfo.getLocation();
                SwingUtilities.convertPointFromScreen(point, gridPanel);
                for (int i = 0; i < grille.getTailleX(); i++) {
                    for (int j = 0; j < grille.getTailleY(); j++) {
                        CelluleButton btn = gridPanel.getCelluleButtonByIndex(i, j);
                        Rectangle bounds = btn.getBounds();
                        if (bounds.contains(point)) {
                            controller.actOnSpaceClick(i,j);
                            return true; // Consomme l'événement
                        }
                    }
                }
            }
            return false; // Ne consomme pas l'événement dans les autres cas
        });

    }
}

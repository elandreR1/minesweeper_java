import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

public class MyKeyDispatcher implements KeyEventDispatcher {

    public MyKeyDispatcher() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    @Override
    public boolean dispatchKeyEvent(java.awt.event.KeyEvent e) {
        return false;
    }
}

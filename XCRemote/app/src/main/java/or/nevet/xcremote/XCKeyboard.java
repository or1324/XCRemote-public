package or.nevet.xcremote;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.KeyEvent;

public class XCKeyboard extends InputMethodService implements KeySender{
    public void sendKey(KeyEvent keyEvent) {
        if (getCurrentInputConnection() != null)
        getCurrentInputConnection().sendKeyEvent(keyEvent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AccessibilityKeyDetector.setKeyboard(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AccessibilityKeyDetector.setKeyboard(null);
    }
}
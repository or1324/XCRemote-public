package or.nevet.xcremote;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;
import java.util.List;

public class AccessibilityKeyDetector extends AccessibilityService {

    private static KeySender keyboard;
    public static boolean appOpen = true;
    //If I will add another button I need to change the returnToDefault method on Settings class and the AccessibilityKeyDetector class and the chooseactivity class.

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        if (keyboard != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            KeyEvent replace = null;
            SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_F6:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("T_PRESS", KeyEvent.KEYCODE_F6));
                    break;
                case KeyEvent.KEYCODE_F3:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("Y_HOLD", KeyEvent.KEYCODE_F3));
                    break;
                case KeyEvent.KEYCODE_F4:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("Y_PRESS", KeyEvent.KEYCODE_F4));
                    break;
                case KeyEvent.KEYCODE_F1:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("O_HOLD", KeyEvent.KEYCODE_F1));
                    break;
                case KeyEvent.KEYCODE_M:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("O_PRESS", KeyEvent.KEYCODE_M));
                    break;
                case KeyEvent.KEYCODE_ESCAPE:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("X_PRESS", KeyEvent.KEYCODE_ESCAPE));
                    break;
                case KeyEvent.KEYCODE_F2:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("T_HOLD", KeyEvent.KEYCODE_F2));
                    break;
                case KeyEvent.KEYCODE_T:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("X_HOLD", KeyEvent.KEYCODE_T));
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("STICK_PRESS", KeyEvent.KEYCODE_ENTER));
                    break;
                case KeyEvent.KEYCODE_P:
                    replace = new KeyEvent(event.getAction(), sharedPreferences.getInt("STICK_HOLD", KeyEvent.KEYCODE_P));
                    break;
            }
            if (replace != null) {
                keyboard.sendKey(replace);
                return true;
            }
            else
                return super.onKeyEvent(event);
        }

        return super.onKeyEvent(event);
    }

    public static boolean isThisKeyboardSetAsDefaultIME(Context context) {
        final String defaultIME = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.DEFAULT_INPUT_METHOD);
        return isThisKeyboardSetAsDefaultIME(defaultIME, context.getPackageName());
    }

    public static boolean isThisKeyboardSetAsDefaultIME(String defaultIME, String myPackageName) {
        if (TextUtils.isEmpty(defaultIME))
            return false;

        ComponentName defaultInputMethod = ComponentName.unflattenFromString(defaultIME);
        if (defaultInputMethod.getPackageName().equals(myPackageName)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && event.getEventType() != AccessibilityEvent.TYPE_WINDOWS_CHANGED && event.getContentChangeTypes() != AccessibilityEvent.CONTENT_CHANGE_TYPE_PANE_DISAPPEARED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                String p = "org.xcsoar.testing";
                if (!isPackageExisted(p))
                    p = "org.xcsoar";
                if (event.getPackageName().equals(p)) {
                    appOpen = true;
                } else if (appOpen) {
                    appOpen = false;
//                    if (MainActivity.isOpen) {
//                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("or.nevet.xcremote");
//                        if (launchIntent != null)
//                            startActivity(launchIntent);
//                    }
                    InputMethodManager ime = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    ime.showInputMethodPicker();
                }
            }
        }
//        String p = "org.xcsoar.testing";
//        if (!isPackageExisted(p))
//            p = "org.xcsoar";
//        try {
//            if (event.getPackageName() != null && event.getPackageName().equals(p) && event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
//                appOpen = true;
//                first = true;
//                first2 = true;
//                if (!MainActivity.isOpen) {
//                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("or.nevet.xcremote");
//                    if (launchIntent != null)
//                        startActivity(launchIntent);
//                }
//            } else if (first && !first2 && appOpen && event.getPackageName().equals("com.sec.android.app.launcher") && event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && isThisKeyboardSetAsDefaultIME(this)) {
//                appOpen = false;
//                first = false;
//                first2 = true;
//                InputMethodManager ime = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                ime.showInputMethodPicker();
//            } else if (first && appOpen && event.getPackageName().equals("com.sec.android.app.launcher") && event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && isThisKeyboardSetAsDefaultIME(this))
//                first2 = false;
//        } catch (Exception ignored) {
//
//        }

    }

    public boolean isPackageExisted(String targetPackage) {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onInterrupt() {

    }

    public static void setKeyboard(KeySender keyboard2) {
        keyboard = keyboard2;
    }

}
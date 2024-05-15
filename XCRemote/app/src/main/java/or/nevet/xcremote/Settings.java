package or.nevet.xcremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;

public class Settings {

    public static void saveSetting(Context c, int keyCode, String name) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, keyCode);
        editor.apply();
    }

    public static void returnToDefault(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("T_PRESS", KeyEvent.KEYCODE_F6);
        editor.putInt("Y_HOLD", KeyEvent.KEYCODE_F3);
        editor.putInt("Y_PRESS", KeyEvent.KEYCODE_F4);
        editor.putInt("O_HOLD", KeyEvent.KEYCODE_F1);
        editor.putInt("O_PRESS", KeyEvent.KEYCODE_M);
        editor.putInt("X_PRESS", KeyEvent.KEYCODE_ESCAPE);
        editor.putInt("T_HOLD", KeyEvent.KEYCODE_F2);
        editor.putInt("X_HOLD", KeyEvent.KEYCODE_T);
        editor.putInt("STICK_PRESS", KeyEvent.KEYCODE_ENTER);
        editor.putInt("STICK_HOLD", KeyEvent.KEYCODE_P);
        editor.apply();
    }

    public static void saveAutoStart(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Autostart XCRemote", !sharedPreferences.getBoolean("Autostart XCRemote", false));
        editor.apply();
    }
    public static boolean getAutoStart(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("Autostart XCRemote", false);
    }

    public static void saveIsGerman(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsGerman", !sharedPreferences.getBoolean("IsGerman", false));
        editor.apply();
    }
    public static boolean getIsGerman(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("IsGerman", false);
    }

}

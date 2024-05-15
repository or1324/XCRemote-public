package or.nevet.xcremote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class SetupActivity extends AppCompatActivity {

    private InputMethodChangeReceiver mReceiver;

    Button language;
    Button autostart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ImageButton triangle = findViewById(R.id.imageButton3);
        ImageButton circle = findViewById(R.id.imageButton);
        ImageButton x = findViewById(R.id.imageButton4);
        ImageButton square = findViewById(R.id.imageButton2);
        ImageButton stick = findViewById(R.id.imageButton5);
        language = findViewById(R.id.language);
        autostart = findViewById(R.id.autostart_xcremote);
        IntentFilter filter = new IntentFilter(Intent.ACTION_INPUT_METHOD_CHANGED);
        mReceiver = new InputMethodChangeReceiver();
        registerReceiver(mReceiver, filter);
        if (Settings.getAutoStart(this))
            findViewById(R.id.autostart_xcremote).setBackground(getDrawable(R.drawable.buttor_checked));
        if (Settings.getIsGerman(this))
            changeToGerman();
        findViewById(R.id.start_xcsoar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = "org.xcsoar.testing";
                if (!isPackageExisted(p, SetupActivity.this))
                    p = "org.xcsoar";
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(p);
                if (launchIntent != null)
                    startActivity(launchIntent);
                else
                    Toast.makeText(SetupActivity.this, "You do not have xcsoar installed", Toast.LENGTH_SHORT).show();
            }
        });
        autostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.saveAutoStart(SetupActivity.this);
                if (Settings.getAutoStart(SetupActivity.this))
                    autostart.setBackground(getDrawable(R.drawable.buttor_checked));
                else
                    autostart.setBackground(getDrawable(R.drawable.buttor));
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.saveIsGerman(SetupActivity.this);
                if (Settings.getIsGerman(SetupActivity.this))
                    changeToGerman();
                else
                    changeToEnglish();
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.returnToDefault(SetupActivity.this);
                Toast.makeText(SetupActivity.this, "All of the actions were set to default", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.switch_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager ime = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                ime.showInputMethodPicker();
            }
        });
        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "△ Press");
                startActivity(intent);
            }
        });
        stick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "Stick Press");
                startActivity(intent);
            }
        });
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "O Press");
                startActivity(intent);
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "X Press");
                startActivity(intent);
            }
        });
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "⬜ Press");
                startActivity(intent);
            }
        });
        triangle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "△ Hold");
                startActivity(intent);
                return true;
            }
        });
        stick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "Stick Hold");
                startActivity(intent);
                return true;
            }
        });
        circle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "O Hold");
                startActivity(intent);
                return true;
            }
        });
        x.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "X Hold");
                startActivity(intent);
                return true;
            }
        });
        square.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(SetupActivity.this, ChooseActivity.class);
                intent.putExtra("BUTTON_NAME", "⬜ Hold");
                startActivity(intent);
                return true;
            }
        });
    }

    private void changeToGerman() {
        language.setBackground(getDrawable(R.drawable.buttor_checked));
        language.setText("English");
        autostart.setText("xcremote automatisch starten");
        ((Button)findViewById(R.id.start_xcsoar)).setText("xcsoar starten");
        ((Button)findViewById(R.id.button)).setText("zurück zur Standardeinstellung");
        ((Button)findViewById(R.id.switch_keyboard)).setText("klicken & xcremote aktivieren");
    }

    private void changeToEnglish() {
        language.setBackground(getDrawable(R.drawable.buttor));
        language.setText("Deutsch");
        autostart.setText("autostart xcremote");
        ((Button)findViewById(R.id.start_xcsoar)).setText("start xcsoar");
        ((Button)findViewById(R.id.button)).setText("Return to default");
        ((Button)findViewById(R.id.switch_keyboard)).setText("click & activate xcremote");
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
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

    public boolean isPackageExisted(String targetPackage, Context c) {
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isThisKeyboardSetAsDefaultIME(this)) {
            ((Button)findViewById(R.id.switch_keyboard)).setText(getString(R.string.click_activate_xcremote));
        }
        else
            ((Button)findViewById(R.id.switch_keyboard)).setText(getString(R.string.default_keyboard_string));
    }

    public class InputMethodChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_INPUT_METHOD_CHANGED)) {
                if (!isThisKeyboardSetAsDefaultIME(context))
                    ((Button)findViewById(R.id.switch_keyboard)).setText(getString(R.string.click_activate_xcremote));
                else
                    ((Button)findViewById(R.id.switch_keyboard)).setText(getString(R.string.default_keyboard_string));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
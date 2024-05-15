package or.nevet.xcremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        String text = getIntent().getStringExtra("BUTTON_NAME");
        ((TextView)findViewById(R.id.textView)).setText(text);
        highlightCurrent(text);
        if (Settings.getIsGerman(this))
            changeToGerman();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.key1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_ESCAPE, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_T, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F6, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F2, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F4, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F3, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_M, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F1, ((Button)view).getText().toString());
            }
        });
        findViewById(R.id.key9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_ENTER, ((Button)view).getText().toString());

            }
        });
        findViewById(R.id.key10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_P, ((Button)view).getText().toString());

            }
        });
        findViewById(R.id.key11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_Z, ((Button)view).getText().toString());

            }
        });
        findViewById(R.id.key12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F5, ((Button)view).getText().toString());
            }
        });

        findViewById(R.id.key13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F7, ((Button)view).getText().toString());
            }
        });

        findViewById(R.id.key14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_F8, ((Button)view).getText().toString());
            }
        });

        findViewById(R.id.key15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_7, ((Button)view).getText().toString());
            }
        });

        findViewById(R.id.key16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_8, ((Button)view).getText().toString());
            }
        });

        findViewById(R.id.key17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_9, ((Button)view).getText().toString());
            }
        });

        findViewById(R.id.key18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(text, KeyEvent.KEYCODE_0, ((Button)view).getText().toString());
            }
        });

    }

    private void changeToGerman() {
        ((Button)findViewById(R.id.key2)).setText("Aufgabe Abbruch");
        ((Button)findViewById(R.id.key3)).setText("Alternativen");
        ((Button)findViewById(R.id.key4)).setText("Analyse");
        ((Button)findViewById(R.id.key6)).setText("Checkliste");
        ((Button)findViewById(R.id.key11)).setText("Autozoom an/aus");
        ((Button)findViewById(R.id.key12)).setText("Wegpunkte");
        ((Button)findViewById(R.id.key13)).setText("Aufgabe");
        ((Button)findViewById(R.id.key14)).setText("Flugeinstellungen");
        ((Button)findViewById(R.id.key15)).setText("Nächster Wegpunkt");
        ((Button)findViewById(R.id.key16)).setText("Vorheriger Wegpunkt");
        ((Button)findViewById(R.id.key17)).setText("Zielpunkt");
        ((Button)findViewById(R.id.key18)).setText("Ohne Funktion");
        ((Button)findViewById(R.id.back)).setText("zurück");
    }

    public void save(String text, int code, String setting) {
        switch (text) {
            case "X Press":
                Settings.saveSetting(ChooseActivity.this, code, "X_PRESS");
                break;
            case "X Hold":
                Settings.saveSetting(ChooseActivity.this, code, "X_HOLD");
                break;
            case "⬜ Press":
                Settings.saveSetting(ChooseActivity.this, code, "Y_PRESS");
                break;
            case "⬜ Hold":
                Settings.saveSetting(ChooseActivity.this, code, "Y_HOLD");
                break;
            case "O Press":
                Settings.saveSetting(ChooseActivity.this, code, "O_PRESS");
                break;
            case "O Hold":
                Settings.saveSetting(ChooseActivity.this, code, "O_HOLD");
                break;
            case "△ Press":
                Settings.saveSetting(ChooseActivity.this, code, "T_PRESS");
                break;
            case "△ Hold":
                Settings.saveSetting(ChooseActivity.this, code, "T_HOLD");
                break;
            case "Stick Press":
                Settings.saveSetting(ChooseActivity.this, code, "STICK_PRESS");
                break;
            case "Stick Hold":
                Settings.saveSetting(ChooseActivity.this, code, "STICK_HOLD");
                break;
        }
        Toast.makeText(getApplicationContext(), text + " now does " + setting, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void highlightAction(int code) {
        switch (code) {
            case KeyEvent.KEYCODE_ESCAPE:
                findViewById(R.id.key1).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_T:
                findViewById(R.id.key2).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F6:
                findViewById(R.id.key3).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F2:
                findViewById(R.id.key4).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F4:
                findViewById(R.id.key5).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F3:
                findViewById(R.id.key6).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_M:
                findViewById(R.id.key7).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F1:
                findViewById(R.id.key8).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_ENTER:
                findViewById(R.id.key9).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_P:
                findViewById(R.id.key10).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_Z:
                findViewById(R.id.key11).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F5:
                findViewById(R.id.key12).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F7:
                findViewById(R.id.key13).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_F8:
                findViewById(R.id.key14).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_7:
                findViewById(R.id.key15).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_8:
                findViewById(R.id.key16).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_9:
                findViewById(R.id.key17).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
            case KeyEvent.KEYCODE_0:
                findViewById(R.id.key18).setBackground(getDrawable(R.drawable.buttor_checked));
                break;
        }
    }

    private void highlightCurrent(String text) {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        int code;
        switch (text) {
            case "X Press":
                code = sharedPreferences.getInt("X_PRESS", KeyEvent.KEYCODE_ESCAPE);
                highlightAction(code);
                break;
            case "X Hold":
                code = sharedPreferences.getInt("X_HOLD", KeyEvent.KEYCODE_T);
                highlightAction(code);
                break;
            case "⬜ Press":
                code = sharedPreferences.getInt("Y_PRESS", KeyEvent.KEYCODE_F4);
                highlightAction(code);
                break;
            case "⬜ Hold":
                code = sharedPreferences.getInt("Y_HOLD", KeyEvent.KEYCODE_F3);
                highlightAction(code);
                break;
            case "O Press":
                code = sharedPreferences.getInt("O_PRESS", KeyEvent.KEYCODE_M);
                highlightAction(code);
                break;
            case "O Hold":
                code = sharedPreferences.getInt("O_HOLD", KeyEvent.KEYCODE_F1);
                highlightAction(code);
                break;
            case "△ Press":
                code = sharedPreferences.getInt("T_PRESS", KeyEvent.KEYCODE_F6);
                highlightAction(code);
                break;
            case "△ Hold":
                code = sharedPreferences.getInt("T_HOLD", KeyEvent.KEYCODE_F2);
                highlightAction(code);
                break;
            case "Stick Press":
                code = sharedPreferences.getInt("STICK_PRESS", KeyEvent.KEYCODE_ENTER);
                highlightAction(code);
                break;
            case "Stick Hold":
                code = sharedPreferences.getInt("STICK_HOLD", KeyEvent.KEYCODE_P);
                highlightAction(code);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
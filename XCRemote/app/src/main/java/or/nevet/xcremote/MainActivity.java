package or.nevet.xcremote;

import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    volatile boolean foreground = true;
    volatile boolean isAllowedK = false;
    volatile boolean isAllowedA = false;
    public static boolean isOpen = false;
    volatile boolean updated = false;
    private byte[] firstFile;
    private byte[] secondFile;
    private boolean isFirst;
    private boolean justStarted = true;
    private boolean isSecond;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Uri uri = result.getData().getData();
                            OutputStream os = getContentResolver().openOutputStream(uri);
                            if (isFirst) {
                                isFirst = false;
                                os.write(firstFile);
                                firstFile = null;
                            }
                            else if (isSecond) {
                                os.write(secondFile);
                                updated = true;
                                Toast.makeText(MainActivity.this, "The files were updated successfully :)",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            new AlertDialog.Builder(MainActivity.this).setMessage("Please send this to the developer:\n\n"+e.toString()).create().show();
                            updated = true;
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOpen = true;
        setContentView(R.layout.activity_main);
        CheckNetwork network = new CheckNetwork(getApplicationContext());
        network.registerNetworkCallback();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!foreground || !isAllowedK || !isAllowedA || !updated);
                firstFile = null;
                secondFile = null;
                Intent intent3 = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(intent3);
            }
        }).start();
        if (mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkPermissionOverlay();
                                Toast.makeText(MainActivity.this, "Checking for updates...", Toast.LENGTH_LONG).show();
                                checkForUpdates();
                            } else {
                                new AlertDialog.Builder(MainActivity.this).setMessage("Please make sure you have an internet connection and reenter the app. You need internet connection in order for us to check for updates. If it is still does not work, please contact the developer.").create().show();
                            }
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Checking for updates...", Toast.LENGTH_LONG).show();
            checkForUpdates();
        }
        checkPermissionKeyboard();
        checkPermissionAccessibility();
    }

    public void checkPermissionKeyboard() {
        if (!isImeEnabled())  {
            new AlertDialog.Builder(this).setMessage("Do you want to allow the keyboard? It has to be allowed for the app to work...").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                    startActivity(enableIntent);
                    foreground = false;
                    isAllowedK = true;
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isAllowedK = true;
                }
            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    isAllowedK = true;
                }
            }).show();
        }
        else
            isAllowedK = true;
    }

    public void checkPermissionAccessibility() {
        if (!isAccessibilityServiceEnabled(this, AccessibilityKeyDetector.class)) {
            new AlertDialog.Builder(this).setMessage("Do you want to allow the accessibility service? It has to be allowed for the app to work...").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    /** if not construct intent to request permission */
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    /** request permission via start activity for result */
                    startActivity(intent);
                    foreground = false;
                    isAllowedA = true;
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isAllowedA = true;
                }
            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    isAllowedA = true;
                }
            }).show();
        }
        else
            isAllowedA = true;
    }

    public void checkPermissionOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
                return true;
        }

        return false;
    }

    boolean isImeEnabled() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        for (InputMethodInfo imi : imm.getEnabledInputMethodList()) {
            if (imi.getPackageName().equals("or.nevet.xcremote")) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        foreground = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        startActivity(intent);
        foreground = true;
    }

    private void checkForUpdates() {
        if (CheckNetwork.isConnected) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference version = database.getReference("version");
            version.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    if ((long) snapshot.getValue() > getVersion()) {
                        StorageReference storageRef = storage.getReference("config_xcremote.xci");
                        StorageReference storageRef2 = storage.getReference("config_xcremote&xcvario.xci");
                        final long ONE_MEGABYTE = 1024 * 1024 * 100;
                        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                storageRef2.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes2) {
                                        saveFile(bytes2, false, (long) snapshot.getValue());
                                        saveFile(bytes, true, (long) snapshot.getValue());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        new AlertDialog.Builder(MainActivity.this).setMessage("There was an error when downloading updates. Please make sure that you are connected to the internet. If you are connected, please send this to the developer:\n\n" + exception.toString()).create().show();
                                        updated = true;
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                new AlertDialog.Builder(MainActivity.this).setMessage("There was an error when downloading updates. Please make sure that you are connected to the internet. If you are connected, please send this to the developer:\n\n" + exception.toString()).create().show();
                                updated = true;
                            }
                        });
                    } else
                        updated = true;
                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "There was an error. Please make sure that you are connected to the internet. If this message still appears, please contact the developer.", Toast.LENGTH_LONG).show();
                    updated = true;
                }
            });
        } else {
            new AlertDialog.Builder(MainActivity.this).setMessage("You are not connected to the internet. Please connect and after that click \"NEXT\", Or, if you can not connect, click \"SKIP\".").setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    checkForUpdates();
                }
            }).setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    updated = true;
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    checkForUpdates();
                }
            }).create().show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen = false;
    }

    private void saveFile(byte[] bytes, boolean isFirstFile, long vers) {
        Intent intent2 = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent2.addCategory(Intent.CATEGORY_OPENABLE);
        intent2.setType("*/*");
        String path = "XCSoarData";
        intent2.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("content://com.android.externalstorage.documents/document/primary%3A"+path));
        if (isFirstFile) {
            intent2.putExtra(Intent.EXTRA_TITLE, "config_xcremote.xci");
            new android.app.AlertDialog.Builder(this).setMessage("There is an update to the xcsoar files. The next window is supposed to bring you to the XCSoarData folder. If it does not do that, please navigate into there. On the next window, please press 'Save'. After this message, there would be another one, and you will need to allow it too and press 'Save' there too.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    isFirst = true;
                    firstFile = bytes;
                    mGetContent.launch(intent2);
                }
            }).show();
        }
        else {
            intent2.putExtra(Intent.EXTRA_TITLE, "config_xcremote&xcvario.xci");
            new android.app.AlertDialog.Builder(this).setMessage("There is an update to the xcsoar files. The next window is supposed to bring you to the XCSoarData folder. If it does not do that, please navigate into there. On the next window, please press 'Save'.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    isSecond = true;
                    secondFile = bytes;
                    saveVersion(vers);
                    mGetContent.launch(intent2);
                }
            }).show();
        }
    }

    private void saveVersion(long version) {
        SharedPreferences preferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("version", version);
        editor.apply();
    }

    private long getVersion() {
        SharedPreferences preferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        return preferences.getLong("version", 0);
    }
}
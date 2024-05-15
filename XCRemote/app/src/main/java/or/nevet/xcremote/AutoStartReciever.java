package or.nevet.xcremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (Settings.getAutoStart(context)) {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("or.nevet.xcremote");
            if (launchIntent != null)
                context.startActivity(launchIntent);
        }
    }
}
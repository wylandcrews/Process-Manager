package wyland.crews.api15;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.*;


public class MainActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager manager = (ActivityManager)
                getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningProcesses =
                manager.getRunningAppProcesses();
        if(runningProcesses != null && runningProcesses.size() > 0) {
            setListAdapter(new ListAdapter(this, runningProcesses));
        } else {
            Toast.makeText(getApplicationContext(), "No applications are running", Toast.LENGTH_LONG).show();
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogConfirm));
        alertBuilder.setCancelable(true);
        alertBuilder.setMessage("Process Manager opened! \nSelect a process to kill it.");
        alertBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Select a Process to Kill it.", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected void onListItemClick(final ListView l, View v, final int position, long id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogConfirm));
        alertBuilder.setCancelable(true);
        alertBuilder.setMessage("Are you sure you want to kill this process?");
        alertBuilder.setPositiveButton("Kill It!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int pid = ((RunningAppProcessInfo)getListAdapter().getItem(position)).pid;
                Process.killProcess(pid);
                ArrayAdapter adapter = (ArrayAdapter) l.getAdapter();
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Process " + pid + " Killed.", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Cancelled. Process still running.", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


}

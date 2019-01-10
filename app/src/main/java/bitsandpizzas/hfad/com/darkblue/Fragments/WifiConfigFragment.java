package bitsandpizzas.hfad.com.darkblue.Fragments;


import android.Manifest;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.EspNetUtil;

import java.util.List;

import bitsandpizzas.hfad.com.darkblue.EspUtils;
import bitsandpizzas.hfad.com.darkblue.R;

import static android.content.Context.WIFI_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class WifiConfigFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "WifiConfigActivity";

    private static final int REQUEST_PERMISSION = 0x01;

    private TextView mApSsidTV;
    private TextView mApBssidTV;
    private EditText mApPasswordET;
    private EditText mDeviceCountET;
    private RadioGroup mPackageModeGroup;
    private TextView mMessageTV;
    private Button mConfirmBtn;

 private  IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    private static EsptouchAsyncTask4 mTask;

    private boolean mReceiverRegistered = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            assert wifiManager != null;

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    WifiInfo wifiInfo;
                    if (intent.hasExtra(WifiManager.EXTRA_WIFI_INFO)) {
                        wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    } else {
                        wifiInfo = wifiManager.getConnectionInfo();
                    }
                    onWifiChanged(wifiInfo);
                    break;
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(wifiManager.getConnectionInfo());
                    onLocationChanged();
                    break;
            }
        }
    };

    private boolean mDestroyed = false;

    public WifiConfigFragment() {
        // Required empty public constructor
    }

    private void onLocationChanged() {
        boolean enable;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            enable = false;
        } else {
            boolean locationGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean locationNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            enable = locationGPS || locationNetwork;
        }

        if (!enable) {
            mMessageTV.setText(R.string.location_disable_message);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wifi_config, container, false);

        mApSsidTV = view.findViewById(R.id.ap_ssid_text);
        mApBssidTV = view.findViewById(R.id.ap_bssid_text);
        mApPasswordET = view.findViewById(R.id.ap_password_edit);
        mDeviceCountET = view.findViewById(R.id.device_count_edit);
        mDeviceCountET.setText("1");
        mPackageModeGroup = view.findViewById(R.id.package_mode_group);
        mMessageTV = view.findViewById(R.id.message);
        mConfirmBtn = view.findViewById(R.id.confirm_btn);
        mConfirmBtn.setEnabled(false);
        mConfirmBtn.setOnClickListener(this);

        TextView versionTV = view.findViewById(R.id.version_tv);
        versionTV.setText(IEsptouchTask.ESPTOUCH_VERSION);

        if (isSDKAtLeastP()) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_PERMISSION);
            } else {
                registerBroadcastReceiver();
            }

        } else {
            registerBroadcastReceiver();
        }










return  view;


    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmBtn) {
            byte[] ssid = mApSsidTV.getTag() == null ? ByteUtil.getBytesByString(mApSsidTV.getText().toString())
                    : (byte[]) mApSsidTV.getTag();
            byte[] password = ByteUtil.getBytesByString(mApPasswordET.getText().toString());
            byte [] bssid = EspNetUtil.parseBssid2bytes(mApBssidTV.getText().toString());
            byte[] deviceCount = mDeviceCountET.getText().toString().getBytes();
            byte[] broadcast = {(byte) (mPackageModeGroup.getCheckedRadioButtonId() == R.id.package_broadcast
                    ? 1 : 0)};

            if(mTask != null) {
                mTask.cancelEsptouch();
            }
            mTask = new EsptouchAsyncTask4();
            mTask.execute(ssid, bssid, password, deviceCount, broadcast);
        }

    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
                Toast.makeText(getActivity(), text,
                        Toast.LENGTH_LONG).show();
            }

        });
    }


    private void onWifiChanged(WifiInfo info) {
        if (info == null) {
            mApSsidTV.setText("");
            mApSsidTV.setTag(null);
            mApBssidTV.setTag("");
            mMessageTV.setText("");
            mConfirmBtn.setEnabled(false);

            if (mTask != null) {
                mTask.cancelEsptouch();
                mTask = null;
                new AlertDialog.Builder(getActivity())
                        .setMessage("Wifi disconnected or changed")
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            mApSsidTV.setText(ssid);
            mApSsidTV.setTag(ByteUtil.getBytesByString(ssid));
            byte[] ssidOriginalData = EspUtils.getOriginalSsidBytes(info);
            mApSsidTV.setTag(ssidOriginalData);

            String bssid = info.getBSSID();
            mApBssidTV.setText(bssid);

            mConfirmBtn.setEnabled(true);
            mMessageTV.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int frequence = info.getFrequency();
                if (frequence > 4900 && frequence < 5900) {
                    // Connected 5G wifi. Device does not support 5G
                    mMessageTV.setText(R.string.wifi_5g_message);
                }
            }
        }
    }

    private boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
       getActivity().registerReceiver(mReceiver, filter);
        mReceiverRegistered = true;
    }

    private   class EsptouchAsyncTask4 extends AsyncTask<byte[], Void, List<IEsptouchResult>> {


        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();
        private ProgressDialog mProgressDialog;
        private AlertDialog mResultDialog;
        private IEsptouchTask mEsptouchTask;

        EsptouchAsyncTask4() {

        }

        void cancelEsptouch() {
            cancel(true);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (mResultDialog != null) {
                mResultDialog.dismiss();
            }
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Esptouch is configuring, please wait for a moment...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Log.i(TAG, "progress dialog back pressed canceled");
                        }
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getActivity().getText(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            synchronized (mLock) {
                                if (__IEsptouchTask.DEBUG) {
                                    Log.i(TAG, "progress dialog cancel button canceled");
                                }
                                if (mEsptouchTask != null) {
                                    mEsptouchTask.interrupt();
                                }
                            }
                        }
                    });
            mProgressDialog.show();
        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {

            int taskResultCount;
            synchronized (mLock) {
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                byte[] deviceCountData = params[3];
                byte[] broadcastData = params[4];
                taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));

                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, getActivity());
                mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {

            mProgressDialog.dismiss();
            mResultDialog = new AlertDialog.Builder(getActivity())
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
            mResultDialog.setCanceledOnTouchOutside(false);
            if (result == null) {
                mResultDialog.setMessage("Create Esptouch task failed, the esptouch port could be used by other thread");
                mResultDialog.show();
                return;
            }

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = ")
                                .append(resultInList.getBssid())
                                .append(", InetAddress = ")
                                .append(resultInList.getInetAddress().getHostAddress())
                                .append("\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's ")
                                .append(result.size() - count)
                                .append(" more result(s) without showing\n");
                    }
                    mResultDialog.setMessage(sb.toString());
                } else {
                    mResultDialog.setMessage("Esptouch fail");
                }

                mResultDialog.show();
            }

         mTask = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!mDestroyed) {
                        registerBroadcastReceiver();
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDestroyed = true;
        if (mReceiverRegistered) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }
}

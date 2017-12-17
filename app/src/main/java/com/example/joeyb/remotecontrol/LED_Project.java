package com.example.joeyb.remotecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Set;
import java.util.UUID;
public class LED_Project extends AppCompatActivity {
    private ToggleButton tbE1, tbE2, tbE3;
    private String deviceName = "Joeys LED Sign";
    private String deviceAddress = "98:d3:32:10:75:91";
    private TextView R1, R2, R3, R4, G1, G2, G3, G4, B1, B2, B3, B4, LetterJ, LetterO, LetterE, LetterY, JColor, OColor, EColor, YColor;
    private BluetoothDevice mDevice;
    private ConnectedThread mConnectedThread;
    private String RJ, RO, RE, RY, GJ, GO, GE, GY, BJ, BO, BE, BY;
    private BluetoothAdapter mBTAdapter;
    private ConnectThread mConnectThread;
    private static final String TAG = "LED_Project";
    private SeekBar R1sb, R2sb, R3sb, R4sb, G1sb, G2sb, G3sb, G4sb, B1sb, B2sb, B3sb, B4sb;
    private boolean found = false;
    private OutputStream mmOutStream;
    ArrayList<byte[]> byteBuffer = new ArrayList();
    private int R1Val, R2Val, R3Val, R4Val, G1Val, G2Val, G3Val, G4Val, B1Val, B2Val, B3Val, B4Val;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led__project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else if (!mBTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, ActivityNumbers.BTAdapter);
            mBTAdapter.enable();

        }
        if(mBTAdapter != null) {
            Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if(deviceName.equals(device.getName())) {
                        Log.d(TAG, "onCreate: found the right device");
                        mDevice = device;
                        found = true;
                        Log.d(TAG, "onCreate: Name: " + mDevice.getName());
                    }
                }
            }
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            registerReceiver(mReceiver4, filter);
            mConnectThread = new ConnectThread(mDevice);
            mConnectThread.start();
            R1 = (TextView) findViewById(R.id.R1);
            R2 = (TextView) findViewById(R.id.R2);
            R3 = (TextView) findViewById(R.id.R3);
            R4 = (TextView) findViewById(R.id.R4);
            G1 = (TextView) findViewById(R.id.G1);
            G2 = (TextView) findViewById(R.id.G2);
            G3 = (TextView) findViewById(R.id.G3);
            G4 = (TextView) findViewById(R.id.G4);
            B1 = (TextView) findViewById(R.id.B1);
            B2 = (TextView) findViewById(R.id.B2);
            B3 = (TextView) findViewById(R.id.B3);
            B4 = (TextView) findViewById(R.id.B4);
            LetterJ = (TextView) findViewById(R.id.LetterJ);
            LetterO = (TextView) findViewById(R.id.LetterO);
            LetterE = (TextView) findViewById(R.id.LetterE);
            LetterY = (TextView) findViewById(R.id.LetterY);
            tbE1 = (ToggleButton) findViewById(R.id.tbE1);
            tbE1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        setProgressZero();
                        if(tbE2.isChecked())
                        tbE2.setChecked(false);
                        if(tbE3.isChecked())
                        tbE3.setChecked(false);
                        tbE1.setChecked(true);
                        try {
                            mConnectedThread.write("E1T".getBytes());
                        }catch(NullPointerException e){
                            Log.d(TAG, "onCheckedChanged: Could not write data " + e);
                            Toast.makeText(getApplicationContext(), "Device is not connected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!isChecked){
                        mConnectedThread.write("E1F".getBytes());
                        //Stop effect here
                    }
                }
            });
            tbE2 = (ToggleButton) findViewById(R.id.tbE2);
            tbE2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        setProgressZero();
                        if(tbE1.isChecked())
                        tbE1.setChecked(false);
                        if(tbE3.isChecked())
                        tbE3.setChecked(false);
                        tbE2.setChecked(true);
                        try {
                            mConnectedThread.write("E2T".getBytes());
                        }catch(NullPointerException e){
                            Log.d(TAG, "onCheckedChanged: Could not write data " + e);
                            Toast.makeText(getApplicationContext(), "Device is not connected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!isChecked){
                        try {
                            mConnectedThread.write("E2F".getBytes());
                        }catch(NullPointerException e){
                            Log.d(TAG, "onCheckedChanged: Could not write data " + e);
                            Toast.makeText(getApplicationContext(), "Device is not connected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            tbE3 = (ToggleButton) findViewById(R.id.tbE3);
            tbE3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        setProgressZero();
                        if(tbE1.isChecked())
                        tbE1.setChecked(false);
                        if(tbE2.isChecked())
                        tbE2.setChecked(false);
                        tbE3.setChecked(true);
                        try {
                            mConnectedThread.write("E3T".getBytes());
                        }catch(NullPointerException e){
                            Log.d(TAG, "onCheckedChanged: Could not write data " + e);
                            Toast.makeText(getApplicationContext(), "Device is not connected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!isChecked){
                        try {
                            mConnectedThread.write("E3F".getBytes());
                        }catch(NullPointerException e){
                            Log.d(TAG, "onCheckedChanged: Could not write data " + e);
                            Toast.makeText(getApplicationContext(), "Device is not connected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            R1sb = (SeekBar) findViewById(R.id.R1sb);
            R2sb = (SeekBar) findViewById(R.id.R2sb);
            R3sb = (SeekBar) findViewById(R.id.R3sb);
            R4sb = (SeekBar) findViewById(R.id.R4sb);
            G1sb = (SeekBar) findViewById(R.id.G1sb);
            G2sb = (SeekBar) findViewById(R.id.G2sb);
            G3sb = (SeekBar) findViewById(R.id.G3sb);
            G4sb = (SeekBar) findViewById(R.id.G4sb);
            B1sb = (SeekBar) findViewById(R.id.B1sb);
            B2sb = (SeekBar) findViewById(R.id.B2sb);
            B3sb = (SeekBar) findViewById(R.id.B3sb);
            B4sb = (SeekBar) findViewById(R.id.B4sb);
            JColor = (TextView) findViewById(R.id.JColor);
            OColor = (TextView) findViewById(R.id.OColor);
            EColor = (TextView) findViewById(R.id.EColor);
            YColor = (TextView) findViewById(R.id.YColor);
            SharedPreferences sharedPref = getSharedPreferences("Preferences", MODE_PRIVATE);
            R1Val = sharedPref.getInt("JRsb", 0);
            G1Val = sharedPref.getInt("JGsb", 0);
            B1Val = sharedPref.getInt("JBsb", 0);
            R2Val = sharedPref.getInt("ORsb", 0);
            G2Val = sharedPref.getInt("OGsb", 0);
            B2Val = sharedPref.getInt("OBsb", 0);
            R3Val = sharedPref.getInt("ERsb", 0);
            G3Val = sharedPref.getInt("EGsb", 0);
            B3Val = sharedPref.getInt("EBsb", 0);
            R4Val = sharedPref.getInt("YRsb", 0);
            G4Val = sharedPref.getInt("YGsb", 0);
            B4Val = sharedPref.getInt("YBsb", 0);
            R1sb.setProgress(R1Val);
            R2sb.setProgress(R2Val);
            R3sb.setProgress(R3Val);
            R4sb.setProgress(R4Val);
            G1sb.setProgress(G1Val);
            G2sb.setProgress(G2Val);
            G3sb.setProgress(G3Val);
            G4sb.setProgress(G4Val);
            B1sb.setProgress(B1Val);
            B2sb.setProgress(B2Val);
            B3sb.setProgress(B3Val);
            B4sb.setProgress(B4Val);
            boolean temp1, temp2, temp3;
            temp1 = sharedPref.getBoolean("E1", false);
            temp2 = sharedPref.getBoolean("E2", false);
            temp3 = sharedPref.getBoolean("E3", false);
            tbE1.setChecked(temp1);
            tbE2.setChecked(temp2);
            tbE3.setChecked(temp3);
            R1sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    R1Val = progress;
                    RJ = "JR" + (255 - progress);
                    JColor.setBackgroundColor(Color.rgb(R1Val, G1Val, B1Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(RJ.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            R2sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    R2Val = progress;
                    RO = "OR" + (255 - progress);
                    OColor.setBackgroundColor(Color.rgb(R2Val, G2Val, B2Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(RO.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            R3sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    R3Val = progress;
                    RE = "ER" + (255 - progress);
                    EColor.setBackgroundColor(Color.rgb(R3Val, G3Val, B3Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(RE.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            R4sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    R4Val = progress;
                    RY = "YR" + (255 - progress);
                    YColor.setBackgroundColor(Color.rgb(R4Val, G4Val, B4Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(RY.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            G1sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    G1Val = progress;
                    GJ = "JG" + (255 - progress);
                    JColor.setBackgroundColor(Color.rgb(R1Val, G1Val, B1Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    //Log.d(TAG, "onProgressChanged: " + GJ);
                    if(fromUser)
                    mConnectedThread.write(GJ.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            G2sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    G2Val = progress;
                    GO = "OG" + (255 - progress);
                    OColor.setBackgroundColor(Color.rgb(R2Val, G2Val, B2Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(GO.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            G3sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    G3Val = progress;
                    GE = "EG" + (255 - progress);
                    EColor.setBackgroundColor(Color.rgb(R3Val, G3Val, B3Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(GE.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            G4sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    G4Val = progress;
                    GY = "YG" + (255 - progress);
                    YColor.setBackgroundColor(Color.rgb(R4Val, G4Val, B4Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(GY.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            B1sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    B1Val = progress;
                    BJ = "JB" + (255 - progress);
                    JColor.setBackgroundColor(Color.rgb(R1Val, G1Val, B1Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    Log.d(TAG, "onProgressChanged: " + BJ);
                    if(fromUser)
                    mConnectedThread.write(BJ.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            B2sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    B2Val = progress;
                    BO = "OB" + (255 - progress);
                    OColor.setBackgroundColor(Color.rgb(R2Val, G2Val, B2Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(BO.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            B3sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    B3Val = progress;
                    BE = "EB" + (255 - progress);
                    EColor.setBackgroundColor(Color.rgb(R3Val, G3Val, B3Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(BE.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            B4sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    B4Val = progress;
                    BY = "YB" + (255 - progress);
                    YColor.setBackgroundColor(Color.rgb(R4Val, G4Val, B4Val));
                    tbE1.setChecked(false);
                    tbE2.setChecked(false);
                    tbE3.setChecked(false);
                    if(fromUser)
                    mConnectedThread.write(BY.getBytes());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void btnOffJ(View view) {
        R1sb.setProgress(0, true);
        B1sb.setProgress(0, true);
        G1sb.setProgress(0, true);
        mConnectedThread.write("JF".getBytes());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void btnOffO(View view) {
        R2sb.setProgress(0, true);
        B2sb.setProgress(0, true);
        G2sb.setProgress(0, true);
        mConnectedThread.write("OF".getBytes());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void btnOffE(View view){
        R3sb.setProgress(0, true);
        B3sb.setProgress(0, true);
        G3sb.setProgress(0, true);
        mConnectedThread.write("EF".getBytes());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void btnOffY(View view){
        R4sb.setProgress(0, true);
        B4sb.setProgress(0, true);
        G4sb.setProgress(0, true);
        mConnectedThread.write("YF".getBytes());
    }

    private class ConnectThread extends Thread {
            private BluetoothSocket mmSocket;
            private final BluetoothServerSocket mmServer;
            public ConnectThread(BluetoothDevice device) {
                BluetoothServerSocket tmp = null;
                try{
                    tmp = mBTAdapter.listenUsingRfcommWithServiceRecord("Name", MY_UUID);
                }catch(IOException e){
                    Log.d(TAG, "ConnectThread: Could not create server socket" + e);
                }
                mmServer = tmp;
                Log.d(TAG, "ConnectThread: mmServer = " + mmServer );
            }
            public void run() {
                BluetoothSocket socket;
                // Keep listening until exception occurs or a socket is returned.
                while (true) {
                    try {
                        Log.d(TAG, "run: Attempting to accept");
                        Method m = mDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
                        socket = (BluetoothSocket) m.invoke(mDevice, Integer.valueOf(1));
                    } catch (Exception e) {
                        Log.e(TAG, "Socket connection failed", e);
                        break;
                    }
                    if (socket != null) {
                        // A connection was accepted. Perform work associated with
                        // the connection in a separate thread.
                        Log.d(TAG, "run: Socket received");
                        mmSocket = socket;
                        mConnectedThread = new ConnectedThread(socket);
                        mConnectedThread.start();
                        break;
                    }
                }
            }

            // Closes the connect socket and causes the thread to finish.
            public void cancel() {
                try {
                    mmServer.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
            }
        }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Constructor started");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            //WriteData writeData = new WriteData();
            //writeData.start();
            Log.d(TAG, "ConnectedThread: " + mmOutStream);
            Log.d(TAG, "ConnectedThread: Constructor finished");
        }

        public void run() {
            Log.d(TAG, "run: ConnectedThread run called");
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                if(!mmSocket.isConnected()){
                    try{
                        mmSocket.connect();
                    }catch (IOException e){
                        Log.d(TAG, "run: Could not connect" + e);
                    }
                }
                try {
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    for(int i = begin; i < bytes; i++) {
                        String message = "hi";
                        if(buffer[i] == message.getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, "run: Thread is exiting");
                    Log.d(TAG, "write: " + e);
                    break;
                }
            }
        }
        public void write(byte[] bytes) {
            //byteBuffer.add(bytes);
            try{
                mmOutStream.write(bytes);
            }catch(IOException e){
                Log.d(TAG, "write: Writing failed" + e);
            }
            Log.d(TAG, "write: Byte-" + bytes);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel: " + mmSocket);
                mmSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "cancel: " + e);
            }
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;

            switch(msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        /*mConnectThread.cancel();
        mConnectThread.start();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*mConnectedThread.cancel();
        mConnectThread.cancel();
        mConnectThread.start();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnectedThread.cancel();
        mConnectThread.cancel();
        mBTAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver4);
    }
    private BroadcastReceiver mReceiver4 = new BroadcastReceiver(){
        @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
        @Override
        public void onReceive(Context context, Intent intent){
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
                BluetoothDevice mmDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(mmDevice.getName().equals(deviceName)){
                    mConnectThread = new ConnectThread(mmDevice);
                    mConnectThread.start();
                }
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setProgressZero() {
        R1sb.setProgress(0, true);
        B1sb.setProgress(0, true);
        G1sb.setProgress(0, true);
        R2sb.setProgress(0, true);
        B2sb.setProgress(0, true);
        G2sb.setProgress(0, true);
        R3sb.setProgress(0, true);
        B3sb.setProgress(0, true);
        G3sb.setProgress(0, true);
        R4sb.setProgress(0, true);
        B4sb.setProgress(0, true);
        G4sb.setProgress(0, true);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: onStop called");
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
        editor.putInt("JRsb", R1Val);
        editor.putInt("JGsb", G1Val);
        editor.putInt("JBsb", B1Val);
        editor.putInt("ORsb", R2Val);
        editor.putInt("OGsb", G2Val);
        editor.putInt("OBsb", B2Val);
        editor.putInt("ERsb", R3Val);
        editor.putInt("EGsb", G3Val);
        editor.putInt("EBsb", B3Val);
        editor.putInt("YRsb", R4Val);
        editor.putInt("YGsb", G4Val);
        editor.putInt("YBsb", B4Val);
        editor.putBoolean("E1", tbE1.isChecked());
        editor.putBoolean("E2", tbE2.isChecked());
        editor.putBoolean("E3", tbE3.isChecked());
        editor.apply();
    }
    /*    public class WriteData extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(0, 100);
                }catch(InterruptedException e){
                    Log.d(TAG, "run: Interrupted exception thrown" + e);
                }
                if(byteBuffer.size() > 0){
                    try{
                        mmOutStream.write(byteBuffer.get(0));
                        Log.d(TAG, "run: Byte received-" + byteBuffer.get(0));
                        byteBuffer.remove(0);
                    }catch(IOException e){
                        Log.d(TAG, "run: Failed to write data from buffer");
                    }
                }
            }
        }
    }*/
}
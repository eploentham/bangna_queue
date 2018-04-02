package com.bangna.ekapop.bangna_queue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlusActivity extends AppCompatActivity {
    public final static char LF  = (char) 0x0A;
    public final static char TAB  = (char) 0x09;

    JsonParser jsonparser = new JsonParser();
    String ab;
    Boolean pageLoad = false;
    JSONArray jarrS, jarrQ, jarrP;

    TextView lbPStaff, lbPQLast, lbPQOnhand, lbPQCurrent1,lbPQOnhand1,myLabel;
    Spinner cboPStaff;
    Button btnPPlus;
    QueueControl qc;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";

    public ArrayList<String> sCboTable = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
//        qc = (QueueControl) getIntent().getSerializableExtra("QueueControl");
        qc = new QueueControl();

        pageLoad = true;
        lbPQLast = findViewById(R.id.lbPQLast);
        lbPStaff = findViewById(R.id.lbPStaff);
        lbPQCurrent1 = findViewById(R.id.lbPQLast1);
        lbPQOnhand = findViewById(R.id.lbPQOnhand);
        lbPQOnhand1 = findViewById(R.id.lbPQOnhand1);
        myLabel = findViewById(R.id.myLabel);

        cboPStaff = findViewById(R.id.cboPStaff);
        btnPPlus = findViewById(R.id.btnPPlus);

        lbPStaff.setText(R.string.lbPStaff);
        lbPQLast.setText(R.string.lbPQLast);
        lbPQOnhand.setText(R.string.lbPQOnhand);
        //lbPQPlus.setText(R.string.lbPQPlus);
        btnPPlus.setText(R.string.btnPPlus);
        myLabel.setText("");

        new retrieveDoctor().execute();

        cboPStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!pageLoad){
                    String sfId = qc.getStaff(cboPStaff.getSelectedItem().toString(),"id");
                    new retrieveDoctorQueueLast().execute(sfId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnPPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sfId = qc.getStaff(cboPStaff.getSelectedItem().toString(),"id");
                String row = lbPQCurrent1.getText().toString();
                row = String.valueOf(Integer.parseInt(row)+1);
                new insertQueue().execute(sfId, row);
            }
        });
        pageLoad= false;
    }
    class insertQueue extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            //Log.d("Login attempt", jobj.toString());arg0[0]
//            try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userdb",qc.UserDB));
            params.add(new BasicNameValuePair("passworddb",qc.PasswordDB));
            params.add(new BasicNameValuePair("staff_id",arg0[0]));
            params.add(new BasicNameValuePair("row_1",arg0[1]));
            jarrP = jsonparser.getJSONFromUrl(qc.hostInsertQueue,params);

//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            return ab;
        }
        @Override
        protected void onPostExecute(String ab){
            String aaa = ab;
            printQueue();
        }
        @Override
        protected void onPreExecute() {
            String aaa = ab;

        }
    }
    private void printQueue(){
        if(jarrP!=null){
            try {
                for (int i = 0; i < jarrP.length(); i++) {
                    JSONObject catObj = (JSONObject) jarrP.get(i);
                    if(catObj.getString("success").equals("ok")){
                        lbPQCurrent1.setText(catObj.getString("remark"));
                        lbPQOnhand1.setText(catObj.getString("onhand"));
                        printSlip(lbPQCurrent1.getText().toString());
                    }
                }
            } catch (JSONException ex) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class retrieveDoctorQueueLast extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            //Log.d("Login attempt", jobj.toString());arg0[0]
//            try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userdb",qc.UserDB));
            params.add(new BasicNameValuePair("passworddb",qc.PasswordDB));
            params.add(new BasicNameValuePair("staff_id",arg0[0]));
            jarrQ = jsonparser.getJSONFromUrl(qc.hostGetDoctorQueueLast,params);

//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            return ab;
        }
        @Override
        protected void onPostExecute(String ab){
            String aaa = ab;
            setQueue();
        }
        @Override
        protected void onPreExecute() {
            String aaa = ab;

        }
    }
    private void setQueue(){
        if(jarrQ!=null){
            try {
                for (int i = 0; i < jarrQ.length(); i++) {
                    JSONObject catObj = (JSONObject) jarrQ.get(i);
                    lbPQCurrent1.setText(catObj.getString(qc.sf.dbRow1));
                    lbPQOnhand1.setText(catObj.getString("onhand"));

                }
            } catch (JSONException ex) {

            }
        }
        pageLoad=false;
    }

    class retrieveDoctor extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            //Log.d("Login attempt", jobj.toString());
//            try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userdb",qc.UserDB));
            params.add(new BasicNameValuePair("passworddb",qc.PasswordDB));
            jarrS = jsonparser.getJSONFromUrl(qc.hostGetDoctor,params);

//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            return ab;
        }
        @Override
        protected void onPostExecute(String ab){
            String aaa = ab;
            setCboStaff();

        }
        @Override
        protected void onPreExecute() {
            String aaa = ab;

        }
    }
    private void setCboStaff(){
        if(jarrS!=null){
            qc.sCboStaff.clear();
            qc.sStaff.clear();
            //JSONArray categories = jobj.getJSONArray("area");
            //JSONArray json = new JSONArray(jobj);
            try {
                for (int i = 0; i < jarrS.length(); i++) {
                    JSONObject catObj = (JSONObject) jarrS.get(i);
                    qc.sCboStaff.add(catObj.getString("prefix")+ " " + catObj.getString(qc.sf.dbFNameT)+ " " + catObj.getString(qc.sf.dbLNameT));
                    qc.sStaff.add(catObj.getString(qc.sf.dbID)+"@"+catObj.getString(qc.sf.dbCode)+"@"+catObj.getString(qc.sf.dbFNameT)+"@"+catObj.getString(qc.sf.dbLNameT)+"@"+catObj.getString("prefix"));
                }
                ArrayAdapter<String> adaStaff = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,qc.sCboStaff);
                pageLoad = true;
                cboPStaff.setAdapter(adaStaff);
                pageLoad = false;
                String sfId = qc.getStaff(cboPStaff.getSelectedItem().toString(),"id");
                new retrieveDoctorQueueLast().execute(sfId);

                //imageArea.setImageResource(R.drawable.green1);
            }catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("setCboArea ",e.getMessage());
            }
        }
    }
    private  void printSlip(String data) throws IOException {

        DateFormat df = new android.text.format.DateFormat();
        String aa = df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
        String doc = cboPStaff.getSelectedItem().toString();
        findBT();
        openBT();
        printCustom("\n",0,0);
        printCustom("\n",0,0);
        printCustom("Bangna1 General Hospital",3,1);
        //printCustom("\n",0,0);
        //printCustom("แพทย์ "+doc,4,1);
        printCustom("\n",0,0);
        printCustom(data,4,1);
        printCustom("\n",0,0);
        printCustom("Date : "+aa,0,0);
        printCustom("\n",0,0);
        printCustom("\n",0,0);
        printCustom("\n",0,0);
        printCustom("\n",0,0);
        closeBT();
    }
    private void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("TPA310")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    private void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendData(String data) throws IOException {
        try {

            // the text typed by the user
            String msg = data;
            msg += LF;
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            mmOutputStream.write(msg.getBytes());

            // tell the user data were sent
            myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        byte[] bb4 = new byte[]{0x1B,0x21,0x40}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
                case 4:
                    mmOutputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

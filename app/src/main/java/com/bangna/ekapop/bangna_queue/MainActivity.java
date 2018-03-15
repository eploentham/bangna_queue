package com.bangna.ekapop.bangna_queue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    JsonParser jsonparser = new JsonParser();
    JSONArray jarrS;

    TextView lbMMessage;
    Button btnMPlus, btnMMinus, btnMSetup;
    ImageView imageStaff;

    QueueControl qc;

    String ab;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMMinus = findViewById(R.id.btnMMinus);
        btnMPlus = findViewById(R.id.btnMPlus);
        btnMSetup = findViewById(R.id.btnMSetup);
        lbMMessage = findViewById(R.id.lbMMessage);
        imageStaff = findViewById(R.id.imageStaff);

        btnMMinus.setText(R.string.btnMMinus);
        btnMPlus.setText(R.string.btnMPlus);
        btnMSetup.setText(R.string.btnMSetup);
        lbMMessage.setText("");

        qc = new QueueControl();

        try {
            final int READ_BLOCK_SIZE = 100;
            //File file = new File("initial.cnf");
            File file = getFileStreamPath("initial.cnf");
            if(file.exists()){
                FileInputStream fileIn = openFileInput("initial.cnf");
                InputStreamReader InputRead= new InputStreamReader(fileIn);
                char[] inputBuffer= new char[READ_BLOCK_SIZE];
                String s="";
                int charRead;

                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    s +=readstring;
                }
                InputRead.close();
//                    txtIaHost.setText(s);
                String[] p = s.split(";");
                if(p.length>0){
                    qc.hostIP = p[0].replace("host=","");
//                        txtIaPrint.setText(p[1].replace("printer=",""));
//                        rs.hostIP = s;
//                txtIaHost.setText(p[0].replace("host=",""));
                    //txtIaPrint.setText(p[1].replace("printer=",""));
                    qc.hostPORT =  p[4].replace("PortNumber=","").replace("\n","");
                    qc.hostWebDirectory =p[5].replace("WebDirectory=","").replace("\n","");
                    qc.UserDB =p[6].replace("UserDB=","").replace("\n","");
                    qc.PasswordDB =p[7].replace("PasswordDB=","").replace("\n","");
                    qc.TextSize =p[8].replace("TextSize=","").replace("\n","");
                    qc.AccessMode =p[13].replace("AccessMethod=","").replace("\n","");
                    qc.HostID =p[12].replace("HostID=","").replace("\n","");
//                txtIaTaxID.setText(p[3].replace("TaxID=",""));
//                txtIaPortID.setText(p[4].replace("PortNumber=",""));
//                txtIaWebDirectory.setText(p[5].replace("WebDirectory=",""));
                }
                fileIn.close();
            }

            qc.refresh();
//            }
        }catch (Exception e) {
            e.printStackTrace();
//            fileErr=true;
        }
        if(!qc.hostIP.equals("")) {
            new chkServerReachable().execute();
        }

        btnMPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(view.getContext(), PlusActivity.class).putExtra("QueueControl",qc),0);
            }
        });
        btnMMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(view.getContext(), MinusActivity.class).putExtra("QueueControl",qc),0);
            }
        });
    }
    class retrieveStaff extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            //Log.d("Login attempt", jobj.toString());
//            try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userdb",qc.UserDB));
            params.add(new BasicNameValuePair("passworddb",qc.PasswordDB));
            jarrS = jsonparser.getJSONFromUrl(qc.hostGetStaff,params);

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
                    qc.sCboStaff.add(catObj.getString(qc.sf.dbNameT));
                    qc.sStaff.add(catObj.getString(qc.sf.dbID)+"@"+catObj.getString(qc.sf.dbCode)+"@"+catObj.getString(qc.sf.dbNameT));
                }
                imageStaff.setImageResource(R.drawable.green);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("setCboStaff ",e.getMessage());
            }
        }
    }
    class chkServerReachable extends AsyncTask<String,String,String> {
        Boolean chk = false;

        @Override
        protected String doInBackground(String... arg0) {
            //Log.d("Login attempt", jobj.toString());
            Socket socket;
//            final String host = "10.0.1.51";
            final int port = 80;
            final int timeout = 20000;   // 10 seconds

            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(qc.hostIP, port), timeout);
                chk = true;
            } catch (UnknownHostException uhe) {
                chk = false;
                Log.e("ServerSock", "I couldn't resolve the host you've provided!");
                pd.dismiss();
                ab = "ServerSock I couldn't resolve the host you've provided!";
            } catch (SocketTimeoutException ste) {
                chk = false;
                Log.e("ServerSock", "After a reasonable amount of time, I'm not able to connect, Server is probably down!");
                pd.dismiss();
                ab = "ServerSock After a reasonable amount of time, I'm not able to connect, Server is probably down!";
            } catch (IOException ioe) {
                chk = false;
                Log.e("ServerSock", "Hmmm... Sudden disconnection, probably you should start again!");
                pd.dismiss();
                ab = "ServerSock Hmmm... Sudden disconnection, probably you should start again!";
            }
            return ab + qc.hostIP;
        }

        @Override
        protected void onPostExecute(String ab) {
//            chk=true;
            if (chk) {
                new retrieveStaff().execute();

            } else {
                lbMMessage.setVisibility(View.VISIBLE);
                lbMMessage.setText(ab);
            }
            pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setTitle("Loading...");
            pd.setMessage("Loading information...");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setProgress(0);
            pd.show();

        }
    }
}

package com.bangna.ekapop.bangna_queue;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlusActivity extends AppCompatActivity {
    JsonParser jsonparser = new JsonParser();
    String ab;
    Boolean pageLoad = false;
    JSONArray jarrS, jarrQ;

    TextView lbPStaff, lbPQLast, lbPQPlus, lbPQCurrent1;
    Spinner cboPStaff;
    Button btnPPlus;
    QueueControl qc;

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

        cboPStaff = findViewById(R.id.cboPStaff);
        btnPPlus = findViewById(R.id.btnPPlus);

        lbPStaff.setText(R.string.lbPStaff);
        lbPQLast.setText(R.string.lbPQLast);
        //lbPQPlus.setText(R.string.lbPQPlus);
        btnPPlus.setText(R.string.btnPPlus);

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
            params.add(new BasicNameValuePair("row_1",arg0[0]));
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
                    qc.sCboStaff.add(catObj.getString(qc.sf.dbFNameT)+ " " + catObj.getString(qc.sf.dbLNameT));
                    qc.sStaff.add(catObj.getString(qc.sf.dbID)+"@"+catObj.getString(qc.sf.dbCode)+"@"+catObj.getString(qc.sf.dbFNameT)+"@"+catObj.getString(qc.sf.dbLNameT));
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
}

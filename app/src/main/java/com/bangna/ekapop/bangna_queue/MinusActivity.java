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

public class MinusActivity extends AppCompatActivity {
    JsonParser jsonparser = new JsonParser();
    String ab, queueId="";
    Boolean pageLoad = false;
    JSONArray jarrS, jarrQ, jarrP;

    TextView lbMiStaff, lbMiQCurrent,lbMiQCurrent1;
    Button btnMiMinus;
    Spinner cboMiStaff;

    QueueControl qc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minus);

        qc = new QueueControl();
        pageLoad = true;

        lbMiQCurrent = findViewById(R.id.lbMiQCurrent);
        lbMiStaff = findViewById(R.id.lbMiStaff);
        btnMiMinus = findViewById(R.id.btnMiMinus);
        cboMiStaff = findViewById(R.id.cboMiStaff);
        lbMiQCurrent1 = findViewById(R.id.lbMiQCurrent1);

        lbMiStaff.setText(R.string.lbMiStaff1);
        lbMiQCurrent.setText(R.string.lbMiQCurrent);
        btnMiMinus.setText(R.string.btnMiMinus);
        cboMiStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!pageLoad){
                    String sfId = qc.getStaff(cboMiStaff.getSelectedItem().toString(),"id");
                    new retrieveDoctorQueueFirst().execute(sfId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnMiMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sfId = qc.getStaff(cboMiStaff.getSelectedItem().toString(),"id");

                new updateQueueFinish().execute(sfId,queueId);
            }
        });

        new retrieveDoctor().execute();
    }
    class updateQueueFinish extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            //Log.d("Login attempt", jobj.toString());arg0[0]
//            try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userdb",qc.UserDB));
            params.add(new BasicNameValuePair("passworddb",qc.PasswordDB));
            params.add(new BasicNameValuePair("staff_id",arg0[0]));
            params.add(new BasicNameValuePair("queue_id",arg0[1]));
            jarrP = jsonparser.getJSONFromUrl(qc.hostUpdateQueueFinish,params);

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
//                        lbMiQCurrent1.setText(catObj.getString("remark"));
                        btnMiMinus.setEnabled(false);
                    }
                }
            } catch (JSONException ex) {

            }
        }
    }
    class retrieveDoctorQueueFirst extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            //Log.d("Login attempt", jobj.toString());arg0[0]
//            try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userdb",qc.UserDB));
            params.add(new BasicNameValuePair("passworddb",qc.PasswordDB));
            params.add(new BasicNameValuePair("staff_id",arg0[0]));
            jarrQ = jsonparser.getJSONFromUrl(qc.hostGetDoctorQueueFirst,params);

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
                    lbMiQCurrent1.setText(catObj.getString(qc.sf.dbRow1));
                    queueId = catObj.getString("queue_id");
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
                cboMiStaff.setAdapter(adaStaff);
                pageLoad = false;
                String sfId = qc.getStaff(cboMiStaff.getSelectedItem().toString(),"id");
                new retrieveDoctorQueueFirst().execute(sfId);

                //imageArea.setImageResource(R.drawable.green1);
            }catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("setCboArea ",e.getMessage());
            }
        }
    }
}

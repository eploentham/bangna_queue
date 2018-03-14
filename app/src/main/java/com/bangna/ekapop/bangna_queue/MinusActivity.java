package com.bangna.ekapop.bangna_queue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MinusActivity extends AppCompatActivity {

    TextView lbMiStaff, lbMiQCurrent;
    Button btnMiMinus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minus);

        lbMiQCurrent = findViewById(R.id.lbMiQCurrent);
        lbMiStaff = findViewById(R.id.lbMiStaff);
        btnMiMinus = findViewById(R.id.btnMiMinus);

        lbMiStaff.setText(R.string.lbMiStaff1);
        lbMiQCurrent.setText(R.string.lbMiQCurrent);
        btnMiMinus.setText(R.string.btnMiMinus);

    }
}

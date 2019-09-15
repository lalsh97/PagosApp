package com.example.pagosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class activity_detallespagos extends AppCompatActivity {

    private TextView txtDesarrollo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallespagos);

        txtDesarrollo = (TextView) findViewById(R.id.txtDesarrollo);

    }
}

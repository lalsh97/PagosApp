package com.example.pagosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class activity_detallespago extends AppCompatActivity {

    private TextView txtId, txtEstatus, txtMonto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallespago);

        txtId = (TextView) findViewById(R.id.txtId);
        txtEstatus = (TextView) findViewById(R.id.txtEstatus);
        txtMonto = (TextView) findViewById(R.id.txtMonto);

        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            verDetalles(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

   private void verDetalles(JSONObject response, String monto) {
        try{
            txtId.setText(response.getString("id"));
            txtEstatus.setText(response.getString("state"));
            txtMonto.setText("$"+monto);

        }catch(JSONException e){
            e.printStackTrace();
        }
   }
}

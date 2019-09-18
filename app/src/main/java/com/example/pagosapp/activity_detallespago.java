package com.example.pagosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

//Activity donde se mostrarán todos los detalles del pago realizado.
public class activity_detallespago extends AppCompatActivity {

    private TextView txtId, txtEstatus, txtMonto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallespago);

        //Se instancian objetos de tipo TextView para mostrar los detalles del pago.
        txtId = findViewById(R.id.txtId);
        txtEstatus = findViewById(R.id.txtEstatus);
        txtMonto = findViewById(R.id.txtMonto);

        //Se crea un objeto de tipo Intent al cual se le pasará el intent de la activity anterior a partir del método getIntent.
        Intent intent = getIntent();

        try{
            //Se crea un objeto de tipo JSON que almacena todos los detalles del pago realizado dentro de la API de PayPal.
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            //Se llama al método verDetalles que será quien muestre en pantalla todos los detalles del pago en los respectivos TextView.
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

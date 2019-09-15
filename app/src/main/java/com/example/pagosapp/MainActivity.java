package com.example.pagosapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pagosapp.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
    //Para usar la cuenta de sandbox como test.

    .clientId(Config.PAYPAL_CLIENTE_ID);

    Button btnPayPal;
    Button btnStipe;
    EditText txtMonto;

    String monto = "";

    @Override
    protected void onDestroy() {
        stopService( new Intent(this, PayPalService.class) );
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnPayPal = (Button) findViewById(R.id.btnPayPal);
        txtMonto = (EditText) findViewById(R.id.txtMonto);

        btnStipe = (Button) findViewById(R.id.btnStripe);


        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarPago();
            }
        });

        btnStipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this, activity_detallespagos.class));
            }
        });

    }

    private void procesarPago() {

        monto = txtMonto.getText().toString();

        String montoo ="55";
                                                                        //Mandar monto y no montoo.
        PayPalPayment payPalPayment = new PayPalPayment( new BigDecimal( String.valueOf(montoo)), "MXN","Pagado",PayPalPayment
        .PAYMENT_INTENT_SALE);

        //Enviar parámetros.

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if( resultCode == PAYPAL_REQUEST_CODE ) {
            if( resultCode ==RESULT_OK ) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if( confirmation != null ) {
                    try{
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity( new Intent(this, activity_detallespago.class).putExtra("PaymentDetails",paymentDetails)
                                .putExtra("PaymentAmount",monto));
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if( resultCode == Activity.RESULT_CANCELED )
                Toast.makeText(this,"Cancelada",Toast.LENGTH_SHORT).show();
        }else if( resultCode == PaymentActivity.RESULT_EXTRAS_INVALID )
                Toast.makeText(this,"Invalida",Toast.LENGTH_SHORT).show();
    }

}

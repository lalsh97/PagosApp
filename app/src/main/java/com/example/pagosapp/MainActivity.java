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

//Clase principal (La del menú principal).
public class MainActivity extends AppCompatActivity {

    //SE declara una variable de tipo entera que tendrá el valor de la solicitud a hacer.
    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                                                                                    //Para usar la cuenta de sandbox como test.

    .clientId(Config.PAYPAL_CLIENTE_ID); /*Se llama a la variable PayPal_Cliente_Id de la clase Config del paquete Config, esa variable
    almacena el código o el identificador de la cuenta de PayPal a la cual se le realizarán las transacciones a la hora de los pagos.
    Lo obtuve de una cuenta que creé dentro de PayPalSandbox.
    */


    Button btnPayPal;
    Button btnStipe;

    //Cuadro de texto para ingresar la cantidad a pagar, pero ya no lo utilize, porqué le pase la cantidad por medio de un String.
    EditText txtMonto;

    String monto = "20";

    /*No recuerdo que otra funcionalidad tenía este método además de eliminar la instancia del objeto Intent en caso de que hubiera un error
    o excepción.
    */
    @Override
    protected void onDestroy() {
        stopService( new Intent(this, PayPalService.class) );
        super.onDestroy();
    }


    //Método para instanciar los botones y poder mandar los datos de una activity a otra por medio de un objeto Intent.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Se crea un objeto de tipo Intent, de parámetros recibe el paquete para el cual o al cual necesita acceder
        en este caso es el mismo donde está esta clase, y la clase para la cual creará el Intent, que será la clase de los servicios
        de PayPal.

         */
        Intent intent = new Intent(this, PayPalService.class);
        //Se utiliza el método putExtra para enviar o transferir datos dentro de un Intent desde un activity a otro.
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent); //Se inicializa el servicio de la API de PayPal.

        btnPayPal = findViewById(R.id.btnPayPal);
        txtMonto =  findViewById(R.id.txtMonto);

        //btnStipe = (Button) findViewById(R.id.btnStripe);

        //Se agrega un Listener para que el botón tenga funcionalidad y en caso de que se presione manda a llamar al método procesarPago.

        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarPago();
            }
        });

        /*Igual al botón de pagar con Stripe se le agrega un listener que sólo cambia de activity cuando
        se presiona y ya no hace nada.
         */
        /*btnStipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this, activity_detallespagos.class));
            }
        });*/

    }

    private void procesarPago() {

        //Era para obtener el valor de la cantidad a pagar,que se ingreso al cuadro de texto.
        //monto = txtMonto.getText().toString();


        /*Se crea un objeto de tipo PayPalPayment, al cual se le envian 4 parámetros, el monto a pagar de tipo int, la abreviación del tipo de moneda
        de tipo String, un mensaje que aparacerá cuando se realice de forma exitosa el pago, y el atributo PAYMENT_INTENT_SALE.
         */
                                                                        // Mandar monto y no montoo.
        PayPalPayment payPalPayment = new PayPalPayment( new BigDecimal( String.valueOf(monto)), "MXN","Monto a pagar",PayPalPayment
        .PAYMENT_INTENT_SALE);


        /*Enviar parámetros.
        Se crea un Intent para que se redireccione a la vista para realizar el pago en PayPal.
         */
        Intent intent = new Intent(this, PaymentActivity.class);
        //se almacenan los datos que serán enviados por el Intent con el método putExtra(nombre del atributo, valor).
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);

        startActivityForResult(intent,PAYPAL_REQUEST_CODE); //Inicia la actividad y después manda a llamar al método onActivityResult
        //que es quien se encargará de la confirmación de pagos e inicio de sesión.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Se verifica que no haya errores en el inicio de sesión.
        if( requestCode == PAYPAL_REQUEST_CODE ) {
            if( resultCode == RESULT_OK ) {
                //Se crea un objeto de tipo PaymentConfirmation para despues verificar si no hubo errores al momento de realizar el pago.
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if( confirmation != null ) {
                    try{
                        //Se obtienen los detalles del pago ( cantidad
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity( new Intent(this, activity_detallespago.class).putExtra("PaymentDetails",paymentDetails)
                                .putExtra("PaymentAmount",monto));

                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if( resultCode == Activity.RESULT_CANCELED )
                Toast.makeText(this,"Transferencia cancelada",Toast.LENGTH_SHORT).show();
        }else if( resultCode == PaymentActivity.RESULT_EXTRAS_INVALID )
                Toast.makeText(this,"Pago inválido",Toast.LENGTH_SHORT).show();
    }

}

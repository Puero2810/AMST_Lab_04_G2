package com.example.amst_lab4_g2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrosTemp extends AppCompatActivity {
    private RequestQueue ListaRequest = null;
    private Button salir, agregar;
    private EditText tt1;
    private String id_fin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros_temp);
        tt1 = (EditText) findViewById(R.id.text_temp);
        agregar = (Button) findViewById(R.id.agregar_registro_temp);
        salir = (Button) findViewById(R.id.salir_registro_temp);
        ListaRequest = Volley.newRequestQueue(this);
        leer();

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregar_registro_temp();
            }
        });
    }

    public void leer(){
        String url_registros = "https://amst-lab-api.herokuapp.com/api/lecturas";
        JsonArrayRequest requestRegistros = new JsonArrayRequest(
                Request.Method.GET, url_registros, null,
                response -> {
                    mostrarTemperaturas(response);
                }, error -> System.out.println(error)
        );
        ListaRequest.add(requestRegistros);
    }

    private void mostrarTemperaturas(JSONArray temperaturas){
        JSONObject registroTemp;
        try {
            for (int i = 0; i < temperaturas.length(); i++) {
                registroTemp = temperaturas.getJSONObject(i);
                id_fin = registroTemp.getString("id");
            }
            System.out.println("Terminar");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }

    public void agregar_registro_temp(){
        int id = Integer.parseInt(id_fin);
        id++;

        String key = "temperatura";

        Double value = Double.valueOf(tt1.getText().toString());

        Date fecha = new Date();
        Calendar calendario = new GregorianCalendar();
        calendario.setTime(fecha);
        String dia = Integer.toString(calendario.get(Calendar.DATE));
        String mes = Integer.toString(calendario.get(Calendar.MONTH));
        String annio = Integer.toString(calendario.get(Calendar.YEAR));
        String hora = Integer.toString(calendario.get(Calendar.HOUR_OF_DAY));
        String minuto = Integer.toString(calendario.get(Calendar.MINUTE));
        String segundo = Integer.toString(calendario.get(Calendar.SECOND));
        String microsegundo = Integer.toString(calendario.get(Calendar.MILLISECOND));
        String date_created = annio+"-"+mes+"-"+dia+"T"+hora+":"+minuto+":"+segundo+"."+microsegundo;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("key", key);
        params.put("value", value);
        params.put("date_created", date_created);
        JSONObject parametros = new JSONObject(params);
        String url_registros = " https://amst-lab-api.herokuapp.com/api/lecturas";
        JsonObjectRequest requestRegistros = new JsonObjectRequest (
                Request.Method.POST, url_registros, parametros,
                response -> {

                }, error -> System.out.println(error)
        );
        ListaRequest.add(requestRegistros);
    }

    public void salir_registro_temp(View view){
        Intent intent= new Intent(RegistrosTemp.this, MainActivity.class);
        startActivity(intent);
    }
}
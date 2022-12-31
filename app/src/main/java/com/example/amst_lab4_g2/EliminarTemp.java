package com.example.amst_lab4_g2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EliminarTemp extends AppCompatActivity {
    private RequestQueue ListaRequest = null;
    private Button eliminar, salir;
    private Spinner s;
    List<String> listid = new ArrayList<String>();
    List<String> listemp = new ArrayList<String>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_temp);

        salir = (Button) findViewById(R.id.salir_eliminar_temp);

        ListaRequest = Volley.newRequestQueue(this);
        s = (Spinner) findViewById(R.id.lista_id);
        leer();

        eliminar = (Button) findViewById(R.id.eliminar_registro_temp);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = listemp.indexOf(s.getSelectedItem());
                i++;
                String url_registros = "https://amst-lab-api.herokuapp.com/api/lecturas/"+i;
                JsonArrayRequest requestRegistros = new JsonArrayRequest(
                        Request.Method.DELETE, url_registros, null,
                        response -> {

                        }, error -> System.out.println(error)
                );
                ListaRequest.add(requestRegistros);
            }
        });
    }

    public void leer(){
        System.out.println("leer");
        String url_registros = "https://amst-lab-api.herokuapp.com/api/lecturas";
        JsonArrayRequest requestRegistros = new JsonArrayRequest(
                Request.Method.GET, url_registros, null,
                response -> {
                    llenarSpinner(response);
                }, error -> System.out.println(error)
        );
        ListaRequest.add(requestRegistros);
    }

    public void llenarSpinner(JSONArray temperaturas){
        JSONObject registroTemp;
        try {
            for (int i = 0; i < temperaturas.length(); i++) {
                registroTemp = temperaturas.getJSONObject(i);
                String id = registroTemp.getString("id");
                String temp = registroTemp.getString("value");
                listid.add(id);
                listemp.add(temp);
            }
            s.setAdapter(new ArrayAdapter<String>(EliminarTemp.this, android.R.layout.simple_spinner_dropdown_item, listemp));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }

    public void salir_eliminar_temp(View view){
        Intent intent= new Intent(EliminarTemp.this, MainActivity.class);
        startActivity(intent);
    }
}
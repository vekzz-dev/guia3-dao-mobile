package sv.edu.ufg.agenda;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Pantalla inicial del proyecto.
 *
 * En el Paso 6 de la guia esta clase se renombra a MateriasActivity y se
 * mueve al paquete ui.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

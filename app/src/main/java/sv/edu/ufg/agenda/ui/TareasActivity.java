package sv.edu.ufg.agenda.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import sv.edu.ufg.agenda.AgendaApp;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.dao.DAOFactory;
import sv.edu.ufg.agenda.dao.MateriaDAO;
import sv.edu.ufg.agenda.dao.TareaDAO;
import sv.edu.ufg.agenda.modelo.Materia;
import sv.edu.ufg.agenda.modelo.Tarea;

/**
 * Pantalla de tareas de una materia (Paso 7).
 *
 * <p>Recibe el id de la materia por Intent. Si esa materia no existe, la
 * pantalla se cierra: es la regla 2 del caso de estudio.
 */
public class TareasActivity extends AppCompatActivity implements TareaAdapter.Acciones {

    private MateriaDAO materiaDAO;
    private TareaDAO tareaDAO;
    private TareaAdapter adapter;

    private int materiaId;
    private Materia materia;

    private TextView tvEncabezado;
    private EditText etTitulo;
    private EditText etFecha;
    private Button btnGuardar;

    /** null = se esta agregando una tarea nueva. */
    private Tarea enEdicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        DAOFactory daos = ((AgendaApp) getApplication()).daos();
        materiaDAO = daos.materias();
        tareaDAO = daos.tareas();

        materiaId = getIntent().getIntExtra("materiaId", -1);
        materia = materiaDAO.buscar(materiaId);
        if (materia == null) {
            finish();
            return;
        }

        tvEncabezado = findViewById(R.id.tvEncabezado);
        etTitulo = findViewById(R.id.etTitulo);
        etFecha = findViewById(R.id.etFecha);
        btnGuardar = findViewById(R.id.btnGuardar);

        adapter = new TareaAdapter(this);
        RecyclerView lista = findViewById(R.id.rvTareas);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardar());
        refrescar();
    }

    // ---------- Formulario ----------

    private void guardar() {
        String titulo = etTitulo.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        if (titulo.isEmpty()) {
            etTitulo.setError("El título es obligatorio");
            return;
        }

        if (!fechaValida(fecha)) {
            etFecha.setError("La fecha debe ser válida y tener el formato aaaa-mm-dd");
            return;
        }

        if (enEdicion == null) {
            tareaDAO.insertar(new Tarea(0, materiaId, titulo, fecha));
        } else {
            enEdicion.setTitulo(titulo);
            enEdicion.setFechaEntrega(fecha);
            tareaDAO.actualizar(enEdicion);
        }

        limpiarFormulario();
        refrescar();
    }

    /** Acepta solo fechas reales: rechaza, por ejemplo, 2026-02-30. */
    private boolean fechaValida(String texto) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formato.setLenient(false);
        try {
            formato.parse(texto);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    private void limpiarFormulario() {
        enEdicion = null;
        etTitulo.setText("");
        etFecha.setText("");
        btnGuardar.setText("Agregar tarea");
    }

    // ---------- Lista y encabezado ----------

    private void refrescar() {
        List<Tarea> tareas = tareaDAO.listarPorMateria(materiaId);
        // El formato aaaa-mm-dd se ordena como texto igual que como fecha.
        Collections.sort(tareas, (a, b) -> a.getFechaEntrega().compareTo(b.getFechaEntrega()));
        adapter.mostrar(tareas);
        actualizarEncabezado(tareas);
    }

    private void actualizarEncabezado(List<Tarea> tareas) {
        int completadas = 0;
        for (Tarea t : tareas) {
            if (t.isCompletada()) {
                completadas++;
            }
        }
        tvEncabezado.setText(materia.getNombre() + " · " + completadas + " de "
                + tareas.size() + " completadas");
    }

    // ---------- Acciones de la fila ----------

    @Override
    public void editar(Tarea tarea) {
        enEdicion = tarea;
        etTitulo.setText(tarea.getTitulo());
        etFecha.setText(tarea.getFechaEntrega());
        btnGuardar.setText("Actualizar tarea");
    }

    @Override
    public void cambiarCompletada(Tarea tarea, boolean completada) {
        tarea.setCompletada(completada);
        tareaDAO.actualizar(tarea);
        // Solo se recalcula el encabezado: el CheckBox ya esta en el estado correcto.
        actualizarEncabezado(tareaDAO.listarPorMateria(materiaId));
    }

    @Override
    public void eliminar(Tarea tarea) {
        tareaDAO.eliminar(tarea.getId());

        if (enEdicion != null && enEdicion.getId() == tarea.getId()) {
            limpiarFormulario();
        }
        refrescar();
    }
}

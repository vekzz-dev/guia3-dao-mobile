package sv.edu.ufg.agenda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.modelo.Tarea;

/**
 * Conecta la lista de tareas de una materia con la fila de item_tarea.
 *
 * <p>Avisa a la Activity que accion se pidio mediante {@link Acciones}, sin
 * conocer los DAO.
 */
public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.Fila> {

    /** Acciones que la pantalla debe atender. */
    public interface Acciones {

        void editar(Tarea tarea);

        void cambiarCompletada(Tarea tarea, boolean completada);

        void eliminar(Tarea tarea);
    }

    private final List<Tarea> datos = new ArrayList<>();
    private final Acciones acciones;

    public TareaAdapter(Acciones acciones) {
        this.acciones = acciones;
    }

    /** Reemplaza el contenido de la lista y redibuja. */
    public void mostrar(List<Tarea> nuevos) {
        datos.clear();
        datos.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fila onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext())
                .inflate(R.layout.item_tarea, padre, false);
        return new Fila(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Fila fila, int posicion) {
        Tarea t = datos.get(posicion);

        fila.tvTexto.setText(t.getTitulo() + "\n" + t.getFechaEntrega());

        // Se quita el escucha antes de marcar el CheckBox: si no, al reciclar la
        // fila el marcado heredado dispararia un guardado que nadie pidio.
        fila.cbCompletada.setOnCheckedChangeListener(null);
        fila.cbCompletada.setChecked(t.isCompletada());
        fila.cbCompletada.setOnCheckedChangeListener(
                (v, marcado) -> acciones.cambiarCompletada(t, marcado));

        fila.itemView.setOnClickListener(v -> acciones.editar(t));
        fila.btnEliminar.setOnClickListener(v -> acciones.eliminar(t));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    static class Fila extends RecyclerView.ViewHolder {

        final CheckBox cbCompletada;
        final TextView tvTexto;
        final Button btnEliminar;

        Fila(@NonNull View vista) {
            super(vista);
            cbCompletada = vista.findViewById(R.id.cbCompletada);
            tvTexto = vista.findViewById(R.id.tvTexto);
            btnEliminar = vista.findViewById(R.id.btnEliminar);
        }
    }
}

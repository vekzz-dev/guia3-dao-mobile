package sv.edu.ufg.agenda.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ufg.agenda.modelo.Tarea;

/**
 * Implementacion JSON del contrato de tareas.
 *
 * Hereda el CRUD completo de {@link JsonDAOBase} y solo agrega la consulta
 * propia de la entidad.
 */
public class TareaDAOJson extends JsonDAOBase<Tarea> implements TareaDAO {

    public TareaDAOJson(File archivo) {
        super(archivo, Tarea.class);
    }

    @Override
    public List<Tarea> listarPorMateria(int materiaId) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : cargar()) {
            if (t.getMateriaId() == materiaId) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}

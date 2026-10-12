package sv.edu.ufg.agenda.dao;

import java.util.List;

import sv.edu.ufg.agenda.modelo.Tarea;

/**
 * Contrato de acceso a datos de las tareas.
 */
public interface TareaDAO extends DAO<Tarea> {

    List<Tarea> listarPorMateria(int materiaId);
}

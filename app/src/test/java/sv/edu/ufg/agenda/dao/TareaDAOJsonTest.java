package sv.edu.ufg.agenda.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sv.edu.ufg.agenda.modelo.Tarea;

/**
 * Prueba de la implementacion JSON de tareas sobre una carpeta temporal.
 *
 * No necesita emulador porque la capa de datos recibe un File en lugar de un
 * Context (ver {@link JsonDAOBase}).
 *
 * NOTA: esta prueba NO se ha ejecutado; la maquina donde se escribio el
 * proyecto no tiene JDK 17, Gradle ni el SDK de Android instalados.
 */
public class TareaDAOJsonTest {

    @Rule
    public TemporaryFolder carpeta = new TemporaryFolder();

    private TareaDAOJson dao;

    @Before
    public void preparar() throws Exception {
        dao = new TareaDAOJson(new File(carpeta.getRoot(), "tareas.json"));
    }

    @Test
    public void insertarAsignaIdCorrelativoYPersiste() {
        dao.insertar(new Tarea(0, 1, "Guia practica 3", "2026-10-20"));
        dao.insertar(new Tarea(0, 1, "Examen parcial", "2026-10-25"));

        List<Tarea> todas = dao.listar();
        assertEquals(2, todas.size());
        assertEquals(1, todas.get(0).getId());
        assertEquals(2, todas.get(1).getId());
        assertFalse(todas.get(0).isCompletada());
    }

    @Test
    public void listarPorMateriaDevuelveSoloLasDeEsaMateria() {
        dao.insertar(new Tarea(0, 1, "De programacion", "2026-10-20"));
        dao.insertar(new Tarea(0, 2, "De redes", "2026-10-21"));
        dao.insertar(new Tarea(0, 1, "De programacion", "2026-10-22"));

        assertEquals(2, dao.listarPorMateria(1).size());
        assertEquals(1, dao.listarPorMateria(2).size());
        assertTrue(dao.listarPorMateria(3).isEmpty());
    }

    @Test
    public void eliminarQuitaLaTareaYNoFallaSiNoExiste() {
        dao.insertar(new Tarea(0, 1, "De programacion", "2026-10-20"));

        assertTrue(dao.eliminar(1));
        assertTrue(dao.listar().isEmpty());
        assertFalse(dao.eliminar(1));
    }

    @Test
    public void actualizarGuardiaElEstadoDeCompletada() {
        dao.insertar(new Tarea(0, 1, "De programacion", "2026-10-20"));

        Tarea guardada = dao.buscar(1);
        guardada.setCompletada(true);

        assertTrue(dao.actualizar(guardada));
        assertTrue(dao.buscar(1).isCompletada());
    }
}

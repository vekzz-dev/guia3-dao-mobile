package sv.edu.ufg.agenda.dao;

import java.io.File;

/**
 * Fabrica de DAO: construye las implementaciones una sola vez y las entrega
 * por su interfaz.
 *
 * <p>Las pantallas piden los DAO aqui y nunca usan {@code new} con una clase
 * JSON. Cambiar de almacen (por ejemplo a SQLite) es cambiar solo este
 * constructor.
 */
public class DAOFactory {

    private final MateriaDAO materiaDAO;
    private final TareaDAO tareaDAO;

    public DAOFactory(File carpetaDatos) {
        this.materiaDAO = new MateriaDAOJson(new File(carpetaDatos, "materias.json"));
        this.tareaDAO = new TareaDAOJson(new File(carpetaDatos, "tareas.json"));
    }

    public MateriaDAO materias() {
        return materiaDAO;
    }

    public TareaDAO tareas() {
        return tareaDAO;
    }
}

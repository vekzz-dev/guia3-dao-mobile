package sv.edu.ufg.agenda.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ufg.agenda.modelo.Materia;

/**
 * Implementacion JSON del contrato de materias.
 *
 * Hereda el CRUD completo de {@link JsonDAOBase} y solo agrega la consulta
 * propia de la entidad.
 */
public class MateriaDAOJson extends JsonDAOBase<Materia> implements MateriaDAO {

    public MateriaDAOJson(File archivo) {
        super(archivo, Materia.class);
    }

    @Override
    public List<Materia> buscarPorNombre(String texto) {
        List<Materia> resultado = new ArrayList<>();
        for (Materia m : cargar()) {
            if (m.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}

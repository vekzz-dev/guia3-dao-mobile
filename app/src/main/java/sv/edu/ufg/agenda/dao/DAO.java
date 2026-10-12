package sv.edu.ufg.agenda.dao;

import java.util.List;

/**
 * Operaciones comunes de acceso a datos.
 *
 * Este tipo es el contrato del equipo: las implementaciones lo cumplen y las
 * pantallas lo consumen. No menciona JSON, archivos ni Android.
 */
public interface DAO<T> {

    void insertar(T entidad);

    T buscar(int id);

    List<T> listar();

    boolean actualizar(T entidad);

    boolean eliminar(int id);
}

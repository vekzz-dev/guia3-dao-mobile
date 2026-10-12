package sv.edu.ufg.agenda.modelo;

/**
 * Contrato minimo que cumplen las entidades para que la clase base generica
 * pueda leer y asignar el id sin conocer el tipo concreto.
 */
public interface Entidad {

    int getId();

    void setId(int id);
}

package sv.edu.ufg.agenda.modelo;

/**
 * Tarea de una materia. Guarda el id de su materia en el campo materiaId,
 * que es lo que forma la relacion de uno a muchos.
 */
public class Tarea implements Entidad {

    private int id;
    private int materiaId;
    private String titulo;
    /** Texto con formato aaaa-mm-dd. */
    private String fechaEntrega;
    private boolean completada;

    /** Constructor vacio: lo necesita Gson para deserializar. */
    public Tarea() {
    }

    public Tarea(int id, int materiaId, String titulo, String fechaEntrega) {
        this.id = id;
        this.materiaId = materiaId;
        this.titulo = titulo;
        this.fechaEntrega = fechaEntrega;
        this.completada = false;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}

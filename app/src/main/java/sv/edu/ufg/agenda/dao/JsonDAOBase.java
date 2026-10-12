package sv.edu.ufg.agenda.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ufg.agenda.modelo.Entidad;

/**
 * Base generica del patron DAO sobre un archivo JSON.
 *
 * La lectura, la escritura y la asignacion de ids se escriben una sola vez
 * para cualquier entidad. En Android se usa java.io en lugar de java.nio.file
 * porque funciona en todas las versiones que admite el proyecto.
 *
 * <p>El constructor recibe un {@link File} y no un {@code Context}: asi la capa
 * de datos no depende de Android y se puede probar con JUnit sin emulador.
 */
public abstract class JsonDAOBase<T extends Entidad> implements DAO<T> {

    private final File archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type tipoLista;

    protected JsonDAOBase(File archivo, Class<T> clase) {
        this.archivo = archivo;
        this.tipoLista = TypeToken.getParameterized(List.class, clase).getType();
    }

    // ---------- Operaciones del contrato (iguales para cualquier entidad) ----------

    @Override
    public void insertar(T entidad) {
        List<T> lista = cargar();
        entidad.setId(siguienteId(lista));
        lista.add(entidad);
        guardar(lista);
    }

    @Override
    public T buscar(int id) {
        for (T e : cargar()) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public List<T> listar() {
        return cargar();
    }

    @Override
    public boolean actualizar(T nuevo) {
        List<T> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == nuevo.getId()) {
                lista.set(i, nuevo);
                guardar(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        List<T> lista = cargar();
        boolean quitado = lista.removeIf(e -> e.getId() == id);
        if (quitado) {
            guardar(lista);
        }
        return quitado;
    }

    // ---------- Metodos reutilizables para las clases hijas ----------

    /** Lee el archivo completo. Si no existe, devuelve una lista vacia. */
    protected List<T> cargar() {
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader lector = new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            List<T> lista = gson.fromJson(lector, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo leer " + archivo, ex);
        }
    }

    /** Escribe la lista completa en el archivo, creando la carpeta si hace falta. */
    protected void guardar(List<T> lista) {
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            throw new RuntimeException("No se pudo crear " + carpeta);
        }
        try (Writer escritor = new OutputStreamWriter(
                new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            gson.toJson(lista, tipoLista, escritor);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo escribir " + archivo, ex);
        }
    }

    /** Id correlativo: el mayor existente mas uno. */
    private int siguienteId(List<T> lista) {
        int mayor = 0;
        for (T e : lista) {
            mayor = Math.max(mayor, e.getId());
        }
        return mayor + 1;
    }
}

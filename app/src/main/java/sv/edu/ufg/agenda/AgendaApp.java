package sv.edu.ufg.agenda;

import android.app.Application;

import java.io.File;

import sv.edu.ufg.agenda.dao.DAOFactory;

/**
 * Mantiene viva la fabrica de DAO mientras la aplicacion este abierta.
 *
 * <p>Se registra en el manifiesto con {@code android:name=".AgendaApp"}; sin
 * ese atributo la Activity recibe un Application normal y la aplicacion se
 * cierra con ClassCastException.
 */
public class AgendaApp extends Application {

    private DAOFactory daos;

    @Override
    public void onCreate() {
        super.onCreate();
        daos = new DAOFactory(new File(getFilesDir(), "datos"));
    }

    public DAOFactory daos() {
        return daos;
    }
}

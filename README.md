# Agenda Académica — patrón DAO con archivos JSON

Aplicación Android (Java) que registra las materias de un estudiante y las tareas
de cada una, guardando todo en archivos JSON dentro del almacenamiento interno
del dispositivo. El acceso a datos está detrás del patrón **DAO**, con una clase
base genérica que evita repetir la lectura y la escritura, y una fábrica que
entrega las implementaciones por su interfaz. Las pantallas nunca saben que los
datos viven en un archivo.

## Camino rápido

1. **Clonar y abrir**

   ```bash
   git clone git@github.com:vekzz-dev/guia3-dao-mobile.git
   ```

   Abrir la carpeta en Android Studio y dejar que sincronice. El proyecto no
   incluye `gradle/wrapper/gradle-wrapper.jar` por ser binario: Studio lo
   regenera al primer *sync*.

2. **Ejecutar** en un emulador o en un celular con depuración USB (minSdk 24).

3. **Confirmar la persistencia.** Registrar una materia y una tarea, cerrar la
   app por completo y volver a abrirla. Los datos siguen ahí. Se pueden ver en
   *Device Explorer* → `/data/data/sv.edu.ufg.agenda/files/datos/`.

## Cómo está organizado

```
Pantalla (Activity)  →  Interfaz DAO  →  JsonDAOBase<T>  →  materias.json / tareas.json
```

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Modelo | `sv.edu.ufg.agenda.modelo` | `Materia` y `Tarea`, ambas `Entidad` |
| Contrato | `sv.edu.ufg.agenda.dao` | `DAO<T>`, `MateriaDAO`, `TareaDAO` |
| Persistencia | `sv.edu.ufg.agenda.dao` | `JsonDAOBase<T>` y las dos implementaciones JSON |
| Fábrica | `sv.edu.ufg.agenda.dao` | `DAOFactory`, creada una vez en `AgendaApp` |
| Interfaz | `sv.edu.ufg.agenda.ui` | Adaptadores y Activities |

**Regla que sostiene el diseño:** ninguna clase de `ui` importa `MateriaDAOJson`
ni `TareaDAOJson`. Se comprueba con:

```bash
grep -rn "DAOJson" app/src/main/java/sv/edu/ufg/agenda/ui/   # no debe devolver nada
```

## Reglas del caso de estudio

| # | Regla | Dónde se aplica |
|---|---|---|
| 1 | El nombre de la materia y el título de la tarea son obligatorios | Validación en el formulario |
| 2 | Una tarea solo se crea para una materia que existe | `TareasActivity` cierra si `buscar()` devuelve null |
| 3 | No se elimina una materia que todavía tiene tareas | `eliminar()` consulta `listarPorMateria()` antes |
| 4 | La fecha de entrega debe ser válida con formato `aaaa-mm-dd` | `SimpleDateFormat` con `setLenient(false)` |

## Estructura de archivos

```
app/src/main/java/sv/edu/ufg/agenda/
├── AgendaApp.java            Application: mantiene viva la fábrica
├── modelo/                   Entidad, Materia, Tarea
├── dao/                      DAO, MateriaDAO, TareaDAO,
│                             JsonDAOBase, MateriaDAOJson,
│                             TareaDAOJson, DAOFactory
└── ui/                       MateriaAdapter, MateriasActivity,
                              TareaAdapter, TareasActivity

app/src/main/res/layout/      activity_materias, item_materia,
                              activity_tareas, item_tarea

app/src/test/java/.../dao/    TareaDAOJsonTest (JUnit + TemporaryFolder)
```

## Cambiar de JSON a base de datos

No se toca ninguna pantalla. Se crean `MateriaDAOSqlite` y `TareaDAOSqlite`
cumpliendo los mismos contratos, y se cambia **solo el constructor de
`DAOFactory`** para que reciba el `Context` en lugar de la carpeta. Los layouts,
los adaptadores y las Activities quedan igual.

## Trabajo y verificación

Este proyecto se desarrolló **individualmente**, ejecutando los tres roles que la
guía propone como fases sucesivas: cada rol tiene su rama y su merge a `main`.

El registro completo —declaración de autoría, mapa de roles, hallazgos de
integración y estado de verificación— está en **[BITACORA.md](BITACORA.md)**.

## Siguiente paso

Abrir el proyecto, dejar que Android Studio regenere el wrapper y ejecutar la
prueba `TareaDAOJsonTest`. Lo que falta verificar está listado en
[BITACORA.md](BITACORA.md#estado-de-verificación).

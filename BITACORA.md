# Bitácora de trabajo — Guía práctica 3

## Declaración de autoría

**Kevin René Flores Martínez — FM100224**
Desarrollo de Aplicaciones Multiplataforma (DAP) · Guía práctica 3

Este proyecto fue desarrollado **individualmente**. La guía está pensada para
equipos de tres integrantes y reparte el trabajo en tres roles; aquí esos roles
se ejecutaron como **fases sucesivas de una sola persona**, cada una en su
propia rama y con su merge a `main`.

No hay colaboradores, ni cuentas, ni identidades simuladas. Todos los commits
están firmados por el mismo autor, y eso es visible en el historial.

## Mapa de roles

| Rol de la guía | Pasos | Rama | Qué aporta |
|---|---|---|---|
| — | 1 | `main` (commit inicial) | Proyecto Android, dependencias y versiones |
| **A. Modelo y contrato** | 2 | `rol-a` | Entidades e interfaces; el contrato que desbloquea a los demás |
| **B. Persistencia** | 3, 4 y 8 | `rol-b` | `JsonDAOBase<T>`, las implementaciones JSON y su prueba |
| **A. Fábrica** | 5 | `rol-a-fabrica` | `DAOFactory` y `AgendaApp` |
| **C. Interfaz móvil** | 6 | `rol-c` | Pantalla de materias |
| **C. Interfaz móvil** | 7 | `rol-c-tareas` | Pantalla de tareas e integración final |
| — | 9 | `docs/bitacora` | Esta bitácora |

El orden no es casual y sigue la razón de ser del patrón: **el contrato se
publica primero**. A partir de ahí, la persistencia y la interfaz se escriben
contra interfaces, sin que ninguna de las dos capas conozca a la otra.

## Hallazgos de integración

1. **La fábrica no compila antes que la persistencia.** La guía asigna a rol A
   tanto el contrato (Paso 2) como la fábrica (Paso 5), pero `DAOFactory` hace
   `new MateriaDAOJson(...)`, que es código del rol B. Trabajando en paralelo
   esa dependencia se resuelve esperando; aquí se resolvió partiendo rol A en
   dos momentos: el contrato al principio y la fábrica después de `rol-b`. Es el
   único punto donde el reparto de la guía no se puede seguir al pie de la letra
   sin bloquearse.

2. **`AndroidManifest.xml` es el archivo de conflicto natural del equipo.**
   Aparece en el Paso 5 (registro de `AgendaApp` y de las actividades) y otra
   vez en el Paso 6 (rename de `MainActivity`). En un equipo real conviene que lo
   toque una sola persona.

3. **Los archivos generados ensucian cualquier integración.** `build/`,
   `.gradle/`, `.idea/` y `local.properties` están en `.gitignore` para que nadie
   tenga que resolver un conflicto en un archivo que no escribió a mano.

## Decisiones de diseño

- **El cálculo del contador de completadas vive en la Activity.** La lista ya se
  pide con `listarPorMateria()`, así que contar sobre ella no agrega una consulta
  ni obliga a tocar el contrato. Si el cálculo se necesitara en otra pantalla,
  el lugar correcto sería un método del DAO.
- **La fecha se guarda como texto `aaaa-mm-dd`.** Con ese formato el orden
  alfabético coincide con el cronológico, así que la lista se ordena sin
  convertir nada.
- **`JsonDAOBase` recibe un `File`, no un `Context`.** Eso mantiene la capa de
  datos independiente de Android y hace posible la prueba con `TemporaryFolder`
  sin emulador.
- **La validación vive en la interfaz, no en el DAO.** El estilo del curso
  mantiene el DAO delgado: sabe guardar y consultar, no decidir reglas de
  negocio.
- **Un DAO en memoria** sería útil para probar la interfaz sin depender de
  archivos, y para trabajar en paralelo mientras la persistencia todavía no
  existe: la interfaz se programa contra `MateriaDAO` y da igual cuál de las dos
  implementaciones esté detrás.

## Estado de verificación

Lo que **sí** se comprobó:

- El paquete `ui` no conoce las clases JSON:
  `grep -rn "DAOJson" app/src/main/java/sv/edu/ufg/agenda/ui/` no devuelve nada.
- El historial conserva las seis ramas y sus merges (`git log --oneline --graph`).
- `.gitignore` cubre los archivos generados.

Lo que **no** se pudo comprobar, y hay que comprobar antes de entregar:

- **El build.** El proyecto se escribió sin compilar: la máquina usada no tiene
  JDK 17, ni Gradle, ni el SDK de Android. El primer *Sync Now* en Android Studio
  es la prueba pendiente.
- **La prueba JUnit** de `TareaDAOJsonTest` no se ejecutó, por la misma razón.
- **Las capturas** de `materias.json` y `tareas.json` en el *Device Explorer*
  (Paso 8) no se generaron.
- **El recorrido funcional** del Paso 9: crear materia, agregar tareas, editar,
  marcar completada, intentar borrar una materia con tareas y reabrir la app
  para confirmar la persistencia.

Las versiones se fijaron contra la matriz oficial de compatibilidad —AGP 8.7.3,
Gradle 8.9, JDK 17, `compileSdk 35`, `minSdk 24`— eligiendo a propósito una
combinación conservadora: un *plugin* antiguo abre en un Android Studio nuevo,
pero no al contrario.

## Historial

```bash
git log --oneline --graph --all
```

Cada rama entra a `main` con un merge `--no-ff`, de modo que la bifurcación
queda visible en el grafo en lugar de perderse en un avance lineal.

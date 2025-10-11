# Visor de Brazo Robótico 3D

Este proyecto es una aplicación Java con JavaFX que permite visualizar y controlar un brazo robótico cargando archivos .obj.

## Configuración del Proyecto en IntelliJ IDEA

### Paso 1: Crear la estructura del proyecto
1. Crea una nueva carpeta llamada "RoboticArmViewer"
2. Dentro crea esta estructura de carpetas:
```
RoboticArmViewer/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── roboticarm/
│       │           └── viewer/
│       └── resources/
│           └── models/
├── pom.xml
└── README.md
```

### Paso 2: Copiar archivos del proyecto
1. Coloca `RoboticArmViewer.java` y `ObjLoader.java` en: `src/main/java/com/roboticarm/viewer/`
2. Coloca `pom.xml` en la raíz del proyecto
3. Copia tus archivos .obj en: `src/main/resources/models/`
   - Base.obj
   - Hombro.obj
   - Brazo-A.obj
   - Brazo-B.obj
   - Punta.obj

### Paso 3: Abrir en IntelliJ IDEA
1. Abre IntelliJ IDEA
2. Selecciona "Open" y navega hasta la carpeta "RoboticArmViewer"
3. IntelliJ detectará automáticamente que es un proyecto Maven

### Paso 4: Configurar el JDK
1. Ve a File → Project Structure (Ctrl+Alt+Shift+S)
2. En "Project Settings" → "Project"
3. Asegúrate de tener JDK 11 o superior seleccionado
4. Si no tienes JDK instalado, click en "Download JDK"

### Paso 5: Ejecutar la aplicación
1. Abre `RoboticArmViewer.java`
2. Busca el método `setupRoboticArm()` (línea ~65)
3. **IMPORTANTE**: Reemplaza estas líneas:
```java
// REEMPLAZA ESTAS LÍNEAS:
MeshView baseMesh = createPlaceholderBase();
MeshView hombroMesh = createPlaceholderHombro();
MeshView brazoAMesh = createPlaceholderBrazoA();
MeshView brazoBMesh = createPlaceholderBrazoB();
MeshView puntaMesh = createPlaceholderPunta();

// POR ESTAS:
MeshView baseMesh = ObjLoader.loadObjFile("Base.obj");
MeshView hombroMesh = ObjLoader.loadObjFile("Hombro.obj");
MeshView brazoAMesh = ObjLoader.loadObjFile("Brazo-A.obj");
MeshView brazoBMesh = ObjLoader.loadObjFile("Brazo-B.obj");
MeshView puntaMesh = ObjLoader.loadObjFile("Punta.obj");
```
4. Click derecho en el archivo → "Run RoboticArmViewer.main()"

### Alternativa: Ejecutar con Maven
Desde la terminal en la raíz del proyecto:
```bash
mvn clean javafx:run
```

## Controles de la aplicación

- **Arrastrar mouse**: Rotar la vista 3D
- **Rueda del mouse**: Zoom in/out
- **Sliders del panel derecho**: Controlar cada articulación del brazo
  - Base: Rotación horizontal (-180° a 180°)
  - Hombro: Movimiento vertical (-90° a 90°)
  - Brazo A: Articulación del codo (-120° a 120°)  
  - Brazo B: Articulación final (-90° a 90°)
- **Botón Reset**: Volver a posición inicial

## Características

- ✓ Visualización 3D interactiva
- ✓ Control individual de cada articulación
- ✓ Carga automática de archivos .obj
- ✓ Iluminación realista
- ✓ Interface intuitiva
- ✓ Animaciones suaves

## Solución de problemas

### Error: "JavaFX runtime components are missing"
Ejecuta desde Maven:
```bash
mvn clean javafx:run
```

### Los archivos OBJ no se cargan
1. Verifica que los archivos estén en `src/main/resources/models/`
2. Asegúrate de que tengan los nombres exactos
3. Revisa la consola para mensajes de error

### Error de compilación
1. Verifica que tengas JDK 11 o superior
2. Ejecuta `mvn clean compile` para limpiar y recompilar

## Estructura del código

- `RoboticArmViewer.java`: Aplicación principal con interfaz 3D
- `ObjLoader.java`: Cargador de archivos .obj
- `pom.xml`: Configuración de Maven con dependencias JavaFX

## Notas técnicas

- Usa JavaFX 17 para renderizado 3D
- Compatible con archivos .obj estándar
- Jerarquía de transformaciones para movimientos realistas
- Sistema de iluminación con luz ambiental y direccional

¡Tu brazo robótico está listo para ser visualizado y controlado interactivamente!

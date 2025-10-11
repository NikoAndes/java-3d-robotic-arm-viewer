package com.roboticarm.viewer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;
import javafx.geometry.Point3D;

public class RoboticArmViewer extends Application {

    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;

    // Transformaciones para la cámara
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate translateCamera = new Translate();

    // Objetos del brazo robótico
    private Group baseGroup;
    private Group hombroGroup;
    private Group brazoAGroup;
    private Group brazoBGroup;
    private Group puntaGroup;

    // Rotaciones de las articulaciones
    private final Rotate baseRotation = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate hombroRotation = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate brazoARotation = new Rotate(0, Rotate.X_AXIS);
    private final Rotate brazoBRotation = new Rotate(0, Rotate.X_AXIS);
    private final Rotate puntaRotation = new Rotate(0, Rotate.Y_AXIS);


    // Controles
    private Slider baseSlider, hombroSlider, brazoASlider, brazoBSlider, puntaSlider;
    private Label baseLabel, hombroLabel, brazoALabel, brazoBLabel, puntaLabel;

    // NUEVO: Labels para mostrar coordenadas del mouse
    private Label mouseCoordinatesLabel;
    private Label cameraInfoLabel;

    // Variables para el mouse
    private double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;

    @Override
    public void start(Stage primaryStage) {

        // Crear escena 3D
        Group root3D = new Group();
        SubScene scene3D = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);

        // SOLUCIÓN: Hacer la SubScene receptiva de eventos en toda su área
        scene3D.setPickOnBounds(true);
        scene3D.setFill(Color.TRANSPARENT); // Importante para que capture eventos en áreas vacías

        // Configurar cámara
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.getTransforms().addAll(rotateX, rotateY, translateCamera);
        scene3D.setCamera(camera);

        // Inicializar posición de la cámara
        rotateX.setAngle(CAMERA_INITIAL_X_ANGLE);
        rotateY.setAngle(CAMERA_INITIAL_Y_ANGLE);
        translateCamera.setZ(CAMERA_INITIAL_DISTANCE);

        // Cargar y configurar el brazo robótico
        setupRoboticArm(root3D);

        // Agregar iluminación
        addLighting(root3D);

        // MEJORADO: Configurar controles de mouse mejorados
        setupImprovedMouseControls(scene3D, camera);

        // Crear panel de controles mejorado
        VBox controlPanel = createImprovedControlPanel();

        // NUEVO: Crear panel de información del mouse
        VBox mouseInfoPanel = createMouseInfoPanel();

        // Layout principal mejorado
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(scene3D);
        mainLayout.setRight(controlPanel);
        mainLayout.setBottom(mouseInfoPanel);  // Panel de coordenadas abajo
        mainLayout.setStyle("-fx-background-color: #2b2b2b;");

        // Configurar escena principal
        Scene scene = new Scene(mainLayout, 1200, 650); // Un poco más alta para el panel inferior
        scene.setFill(Color.GRAY);

        primaryStage.setTitle("Brazo Robótico - CONTROLES MEJORADOS");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Actualizar info inicial de cámara
        updateCameraInfo();
    }

    private void setupRoboticArm(Group root3D) {
        // Crear grupos para cada parte del brazo
        baseGroup = new Group();
        hombroGroup = new Group();
        brazoAGroup = new Group();
        brazoBGroup = new Group();
        puntaGroup = new Group();

        hombroRotation.setPivotX(0);    // Centro en X
        hombroRotation.setPivotY(0);    // Centro en Y
        hombroRotation.setPivotZ(0);    // Centro en Z


        // Cargar modelos OBJ
        MeshView baseMesh = ObjLoader.loadObjFile("Base.obj");
        MeshView hombroMesh = ObjLoader.loadObjFile("Hombro.obj");
        MeshView brazoAMesh = ObjLoader.loadObjFile("Brazo-A.obj");
        MeshView brazoBMesh = ObjLoader.loadObjFile("Brazo-B.obj");
        MeshView puntaMesh = ObjLoader.loadObjFile("Punta.obj");;

        // Configurar materiales
        if (baseMesh != null) {
            PhongMaterial baseMaterial = new PhongMaterial();
            baseMaterial.setDiffuseColor(Color.DARKGRAY);
            baseMaterial.setSpecularColor(Color.LIGHTGRAY);
            baseMesh.setMaterial(baseMaterial);
            baseGroup.getChildren().add(baseMesh);
        }

        if (hombroMesh != null) {
            PhongMaterial hombroMaterial = new PhongMaterial();
            hombroMaterial.setDiffuseColor(Color.GRAY);
            hombroMaterial.setSpecularColor(Color.LIGHTGRAY);
            hombroMesh.setMaterial(hombroMaterial);
            hombroMesh.getTransforms().addAll(
                new Rotate(180, Rotate.Z_AXIS)
            );
            hombroGroup.getChildren().add(hombroMesh);
        }

        if (brazoAMesh != null) {
            PhongMaterial brazoAMaterial = new PhongMaterial();
            brazoAMaterial.setDiffuseColor(Color.DARKSLATEGRAY);
            brazoAMaterial.setSpecularColor(Color.LIGHTGRAY);
            brazoAMesh.setMaterial(brazoAMaterial);
            brazoAGroup.getChildren().add(brazoAMesh);
        }

        if (brazoBMesh != null) {
            PhongMaterial brazoBMaterial = new PhongMaterial();
            brazoBMaterial.setDiffuseColor(Color.SLATEGRAY);
            brazoBMaterial.setSpecularColor(Color.LIGHTGRAY);
            brazoBMesh.setMaterial(brazoBMaterial);
            brazoBGroup.getChildren().add(brazoBMesh);
        }

        if (puntaMesh != null) {
            PhongMaterial puntaMaterial = new PhongMaterial();
            puntaMaterial.setDiffuseColor(Color.DARKRED);
            puntaMaterial.setSpecularColor(Color.RED);
            puntaMesh.setMaterial(puntaMaterial);
            puntaMesh.getTransforms().addAll(
                new Rotate(180, Rotate.X_AXIS)
            );
            puntaGroup.getChildren().add(puntaMesh);
        }

        // Configuración con tus coordenadas exactas
        baseGroup.getTransforms().addAll(
            // Sin rotación (base fija)
        );

        hombroGroup.getTransforms().addAll(
            new Translate(47, 45, 9),
            hombroRotation
        );

        brazoAGroup.getTransforms().addAll(
            new Translate(-32, -22, 8),
            brazoARotation
        );

        brazoBGroup.getTransforms().addAll(
            new Translate(6, 46, 5),
            brazoBRotation
        );

        puntaGroup.getTransforms().addAll(
            new Translate(1.5, 50, 12),
            puntaRotation
        );

        // Jerarquía
        brazoBGroup.getChildren().add(puntaGroup);
        brazoAGroup.getChildren().add(brazoBGroup);
        hombroGroup.getChildren().add(brazoAGroup);
        baseGroup.getChildren().add(hombroGroup);

        root3D.getChildren().add(baseGroup);
    }

    private void addLighting(Group root3D) {
        AmbientLight ambientLight = new AmbientLight(Color.color(0.4, 0.4, 0.4));
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(-100);
        pointLight.setTranslateY(-100);
        pointLight.setTranslateZ(-200);
        PointLight fillLight = new PointLight(Color.color(0.3, 0.3, 0.3));
        fillLight.setTranslateX(100);
        fillLight.setTranslateY(100);
        fillLight.setTranslateZ(200);
        root3D.getChildren().addAll(ambientLight, pointLight, fillLight);
    }

    private void setupImprovedMouseControls(SubScene scene3D, PerspectiveCamera camera) {

        // SOLUCIÓN: Eventos de mouse funcionan en toda la SubScene
        scene3D.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene3D.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            if (me.isPrimaryButtonDown()) {
                // Rotación con botón izquierdo
                rotateY.setAngle(rotateY.getAngle() - mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED);
                rotateX.setAngle(rotateX.getAngle() + mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED);
                updateCameraInfo(); // Actualizar info de cámara
            } else if (me.isSecondaryButtonDown()) {
                // Pan con botón derecho
                translateCamera.setX(translateCamera.getX() + mouseDeltaX * 0.5);
                translateCamera.setY(translateCamera.getY() + mouseDeltaY * 0.5);
                updateCameraInfo();
            }
        });

        // NUEVO: Evento de movimiento del mouse para mostrar coordenadas
        scene3D.setOnMouseMoved((MouseEvent me) -> {
            updateMouseCoordinates(me.getSceneX(), me.getSceneY());
        });

        scene3D.setOnScroll((ScrollEvent event) -> {
            double z = translateCamera.getZ();
            double newZ = z + event.getDeltaY() * 2;
            if (newZ > -50) newZ = -50;
            if (newZ < -1000) newZ = -1000;
            translateCamera.setZ(newZ);
            updateCameraInfo(); // Actualizar info de zoom
        });
    }

    // NUEVO: Método para crear panel de información del mouse
    private VBox createMouseInfoPanel() {
        VBox mouseInfoPanel = new VBox(5);
        mouseInfoPanel.setPadding(new Insets(5));
        mouseInfoPanel.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #4a4a4a; -fx-border-width: 1px;");

        // Título del panel
        Label infoTitle = new Label("📍 Información del Mouse y Cámara");
        infoTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Label para coordenadas del mouse
        mouseCoordinatesLabel = new Label("Mouse: (0, 0)");
        mouseCoordinatesLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: lightgreen;");

        // Label para información de la cámara
        cameraInfoLabel = new Label("Cámara: Rotación(0°, 0°) Zoom(-450)");
        cameraInfoLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: lightblue;");

        // Instrucciones mejoradas
        Label instructions = new Label("💡 Controles: Arrastrar=Rotar | Click derecho=Mover | Rueda=Zoom | FUNCIONA EN TODA EL ÁREA 3D");
        instructions.setStyle("-fx-font-size: 10px; -fx-text-fill: lightyellow;");
        instructions.setWrapText(true);

        mouseInfoPanel.getChildren().addAll(infoTitle, mouseCoordinatesLabel, cameraInfoLabel, instructions);
        return mouseInfoPanel;
    }

    // NUEVO: Método para actualizar coordenadas del mouse
    private void updateMouseCoordinates(double x, double y) {
        mouseCoordinatesLabel.setText(String.format("🖱️ Mouse: (%.1f, %.1f)", x, y));
    }

    // NUEVO: Método para actualizar información de cámara
    private void updateCameraInfo() {
        cameraInfoLabel.setText(String.format("📷 Cámara: Rotación(%.1f°, %.1f°) Pos(%.1f, %.1f, %.1f)", 
            rotateX.getAngle(), 
            rotateY.getAngle(), 
            translateCamera.getX(),
            translateCamera.getY(),
            translateCamera.getZ()));
    }

    private VBox createImprovedControlPanel() {
        VBox controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setStyle("-fx-background-color: #3b3b3b;");
        controlPanel.setPrefWidth(300);

        Label title = new Label("🦾 Control del Brazo Robótico");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        baseSlider = new Slider(-180, 180, 0);
        baseSlider.setDisable(true);
        baseLabel = new Label("Base: FIJA");
        baseLabel.setStyle("-fx-text-fill: lightgray;");

        hombroSlider = new Slider(-180, 180, 0);
        hombroLabel = new Label("Hombro (Y-horizontal): 0.0°");
        hombroLabel.setStyle("-fx-text-fill: white;");

        brazoASlider = new Slider(-90, 90, 0);
        brazoALabel = new Label("Brazo A (X-vertical): 0.0°");
        brazoALabel.setStyle("-fx-text-fill: white;");

        brazoBSlider = new Slider(-90, 90, 0);
        brazoBLabel = new Label("Brazo B (X-vertical): 0.0°");
        brazoBLabel.setStyle("-fx-text-fill: white;");

        puntaSlider = new Slider(-90, 90, 0);
        puntaLabel = new Label("Punta (Y-rotación): 0.0°");
        puntaLabel.setStyle("-fx-text-fill: white;");

        setupSliderListeners();

        Button resetButton = new Button("🔄 Reset Posición");
        resetButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        resetButton.setOnAction(e -> resetPosition());

        // NUEVO: Botón para resetear cámara
        Button resetCameraButton = new Button("📷 Reset Cámara");
        resetCameraButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        resetCameraButton.setOnAction(e -> resetCamera());

        Label improvements = new Label("✅ MEJORAS APLICADAS:\n" +
                "• Coordenadas del mouse visibles\n" +
                "• Controles funcionan en toda el área\n" +
                "• Información de cámara en tiempo real\n" +
                "• Reset de cámara independiente\n" +
                "• Coordenadas exactas aplicadas");
        improvements.setStyle("-fx-text-fill: lightgreen; -fx-font-size: 9px;");
        improvements.setWrapText(true);

        controlPanel.getChildren().addAll(
            title, new Separator(),
            baseLabel, baseSlider,
            hombroLabel, hombroSlider,
            brazoALabel, brazoASlider,
            brazoBLabel, brazoBSlider,
            puntaLabel, puntaSlider,
            new Separator(), 
            resetButton, resetCameraButton,
            new Separator(), improvements
        );

        return controlPanel;
    }

    private void setupSliderListeners() {
        baseSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            baseLabel.setText("Base: FIJA");
        });

        hombroSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            hombroRotation.setAngle(newVal.doubleValue());
            hombroLabel.setText(String.format("Hombro (Y-horizontal): %.1f°", newVal.doubleValue()));
        });

        brazoASlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            brazoARotation.setAngle(newVal.doubleValue());
            brazoALabel.setText(String.format("Brazo A (X-vertical): %.1f°", newVal.doubleValue()));
        });

        brazoBSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            brazoBRotation.setAngle(newVal.doubleValue());
            brazoBLabel.setText(String.format("Brazo B (X-vertical): %.1f°", newVal.doubleValue()));
        });

        puntaSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            puntaRotation.setAngle(newVal.doubleValue());
            puntaLabel.setText(String.format("Punta (Y-rotación): %.1f°", newVal.doubleValue()));
        });
    }

    private void resetPosition() {
        baseSlider.setValue(0);
        hombroSlider.setValue(0);
        brazoASlider.setValue(0);
        brazoBSlider.setValue(0);
        puntaSlider.setValue(0);
    }

    // NUEVO: Método para resetear la cámara
    private void resetCamera() {
        rotateX.setAngle(CAMERA_INITIAL_X_ANGLE);
        rotateY.setAngle(CAMERA_INITIAL_Y_ANGLE);
        translateCamera.setX(0);
        translateCamera.setY(0);
        translateCamera.setZ(CAMERA_INITIAL_DISTANCE);
        updateCameraInfo();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
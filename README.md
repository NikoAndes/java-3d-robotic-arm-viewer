# Java 3D Robotic Arm Viewer

![Language](https://img.shields.io/badge/Language-Java-orange) ![Graphics](https://img.shields.io/badge/Graphics-Java3D%2FSwing-blue) ![Pattern](https://img.shields.io/badge/Pattern-OOP-green) ![License](https://img.shields.io/badge/License-MIT-yellow) ![Status](https://img.shields.io/badge/Status-Active-brightgreen)

## Overview

Interactive 3D robotic arm viewer built in Java with a Swing GUI. Renders a multi-joint robotic arm in 3D space with real-time joint angle control via sliders. Visualizes forward kinematics by computing end-effector position from joint angles. Designed as an educational tool for understanding robotics and spatial transformations.

## Features

- 3D visualization of a multi-joint robotic arm (3-DOF)
- Real-time joint angle control with interactive sliders
- Forward kinematics computation and end-effector position display
- Rotation and zoom controls for 3D viewport
- Color-coded joint and link visualization
- Coordinate frame display (X/Y/Z axes)

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| GUI | Java Swing |
| 3D Rendering | Java2D with affine transforms / Java3D |
| Build Tool | Maven |
| IDE | IntelliJ IDEA |

## Project Structure

```
java-3d-robotic-arm-viewer/
├── src/main/java/com/nikoandes/robotarm/
│   ├── Main.java              # Entry point
│   ├── RoboticArm.java        # Arm model and kinematics
│   ├── ArmViewer3D.java       # 3D rendering panel
│   ├── ControlPanel.java      # Slider controls for joints
│   └── Joint.java             # Joint data model
├── pom.xml
├── .gitignore
├── LICENSE
└── README.md
```

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

```bash
git clone https://github.com/NikoAndes/java-3d-robotic-arm-viewer.git
cd java-3d-robotic-arm-viewer
mvn clean package
java -jar target/robotic-arm-viewer.jar
```

### Controls
- Sliders: Adjust joint angles (J1, J2, J3)
- Mouse drag: Rotate 3D view
- Scroll: Zoom in/out
- Reset button: Return to default position

## What I Learned

- 3D coordinate transformations and rotation matrices
- Forward kinematics principles for robotic arms
- Building interactive graphical applications with Java Swing
- Applying affine transforms for 3D projection in 2D canvas
- Object-oriented modeling of mechanical systems

## Future Improvements

- [ ] Implement inverse kinematics (IK) solver
- [ ] Add trajectory planning and animation playback
- [ ] Export arm configuration to JSON
- [ ] Add collision detection between links
- [ ] Migrate rendering to JavaFX for hardware acceleration
- [ ] Add gripper end-effector with open/close control

## Author

**Nicolas Isaza Sierra** — [GitHub @NikoAndes](https://github.com/NikoAndes)

Mechatronics engineering student | Robotics & Java enthusiast | UMNG, Colombia

## License

MIT License — see [LICENSE](LICENSE) for details.

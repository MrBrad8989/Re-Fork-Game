# 🎲 Re-Fork Game

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-Hedgehog-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=for-the-badge&logo=android&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)
![API](https://img.shields.io/badge/Min%20SDK-24-green?style=for-the-badge&logo=android)

**Una reinvención moderna del clásico juego de Parchís, construida con la potencia de Kotlin y Jetpack Compose.**

[Ver Demo](#-capturas) • [Instalación](#-instalación) • [Cómo Jugar](#-cómo-jugar) • [Contribuir](#-contribuir)

</div>

---

## 📖 Descripción

**Re-Fork Game** es un proyecto académico de 2º DAM (Desarrollo de Aplicaciones Multiplataforma) que lleva el tradicional juego de mesa a la era digital moderna.

Desarrollado íntegramente en **Kotlin**, el juego destaca por el uso de **Jetpack Compose** para crear una interfaz de usuario reactiva, fluida y altamente personalizable. No es solo un juego, es una demostración técnica de arquitectura limpia (MVVM), persistencia de datos y gestión de estados complejos en Android.

---

## ✨ Características Principales

| Característica | Descripción |
| :--- | :--- |
| 🎮 **Multijugador Híbrido** | Soporte para 2 a 4 jugadores en el mismo dispositivo, mezclando humanos y CPUs. |
| 🤖 **IA Adaptativa** | Juega contra la CPU con velocidades configurables (Lenta, Normal, Rápida). |
| 🎨 **Personalización Total** | Elige entre 3 skins de tablero únicos: **Classic**, **Midnight** (Oscuro) y **Candy** (Vibrante). |
| 💰 **Economía de Juego** | Gana monedas jugando y desbloquea nuevos diseños en la Tienda integrada. |
| 💾 **Persistencia** | Tu progreso, monedas y configuraciones se guardan automáticamente usando **DataStore**. |
| 📱 **Diseño Moderno** | Interfaz Material Design 3 con animaciones fluidas, transiciones y feedback háptico. |


---

## 🛠️ Stack Tecnológico

El proyecto utiliza las últimas tecnologías recomendadas para el desarrollo Android moderno:

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Arquitectura:** MVVM (Model-View-ViewModel)
* **Navegación:** Navigation Compose
* **Gestión de Estado:** `StateFlow` y `ViewModel`
* **Persistencia de Datos:** [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) (Reemplazo moderno de SharedPreferences)
* **Concurrencia:** Coroutines
* **Build System:** Gradle Kotlin DSL

---

## 🏗️ Estructura del Proyecto

El código sigue una arquitectura limpia y modular para facilitar el mantenimiento:

```text
app/src/main/java/com/example/parchismania/
├── data/              # Capa de Datos (DataStore, Modelos de Persistencia)
├── engine/            # Lógica pura del juego (Reglas, Movimientos, IA)
├── ui/                # Capa de Presentación (Compose)
│   ├── pantallas/     # Pantallas de configuración y menú
│   ├── screens/       # Pantallas principales (Juego, Tienda, Victoria)
│   ├── theme/         # Tema y tipografía personalizada
│   └── widgets/       # Componentes reutilizables (Tablero, Fichas, Logo)
└── GameViewModel.kt   # Nexo entre la UI y la Lógica (State Management)
```

---

## 🎮 Guía de Juego y Reglas

El juego sigue las reglas tradicionales del Parchís con algunas adaptaciones para partidas rápidas:

### Mecánicas Básicas
1.  **Inicio:** Pulsa "JUGAR", selecciona de 2 a 4 jugadores (puedes mezclar Humanos y CPU) y elige sus colores.
2.  **Salida:** Necesitas sacar un **5** para sacar una ficha de casa a la casilla de salida.
3.  **Movimiento:** Toca cualquier ficha resaltada para moverla.
4.  **El 6:** Si sacas un 6, repites turno. ¡Cuidado! Si sacas tres 6 seguidos, tu última ficha movida vuelve a casa.

### Sistema de Puntuación y Economía
Gana monedas para comprar skins en la tienda. Las recompensas están definidas en el motor del juego:

* **+50 monedas:** Ganar la partida.
* **+15 monedas:** Comer una ficha rival (Captura).
* **+10 monedas:** Meter una ficha en la meta.
* **+2 monedas:** Sacar una ficha de casa.

> **Nota:** Las monedas se guardan automáticamente entre sesiones.

---

## 🗺️ Roadmap y Mejoras Futuras

- [x] **v1.0.0 - MVP Completo:** Motor de juego, IA básica, Tienda y Persistencia.
- [ ] **v1.1.0 - Mejoras de IA:** Implementar algoritmo Minimax para una CPU más difícil.
- [ ] **v2.0.0 - Online:** Implementar Firebase Realtime Database para multijugador remoto.
- [ ] **Sonido:** Añadir efectos de sonido al tirar dados y mover fichas.

---

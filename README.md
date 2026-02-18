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

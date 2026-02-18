# 🎲 Re-Fork Game
**Juego de Parchís moderno desarrollado en Kotlin con Jetpack Compose**
---
## 📖 Descripción
**Re-Fork Game** es una versión moderna y mejorada del clásico juego de Parchís, desarrollada completamente en **Kotlin** utilizando **Jetpack Compose** para una interfaz de usuario fluida y reactiva. Proyecto de 2º DAM.
### ✨ Características Principales
- 🎮 **Modo Multijugador Local** - Juega con 2-4 jugadores
- 🤖 **IA Inteligente** - CPU con diferentes niveles de dificultad
- 🎨 **Interfaz Moderna** - Material Design 3 y Jetpack Compose
- 🛒 **Sistema de Tienda** - Compra skins con monedas ganadas
- 💰 **Sistema de Monedas** - Gana jugando y desbloquea contenido
- 🎯 **Skins Personalizables** - 3 temas: Classic, Midnight, Candy
- ⚙️ **Configuración Completa** - Sonido, vibración, animaciones, velocidad IA
- 💾 **Guardado Automático** - Persistencia con DataStore
- 🎨 **Animaciones Fluidas** - Transiciones suaves y efectos visuales
- 📱 **Diseño Responsive** - Adaptado a diferentes pantallas
---
## 🚀 Instalación
### Requisitos
- Android Studio Hedgehog | 2023.1.1+
- JDK 17+
- Android SDK 34+
- Dispositivo con Android 7.0 (API 24)+
### Pasos
1. **Clona el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/ReForkParchisKotlin.git
   cd ReForkParchisKotlin
   ```
2. **Abre en Android Studio**
3. **Sincroniza Gradle**
4. **Ejecuta**
   ```bash
   ./gradlew installDebug
   ```
---
## 🎮 Cómo Jugar
1. Abre la app con logo RF animado
2. Toca JUGAR
3. Configura jugadores (2-4) y colores
4. Toca círculos para asignar colores
5. Iniciar Partida y juega
### Reglas
- 🎲 Tira el dado
- 🏠 Saca con 5
- 🎯 Mueve tocando fichas
- 🍴 Come rivales
- 🏆 Mete todas tus fichas primero
### Monedas
- +10 por ficha en meta
- +5 por comer rival
- +50 por ganar
---
## 🛠️ Tecnologías
- **Kotlin** 100%
- **Jetpack Compose** - UI
- **Material Design 3**
- **Navigation Compose**
- **ViewModel & StateFlow**
- **DataStore** - Persistencia
- **Coroutines**
---
## 🏗️ Arquitectura MVVM
```
View (Compose) → ViewModel → Model (Engine + Data)
```
### Estructura
```
app/src/main/java/com/example/parchismania/
├── ui/
│   ├── pantallas/ (MenuPrincipal, ConfiguracionPartida, etc)
│   ├── screens/ (GameScreen, VictoryScreen, ShopScreen)
│   ├── widgets/ (ParchisBoard, AppLogo)
│   ├── theme/
│   └── AppNav.kt
├── engine/ (GameEngine, GameState)
├── data/ (BoardSkin, Wallet, Settings)
└── GameViewModel.kt
```
---
## 🎨 Logo RF
```
    ╔══════════╗
    ║  R   F   ║
    ║  • • • • ║
    ╚══════════╝
```
- Letras RF bold
- 4 puntos (dado)
- Gradiente moderno
- Vectorial
- Animado
---
## 📊 Estadísticas
- **8,000+** líneas de código
- **45+** archivos
- **6** pantallas principales
- **25+** componentes
- **3** skins del tablero
---
## 🚀 Roadmap
### v1.0.0 (Actual)
- ✅ Juego completo
- ✅ IA 3 niveles
- ✅ Sistema monedas/tienda
- ✅ 3 skins
- ✅ Configuración
- ✅ Guardado auto
### v2.0.0 (Futuro)
- [ ] Multijugador online
- [ ] Más skins
- [ ] Sistema logros
- [ ] Estadísticas
- [ ] Tutorial
- [ ] Modo torneo
---
## 🤝 Contribuir
1. Fork
2. Branch (`git checkout -b feature/X`)
3. Commit (`git commit -m 'Add X'`)
4. Push (`git push origin feature/X`)
5. Pull Request
---
## 📝 Licencia
MIT License - Copyright (c) 2026 Re-Fork Game
---
## 👨‍💻 Autor
Proyecto de 2º DAM - Desarrollo de Aplicaciones Multiplataforma
---
## ❓ FAQ
**¿Offline?** Sí, funciona sin internet.
**¿Solo contra CPU?** Sí, 1 humano + CPUs.
**¿Se guardan monedas?** Sí, todo se guarda automáticamente.
---
<div align=center>
**Hecho con ❤️ usando Kotlin y Jetpack Compose**
⭐ Dale una estrella si te gusta ⭐
</div>
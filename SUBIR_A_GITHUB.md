# 🚀 Guía para Subir a GitHub

## Paso 1: Inicializar Git

```bash
# Abre PowerShell en la carpeta del proyecto
cd "E:\SEGUNDO DE DAM\PROYECTO INTERMODULAR\PROYECTOs\ReForkParchisKotlin"

# Inicializa el repositorio Git
git init

# Añade todos los archivos
git add .

# Primer commit
git commit -m "🎲 Initial commit: Re-Fork Game v1.0.0

- Juego de Parchís completo con Jetpack Compose
- IA con 3 niveles de dificultad  
- Sistema de monedas y tienda
- 3 skins del tablero
- Logo RF personalizado
- Configuración completa
- Guardado automático con DataStore"
```

## Paso 2: Crear Repositorio en GitHub

1. Ve a https://github.com
2. Click en "+" → "New repository"
3. Nombre: `ReForkParchisKotlin` o `Re-Fork-Game`
4. Descripción: "🎲 Juego de Parchís moderno con Kotlin y Jetpack Compose"
5. **NO** marques "Initialize with README" (ya tenemos uno)
6. Click "Create repository"

## Paso 3: Conectar con GitHub

```bash
# Añade el repositorio remoto (reemplaza TU_USUARIO con tu nombre de usuario)
git remote add origin https://github.com/TU_USUARIO/ReForkParchisKotlin.git

# Verifica que se añadió correctamente
git remote -v

# Cambia la rama a main (si estás en master)
git branch -M main

# Sube el código
git push -u origin main
```

## Paso 4: (Opcional) Configurar Usuario

Si es la primera vez usando Git:

```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu-email@example.com"
```

## Paso 5: Verificar en GitHub

1. Recarga la página de tu repositorio
2. Verás:
   - ✅ README.md con toda la documentación
   - ✅ Código del proyecto
   - ✅ LICENSE
   - ✅ .gitignore

## 🎉 ¡Listo!

Tu proyecto ahora está en GitHub con:
- 📄 README increíble
- 📜 Licencia MIT
- 🚫 .gitignore configurado
- 🎨 Logo RF
- 📱 Código completo

---

## Comandos Útiles para el Futuro

### Hacer cambios y subirlos

```bash
# Ver estado
git status

# Añadir cambios
git add .

# Commit
git commit -m "Descripción del cambio"

# Subir
git push
```

### Ver historial

```bash
git log --oneline
```

### Crear rama

```bash
git checkout -b feature/nueva-funcionalidad
```

### Cambiar entre ramas

```bash
git checkout main
git checkout feature/nueva-funcionalidad
```

---

## 📋 Checklist

Antes de subir, verifica que:

- [ ] Has inicializado Git (`git init`)
- [ ] Has creado el repositorio en GitHub
- [ ] Has añadido el remote (`git remote add origin...`)
- [ ] Has hecho el commit inicial
- [ ] Has subido el código (`git push -u origin main`)
- [ ] README.md se ve bien en GitHub
- [ ] LICENSE está presente
- [ ] .gitignore funciona (no se suben archivos innecesarios)

---

## 🆘 Solución de Problemas

### Error: "remote origin already exists"

```bash
git remote remove origin
git remote add origin https://github.com/TU_USUARIO/ReForkParchisKotlin.git
```

### Error: "failed to push"

```bash
git pull origin main --rebase
git push -u origin main
```

### Error: "Permission denied"

Configura tu token de acceso personal en GitHub Settings → Developer settings → Personal access tokens

---

## 🎯 Resultado Final

Tu repositorio en GitHub mostrará:

```
ReForkParchisKotlin/
├── 📄 README.md          ← Documentación increíble
├── 📜 LICENSE           ← MIT License
├── 🚫 .gitignore        ← Archivos ignorados
├── 📱 app/              ← Código de la app
├── 🔧 build.gradle.kts
├── ⚙️ settings.gradle.kts
└── 📚 Documentación adicional
```

**URL del repo:** `https://github.com/TU_USUARIO/ReForkParchisKotlin`

---

**¡Tu proyecto Re-Fork Game ahora está en GitHub!** 🎉


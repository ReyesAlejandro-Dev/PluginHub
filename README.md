# 🔌 PluginHub v1.0

**Gestor centralizado de plugins para servidores Paper/Spigot con integración multi-fuente**

PluginHub es un plugin avanzado y altamente configurable que permite buscar, instalar y actualizar plugins de Minecraft desde múltiples fuentes (SpigotMC, Modrinth, Hangar, BukkitDev) directamente desde el juego, sin necesidad de descargas manuales.

---

## ✨ Características Principales

### 🌐 Integración Multi-Fuente
- **SpigotMC** - Acceso a miles de plugins de Spigot vía API Spiget
- **Modrinth** - Plugins modernos y optimizados
- **Hangar** - Repositorio oficial de PaperMC
- **BukkitDev** - Plugins clásicos de Bukkit

### 🚀 Características Avanzadas
- 🔍 **Búsqueda en tiempo real** - Busca en todas las fuentes simultáneamente
- ⚡ **Instalación automática** - Descarga e instala con un solo comando
- 📊 **Información detallada** - Descargas, ratings, autor, versiones soportadas
- 🔄 **Sistema de actualizaciones** - Mantén tus plugins al día
- 🎨 **Interfaz colorida** - Mensajes claros y visualmente atractivos
- ⚙️ **Configuración flexible** - Personaliza timeouts, reintentos y más
- 🔒 **Seguro y confiable** - Validación de fuentes y URLs
- 📦 **Caché inteligente** - Mejora el rendimiento de búsquedas
- 🌐 **Operaciones asíncronas** - No bloquea el servidor durante descargas
- 🔄 **Reintentos automáticos** - Con backoff exponencial
- 🎯 **Sin plugins prepuestos** - Busca cualquier plugin disponible

---

## 📋 Requisitos

- **Servidor**: Paper 1.21+ o Spigot 1.21+
- **Java**: 21 o superior
- **Permisos**: Acceso de administrador para instalación

---

## 🚀 Instalación

1. Descarga el archivo `PluginHub.jar`
2. Colócalo en la carpeta `plugins/` de tu servidor
3. Reinicia el servidor
4. ¡Listo! Usa `/pluginhub help` para comenzar

---

## 📖 Comandos

### Comando Principal
```
/pluginhub [help|version|reload|info]
```
- `help` - Muestra la ayuda completa
- `version` - Información de la versión
- `reload` - Recarga la configuración
- `info` - Estadísticas del sistema
- `clearcache` - Limpia el caché de búsqueda

### Buscar Plugins
```
/phsearch <nombre>
```
Busca plugins en **todas las fuentes** simultáneamente (Spigot, Modrinth, Hangar, BukkitDev).

**Ejemplos:**
```
/phsearch essentials
/phsearch world edit
/phsearch permissions
/phsearch coreprotect
```

**Información mostrada:**
- Nombre y versión
- Autor y fuente
- Descripción
- Número de descargas
- Rating (si disponible)
- Enlace directo

### Instalar Plugins
```
/phinstall <nombre>
```
Descarga e instala un plugin automáticamente.

**Ejemplos:**
```
/phinstall essentialsx
/phinstall luckperms
/phinstall worldedit
```

### Actualizar Plugins
```
/phupdate [nombre]
```
- Sin argumentos: Lista plugins instalados
- Con nombre: Actualiza un plugin específico

**Ejemplos:**
```
/phupdate                    # Listar todos
/phupdate essentialsx        # Actualizar uno
```

---

## 🔑 Permisos

| Permiso | Descripción | Por defecto |
|---------|-------------|-------------|
| `pluginhub.admin` | Acceso completo a todos los comandos | OP |
| `pluginhub.search` | Permite buscar plugins | Todos |
| `pluginhub.install` | Permite instalar plugins | OP |
| `pluginhub.update` | Permite actualizar plugins | OP |

---

## ⚙️ Configuración

El archivo `config.yml` se genera automáticamente en `plugins/PluginHub/`:

```yaml
# Configuración de descargas
download:
  timeout: 30000      # Timeout en milisegundos
  retries: 3          # Reintentos en caso de fallo

# Actualizaciones automáticas (v2.0)
auto-update:
  enabled: false
  check-interval: 24  # Horas

# Sistema de caché
cache:
  enabled: true
  duration-minutes: 60

# Fuentes confiables
trusted-sources:
  - "https://www.spigotmc.org"
  - "https://dev.bukkit.org"
  - "https://github.com"
```

---

## ⚙️ Configuración Altamente Personalizable

PluginHub incluye **más de 100 opciones configurables** en `plugins/PluginHub/config.yml`

### 🎛️ Principales Categorías

**General** - Idioma, banner, debug, actualizaciones  
**Búsqueda** - Resultados máximos, timeout, fuentes habilitadas  
**Descargas** - Timeout, reintentos, progreso, tamaño máximo  
**Caché** - Duración, tamaño, persistencia  
**Seguridad** - Fuentes confiables, SSL, HTTPS  
**Rendimiento** - Threads, búsquedas simultáneas, cooldown  
**Mensajes** - Personaliza todos los mensajes del plugin  
**Notificaciones** - Discord webhooks, alertas  
**Comandos** - Habilitar/deshabilitar, cooldowns  
**Avanzado** - Proxy, User-Agent, HTTP config  
**Experimental** - Características en desarrollo

Ver el archivo `config.yml` completo con todas las opciones y comentarios detallados.

**Recargar configuración:**
```bash
/pluginhub reload
```

---

## 🌍 Fuentes de Plugins

PluginHub busca automáticamente en múltiples repositorios:

### SpigotMC (Spiget API)
- Miles de plugins disponibles
- Información de descargas y ratings
- Actualizaciones automáticas

### Modrinth
- Plugins modernos y optimizados
- Soporte para múltiples versiones
- Categorización avanzada

### Hangar (PaperMC)
- Repositorio oficial de Paper
- Plugins verificados
- Optimizados para Paper

### BukkitDev
- Plugins clásicos de Bukkit
- Gran variedad histórica
- Compatibilidad legacy

**¡Busca cualquier plugin disponible en estas fuentes!** No hay límites ni plugins prepuestos.

---

## 🛠️ Desarrollo

### Compilar desde el código fuente

```bash
# Clonar el repositorio
git clone https://github.com/tuusuario/PluginHub.git
cd PluginHub

# Compilar con Gradle
./gradlew build

# El JAR estará en build/libs/
```

### Ejecutar servidor de prueba

```bash
./gradlew runServer
```

---

## 📊 Arquitectura del Código

```
src/main/java/com/pluginhub/
├── PluginHub.java              # Clase principal
├── commands/                   # Comandos del plugin
│   ├── PluginHubCommand.java
│   ├── PluginSearchCommand.java
│   ├── PluginInstallCommand.java
│   └── PluginUpdateCommand.java
├── managers/                   # Lógica de negocio
│   └── PluginDownloader.java
├── models/                     # Modelos de datos
│   └── PluginInfo.java
└── utils/                      # Utilidades
    ├── ConfigManager.java
    └── ColorLogger.java
```

### Mejoras Implementadas v2.0

✅ **Integración Multi-API** - Spigot, Modrinth, Hangar, BukkitDev  
✅ **Búsqueda paralela** - Busca en todas las fuentes simultáneamente  
✅ **Sin plugins prepuestos** - Acceso a miles de plugins reales  
✅ **HTTP Client moderno** - OkHttp para descargas eficientes  
✅ **JSON parsing** - Gson para APIs REST  
✅ **Web scraping** - Jsoup para BukkitDev  
✅ **Arquitectura limpia** - Separación de responsabilidades  
✅ **Manejo robusto de errores** - Try-catch y logging apropiado  
✅ **Operaciones asíncronas** - CompletableFuture para descargas  
✅ **Validación de entrada** - Null safety y validaciones  
✅ **Configuración externa** - Sistema de config.yml  
✅ **Caché inteligente** - Mejora el rendimiento  
✅ **Tab completion** - Autocompletado en comandos  
✅ **Documentación completa** - JavaDoc en todo el código  
✅ **Reintentos automáticos** - Con backoff exponencial  
✅ **Thread safety** - ConcurrentHashMap y sincronización  
✅ **Builder pattern** - Para construcción de objetos complejos  

---

## 🔮 Roadmap v3.0

- [ ] Actualizaciones automáticas programadas
- [ ] Sistema de dependencias automático
- [ ] Verificación de checksums SHA-256
- [ ] Filtrado por versión de Minecraft
- [ ] Base de datos SQLite para caché persistente
- [ ] Interfaz web de administración
- [ ] Notificaciones de actualizaciones
- [ ] Backup automático antes de actualizar
- [ ] Integración con GitHub Releases
- [ ] Sistema de ratings y reviews
- [ ] Categorías y filtros avanzados
- [ ] Comparación de plugins similares

---

## 🤝 Contribuir

Las contribuciones son bienvenidas! Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

## 👤 Autor

**Pecar**

- GitHub: [@Pecar00](https://github.com/Pecar)

---

## 🙏 Agradecimientos

- Comunidad de SpigotMC
- Desarrolladores de Paper
- Todos los creadores de plugins incluidos

---

## 📞 Soporte

¿Necesitas ayuda? 

- 📧 Email: soporte@pluginhub.com
- 💬 Discord: [Únete a nuestro servidor](https://discord.gg/pluginhub)
- 🐛 Issues: [GitHub Issues](https://github.com/ReyesAlejandro-Dev/PluginHub/issues)

---

<div align="center">

**⭐ Si te gusta este proyecto, dale una estrella en GitHub! ⭐**

Made with ❤️ by Pecar

</div>

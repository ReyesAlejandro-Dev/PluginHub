# 📋 Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

---

## [1.0.0] - 2024-12-06

### 🎉 Lanzamiento Inicial

#### ✨ Añadido
- Sistema completo de búsqueda de plugins
- Instalación automática de plugins desde repositorios
- Sistema de actualización de plugins
- Interfaz de consola colorida con ASCII art
- Configuración externa mediante `config.yml`
- Sistema de caché para mejorar rendimiento
- Operaciones asíncronas para no bloquear el servidor
- Reintentos automáticos con backoff exponencial
- Tab completion en todos los comandos
- Sistema de permisos granular
- Logging mejorado con colores ANSI
- Validación robusta de entrada
- Manejo de errores completo

#### 📦 Plugins Soportados
- EssentialsX
- LuckPerms
- WorldEdit
- Vault
- ProtocolLib
- PlotSquared
- CoreProtect
- Citizens

#### 🏗️ Arquitectura
- Separación de responsabilidades (Commands, Managers, Models, Utils)
- Uso de CompletableFuture para operaciones asíncronas
- Thread-safe con ConcurrentHashMap
- Patrón de diseño limpio y mantenible
- Documentación JavaDoc completa

#### 🔧 Comandos
- `/pluginhub` - Comando principal con subcomandos
- `/phsearch <nombre>` - Buscar plugins
- `/phinstall <nombre>` - Instalar plugins
- `/phupdate [nombre]` - Actualizar plugins

#### ⚙️ Configuración
- Timeout configurable para descargas
- Número de reintentos configurable
- Sistema de caché con duración personalizable
- Lista de fuentes confiables
- Soporte para múltiples idiomas (preparado)

---

## [Unreleased]

### 🔮 Planeado para v2.0
- Integración con API oficial de SpigotMC
- Integración con Modrinth
- Actualizaciones automáticas programadas
- Sistema de dependencias entre plugins
- Verificación de checksums SHA-256
- Soporte para múltiples versiones de Minecraft
- Base de datos SQLite para caché persistente
- Interfaz web de administración
- Notificaciones de actualizaciones disponibles
- Backup automático antes de actualizar
- Rollback de actualizaciones fallidas
- Estadísticas de uso
- Sistema de ratings y reviews

---

## Tipos de Cambios

- `✨ Añadido` - Para nuevas características
- `🔧 Cambiado` - Para cambios en funcionalidad existente
- `⚠️ Deprecado` - Para características que serán removidas
- `🗑️ Removido` - Para características removidas
- `🐛 Corregido` - Para corrección de bugs
- `🔒 Seguridad` - Para vulnerabilidades de seguridad
- `📦 Dependencias` - Para cambios en dependencias
- `📝 Documentación` - Para cambios en documentación

---

[1.0.0]: https://github.com/tuusuario/PluginHub/releases/tag/v1.0.0
[Unreleased]: https://github.com/tuusuario/PluginHub/compare/v1.0.0...HEAD

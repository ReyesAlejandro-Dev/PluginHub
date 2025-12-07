# 📋 Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [1.1.0] - 2024-12-06

### 🎉 Actualización Mayor - Gestión Avanzada de Plugins

#### ✨ Añadido - Nuevos Sistemas de Gestión

**Sistema de Favoritos**
- Comando `/phfavorite` con subcomandos: add, remove, list, clear
- Persistencia en `favorites.yml`
- Gestión completa de plugins favoritos
- Tab completion integrado

**Sistema de Historial**
- Comando `/phhistory` para ver instalaciones y actualizaciones
- Registro automático de todas las operaciones
- Información detallada: versión, fuente, timestamp, usuario
- Persistencia en `history.yml`
- Diferenciación visual entre instalación y actualización

**Sistema de Perfiles**
- Comando `/phprofile` con 7 subcomandos
- 4 perfiles predeterminados: starter-pack, survival, creative, minigames
- Instalación masiva de perfiles completos
- Creación de perfiles personalizados
- Gestión de plugins en perfiles
- Persistencia en `profiles.yml`

**Sistema de Backups**
- Comando `/phbackup` para gestionar backups
- Backup automático antes de actualizar
- Mantiene últimos 5 backups por plugin
- Restauración fácil desde backups
- Limpieza automática de backups antiguos
- Almacenamiento en carpeta `backups/`

**Comando de Información Detallada**
- Comando `/phinfo` para ver detalles completos
- Muestra: versión, autor, descripción, estadísticas
- Enlaces directos a página y descarga
- Estado de instalación
- Formato visual profesional

#### 🔧 Mejorado

- `PluginInstallCommand` ahora registra en historial automáticamente
- `PluginUpdateCommand` crea backups antes de actualizar
- Integración completa de todos los managers en `PluginHub.java`
- Mensajes mejorados con referencias a nuevos comandos
- Tab completion mejorado en todos los comandos

#### ⚙️ Configuración Nueva

- Sección `favorites` en config.yml
- Sección `history` en config.yml
- Sección `profiles` en config.yml
- Sección `backups` en config.yml
- Configuración de límites y comportamiento

#### 🎮 Nuevos Comandos

- `/phfavorite` (alias: `/phfav`) - Gestionar favoritos
- `/phhistory` (alias: `/phhist`) - Ver historial
- `/phprofile` (alias: `/phprof`) - Gestionar perfiles
- `/phbackup` (alias: `/phbak`) - Gestionar backups
- `/phinfo` - Ver información detallada

#### 🔐 Nuevos Permisos

- `pluginhub.favorite` - Gestionar favoritos
- `pluginhub.history` - Ver historial
- `pluginhub.profile` - Gestionar perfiles
- `pluginhub.backup` - Gestionar backups
- `pluginhub.info` - Ver información
- `pluginhub.admin` ahora incluye todos los permisos

#### 📦 Archivos de Datos

- `favorites.yml` - Almacena plugins favoritos
- `history.yml` - Almacena historial de instalaciones
- `profiles.yml` - Almacena perfiles personalizados
- `backups/` - Carpeta para backups de plugins

## [1.0.0] - 2024-12-06

### 🎉 Lanzamiento Inicial - Integración Multi-Fuente y Altamente Configurable

#### ✨ Añadido

**Integración Multi-Fuente**
- Integración con SpigotMC vía API Spiget
- Integración con Modrinth vía API REST
- Integración con Hangar (PaperMC) vía API REST
- Integración con BukkitDev vía web scraping
- Búsqueda paralela en todas las fuentes simultáneamente

**Sistema de Búsqueda**
- Sistema de caché inteligente con timestamps
- Información detallada de plugins (descargas, ratings, autor)
- Builder pattern para PluginInfo
- Enum PluginSource para identificar fuentes
- Comando `/pluginhub clearcache`

**Sistema de Instalación**
- Instalación automática de plugins
- Descarga desde múltiples fuentes
- Reintentos automáticos con backoff exponencial
- Validación de archivos descargados

**Tecnología**
- HTTP client moderno con OkHttp
- JSON parsing con Gson
- Web scraping con Jsoup
- Operaciones asíncronas con CompletableFuture
- Thread-safe con ConcurrentHashMap

#### 🔧 Comandos Iniciales

- `/pluginhub` - Comando principal con subcomandos
- `/phsearch <nombre>` - Buscar plugins
- `/phinstall <nombre>` - Instalar plugins
- `/phupdate [nombre]` - Actualizar plugins

#### ⚙️ Configuración

- Sistema completo de configuración mediante `config.yml`
- 100+ opciones configurables
- Timeout configurable para descargas
- Número de reintentos configurable
- Sistema de caché con duración personalizable
- Lista de fuentes confiables
- Personalización de mensajes

#### 🏗️ Arquitectura

- Separación de responsabilidades (Commands, Managers, Models, Utils)
- Uso de CompletableFuture para operaciones asíncronas
- Thread-safe con ConcurrentHashMap
- Patrón de diseño limpio y mantenible
- Documentación JavaDoc completa

#### 📦 Dependencias

- OkHttp 4.11.0 - Cliente HTTP
- Gson 2.10.1 - JSON parsing
- Jsoup 1.16.1 - HTML parsing
- Paper API 1.21.1

## [Unreleased]

### 🔮 Planeado para v2.0

- Actualizaciones automáticas programadas (cron-like)
- Sistema de dependencias automático
- Verificación de compatibilidad con versión de Minecraft
- Filtros avanzados de búsqueda (categoría, rating, fecha)
- Herramienta de comparación de plugins
- Verificación de checksums SHA-256
- Notificaciones Discord webhook
- API REST para gestión remota
- Interfaz web de administración
- Barra de progreso visual en descargas
- Sugerencias inteligentes (corrección de typos)
- Modo interactivo
- Sistema de whitelist/blacklist
- Dashboard de estadísticas
- Funcionalidad de exportación
- Personalización de temas
- Soporte multiidioma completo

## Tipos de Cambios

- `✨ Añadido` - Para nuevas características
- `🔧 Cambiado` - Para cambios en funcionalidad existente
- `⚠️ Deprecado` - Para características que serán removidas
- `🗑️ Removido` - Para características removidas
- `🐛 Corregido` - Para corrección de bugs
- `🔒 Seguridad` - Para vulnerabilidades de seguridad
- `📦 Dependencias` - Para cambios en dependencias
- `📝 Documentación` - Para cambios en documentación

[1.1.0]: https://github.com/ReyesAlejandro-Dev/PluginHub/releases/tag/v1.1.0
[1.0.0]: https://github.com/ReyesAlejandro-Dev/PluginHub/releases/tag/v1.0.0
[Unreleased]: https://github.com/ReyesAlejandro-Dev/PluginHub/compare/v1.1.0...HEAD

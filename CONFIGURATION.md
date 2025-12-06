# ⚙️ Guía de Configuración - PluginHub v1.0

Documentación completa de todas las opciones de configuración disponibles.

---

## 📋 Tabla de Contenidos

- [General](#general)
- [Búsqueda](#búsqueda)
- [Descargas](#descargas)
- [Caché](#caché)
- [Actualizaciones Automáticas](#actualizaciones-automáticas)
- [Seguridad](#seguridad)
- [Rendimiento](#rendimiento)
- [Mensajes Personalizados](#mensajes-personalizados)
- [Notificaciones](#notificaciones)
- [Estadísticas](#estadísticas)
- [Comandos](#comandos)
- [Integraciones](#integraciones)
- [Avanzado](#avanzado)
- [Experimental](#experimental)

---

## General

Configuración básica del plugin.

```yaml
general:
  language: "es"
  prefix: "&6[&ePluginHub&6]&r"
  show-banner: true
  debug: false
  check-updates: true
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `language` | String | `"es"` | Idioma del plugin (en, es, fr, de, pt) |
| `prefix` | String | `"&6[&ePluginHub&6]&r"` | Prefijo de mensajes |
| `show-banner` | Boolean | `true` | Mostrar banner ASCII al iniciar |
| `debug` | Boolean | `false` | Modo debug (más información en consola) |
| `check-updates` | Boolean | `true` | Verificar actualizaciones al iniciar |

---

## Búsqueda

Configuración del sistema de búsqueda.

```yaml
search:
  max-results: 10
  timeout: 10
  show-detailed-info: true
  sort-by-downloads: true
  sources:
    spigot: true
    modrinth: true
    hangar: true
    bukkit: true
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `max-results` | Integer | `10` | Número máximo de resultados por búsqueda |
| `timeout` | Integer | `10` | Tiempo máximo de espera (segundos) |
| `show-detailed-info` | Boolean | `true` | Mostrar información detallada |
| `sort-by-downloads` | Boolean | `true` | Ordenar por popularidad |
| `sources.spigot` | Boolean | `true` | Habilitar búsqueda en SpigotMC |
| `sources.modrinth` | Boolean | `true` | Habilitar búsqueda en Modrinth |
| `sources.hangar` | Boolean | `true` | Habilitar búsqueda en Hangar |
| `sources.bukkit` | Boolean | `true` | Habilitar búsqueda en BukkitDev |

**Nota:** Deshabilitar fuentes reduce el tiempo de búsqueda pero limita los resultados.

---

## Descargas

Configuración del sistema de descargas.

```yaml
download:
  timeout: 30000
  retries: 3
  retry-delay: 2000
  exponential-backoff: true
  show-progress: true
  verify-integrity: false
  max-file-size: 50
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `timeout` | Integer | `30000` | Timeout en milisegundos |
| `retries` | Integer | `3` | Número de reintentos |
| `retry-delay` | Integer | `2000` | Delay entre reintentos (ms) |
| `exponential-backoff` | Boolean | `true` | Aumentar delay en cada reintento |
| `show-progress` | Boolean | `true` | Mostrar progreso de descarga |
| `verify-integrity` | Boolean | `false` | Verificar integridad del archivo |
| `max-file-size` | Integer | `50` | Tamaño máximo en MB (0 = sin límite) |

---

## Caché

Sistema de caché para mejorar rendimiento.

```yaml
cache:
  enabled: true
  duration-minutes: 60
  clear-on-restart: false
  max-size: 1000
  persistent: false
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `enabled` | Boolean | `true` | Habilitar sistema de caché |
| `duration-minutes` | Integer | `60` | Duración del caché en minutos |
| `clear-on-restart` | Boolean | `false` | Limpiar caché al reiniciar |
| `max-size` | Integer | `1000` | Número máximo de plugins en caché |
| `persistent` | Boolean | `false` | Guardar caché en disco |

**Recomendación:** Mantener el caché habilitado para mejor rendimiento.

---

## Actualizaciones Automáticas

Sistema de actualizaciones automáticas (v2.0).

```yaml
auto-update:
  enabled: false
  check-interval: 24
  whitelist-enabled: false
  whitelist:
    - "essentialsx"
  blacklist-enabled: false
  blacklist:
    - "worldedit"
  backup-before-update: true
  notify-admins: true
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `enabled` | Boolean | `false` | Habilitar actualizaciones automáticas |
| `check-interval` | Integer | `24` | Intervalo de verificación (horas) |
| `whitelist-enabled` | Boolean | `false` | Solo actualizar plugins en whitelist |
| `whitelist` | List | `[]` | Lista de plugins a actualizar |
| `blacklist-enabled` | Boolean | `false` | No actualizar plugins en blacklist |
| `blacklist` | List | `[]` | Lista de plugins a NO actualizar |
| `backup-before-update` | Boolean | `true` | Crear backup antes de actualizar |
| `notify-admins` | Boolean | `true` | Notificar a admins |

---

## Seguridad

Configuración de seguridad.

```yaml
security:
  trusted-sources:
    - "https://www.spigotmc.org"
    - "https://api.modrinth.com"
  verify-ssl: true
  https-only: true
  block-premium: false
  require-confirmation: false
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `trusted-sources` | List | Ver config | URLs permitidas para descargas |
| `verify-ssl` | Boolean | `true` | Verificar certificados SSL |
| `https-only` | Boolean | `true` | Permitir solo HTTPS |
| `block-premium` | Boolean | `false` | Bloquear plugins premium |
| `require-confirmation` | Boolean | `false` | Requerir confirmación para instalar |

---

## Rendimiento

Optimización de rendimiento.

```yaml
performance:
  thread-pool-size: 5
  compress-cache: false
  max-concurrent-searches: 2
  search-cooldown: 3
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `thread-pool-size` | Integer | `5` | Número de threads para operaciones |
| `compress-cache` | Boolean | `false` | Comprimir caché |
| `max-concurrent-searches` | Integer | `2` | Búsquedas simultáneas por jugador |
| `search-cooldown` | Integer | `3` | Cooldown entre búsquedas (segundos) |

---

## Mensajes Personalizados

Personaliza todos los mensajes del plugin.

```yaml
messages:
  search-start: "&e⏳ Buscando '&f{query}&e'..."
  install-success: "&a✓ &f{plugin}&a instalado"
  # ... más mensajes
```

**Variables disponibles:**
- `{query}` - Término de búsqueda
- `{plugin}` - Nombre del plugin
- `{version}` - Versión del plugin
- `{source}` - Fuente del plugin
- `{count}` - Número de resultados

---

## Notificaciones

Sistema de notificaciones.

```yaml
notifications:
  notify-on-start: true
  notify-on-install: true
  notify-on-error: true
  discord:
    enabled: false
    webhook-url: ""
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `notify-on-start` | Boolean | `true` | Notificar al iniciar |
| `notify-on-install` | Boolean | `true` | Notificar instalaciones |
| `notify-on-error` | Boolean | `true` | Notificar errores |
| `discord.enabled` | Boolean | `false` | Habilitar Discord webhook |
| `discord.webhook-url` | String | `""` | URL del webhook |

---

## Estadísticas

Recopilación de estadísticas.

```yaml
statistics:
  enabled: true
  send-anonymous: true
  save-history: true
  show-in-info: true
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `enabled` | Boolean | `true` | Recopilar estadísticas |
| `send-anonymous` | Boolean | `true` | Enviar estadísticas anónimas (bStats) |
| `save-history` | Boolean | `true` | Guardar historial de instalaciones |
| `show-in-info` | Boolean | `true` | Mostrar en `/pluginhub info` |

---

## Comandos

Configuración de comandos.

```yaml
commands:
  search:
    enabled: true
    cooldown: 3
  install:
    enabled: true
    cooldown: 5
```

| Opción | Tipo | Por Defecto | Descripción |
|--------|------|-------------|-------------|
| `<comando>.enabled` | Boolean | `true` | Habilitar comando |
| `<comando>.cooldown` | Integer | Variable | Cooldown en segundos |

---

## Integraciones

Integración con otros plugins.

```yaml
integrations:
  luckperms:
    enabled: true
  vault:
    enabled: false
    charge-for-install: false
    install-cost: 1000.0
```

---

## Avanzado

Configuración avanzada.

```yaml
advanced:
  user-agent: "PluginHub/1.0"
  http-timeout: 15000
  follow-redirects: true
  max-redirects: 5
  proxy:
    enabled: false
    host: ""
    port: 8080
```

---

## Experimental

Características experimentales (v2.0).

```yaml
experimental:
  auto-dependencies: false
  verify-checksums: false
  version-filtering: false
  web-interface:
    enabled: false
    port: 8080
```

**Advertencia:** Las características experimentales pueden ser inestables.

---

## 💡 Ejemplos de Configuración

### Servidor de Producción

```yaml
general:
  debug: false
  check-updates: true

search:
  max-results: 5
  timeout: 5

download:
  retries: 5
  verify-integrity: true

cache:
  enabled: true
  duration-minutes: 120

security:
  verify-ssl: true
  https-only: true
  require-confirmation: true

performance:
  thread-pool-size: 3
  search-cooldown: 5
```

### Servidor de Desarrollo

```yaml
general:
  debug: true
  check-updates: false

search:
  max-results: 20
  timeout: 15

cache:
  enabled: false

security:
  require-confirmation: false

performance:
  thread-pool-size: 10
  search-cooldown: 0
```

---

## 🔄 Recargar Configuración

Después de modificar el `config.yml`:

```bash
/pluginhub reload
```

Los cambios se aplicarán inmediatamente sin necesidad de reiniciar el servidor.

---

## 📞 Soporte

¿Problemas con la configuración?

- [GitHub Issues](https://github.com/ReyesAlejandro-Dev/PluginHub/issues)
- [Documentación Completa](https://github.com/ReyesAlejandro-Dev/PluginHub)

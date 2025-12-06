# 📚 Ejemplos de Uso - PluginHub v2.0

Guía completa con ejemplos del nuevo sistema multi-fuente.

---

## 🎯 Búsqueda Avanzada

### Búsqueda Simple

```bash
/phsearch essentials
```

**Resultado:**
```
⏳ Buscando 'essentials' en múltiples fuentes...
Esto puede tardar unos segundos...

Buscando en SpigotMC...
Buscando en Modrinth...
Buscando en Hangar...
Buscando en BukkitDev...

✓ Se encontraron 5 resultado(s):
════════════════════════════════════════

● EssentialsX (v2.20.1)
  Essential commands and utilities for Minecraft servers
  Autor: EssentialsX Team | Fuente: SpigotMC
  Descargas: 2.5M | Rating: 4.8★
  → /phinstall essentialsx
  https://www.spigotmc.org/resources/9089/

● Essentials (v2.19.7)
  Core essentials plugin
  Autor: md_5 | Fuente: BukkitDev
  Descargas: 1.2M
  → /phinstall essentials
  https://dev.bukkit.org/projects/essentials

════════════════════════════════════════
Usa /phinstall <nombre> para instalar
```

### Búsqueda por Categoría

```bash
# Buscar plugins de protección
/phsearch protection

# Buscar plugins de economía
/phsearch economy

# Buscar plugins de mundos
/phsearch world

# Buscar plugins de permisos
/phsearch permissions
```

### Búsqueda Específica

```bash
# Buscar por nombre exacto
/phsearch LuckPerms

# Buscar por funcionalidad
/phsearch "anti grief"

# Buscar por autor
/phsearch "md_5"
```

---

## 🚀 Instalación desde Diferentes Fuentes

### Desde SpigotMC

```bash
# Buscar primero
/phsearch coreprotect

# Resultado mostrará:
# ● CoreProtect (v22.2)
#   Fuente: SpigotMC
#   → /phinstall coreprotect

# Instalar
/phinstall coreprotect
```

**Salida:**
```
⏳ Instalando CoreProtect v22.2...
Fuente: SpigotMC
Esto puede tardar unos segundos...

⏳ Descargando CoreProtect v22.2 desde SpigotMC (intento 1/3)...
Descargados 2.45 MB
✓ CoreProtect descargado correctamente
✓ CoreProtect instalado correctamente

╔════════════════════════════════════════╗
║  Próximos pasos                        ║
╚════════════════════════════════════════╝
1. Reinicia el servidor para cargar el plugin
2. Configura el plugin según tus necesidades
3. Verifica que funcione correctamente

Más información: https://www.spigotmc.org/resources/8631/
```

### Desde Modrinth

```bash
/phsearch lithium
/phinstall lithium
```

**Ventajas de Modrinth:**
- Plugins modernos y optimizados
- Información de versiones soportadas
- Categorización detallada

### Desde Hangar (PaperMC)

```bash
/phsearch maintenance
/phinstall maintenance
```

**Ventajas de Hangar:**
- Plugins oficiales de Paper
- Verificados y seguros
- Optimizados para Paper

### Desde BukkitDev

```bash
/phsearch worldguard
/phinstall worldguard
```

**Ventajas de BukkitDev:**
- Plugins clásicos
- Gran variedad histórica
- Compatibilidad legacy

---

## 🔍 Comparación de Fuentes

### Ejemplo: Buscar "worldedit"

```bash
/phsearch worldedit
```

**Resultados de múltiples fuentes:**

```
✓ Se encontraron 3 resultado(s):

● WorldEdit (v7.2.15)
  In-game world editing and building tool
  Autor: sk89q | Fuente: SpigotMC
  Descargas: 5.2M | Rating: 4.9★
  → /phinstall worldedit

● WorldEdit (v7.3.0)
  Fast and modern world editing
  Autor: EngineHub | Fuente: Modrinth
  Descargas: 3.8M
  → /phinstall worldedit

● WorldEdit (v7.2.14)
  Classic world editing plugin
  Autor: sk89q | Fuente: BukkitDev
  Descargas: 4.1M
  → /phinstall worldedit
```

**Nota:** El sistema elimina duplicados por nombre, mostrando la versión más popular.

---

## 📊 Información del Sistema

### Ver Estadísticas

```bash
/pluginhub info
```

**Salida:**
```
╔════════════════════════════════════════╗
║      PluginHub - Información           ║
╚════════════════════════════════════════╝
● Plugins en caché: 47
● Plugins instalados: 12
● Fuentes activas: SpigotMC, Modrinth, Hangar, BukkitDev
● Caché habilitado: true
● Auto-actualización: false

Usa /phsearch <nombre> para buscar plugins
```

### Limpiar Caché

```bash
/pluginhub clearcache
```

**Cuándo usar:**
- Después de mucho tiempo sin buscar
- Si los resultados parecen desactualizados
- Para forzar búsqueda fresca

---

## 🎮 Casos de Uso Reales

### Configurar Servidor de Supervivencia

```bash
# 1. Comandos básicos
/phsearch essentials
/phinstall essentialsx

# 2. Permisos
/phsearch luckperms
/phinstall luckperms

# 3. Protección
/phsearch coreprotect
/phinstall coreprotect

# 4. Economía
/phsearch vault
/phinstall vault

# 5. Protección de terrenos
/phsearch griefprevention
/phinstall griefprevention

# 6. Reiniciar servidor
/stop
```

### Configurar Servidor Creativo

```bash
# 1. Edición de mundos
/phsearch worldedit
/phinstall worldedit

# 2. Parcelas
/phsearch plotsquared
/phinstall plotsquared

# 3. Comandos útiles
/phsearch essentialsx
/phinstall essentialsx

# 4. Permisos por mundo
/phsearch luckperms
/phinstall luckperms
```

### Configurar Servidor de Minijuegos

```bash
# 1. Gestión de mundos
/phsearch multiverse
/phinstall multiverse-core

# 2. Lobbies
/phsearch deluxehub
/phinstall deluxehub

# 3. NPCs
/phsearch citizens
/phinstall citizens

# 4. Scoreboards
/phsearch featherboard
/phinstall featherboard
```

---

## 🔧 Configuración Avanzada

### Optimizar Búsquedas

Edita `plugins/PluginHub/config.yml`:

```yaml
# Aumentar duración del caché
cache:
  enabled: true
  duration-minutes: 120  # 2 horas

# Más reintentos para conexiones lentas
download:
  timeout: 45000  # 45 segundos
  retries: 5
```

### Fuentes Confiables

```yaml
trusted-sources:
  - "https://www.spigotmc.org"
  - "https://api.modrinth.com"
  - "https://hangar.papermc.io"
  - "https://dev.bukkit.org"
  - "https://github.com"
```

---

## 🚨 Solución de Problemas

### Error: No se encontraron resultados

```bash
# Problema
/phsearch myplugin
✗ No se encontraron plugins para: myplugin

# Soluciones:
1. Verifica el nombre del plugin
2. Intenta con términos más generales
3. Busca por categoría
4. Limpia el caché: /pluginhub clearcache
```

### Error: Timeout en búsqueda

```bash
# Problema
Error buscando en Spigot: timeout

# Solución:
1. Aumenta el timeout en config.yml
2. Verifica tu conexión a internet
3. Intenta de nuevo más tarde
4. El plugin seguirá buscando en otras fuentes
```

### Plugin no se descarga

```bash
# Problema
✗ Error durante la instalación de plugin

# Soluciones:
1. Verifica que la URL de descarga esté disponible
2. Algunos plugins premium no se pueden descargar automáticamente
3. Intenta desde otra fuente:
   /phsearch plugin
   # Busca el mismo plugin de otra fuente
```

---

## 💡 Tips Profesionales

### 1. Búsqueda Eficiente

```bash
# Mal: Muy genérico
/phsearch plugin

# Bien: Específico
/phsearch "grief protection"
/phsearch worldedit
/phsearch economy vault
```

### 2. Verificar Antes de Instalar

```bash
# Siempre busca primero para ver opciones
/phsearch permissions

# Compara las diferentes fuentes
# Verifica descargas y ratings
# Luego instala el más popular/actualizado
/phinstall luckperms
```

### 3. Mantener Caché Limpio

```bash
# Limpia el caché semanalmente
/pluginhub clearcache

# Luego busca de nuevo
/phsearch essentials
```

### 4. Usar Tab Completion

```bash
/phsearch ess[TAB]     # Autocompleta con plugins en caché
/phinstall luck[TAB]   # Sugiere plugins disponibles
```

---

## 📈 Comparativa de Rendimiento

### Búsqueda v1.0 vs v2.0

**v1.0 (Plugins prepuestos):**
- ❌ Solo 8 plugins disponibles
- ❌ Búsqueda local instantánea pero limitada
- ❌ Sin información de descargas/ratings

**v2.0 (Multi-fuente):**
- ✅ Miles de plugins disponibles
- ✅ Búsqueda en 4 fuentes simultáneamente
- ✅ Información completa (descargas, ratings, autor)
- ✅ Caché inteligente para rendimiento
- ✅ Resultados en 2-5 segundos

---

## 🔗 Recursos Adicionales

- [README.md](README.md) - Documentación completa
- [CHANGELOG.md](CHANGELOG.md) - Cambios en v2.0
- [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - Arquitectura

---

<div align="center">

**¿Encontraste un plugin interesante? ¡Compártelo con la comunidad!**

[GitHub Issues](https://github.com/tuusuario/PluginHub/issues) | [Discussions](https://github.com/tuusuario/PluginHub/discussions)

</div>

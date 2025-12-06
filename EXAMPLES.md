# 📚 Ejemplos de Uso - PluginHub

Esta guía proporciona ejemplos prácticos de cómo usar PluginHub en diferentes escenarios.

---

## 🎯 Casos de Uso Comunes

### 1. Configurar un Servidor Nuevo

Cuando inicias un servidor desde cero, necesitas instalar los plugins básicos:

```bash
# Buscar plugins esenciales
/phsearch essentials

# Instalar EssentialsX
/phinstall essentialsx

# Instalar sistema de permisos
/phinstall luckperms

# Instalar economía
/phinstall vault

# Verificar instalación
/pluginhub info
```

### 2. Buscar un Plugin Específico

```bash
# Búsqueda simple
/phsearch worldedit

# Búsqueda por categoría
/phsearch permissions

# Búsqueda por descripción
/phsearch "world editing"
```

### 3. Instalar Múltiples Plugins

```bash
# Instalar plugins uno por uno
/phinstall essentialsx
/phinstall luckperms
/phinstall worldedit
/phinstall vault

# Después de instalar todos, reinicia el servidor
/stop
# o
/reload confirm
```

### 4. Actualizar Plugins

```bash
# Ver plugins instalados
/phupdate

# Actualizar un plugin específico
/phupdate essentialsx

# Verificar versión después de actualizar
/pluginhub version
```

### 5. Gestión de Configuración

```bash
# Ver información del sistema
/pluginhub info

# Recargar configuración después de cambios
/pluginhub reload

# Ver ayuda completa
/pluginhub help
```

---

## 🎮 Escenarios por Tipo de Servidor

### Servidor de Supervivencia

```bash
# Comandos básicos
/phinstall essentialsx

# Protección de terrenos
/phinstall worldguard

# Economía
/phinstall vault

# Tiendas
/phinstall shopkeepers

# Protección contra griefing
/phinstall coreprotect
```

### Servidor Creativo

```bash
# Edición de mundos
/phinstall worldedit

# Parcelas
/phinstall plotsquared

# Comandos útiles
/phinstall essentialsx

# Permisos
/phinstall luckperms
```

### Servidor de Minijuegos

```bash
# Sistema de lobbies
/phinstall multiverse

# Permisos por mundo
/phinstall luckperms

# Comandos
/phinstall essentialsx

# NPCs
/phinstall citizens
```

### Servidor Roleplay

```bash
# NPCs avanzados
/phinstall citizens

# Chat personalizado
/phinstall chatcontrol

# Economía
/phinstall vault

# Comandos
/phinstall essentialsx
```

---

## 🔧 Configuración Avanzada

### Modificar Timeouts

Edita `plugins/PluginHub/config.yml`:

```yaml
download:
  timeout: 60000  # 60 segundos para plugins grandes
  retries: 5      # Más reintentos
```

Luego recarga:
```bash
/pluginhub reload
```

### Habilitar Caché

```yaml
cache:
  enabled: true
  duration-minutes: 120  # 2 horas
```

### Añadir Fuentes Confiables

```yaml
trusted-sources:
  - "https://www.spigotmc.org"
  - "https://dev.bukkit.org"
  - "https://github.com"
  - "https://tu-repositorio-custom.com"
```

---

## 🎨 Ejemplos de Salida

### Búsqueda Exitosa

```
⏳ Buscando plugins que coincidan con: essentials

✓ Se encontraron 1 resultado(s):
════════════════════════════════════════

● EssentialsX (v2.20.1)
  Essential commands and utilities for Minecraft servers
  → /phinstall essentialsx
  https://www.spigotmc.org/resources/essentialsx.9089/

════════════════════════════════════════
Usa /phinstall <nombre> para instalar
```

### Instalación Exitosa

```
⏳ Instalando EssentialsX v2.20.1...
Esto puede tardar unos segundos...

⏳ Descargando EssentialsX v2.20.1 (intento 1/3)...
Descargados 2.45 MB
✓ EssentialsX descargado correctamente
✓ EssentialsX instalado correctamente

╔════════════════════════════════════════╗
║  Próximos pasos                        ║
╚════════════════════════════════════════╝
1. Reinicia el servidor para cargar el plugin
2. Configura el plugin según tus necesidades
3. Verifica que funcione correctamente

Más información: https://www.spigotmc.org/resources/essentialsx.9089/
```

### Lista de Plugins Instalados

```
⏳ Verificando plugins instalados...

✓ Plugins instalados (5):
════════════════════════════════════════
● EssentialsX (v2.20.1 disponible)
  → /phupdate essentialsx
● LuckPerms (v5.4.121 disponible)
  → /phupdate luckperms
● WorldEdit (v7.2.15 disponible)
  → /phupdate worldedit
● Vault (v1.7.3 disponible)
  → /phupdate vault
● ProtocolLib (v5.1.0 disponible)
  → /phupdate protocollib
════════════════════════════════════════
Usa /phupdate <nombre> para actualizar un plugin
```

---

## 🚨 Solución de Problemas

### Error: Plugin no encontrado

```bash
# Problema
/phinstall worldedi
✗ Plugin no encontrado: worldedi
Intenta buscar con: /phsearch worldedi

# Solución
/phsearch worldedit
/phinstall worldedit
```

### Error: Sin permisos

```bash
# Problema
/phinstall essentialsx
✗ No tienes permiso para instalar plugins
Permiso requerido: pluginhub.install

# Solución (como admin)
/lp user TuNombre permission set pluginhub.install true
```

### Error: Timeout en descarga

```bash
# Problema
✗ Error durante la instalación de essentialsx
● Problemas de conexión a internet

# Solución 1: Reintentar
/phinstall essentialsx

# Solución 2: Aumentar timeout en config.yml
download:
  timeout: 60000
  retries: 5
```

### Plugin ya instalado

```bash
# Problema
/phinstall essentialsx
⚠ El plugin EssentialsX ya está instalado
Usa /phupdate essentialsx para actualizarlo

# Solución
/phupdate essentialsx
```

---

## 📊 Mejores Prácticas

### 1. Planifica antes de instalar

```bash
# Primero busca y revisa
/phsearch permissions
/phsearch economy
/phsearch protection

# Luego instala en orden de dependencias
/phinstall vault          # Primero las APIs
/phinstall luckperms      # Luego los sistemas base
/phinstall essentialsx    # Finalmente los comandos
```

### 2. Verifica después de instalar

```bash
# Instalar
/phinstall luckperms

# Reiniciar
/stop

# Después del reinicio, verificar
/plugins
/lp info
```

### 3. Mantén actualizados tus plugins

```bash
# Revisa regularmente
/phupdate

# Actualiza uno por uno
/phupdate essentialsx
# Reinicia y prueba
/phupdate luckperms
# Reinicia y prueba
```

### 4. Haz backups antes de actualizar

```bash
# Antes de actualizar plugins importantes
# 1. Detén el servidor
/stop

# 2. Haz backup de la carpeta plugins/
# (desde la terminal del servidor)
cp -r plugins/ plugins-backup/

# 3. Inicia y actualiza
/phupdate essentialsx
```

---

## 🎓 Tips y Trucos

### Autocompletado

PluginHub soporta tab completion:

```bash
/phsearch ess[TAB]     → /phsearch essentialsx
/phinstall luck[TAB]   → /phinstall luckperms
/phupdate world[TAB]   → /phupdate worldedit
```

### Búsqueda Inteligente

La búsqueda funciona con:
- Nombres exactos: `essentialsx`
- Nombres parciales: `essential`
- Descripciones: `permissions`
- Palabras clave: `economy`

### Información del Sistema

```bash
# Ver estadísticas
/pluginhub info

# Salida:
╔════════════════════════════════════════╗
║  PluginHub - Información               ║
╚════════════════════════════════════════╝
● Plugins disponibles: 8
● Plugins instalados: 5
● Caché habilitado: true
● Auto-actualización: false
```

---

## 🔗 Recursos Adicionales

- [README.md](README.md) - Documentación principal
- [CONTRIBUTING.md](CONTRIBUTING.md) - Guía de contribución
- [CHANGELOG.md](CHANGELOG.md) - Historial de cambios
- [GitHub Issues](https://github.com/tuusuario/PluginHub/issues) - Reportar problemas

---

<div align="center">

**¿Tienes más ejemplos? ¡Compártelos con la comunidad!**

[Crear Issue](https://github.com/tuusuario/PluginHub/issues/new) | [Discussions](https://github.com/tuusuario/PluginHub/discussions)

</div>

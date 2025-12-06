# 🔌 PluginHub

**Gestor centralizado de plugins para servidores Paper/Spigot**

PluginHub es un plugin moderno y eficiente que permite buscar, instalar y actualizar plugins de Minecraft directamente desde el juego, sin necesidad de descargas manuales.

---

## ✨ Características

- 🔍 **Búsqueda inteligente** - Encuentra plugins por nombre o descripción
- ⚡ **Instalación automática** - Descarga e instala plugins con un solo comando
- 🔄 **Sistema de actualizaciones** - Mantén tus plugins al día
- 🎨 **Interfaz colorida** - Mensajes claros y visualmente atractivos
- ⚙️ **Configuración flexible** - Personaliza timeouts, reintentos y más
- 🔒 **Seguro y confiable** - Solo descarga de fuentes verificadas
- 📦 **Caché inteligente** - Mejora el rendimiento de búsquedas
- 🌐 **Operaciones asíncronas** - No bloquea el servidor durante descargas

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

### Buscar Plugins
```
/phsearch <nombre>
```
Busca plugins disponibles en el repositorio.

**Ejemplos:**
```
/phsearch essentials
/phsearch world edit
/phsearch permissions
```

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

## 📦 Plugins Disponibles

PluginHub incluye soporte para los plugins más populares:

- **EssentialsX** - Comandos esenciales
- **LuckPerms** - Sistema de permisos avanzado
- **WorldEdit** - Edición de mundos
- **Vault** - API de economía y permisos
- **ProtocolLib** - Manipulación de paquetes
- **PlotSquared** - Sistema de parcelas
- **CoreProtect** - Logging y rollback
- **Citizens** - NPCs avanzados

*Más plugins se añadirán en futuras versiones*

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

### Mejoras Implementadas

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

---

## 🔮 Roadmap v2.0

- [ ] Integración con API de SpigotMC
- [ ] Integración con API de Modrinth
- [ ] Actualizaciones automáticas programadas
- [ ] Sistema de dependencias
- [ ] Verificación de checksums
- [ ] Soporte para múltiples versiones
- [ ] Base de datos SQLite para caché
- [ ] Interfaz web de administración
- [ ] Notificaciones de actualizaciones
- [ ] Backup automático antes de actualizar

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

- GitHub: [@Pecar](https://github.com/Pecar)

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
- 🐛 Issues: [GitHub Issues](https://github.com/tuusuario/PluginHub/issues)

---

<div align="center">

**⭐ Si te gusta este proyecto, dale una estrella en GitHub! ⭐**

Made with ❤️ by Pecar

</div>

[CENTER]

[SIZE=7][B][COLOR=#FF6B35]PluginHub[/COLOR][/B][/SIZE]
[SIZE=4][I]Gestor centralizado de plugins con búsqueda multi-fuente y altamente configurable[/I][/SIZE]

[IMG]https://img.shields.io/badge/Version-1.0-brightgreen[/IMG] [IMG]https://img.shields.io/badge/Minecraft-1.21+-blue[/IMG] [IMG]https://img.shields.io/badge/Java-21-orange[/IMG] [IMG]https://img.shields.io/badge/Config-100%2B%20Options-yellow[/IMG]

[URL='https://github.com/ReyesAlejandro-Dev/PluginHub'][IMG]https://img.shields.io/badge/GitHub-Repository-black[/IMG][/URL]
[URL='https://github.com/ReyesAlejandro-Dev/PluginHub/issues'][IMG]https://img.shields.io/badge/Report-Issues-red[/IMG][/URL]
[/CENTER]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]✨ Descripción[/COLOR][/B][/SIZE]

[B]PluginHub[/B] es un plugin avanzado que revoluciona la forma de gestionar plugins en tu servidor. Olvídate de buscar y descargar manualmente - ahora puedes buscar, instalar y actualizar plugins directamente desde el juego.

[B][COLOR=#FF6B35]🌐 Búsqueda Multi-Fuente:[/COLOR][/B]
[LIST]
[*] [B]SpigotMC[/B] - Miles de plugins de Spigot
[*] [B]Modrinth[/B] - Plugins modernos y optimizados
[*] [B]Hangar[/B] - Repositorio oficial de PaperMC
[*] [B]BukkitDev[/B] - Plugins clásicos de Bukkit
[/LIST]

[B][COLOR=#FF6B35]⚡ Características Principales:[/COLOR][/B]
[LIST]
[*] 🔍 Búsqueda en tiempo real en múltiples fuentes
[*] 📥 Instalación automática con un solo comando
[*] 📊 Información detallada (descargas, ratings, autor)
[*] 🔄 Sistema de actualizaciones
[*] 💾 Caché inteligente para mejor rendimiento
[*] 🌐 Operaciones asíncronas (no lag)
[*] 🎨 Interfaz colorida y clara
[*] 🔒 Seguro y confiable
[*] ⚙️ [B]100+ opciones configurables[/B]
[*] 🎛️ Personalización total de mensajes
[*] 🔧 Habilitar/deshabilitar fuentes individualmente
[/LIST]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]📦 Instalación[/COLOR][/B][/SIZE]

[B]1.[/B] Descarga [B]PluginHub-1.0.jar[/B]
[B]2.[/B] Coloca el archivo en la carpeta [B]plugins/[/B] de tu servidor
[B]3.[/B] Reinicia el servidor
[B]4.[/B] ¡Listo! Usa [B]/pluginhub help[/B] para comenzar

[SIZE=5][B]Requisitos:[/B][/SIZE]
[LIST]
[*] [B]Minecraft:[/B] 1.21 o superior
[*] [B]Servidor:[/B] Paper, Spigot, Purpur
[*] [B]Java:[/B] 21 o superior
[/LIST]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🎮 Comandos[/COLOR][/B][/SIZE]

[SIZE=5][B][COLOR=#FF6B35]Comando Principal[/COLOR][/B][/SIZE]
[CODE]/pluginhub [help|version|reload|info|clearcache][/CODE]

[B]Subcomandos:[/B]
[LIST]
[*] [B]help[/B] - Muestra la ayuda completa
[*] [B]version[/B] - Información de la versión
[*] [B]reload[/B] - Recarga la configuración
[*] [B]info[/B] - Estadísticas del sistema
[*] [B]clearcache[/B] - Limpia el caché de búsqueda
[/LIST]

[SIZE=5][B][COLOR=#FF6B35]Buscar Plugins[/COLOR][/B][/SIZE]
[CODE]/phsearch <nombre>[/CODE]

Busca plugins en [B]todas las fuentes[/B] simultáneamente.

[B]Ejemplos:[/B]
[CODE]/phsearch essentials
/phsearch world edit
/phsearch permissions
/phsearch coreprotect[/CODE]

[SIZE=5][B][COLOR=#FF6B35]Instalar Plugins[/COLOR][/B][/SIZE]
[CODE]/phinstall <nombre>[/CODE]

Descarga e instala un plugin automáticamente.

[B]Ejemplos:[/B]
[CODE]/phinstall essentialsx
/phinstall luckperms
/phinstall worldedit[/CODE]

[SIZE=5][B][COLOR=#FF6B35]Actualizar Plugins[/COLOR][/B][/SIZE]
[CODE]/phupdate [nombre][/CODE]

[LIST]
[*] Sin argumentos: Lista plugins instalados
[*] Con nombre: Actualiza un plugin específico
[/LIST]

[B]Ejemplos:[/B]
[CODE]/phupdate
/phupdate essentialsx[/CODE]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🔑 Permisos[/COLOR][/B][/SIZE]

[TABLE="width: 100%"]
[TR]
[TD][B]Permiso[/B][/TD]
[TD][B]Descripción[/B][/TD]
[TD][B]Por Defecto[/B][/TD]
[/TR]
[TR]
[TD][CODE]pluginhub.admin[/CODE][/TD]
[TD]Acceso completo a todos los comandos[/TD]
[TD]OP[/TD]
[/TR]
[TR]
[TD][CODE]pluginhub.search[/CODE][/TD]
[TD]Permite buscar plugins[/TD]
[TD]Todos[/TD]
[/TR]
[TR]
[TD][CODE]pluginhub.install[/CODE][/TD]
[TD]Permite instalar plugins[/TD]
[TD]OP[/TD]
[/TR]
[TR]
[TD][CODE]pluginhub.update[/CODE][/TD]
[TD]Permite actualizar plugins[/TD]
[TD]OP[/TD]
[/TR]
[/TABLE]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]⚙️ Configuración[/COLOR][/B][/SIZE]

El archivo [B]config.yml[/B] se genera automáticamente en [B]plugins/PluginHub/[/B]

[CODE]# Configuración de descargas
download:
  timeout: 30000      # Timeout en milisegundos
  retries: 3          # Reintentos en caso de fallo

# Sistema de caché
cache:
  enabled: true       # Caché activado
  duration-minutes: 60  # Duración del caché

# Fuentes confiables
trusted-sources:
  - "https://www.spigotmc.org"
  - "https://api.modrinth.com"
  - "https://hangar.papermc.io"
  - "https://dev.bukkit.org"[/CODE]

[B]Recargar configuración:[/B]
[CODE]/pluginhub reload[/CODE]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]📸 Capturas de Pantalla[/COLOR][/B][/SIZE]

[SPOILER="Banner de Inicio"]
[IMG]https://i.imgur.com/YourScreenshot1.png[/IMG]
[I]Banner colorido al iniciar el servidor[/I]
[/SPOILER]

[SPOILER="Búsqueda de Plugins"]
[IMG]https://i.imgur.com/YourScreenshot2.png[/IMG]
[I]Búsqueda en múltiples fuentes con información detallada[/I]
[/SPOILER]

[SPOILER="Instalación Automática"]
[IMG]https://i.imgur.com/YourScreenshot3.png[/IMG]
[I]Instalación automática con progreso en tiempo real[/I]
[/SPOILER]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]💡 Ejemplos de Uso[/COLOR][/B][/SIZE]

[SIZE=5][B][COLOR=#FF6B35]Configurar Servidor de Supervivencia[/COLOR][/B][/SIZE]

[CODE]# Instalar comandos básicos
/phinstall essentialsx

# Instalar sistema de permisos
/phinstall luckperms

# Instalar protección
/phinstall coreprotect

# Instalar economía
/phinstall vault

# Reiniciar servidor
/stop[/CODE]

[SIZE=5][B][COLOR=#FF6B35]Configurar Servidor Creativo[/COLOR][/B][/SIZE]

[CODE]# Instalar edición de mundos
/phinstall worldedit

# Instalar sistema de parcelas
/phinstall plotsquared

# Instalar comandos útiles
/phinstall essentialsx

# Reiniciar servidor
/stop[/CODE]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🌍 Fuentes de Plugins[/COLOR][/B][/SIZE]

PluginHub busca automáticamente en múltiples repositorios:

[B][COLOR=#FF6B35]SpigotMC (Spiget API)[/COLOR][/B]
[LIST]
[*] Miles de plugins disponibles
[*] Información de descargas y ratings
[*] Actualizaciones en tiempo real
[/LIST]

[B][COLOR=#FF6B35]Modrinth[/COLOR][/B]
[LIST]
[*] Plugins modernos y optimizados
[*] Soporte para múltiples versiones
[*] Categorización avanzada
[/LIST]

[B][COLOR=#FF6B35]Hangar (PaperMC)[/COLOR][/B]
[LIST]
[*] Repositorio oficial de Paper
[*] Plugins verificados
[*] Optimizados para Paper
[/LIST]

[B][COLOR=#FF6B35]BukkitDev[/COLOR][/B]
[LIST]
[*] Plugins clásicos de Bukkit
[*] Gran variedad histórica
[*] Compatibilidad legacy
[/LIST]

[CENTER][B][SIZE=5]¡Busca cualquier plugin disponible en estas fuentes![/SIZE][/B][/CENTER]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🔧 Información Técnica[/COLOR][/B][/SIZE]

[B][COLOR=#FF6B35]Arquitectura:[/COLOR][/B]
[LIST]
[*] [B]Búsqueda paralela[/B] - 4 APIs simultáneamente
[*] [B]CompletableFuture[/B] - Operaciones asíncronas
[*] [B]OkHttp[/B] - Cliente HTTP moderno
[*] [B]Gson[/B] - Parsing JSON eficiente
[*] [B]Jsoup[/B] - Web scraping para BukkitDev
[*] [B]Thread-safe[/B] - ConcurrentHashMap
[/LIST]

[B][COLOR=#FF6B35]Rendimiento:[/COLOR][/B]
[LIST]
[*] Caché inteligente para reducir llamadas a APIs
[*] Thread pool de 5 threads
[*] Reintentos automáticos con backoff exponencial
[*] Connection pooling optimizado
[/LIST]

[B][COLOR=#FF6B35]Código Abierto:[/COLOR][/B]
[URL='https://github.com/ReyesAlejandro-Dev/PluginHub'][B]GitHub Repository[/B][/URL]
[LIST]
[*] Código completamente abierto
[*] Contribuciones bienvenidas
[*] Documentación completa
[*] Ejemplos de uso
[/LIST]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🚨 Solución de Problemas[/COLOR][/B][/SIZE]

[SPOILER="No encuentra plugins"]
[B]Problema:[/B] La búsqueda no devuelve resultados

[B]Solución:[/B]
[CODE]# Limpia el caché
/pluginhub clearcache

# Intenta de nuevo
/phsearch <nombre>

# Verifica tu conexión a internet
# Verifica que el nombre sea correcto[/CODE]
[/SPOILER]

[SPOILER="Error de permisos"]
[B]Problema:[/B] "No tienes permiso para instalar plugins"

[B]Solución:[/B]
[CODE]# Asegúrate de tener permisos de admin
/lp user <tu_nombre> permission set pluginhub.admin true

# O ser OP del servidor
/op <tu_nombre>[/CODE]
[/SPOILER]

[SPOILER="Plugin no se instala"]
[B]Problema:[/B] Error durante la instalación

[B]Soluciones:[/B]
[LIST=1]
[*] Verifica tu conexión a internet
[*] Intenta desde otra fuente (busca el mismo plugin)
[*] Algunos plugins premium no se pueden descargar automáticamente
[*] Aumenta el timeout en config.yml
[/LIST]
[/SPOILER]

[SPOILER="Servidor lento durante búsqueda"]
[B]Problema:[/B] El servidor se ralentiza al buscar

[B]Explicación:[/B]
Es normal, la búsqueda es en múltiples fuentes y tarda 2-5 segundos. Las operaciones son asíncronas y no deberían causar lag significativo.

[B]Solución:[/B]
Si persiste, ajusta el caché en config.yml para reducir búsquedas frecuentes.
[/SPOILER]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]📊 Estadísticas[/COLOR][/B][/SIZE]

[CENTER][SIZE=5][B]Ver estadísticas del sistema:[/B][/SIZE]
[CODE]/pluginhub info[/CODE]

[B]Muestra:[/B]
[LIST]
[*] Plugins en caché
[*] Plugins instalados
[*] Fuentes activas
[*] Estado del caché
[*] Configuración actual
[/LIST][/CENTER]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🔮 Roadmap v3.0[/COLOR][/B][/SIZE]

[B]Próximas características:[/B]
[LIST]
[*] ✅ Actualizaciones automáticas programadas
[*] ✅ Sistema de dependencias automático
[*] ✅ Verificación de checksums SHA-256
[*] ✅ Filtrado por versión de Minecraft
[*] ✅ Base de datos SQLite para caché persistente
[*] ✅ Interfaz web de administración
[*] ✅ Notificaciones de actualizaciones
[*] ✅ Backup automático antes de actualizar
[*] ✅ Integración con GitHub Releases
[*] ✅ Sistema de ratings y reviews
[/LIST]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🤝 Contribuir[/COLOR][/B][/SIZE]

¡Las contribuciones son bienvenidas!

[B]GitHub:[/B] [URL='https://github.com/ReyesAlejandro-Dev/PluginHub']https://github.com/ReyesAlejandro-Dev/PluginHub[/URL]

[B]Cómo contribuir:[/B]
[LIST=1]
[*] Fork el proyecto
[*] Crea una rama para tu feature
[*] Commit tus cambios
[*] Push a la rama
[*] Abre un Pull Request
[/LIST]

[B]Reportar bugs:[/B]
[URL='https://github.com/ReyesAlejandro-Dev/PluginHub/issues']GitHub Issues[/URL]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]📞 Soporte[/COLOR][/B][/SIZE]

[B]¿Necesitas ayuda?[/B]

[LIST]
[*] 💬 [B]Discord:[/B] [Próximamente]
[*] 🐛 [B]GitHub Issues:[/B] [URL='https://github.com/ReyesAlejandro-Dev/PluginHub/issues']Reportar Problema[/URL]
[*] 📖 [B]Documentación:[/B] [URL='https://github.com/ReyesAlejandro-Dev/PluginHub/blob/main/README.md']README Completo[/URL]
[*] 💡 [B]Ejemplos:[/B] [URL='https://github.com/ReyesAlejandro-Dev/PluginHub/blob/main/EXAMPLES_V2.md']Guía de Ejemplos[/URL]
[/LIST]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]📝 Licencia[/COLOR][/B][/SIZE]

Este proyecto está bajo la Licencia MIT.
Ver [URL='https://github.com/ReyesAlejandro-Dev/PluginHub/blob/main/LICENSE']LICENSE[/URL] para más detalles.

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]👤 Autor[/COLOR][/B][/SIZE]

[B]Alejandro Reyes[/B]
[LIST]
[*] GitHub: [URL='https://github.com/ReyesAlejandro-Dev']@ReyesAlejandro-Dev[/URL]
[*] Proyecto: [URL='https://github.com/ReyesAlejandro-Dev/PluginHub']PluginHub[/URL]
[/LIST]

[HR][/HR]

[SIZE=6][B][COLOR=#4ECDC4]🙏 Agradecimientos[/COLOR][/B][/SIZE]

[LIST]
[*] Comunidad de SpigotMC
[*] Desarrolladores de Paper
[*] Spiget API
[*] Modrinth Team
[*] Todos los creadores de plugins
[/LIST]

[HR][/HR]

[CENTER][SIZE=6][B][COLOR=#FF6B35]⭐ Si te gusta este plugin, dale una estrella en GitHub! ⭐[/COLOR][/B][/SIZE]

[URL='https://github.com/ReyesAlejandro-Dev/PluginHub'][IMG]https://img.shields.io/github/stars/ReyesAlejandro-Dev/PluginHub?style=social[/IMG][/URL]

[SIZE=4][I]Made with ❤️ by Pecar00[/I][/SIZE]

[URL='https://github.com/ReyesAlejandro-Dev/PluginHub'][IMG]https://img.shields.io/badge/GitHub-View%20Source-black?style=for-the-badge&logo=github[/IMG][/URL]
[URL='https://github.com/ReyesAlejandro-Dev/PluginHub/releases'][IMG]https://img.shields.io/badge/Download-Latest%20Release-brightgreen?style=for-the-badge[/IMG][/URL]
[/CENTER]

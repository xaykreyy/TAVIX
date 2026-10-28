# TAVIX — Minecraft Java 1.21.11 (Fabric)

This is the TAVIX client-side mod source project. It includes an in-game menu opened with Right Shift, categories and UI toggle placeholders. The module toggles do not yet implement cheats/gameplay effects.

## Build locally
Requires JDK 21 and Gradle 9.2.1 or compatible. From this folder run:
`gradle build`
The output JAR should be in `build/libs/`.

## Automatic build (no Gradle install)
1. Create a GitHub repository and upload the contents of this folder.
2. Open the repository's **Actions** tab.
3. Select **Build TAVIX** and press **Run workflow** (or push to main/master).
4. When it finishes, open the run and download the artifact named `TAVIX-Minecraft-1.21.11`.
5. Put the resulting `.jar` into `%appdata%\\.minecraft\\mods`.

Use Fabric Loader for Minecraft 1.21.11 and Java 21. This project has not been compiled in this environment, so the first CI build is the actual compile check. If it fails, open the workflow log and use the first compiler error to fix the source.

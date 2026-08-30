# AmberKiln

Deep-work kiln tracker for 2026-08-30. Treat each project as a kiln: log fire sessions to raise heat, write cool-down notes, and review weekly fire hours.

## Features
- Forge dashboard with weekly minutes and hottest kiln
- Kilns (projects) with heat derived from session minutes
- Fire sessions with duration and intensity
- Cool-down notes after a session
- Room persistence, ViewModel + StateFlow, Compose Material 3

## Build
```bash
cd apps/2026-08-30-AmberKiln
chmod +x gradlew
./gradlew assembleDebug
```
APK: `app/build/outputs/apk/debug/`

Dedicated workflow: `.github/workflows/build-amberkiln.yml`

# Stillpoint

A 2026 digital-wellbeing sanctuary for Android.

Stillpoint protects quiet hours, holds a single daily intention, and logs deep-work sessions so attention has a place to land.

## Features

- **Harbor** — daily briefing with live quiet blocks, open intentions, and logged focus minutes
- **Quiet** — weekday sanctuary windows plus energy-tagged intentions
- **Still** — 15 / 25 / 45 / 60 minute focus timer that auto-logs completed sessions
- **Pulse** — close rate, protected minutes, and energy demand insights

## Stack

- Kotlin, Jetpack Compose, Material 3
- ViewModel + StateFlow
- Room persistence
- Navigation Compose
- minSdk 26, targetSdk 35

## Build

```bash
cd apps/2026-08-28-Stillpoint
chmod +x gradlew
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

Dedicated workflow: `.github/workflows/build-stillpoint.yml`

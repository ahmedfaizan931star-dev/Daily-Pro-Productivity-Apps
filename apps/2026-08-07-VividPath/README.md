# VividPath

**Daily Intention • Focused Path Steps • Deep Work Blocks • Evening Reflection**

A polished, calm productivity companion for 2026. Set one clear intention for the day, break it into prioritized path items, protect focus blocks with a beautiful timer, and close the day with structured reflection on mood, energy, wins, and gratitude.

## Features

- **Home Dashboard** — Today's intention, progress stats (completed items, focus minutes, reflection status), and path snapshot
- **Path** — Create prioritized steps (High / Medium / Low) with time estimates. Check them off as you complete them
- **Focus** — Circular timer with presets (15 / 25 / 45 / 50 / 90 min). Start, pause, reset. Sessions are logged locally
- **Reflect** — Mood & energy (1–5), wins, lessons, and gratitude. One reflection per day, editable anytime
- **Persistence** — Room database for path items, intentions, focus sessions, and reflections
- **Material 3 + Dark Mode** — Clean theming that follows system preference

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (KSP)
- Min SDK 26 / Target & Compile SDK 35

## Package

`com.dailyapps.vividpath`

## How to Build

### From this folder (local)

```bash
chmod +x gradlew
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

### GitHub Actions

Pushing to `apps/**` triggers the workflow. It automatically builds the most recently modified app folder under `apps/` (or a specific one via `workflow_dispatch`).

Download the artifact named `app-debug-apk` from the Actions run.

## Design Notes

VividPath is intentionally lightweight and offline-first. No accounts, no cloud, no notifications spam. The goal is a daily ritual: intention → path → focus → reflect.

Created as part of the Daily Pro Productivity Apps series — 2026-08-07.

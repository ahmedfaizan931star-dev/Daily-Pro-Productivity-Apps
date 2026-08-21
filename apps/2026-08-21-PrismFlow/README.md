# PrismFlow

**Prioritize. Focus. Sustain.**

PrismFlow is a professional-grade productivity Android app built for 2026. It combines the Eisenhower Priority Matrix, an adaptive Pomodoro-style focus timer, habit streak tracking, and daily energy logging so you can work smarter without burning out.

## Features

- **Home Dashboard** — Quick overview of open priorities, top habit streaks, and one-tap energy logging (1–5 scale).
- **Eisenhower Matrix** — Four-quadrant task board (Do First / Schedule / Delegate / Eliminate) with add, complete, and delete actions.
- **Focus Timer** — 25-minute or 50-minute sessions with start / pause / reset. Clean, distraction-free UI.
- **Habit Forge** — Create habits, mark them complete, and watch streaks grow. Automatic streak logic based on consecutive days.
- **Persistence** — Room database for tasks, habits, and energy logs. Data survives app restarts.
- **Modern UI** — Jetpack Compose + Material 3, full dark-mode support, dynamic colors on Android 12+.

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Single-module structure
- Min SDK 26 / Target & Compile SDK 35
- ViewModel + StateFlow
- Navigation Compose
- Room + KSP
- Clean presentation + data layers

## How to Build

```bash
cd apps/2026-08-21-PrismFlow
chmod +x gradlew
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/`.

GitHub Actions workflow `build-prismflow.yml` automatically builds on every push to this folder and uploads the debug APK as an artifact.

## Package

`com.dailyapps.prismflow`

---

Created as part of the Daily Pro Productivity Apps series — 2026-08-21.

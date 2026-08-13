# LuminaFocus

**Adaptive Focus + Energy Tracker** — a polished productivity companion for 2026.

## Overview

LuminaFocus helps you stay in flow by combining:

- **Focus sessions** — Pomodoro (25m), Deep Work (50m), and Short (15m) timers with pause/reset and automatic session logging.
- **Micro-habits** — lightweight daily checklist with streak tracking.
- **Energy & mood insights** — log how you feel and review recent patterns.
- **Dashboard** — today’s focus minutes, sessions, habit completion, and average energy/mood at a glance.

Built with modern Android practices: Kotlin, Jetpack Compose, Material 3, ViewModel + StateFlow, Room, and Navigation Compose. Full dark mode support.

## Screens

1. **Home** — Daily overview cards (focus, habits, energy, mood).
2. **Focus** — Circular-style timer controls, preset chips, session counter.
3. **Habits** — Add / toggle / delete micro-habits with streaks.
4. **Insights** — Energy & mood sliders + history list.

## Tech stack

- Min SDK 26, Target/Compile SDK 35
- Kotlin + Jetpack Compose + Material 3
- Room (KSP) for local persistence
- ViewModel + StateFlow
- Navigation Compose

## Build

```bash
cd apps/2026-08-13-LuminaFocus
./gradlew assembleDebug
```

APK will be at `app/build/outputs/apk/debug/`.

A dedicated GitHub Actions workflow (`.github/workflows/build-luminafocus.yml`) builds the debug APK on push to this folder and uploads the artifact.

## Package

`com.dailyapps.luminafocus`

---

Part of the Daily Pro Productivity Apps collection.

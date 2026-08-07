# PulseForge

**Adaptive focus sessions + habit forging + daily priority management**

A polished, modern productivity Android app built for 2026. PulseForge helps you enter deep work states, build consistent habits, and keep your daily priorities crystal clear.

## Features

- **Dashboard** – Instant overview of today's focus minutes, habit completion, and priority progress
- **Focus Pulse Timer** – Classic 25/5, Deep Work 50/10, Long Form 90/15, and fully custom sessions with beautiful circular progress
- **Habit Forge** – Create habits, mark them complete daily, track consistency
- **Daily Forge (Priorities)** – Define up to 5 high-impact priorities for the day and check them off
- **Material 3 + Dark mode** – Clean, modern UI that respects system theme
- **Local persistence** – Room database for habits, completions, focus sessions, and priorities

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room
- Minimum SDK 26 / Target SDK 35

## Package

`com.dailyapps.pulseforge`

## Build

```bash
# From this directory
./gradlew assembleDebug
```

If `gradlew` is missing or the wrapper jar is not present, generate it:

```bash
gradle wrapper --gradle-version 8.7
./gradlew assembleDebug
```

Or open the project in Android Studio (Hedgehog or newer) and run.

## Screens

1. **Home** – Stats + quick start + today's priorities & habits at a glance
2. **Focus** – Full-screen timer with mode selection and controls
3. **Habits** – Manage and complete daily habits
4. **Forge** – Curate and complete your top priorities

---

Generated as part of the Daily Pro Productivity Apps series.

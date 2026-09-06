# EmberWake

A 2026 daily operating system for how you *start* and *close* work.

EmberWake treats energy as a first-class input. You score how you feel, pick a small set of commitments, walk a five-step morning ritual, then shut the day down with a matching evening checklist. Snapshots feed a 21-day pulse so patterns are visible without a spreadsheet.

## Features

- Energy slider with contextual coaching (recover / steady / high flame)
- Commitment list with energy cost and completion tracking
- Wake ritual (hydrate, intent, three commitments, silence noise, first deep block)
- Shutdown ritual (park work, clear desk, log energy, name a win, set first move)
- Insights ledger of energy, ritual completion, and commitments
- DataStore persistence, resets rituals each calendar day, keeps history

## Stack

Kotlin · Jetpack Compose · Material 3 · Navigation Compose · ViewModel + StateFlow · DataStore Preferences

Package: `com.dailyapps.emberwake`
Min SDK 26 · Target / compile 35

## Build

```bash
chmod +x gradlew
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

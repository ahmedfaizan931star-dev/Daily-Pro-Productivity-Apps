# LumenSpan

Energy-span planner for 2026. Plan work against your real energy windows — Peak, Steady, and Recovery — instead of a flat to-do list.

## Features
- Today board grouped by energy band
- Energy check-ins (1–5) with notes
- Task library with duration estimates
- Weekly insights: load vs completed minutes

## Build
```bash
cd apps/2026-09-01-LumenSpan
chmod +x gradlew
./gradlew assembleDebug
```

Package: `com.dailyapps.lumenspan`  
minSdk 26 · targetSdk 35 · Compose + Room

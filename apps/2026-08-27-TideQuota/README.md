# TideQuota

Weekly time-budget planner for 2026. Allocate hours across life domains, log actual blocks, and see where your week is drifting — before Sunday night.

Package: `com.dailyapps.tidequota`  
Min SDK 26 · Target 35 · Kotlin + Jetpack Compose + Material 3 + Room

## Features

- **Harbor** — week dashboard with planned vs logged hours and remaining quota
- **Quotas** — set weekly hour budgets for Deep Work, Learning, Health, Admin, Rest, People
- **Log** — capture time blocks against a domain
- **Drift** — utilization, overspend, and unused budget insights

Data stays on-device (Room).

## Build

```bash
cd apps/2026-08-27-TideQuota
chmod +x gradlew
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/`

GitHub Actions: `.github/workflows/build-tidequota.yml`

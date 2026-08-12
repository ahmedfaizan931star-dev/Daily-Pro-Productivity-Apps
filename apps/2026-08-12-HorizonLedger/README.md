# HorizonLedger

**Mindful expense & budget tracker for focused financial productivity (2026)**

HorizonLedger is a polished Android productivity app that helps you log daily spending and income, set category budgets, track saving goals, and view simple insights — all with a clean Material 3 UI and full dark mode support.

## Features

- **Dashboard** – Today / week / month spend overview, budget remaining, top category
- **Transactions** – Quick add expense or income with categories and notes; swipe-style delete
- **Budgets & Goals** – Monthly category limits with progress bars; saving goals with progress
- **Insights** – Category breakdown, percentages, and contextual tips
- **Persistence** – Room database for offline-first reliability
- **Modern stack** – Kotlin, Jetpack Compose, Material 3, ViewModel + StateFlow, Navigation Compose

## Tech

- Min SDK 26 · Target / Compile SDK 35
- Single-module project
- Clean-ish architecture (data / presentation)

## Build

```bash
cd apps/2026-08-12-HorizonLedger
chmod +x gradlew
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

Or use the dedicated GitHub Actions workflow `build-horizonledger.yml`.

## Package

`com.dailyapps.horizonledger`

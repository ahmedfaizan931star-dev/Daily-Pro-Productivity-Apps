# BeaconQuota

Offline weekly energy planner. Set lane quotas (Deep, Ops, People, Admin), attach beacons (commitments) that consume hours, watch load versus capacity, then close the week.

## Features
- Signal dashboard: reserved hours vs weekly capacity
- Quotas by work lane
- Beacons: named commitments with estimated hours
- Closeout: mark the week complete and start a fresh plan
- Local-only Room database (no cloud)

## Build
```bash
cd apps/2026-08-31-BeaconQuota
chmod +x gradlew
./gradlew assembleDebug
```

Isolated workflow: `.github/workflows/build-beaconquota.yml`

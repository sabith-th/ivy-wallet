# Roadmap

This is a personal fork of the discontinued [IvyWallet](https://github.com/Ivy-Apps/ivy-wallet) app, continued for personal use under the GPL-3.0 license.

## Goals

- Keep the app up to date with the latest Android SDK, Kotlin, and Jetpack libraries
- Apply security patches as they arise
- Gradually clean up legacy/deprecated code left behind by the original project
- Add new features tailored to personal needs

## Phase 1 — Foundation (Housekeeping)

Get the codebase into a healthy, maintainable state before adding new features.

- [ ] Upgrade Kotlin to 2.1.x ([#1](https://github.com/sabith-th/ivy-wallet/issues/1))
- [ ] Upgrade Android Gradle Plugin to latest ([#2](https://github.com/sabith-th/ivy-wallet/issues/2))
- [ ] Upgrade Jetpack Compose to latest ([#3](https://github.com/sabith-th/ivy-wallet/issues/3))
- [ ] Fix Gradle 9.0 deprecation warnings ([#4](https://github.com/sabith-th/ivy-wallet/issues/4))
- [ ] Complete migration of `:temp:legacy-code` ([#5](https://github.com/sabith-th/ivy-wallet/issues/5))
- [ ] Complete migration of `:temp:old-design` to Material3 ([#6](https://github.com/sabith-th/ivy-wallet/issues/6))
- [ ] Replace deprecated `SharedPrefs` with `DataStore` ([#7](https://github.com/sabith-th/ivy-wallet/issues/7))
- [ ] Verify Android 15 (API 35) compatibility ([#8](https://github.com/sabith-th/ivy-wallet/issues/8))

## Phase 2 — Features

Personal feature additions. To be defined.

## Notes

- Track day-to-day tasks and bugs in [GitHub Issues](https://github.com/sabith-th/ivy-wallet/issues)
- This roadmap reflects high-level direction; it will evolve over time

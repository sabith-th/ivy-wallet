# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build the project
./gradlew build

# Run unit tests (all modules)
./gradlew testDebugUnitTest

# Run unit tests for a single module
./gradlew :feature:home:testDebugUnitTest

# Run a single test class
./gradlew :feature:home:testDebugUnitTest --tests "*.HomeViewModelTest"

# Lint (Detekt + Compose lint)
./gradlew detekt lintRelease

# Screenshot tests (Paparazzi) - record new snapshots
./gradlew testDebugUnitTest -Ppaparazzi.record

# Code coverage
./gradlew koverHtmlReport

# Build debug APK
./gradlew assembleDebug
```

**Requirements**: Java 17+, Android Studio (latest stable), Min SDK 28, Target SDK 34.

## Module Architecture

Multi-module Gradle project with convention plugins defined in `buildSrc/`:

- `ivy.feature` — applied to all `:feature:*` modules; includes Hilt, Compose, Paparazzi, serialization
- `ivy.module` — for non-feature shared modules
- `ivy.room`, `ivy.widget`, `ivy.compose`, etc. — single-concern plugins

**Module layers** (dependency direction: feature → shared → data):
- `:app` — entry point (`IvyAndroidApp`), Hilt root, `AppBindingsModule`, hosts `IvyNavGraph`
- `:feature:*` — screen-level modules (accounts, home, transactions, budgets, etc.)
- `:shared:ui:core` — shared Compose components and Material3 theme
- `:shared:ui:navigation` — type-safe navigation via sealed interface screen definitions
- `:shared:domain` — use cases (Arrow `Either<Error, Data>` return types)
- `:shared:data:core` — Room DB, DataStore, Ktor HTTP client, repositories
- `:shared:data:model` — pure domain models (no Android deps)
- `:temp:legacy-code`, `:temp:old-design` — being migrated out; avoid adding code here

Testing support modules: `:shared:data:model-testing` (fixtures), `:shared:ui:testing` (Paparazzi base classes), `:shared:data:core-testing`.

## Key Architectural Patterns

### MVI with Compose Runtime
Each feature screen has a ViewModel that exposes a single `@Immutable` sealed interface `ViewState`. UI is driven by `collectAsState()`. State transitions are the only way to update the UI — no direct mutation.

### Error Handling via Arrow Either
Business logic returns `Either<DomainError, Data>` — never throws. Only throw for truly unrecoverable errors (e.g., OOM). Use Arrow's `either { }` builder and `bind()` for chaining.

### Data Modeling with ADTs
Use sealed interfaces + data classes to make impossible states unrepresentable. Prefer value classes (inline classes) for primitives that carry semantic meaning (e.g., `AccountId`, `Amount`).

### Navigation
Screens are defined as `@Serializable data object` or `data class` implementing sealed interface in `:shared:ui:navigation`. `IvyNavGraph.kt` in `:app` maps every screen to its composable. Use `IvyNavigator` (injected via Hilt) to navigate.

### Dependency Injection
Hilt throughout. Modules annotated `@HiltViewModel`, `@AndroidEntryPoint`. App-level bindings in `AppBindingsModule`. WorkManager integrated with Hilt via `HiltWorker`.

## Testing Conventions

- **Unit tests**: JUnit4 + Kotest assertions + MockK. Given-When-Then structure. Test files in `src/test/java/`.
- **Screenshot tests**: Paparazzi. Extend `PaparazziScreenshotTest` from `:shared:ui:testing`. Test both light and dark themes. Files in `src/test/java/` suffixed `PaparazziTest`.
- **Integration tests**: Android device tests in `src/androidTest/java/`. Used for Room migrations and DataStore tests.
- Test fixtures live in `:shared:data:model-testing`; reuse them rather than creating ad-hoc test data.

## Version Catalog

All dependency versions are in `gradle/libs.versions.toml`. Key versions: Kotlin 2.0.20, Compose 1.6.8, Hilt 2.52, Room 2.6.1, Arrow 1.2.4, Ktor 2.3.12, Kotest 5.9.1, MockK 1.13.12, Paparazzi 1.3.3.

## Code Quality

- **Detekt** configuration at `config/detekt/`. Custom rule set in `:ci-actions:detekt-explicit`.
- **Compose lint** (Slack's compose-lints) enforced via the `ivy.compose` plugin.
- CI runs `testDebugUnitTest`, `detekt`, `lintRelease`, and Paparazzi screenshot tests on every PR.

## Project Status

As of November 2024, this project is **no longer actively maintained** by its original authors and is not accepting PRs. It is open-source under GPL-3.0.

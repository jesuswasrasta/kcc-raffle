# AGENTS.md

## Project

Kotlin/JavaFX desktop app for randomly assigning sponsor prizes to conference attendees at a live event.
Single-module Gradle 7.0.2 project, Kotlin 1.8.10, JavaFX plugin 0.1.0 (JavaFX 21.0.2).
Group: `it.intre`, version `2.0-SNAPSHOT`.

## Run

```bash
./gradlew run    # launches JavaFX GUI (main class: RaffleAppKt)
```

## Setup before first run

Copy sample files in `src/main/resources/`, replacing them with real data:

```
attendees.csv.sample → attendees.csv        (FirstName,LastName,Email)
prizes.csv.sample    → prizes.csv           (#,Name,Description,Image,Secret)
config.properties.sample → config.properties
```

Also copy prize images into `src/main/resources/`. All three working files and `*.log` are gitignored.

## Architecture

Package `it.intre.conf.raffle`:

- **`RaffleApp.kt`** — JavaFX Application entrypoint (the one `gradle run` invokes). Wires the UI flow: welcome → each prize → each winner → recap table.
- **`Engine`** — sealed class for drawing prizes/winners. Production uses `TrulyRandomEngine` (`SecureRandom`); `ZeroSuspenseEngine` returns in insertion order.
- **`DataSource`** — sealed class. `CsvDataSource` reads from resources CSV; `MemoryDataSource` has hardcoded test data.
- **`Store`** — mutable lists of attendees and prizes, populated from a `DataSource`.
- **`Config`** — reads `config.properties` via `ClassLoader.getSystemResource`.

UX components live in `it.intre.conf.raffle.ux` (JavaFX wrappers).

## Gotchas

- **Two `main()` functions exist**: `RaffleApp.kt` (JavaFX GUI) and `Raffle.kt` (console/text mode). `build.gradle.kts` points at `RaffleAppKt`, so `gradle run` always launches the GUI version. The console version in `Raffle.kt` has its own `main()` for standalone execution.
- **`CsvDataSource` uses `ClassLoader.getSystemResource().file`**: this breaks when running from a JAR. Only works from exploded classes (IDE, `gradle run` from project dir).
- **JUnit dependency is broken**: `testImplementation("junit:junit:4.13.1")` is JUnit 4 but `useJUnitPlatform()` expects JUnit 5. No actual tests exist.
- **No CI**: no GitHub workflows or CI config present.

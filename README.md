# kcc-raffle

Live raffle app for randomly assigning sponsor prizes to conference attendees. Built with Kotlin and JavaFX.

The `KCC` in the name comes from its origin at the [Milan Kotlin Community Conference 2018](https://kotlinconf.com/).

## Prerequisites

- **JDK 17 or later** — the Gradle wrapper downloads everything else
- macOS / Windows / Linux (Apple Silicon supported)

Check your Java version:

```bash
java -version
```

## Quickstart

Clone the repo, then create the three required data files from their samples:

```bash
cp src/main/resources/attendees.csv.sample  src/main/resources/attendees.csv
cp src/main/resources/prizes.csv.sample     src/main/resources/prizes.csv
cp src/main/resources/config.properties.sample src/main/resources/config.properties
```

Edit each file to match your event. Also copy any prize images (png/jpg) into `src/main/resources/`.

Then run:

```bash
./gradlew run
```

## How to use it

The app walks through a linear flow:

1. **Welcome screen** — shows your conference logo and total prize count. Click **"LET'S GO!"**.
2. **Prize reveal** — displays each prize (image, name, description) in order from lowest-tier to grand prize. Click **"DRAW!"**.
3. **Winner announcement** — picks a random attendee, animates their name. Click **"OK"** to confirm or **"REDRAW"** to skip and re-pick.
4. **Recap table** — after all prizes are assigned, shows the full winner list sorted by prize tier.

All matches are logged to `raffle.log` (prize secrets and winner emails go to TRACE level).

## Data files

### `attendees.csv`

List of eligible participants:

```
First Name,Last Name,E-Mail
Mario,Rossi,mario@example.com
Anna,Bianchi,anna@example.com
```

Each row must have exactly three comma-separated values.

### `prizes.csv`

Prizes sorted from **worst** (highest number) to **best** (lowest number). They are drawn in CSV order, so the grand prize comes last.

```
#,Name,Description,Image,Secret
19,Book,Publisher XYZ e-book,book.png,XYZ-CODE-001
...
 1,All-Access Pass,1-year subscription,pass.png,SUPER-SECRET
```

| Field         | Notes |
|---------------|-------|
| `#`           | Sort key — highest first, lowest last (grand prize = 1) |
| `Name`        | Short name shown on screen |
| `Description` | Longer description, sponsor credits, etc. |
| `Image`       | Image filename placed in `src/main/resources/` |
| `Secret`      | Optional — promo code, serial number; only appears in `raffle.log` TRACE |

### `config.properties`

App texts and welcome screen:

```properties
app.title = Raffle for MyConf 2025
welcome.title = MyConf 2025
welcome.logo = myconf-logo.png
welcome.note = #myconf raffle
```

### Images

Place any image files referenced in `config.properties` or `prizes.csv` inside `src/main/resources/`.

Missing images fall back to a built-in `ghost.png` placeholder.

## Build

```bash
./gradlew assemble    # compile + jar (no run)
./gradlew run         # compile + launch GUI
./gradlew clean       # wipe build output
```

The project uses Gradle 7.0.2 with the Kotlin JVM plugin 1.8.10 and JavaFX via `org.openjfx.javafxplugin` 0.1.0 (JavaFX 21.0.2).

## Project structure

```
src/main/kotlin/it/intre/conf/raffle/
├── RaffleApp.kt      # JavaFX GUI entrypoint
├── Raffle.kt         # Console/text-mode entrypoint (standalone main)
├── Engine.kt         # TrulyRandomEngine (production) / ZeroSuspenseEngine (test)
├── DataSource.kt     # CsvDataSource (prod) / MemoryDataSource (hardcoded test data)
├── Store.kt          # Mutable lists of attendees & prizes
├── Config.kt         # Reads config.properties
├── Attendee.kt       # Data class + NOBODY sentinel
├── Prize.kt          # Data class + NONE sentinel
├── Result.kt         # Winner + Prize pair
├── InputOutput.kt    # Console and GUI output (used by Raffle.kt)
└── ux/               # JavaFX helpers (scenes, buttons, grids, tables)
```

## Logs

Log output goes to stdout and `raffle.log`. Log level is controlled by `src/main/resources/log4j.properties`. Prize secrets and winner emails are logged at TRACE level — they won't appear at the default DEBUG level.

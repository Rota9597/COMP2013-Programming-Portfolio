# CyborgPlatform — Java Game Refactoring and Extension

**Shuyu Zheng · COMP2013 · University of Nottingham, UK · Exchange study, 2025–2026**

CyborgPlatform is a 2D platform shooter developed through a coursework process of debugging, architectural refactoring, and feature extension. This repository contains the final JavaFX implementation, tests, design documentation, and recorded gameplay previews.

The project demonstrates object-oriented design, separation of responsibilities, game-state management, and testing. The accompanying design report traces the progression from an earlier Swing-based implementation to the modular JavaFX version presented here.

## Development work

My documented work on this project includes:

- **Debugging the earlier version:** resolving build, runtime, and resource-loading issues.
- **Refactoring the architecture:** separating player input, physics, combat, enemy behaviour, camera tracking, and rendering into dedicated components.
- **Organising the entity model:** introducing shared abstractions for entities, moving objects, characters, players, and enemies.
- **Adding configurable difficulty:** Easy, Normal, and Hard configurations for enemy population, movement speed, ammunition, and scoring.
- **Extending gameplay and presentation:** adding collectible coins, difficulty-adjusted scoring, a start screen, theme selection, and a background-music option.
- **Documenting and testing:** writing JUnit tests, a test plan, UML diagrams, and a development changelog.

See the [design report](docs/SoftwareDesign.md) for the reasoning behind the refactoring and the [changelog](docs/Changelog.md) for the recorded development stages. This portfolio presents that development work; it does not claim that every part of the earlier game or its assets was created from scratch for this submission.

## Gameplay previews

The following recordings are included in the recovered coursework archive.

![Gameplay preview](gifs/StartGif.gif)

![Enemy interaction preview](gifs/EnemyExampleGif.gif)

## Features

- Side-scrolling movement, jumping and double jumping.
- Projectile combat, ammunition limits, damage, and enemy behaviour.
- Tile-based levels loaded from a text map.
- A following camera and layered backgrounds.
- Collectible coins and scores for defeating enemies.
- Difficulty selection, theme colour selection, and optional background music.
- A HUD and a victory screen displaying game statistics.

### Controls

| Key | Action |
| --- | --- |
| A / D | Move left / right |
| W | Jump; press again in the air to double jump |
| Space | Shoot |
| Escape | Restart the level |
| Enter | Exit from the victory screen |

## Architecture and suggested reading

The implementation uses a separation of model, rendering, and control responsibilities, coordinated by the game engine.

| Component | Responsibility | Suggested source |
| --- | --- | --- |
| Application | Initialisation, start screen, and JavaFX animation loop | [GameApp.java](src/main/java/app/GameApp.java) |
| Engine | Coordinates updates, level restarts, and victory detection | [GameEngine.java](src/main/java/engine/GameEngine.java) |
| Physics | Movement and platform collision handling | [PhysicsSystem.java](src/main/java/engine/PhysicsSystem.java) |
| Combat | Damage, projectile interactions, rewards, and collectibles | [CombatSystem.java](src/main/java/engine/CombatSystem.java) |
| Difficulty | Creates settings for the selected difficulty | [DifficultyFactory.java](src/main/java/model/DifficultyFactory.java) |
| Input | Translates key states into movement, jumping, and shooting | [PlayerControlSystem.java](src/main/java/engine/PlayerControlSystem.java) |
| Rendering | Draws the scene, HUD, and victory screen | [Renderer.java](src/main/java/render/Renderer.java) |
| Resources | Loads and caches images, animation frames, and fonts | [ResourceManager.java](src/main/java/resources/ResourceManager.java) |

For a short code review, start with `GameEngine`, then follow its calls into `PhysicsSystem` and `CombatSystem`. The design report explains how these responsibilities were extracted from the earlier implementation.

## Technology and setup

The supplied Maven configuration targets **Java 17** and declares **JavaFX 17** and **JUnit Jupiter 5.6.0**. Use a desktop environment with JDK 17 and Maven installed.

### Launch configuration correction

The recovered `pom.xml` still points the JavaFX Maven plugin at `HelloFX`. Before launching, change its `mainClass` entry to:

```xml
<mainClass>app.GameApp</mainClass>
```

Then run the following commands from the repository root (the folder containing `pom.xml`):

```bash
mvn compile
mvn javafx:run
```

Resources are stored under `src/main/resources`; keep that directory structure intact. These instructions are based on the supplied source and configuration; the recovered version has not yet been independently build-tested for this portfolio.

## Tests and verification status

JUnit source files are stored in `test/`, configured as Maven's test source directory. They cover areas including physics, combat, player behaviour, camera boundaries, resource loading, difficulty, scoring, and preferences.

```bash
mvn test
```

The [test plan](docs/Testing.md) also describes integration scenarios and manual UI checks. It records coursework testing activities, rather than a newly verified test result for this GitHub copy.

Known follow-up work before treating the suite as a complete regression check:

- The scenario classes under `test/intergration/` use names such as `FullGameFlow` rather than the usual `*Test` naming convention; the supplied POM has no explicit configuration to include them in the default test run.
- Some older scenario setup needs revision. For example, `FullGameFlow` constructs `GameEngine` with a null scene manager, while the current constructor accesses that manager.
- JavaFX-dependent paths need appropriate runtime initialisation. A passing subset of tests should not be presented as verification of all gameplay or UI behaviour.

## Repository guide

| Location | Contents |
| --- | --- |
| `src/main/java/` | Final JavaFX application and game systems |
| `src/main/resources/` | Level map, sprites, backgrounds, font, and audio |
| `test/` | JUnit tests and integration scenario source files |
| `docs/` | Design report, test plan, changelog, UML diagrams, and screenshots |
| `gifs/` | Recorded gameplay previews |
| `javadoc/` | API documentation included in the archive |

The coursework documentation also describes a GitLab CI pipeline using university-managed runners and scripts. That historical setup is not an active GitHub Actions workflow in this repository.

## Credits

The original project README credits the sprites and images to [CraftPix](https://craftpix.net/freebies/). Those third-party assets are separate from the programming contribution described above. This repository does not grant additional rights to third-party assets or course-provided material.

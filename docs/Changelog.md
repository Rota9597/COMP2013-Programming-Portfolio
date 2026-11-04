# Changelog

### Added
- Store successful opening screenshots in screenshots.[task1_OpenSucc.png](task1_OpenSucc.png)
- Store successful running screenshots in screenshots.[task1_RunSucc.png](task1_OpenSucc.png)
### Changed
- Fixed build and runtime errors in v1.CyborgPlatform, v1.Game.
- Renamed Maps.txt to Map.txt for proper loading.
### Removed

## 20/10/2025
 - Version 1 nearly ready. Code seems to be broken. 

## [v1] - 24/10/2025
### Changed
 - Fixed build and runtime errors in v1.CyborgPlatform, v1.Player, v1.Enemy, and v1.MapBlocks.
 - Added temporary placeholder images to prevent NullPointerExceptions.
 - Renamed Maps.txt to Map.txt for proper loading.
 - v1.Game builds and runs successfully (window opens).
 - Store successful running screenshots in screenshots.[task1_OpenSucc.png](task1_OpenSucc.png)

## [v1] - 30/11/2025
### Changed
- Merge the task1 branch into the dev branch.

## [v1] - 3/11/2025
### Added
 - Added commits in v1 game code files

## [v1] - 4/11/2025
### Added
- Analyzed and summarized the v1 code structure and draw v1 UML diagram in SoftwareDesign.md.
- Designed the initial code structure of v2 and drew the v2 UML diagram in SoftwareDesign.md.
- Added the initial version introduction of v2 in the README.md.
- Removed temporary placeholder images.
### Changed
- Merge the task2 branch into the dev branch.

## [v1] - 10/11/2025
### Changed
- Restored the modifications in v1.CyborgPlatform, v1.Player, v1.Enemy, and v1.MapBlocks.
- Fixed function named error in v1.CyborgPlatform. (line 18: game.loadImages();)
- Fixed path error in v1.Game. (line 48: v1.canvas.cloud = ImageIO.read(new File("src/main/resources/Sprites/cloud.png"));)
- Removed temporary placeholder images.
- v1.Game builds and runs successfully (v1.Game show successfully).
- Store successful running screenshots in screenshots.[task1_RunSucc.png](task1_RunSucc.png)

## [v1] - 17/11/2025
### Added
- Created logical test code java files for v1.
- Created all stub classes for v2
- Created stub test code java files for each class of v2.
- Added the test plan table of v1 in the Testing.md.
- Added the test plan table of v2 in the Testing.md.

## [v1] - 18/11/2025
### Changed
- Fixed v1 PlayerMovementTest bug.

## [v1] - 19/11/2025
### Removed
- Removed v1 BaseGameTest.java to deal with pipline error
### Changed
- Fixed v1 test files to deal with pipline error.
- Modified v1 test plan table in Testing.md to adapt to the changes in the v1 test code files.
- Merge the task3 branch into the dev branch.

## [v2] - 26/11/2025
### Removed
- Removed all stub classes for v2
### Added
- Created the basic class code of v2 and implemented the general game logic in the code, include:
- `GameApp`, `CameraSystem`, `CombatSystem`, `GameEngine`, `LevelBuilder`,`PhysicsSystem`
- `Scene`, `SceneManager`, `TileManager`,`InputManager`, `BackgroundLayer`,`GameObject`
- `Player`, `Enemy`, `bullet`, `TileBlock`, `Platform`, `TileType`, `Renderer`, `ResourceManager`
- Create v2 software package.

## [v2] - 1/12/2025
### Removed
- Remove all the test classes designed for the original v2 stub class
- Remove all the v1 code
### Added
- Created more classes to share and clarify responsibilities, include:
- `EnemyBehaviorSystem`, `PlayerControlSystem`, `Entity`, `Character`, `CloudEffect`
- Write comments for all the code in v2.
### Changed
- Modified some classes to reduce and clarify responsibilities, include:
- `GameObject`, `Player`, `Enemy`, `bullet`,`Renderer`, `PhysicsSystem`, `CombatSystem`
- Adjust the v2 software package and code structure.

## [v2] - 2/12/2025
### Removed
- Remove all the v1 test classes
### Added
- Created unit and integration test code for v2.
### Changed
- Modified `Enemy` and `CombatSystem` to move ammo reward logic.
- Fix the bug in pom.xml to deal with pipline error.

## [v2] - 3/12/2025
### Removed
- Removed all test table for v2_initial_version in Testing.md.
### Added
- Added design description in SoftwareDesign.md for the v2 refactoring code.
- Created v2 final structure UML diagram.
- Added the test plan table of v2_final_version in the Testing.md.

## [v3] - 4/12/2025
### Removed
- Removed all test table for v2_initial_version in Testing.md.
### Added
- Added design description in SoftwareDesign.md for the v3 new features.
- Created v3 primary UML diagram in SoftwareDesign.md.
- Added start game page demo in SoftwareDesign.md for the v3 new features.
- Added the test plan table of v3_primary_version in the Testing.md.
- ### Changed
- Modified v2 design description in SoftwareDesign.md to cover some missing parts.
- Merge the task5 branch into the dev branch.

## [v3] - 5/12/2025
### Added
- Added detailed CI/CD configuration explanation in Testing.md.
- Added new CI pipeline stages.
- Created stub classes for v3 new features.
- ### Changed
- Fixed CI pipeline failure caused by invalid Javadoc commits in:
`GameEngine.java`, `InputManager.java`, `PhysicsSystem.java`, `TileBlock.java`, `Player.java`, `PlayerControlSystem.java`
- Merge the task6 branch into the dev branch.

## [v3] - 6/12/2025
### Added
- Added test-driven development test cases for all V3 planned features, including difficulty configuration, collectibles and score management, and preference.
- Initially create `DifficultyLevel`, `DifficultyConfig`, and `DifficultyFactory` to support difficulty-based gameplay tuning.
- Initially create `ScoreManager` with in-memory scoring, including difficulty-based score multipliers.
- Initially create `CollectibleSpawner` and `CollectibleObject` to generate collectible object.
- Integrated score updates for collectible pickups and enemy defeats.
- Added score display UI in `renderer`.
- Added `coin.png` as image resource for `CollectibleObject` and realize display function.
### Changed
- Extended `CombatSystem` to handle player–collectible interactions and enemy defeat rewards.
- Updated `Player` to trigger scoring events when collecting items or defeating enemies.
- Updated `Renderer` to complete score display.
- Updated `GameEngine` to expose score data for rendering and end-screen presentation.
- Updated `Scene` to complete the addition of collecting items to the scene list.

## [v3] – 7/12/2025
### Removed
- Removed the obsolete `CoinSpawner` class and its associated unit tests after refactoring collectible spawning logic into `LevelBuilder`.
### Added
- Added `StartScreenScene` to render the start menu UI and collect player preferences before gameplay begins.
- Added `Preferences` model to store runtime player preferences for the current session.
- Added `PreferencesManager` as a centralized application-level state manager to safely mediate preference updates from the UI.
### Changed
- Changed theme color to user selection in HUD rendering in the `Renderer`.
- Separating Start Screen presentation from actual game initialization in `GameApp`.
- Refactored difficulty handling to ensure all gameplay-related parameters (enemy count, speed, scoring multiplier, ammo behavior) are consistently derived from `DifficultyConfig`.
- Refactored Scene ownership so that `ScoreManager`, `DifficultyConfig`, and all game objects are owned and managed centrally by `Scene`.

## [v3] – 8/12/2025
### Added
- Added `MusicPlyaer` to complete music play function.
- Added `javafx.media` maven dependency in `pom.xml.
- Added `backgroundmusic.mp3` in resources.
- Added comments to the newly created class.
### Changed
- Changed `StartScreenScene` to realize music control botton.
- Redesigned the positions of the enemies and coins in `LevelBuilder` to make them more in line with the game's logic.
- Updated existing v2 unit and integration tests with minimal constructor argument adjustments to accommodate the introduction of `DifficultyConfig`, while preserving original test intent and behaviour.
- Refactored collectible-related tests to validate difficulty configuration values instead of concrete Scene instantiation, aligning tests with the redesigned v3 architecture.
- Improved comments to the existing class.
- Merge the task7 branch into the dev branch.
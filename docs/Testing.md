# Testing Plan and Results

##  Test Plan Table - v1

**1. Enemy Behaviour Tests**

| Test ID | Feature                | Purpose                                                              | Expected Result                                        |
|---------|------------------------|----------------------------------------------------------------------|--------------------------------------------------------|
| EB-01   | Idle behaviour         | Ensure enemies stop when player >400px                               | speed = 0, isRunning=false                             |
| EB-02   | Walking behaviour      | Normal-speed chase for distance 200–400px                            | speed max abs(1), isRunning=false                      |
| EB-03   | Running behaviour      | Faster chase when distance <200px                                    | speed max abs(3), isRunning=true                       |
| EB-04   | Facing direction       | Ensure enemy faces player                                            | isFacingForwards = correct                             |
| EB-05   | Vertical distance stop | v1.Enemy stops chasing if height difference >100px                   | speed = 0                                              |
| EB-06   | Full jump logic        | Validate complex jump rules                                          | jumpCounter update                                     |
| EB-07   | Jump velocity effect   | Ensure correct jump effects on velocity                              | velocity = -8 or -6                                    |
| EB-08   | Damage and recovery    | Validate health decrease, knockback, slowdown, 300ms invulnerability | health--, isDamaged switched, velocity = -6, speed /=2 |
| EB-09   | Normal death           | killing enemy                                                        | enemies.size() = 0                                     |
| EB-10   | Fall death             | enemy removed when y>900                                             | enemies.size() = 0                                     |

---

**2. Health System Tests**

| Test ID | Feature                   | Purpose                                                    | Expected Result                                          |
|---------|---------------------------|------------------------------------------------------------|----------------------------------------------------------|
| HS-01   | Enemy hurt player         | Valid collision between player and enemy                   | health--, isDamaged=true, velocity = -6, enemy speed= ±4 |
| HS-02   | Direction-based knockback | Validate push direction                                    | speed = ±4                                               |
| HS-03   | Damage immunity           | Ensure no damage when isDamaged=true, allowed after 1000ms | health unchanged when immunity, health-- when recovery   |
| HS-04   | Fall death                | v1.Player dead when y>900                                  | deathCounter++                                           |
| HS-05   | Health depletion          | v1.Player dead when health=0                               | deathCounter++                                           |
| HS-06   | Respawn reset             | kill() resets coordinates                                  | (20,300)                                                 |

---

**3. Physics System Tests**

| Test ID | Feature                    | Purpose                          | Expected Result                  |
|---------|----------------------------|----------------------------------|----------------------------------|
| PS-01   | Map collision slows player | Map collision cause speed reduce | speed /= 2                       |
| PS-02   | Player-enemy collision     | Validate bounding box            | collision=true                   |
| PS-03   | Bullet-enemy collision     | Validate hitbox                  | collision=true                   |
| PS-04   | Bullet-map collision       | Bullet removed                   | activeBullets--                  |
| PS-05   | Gravity (no collision)     | Validate falling                 | y++, velocity += 0.5             |
| PS-06   | Gravity (collision)        | Validate grounded physics        | isGrounded=true, velocity change |
| PS-07   | W reduces gravity          | Validate variable gravity        | acceleration decreases           |

---

**4. Player Movement Tests**

| Test ID | Feature       | Purpose                  | Expected Result              |
|---------|---------------|--------------------------|------------------------------|
| PM-01   | Move right    | Validate acceleration    | speed->5                     |
| PM-02   | Move left     | Validate acceleration    | speed->-5                    |
| PM-03   | Key press     | Validate press           | keyPress[] = true            |
| PM-03   | Stop movement | Immediate Stop Operation | speed = 0                    |
| PM-04   | First jump    | Jump from ground         | velocity = -8, JumpCounter++ |
| PM-05   | Double jump   | Airborne jump            | velocity = -6                |

---

**5. Shooting System Tests**

| Test ID | Feature            | Purpose                                               | Expected Result                                |
|---------|--------------------|-------------------------------------------------------|------------------------------------------------|
| SS-01   | Bullet lifetime    | Remove when >=600px                                   | coordinate changed and finally removed         |
| SS-02   | Hit enemy          | Remove v1.bullet                                      | removed                                        |
| SS-03   | Ammo reward        | From enemy death                                      | ammo += 2                                      |
| SS-04   | Ammo consumption   | Shooting reduces ammo, create v1.bullet, state switch | ammo--, activeBullets.size()++, justShot=true  |
| SS-05   | No ammo            | Cannot shoot                                          | activeBullets.size() unchanged, justShot=false |
| SS-06   | Cooldown           | Prevent continue shooting                             | activeBullets.size() and ammo unchanged        |
| SS-07   | Shooting direction | Left/right speed correct                              | speed = ±10                                    |

---

**6. Game loop and Camera Tests**

| Test ID | Feature                  | Purpose                                                    | Expected Result                                                                           |
|---------|--------------------------|------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| CM-01   | Game Initialization      | Ensure player, enemies and bullets are generated correctly | generate new players, enemies, and v1.bullet. v1.Player(x=20,y=300), health=3, ammo=10    |
| CM-02   | Win Condition            | Verify player wins when reaching the end of map            | game.isWon = true                                                                         |
| CM-03   | Game Reset               | Ensure ESC restart and regenerates all state               | deathCounter+1, generate new players and enemies, reset isWon                             |
| CM-04   | Camera Follows v1.Player | Ensure camera offsets follow player                        | player.updateCmr() = 1375                                                                 |
| CM-05   | Camera Boundaries        | Ensure camera does not scroll out of map                   | player.updateCmr() = 0 when player.x = 0; player.updateCmr() = 8690 when player.x = 10000 | 
| CM-06   | Game Timer Increases     | Verify timer increases with real world time                | Record time → sleep 200ms and timer increased ≥150ms                                      |

---

##  Test Plan Table - v2

### Unit Tests

**1. CameraSystemTest**

| Test ID | Test Method                          | Purpose                         | Expected Result                   |
|---------|--------------------------------------|---------------------------------|-----------------------------------|
| CS-01   | testCameraFollowCenter()             | Validate camera tracking player | camera.x/y changes proportionally |
| CS-02   | testCameraClampLeftBoundary()        | Validate left bounds            | camera.x left clamped             |
| CS-03   | testCameraClampRightBoundary()       | Validate right bounds           | camera.x right clamped            |

---

**2. CombatSystemTest**

| Test ID | Test Method                     | Purpose              | Expected Result                  |
|---------|---------------------------------|----------------------|----------------------------------|
| COM-01  | testBulletHitsEnemy()           | Validate bullet hurt | Enemy health-- and speed change  |
| COM-02  | testEnemyPlayerCollision()      | Validate enemy hurt  | Player health-- and speed change |
| COM-03  | testEnemyDeathGivesAmmoReward() | Ammo reward logic    | ammo += 2                        |

---

**3. EnemyBehaviorSystemTest**

| Test ID | Test Method                         | Purpose                                  | Expected Result                   |
|---------|-------------------------------------|------------------------------------------|-----------------------------------|
| EBS-01  | testEnemyChasesPlayerRightWalking() | Validate walk chase when player is right | Enemy facing left, max_speed = 1  |
| EBS-02  | testEnemyChasesPlayerLeftRunning()  | Validate run chase when player is left   | Enemy facing right, max_speed = 3 |
| EBS-03  | testEnemyStopsWhenFarAway()         | Stop chase when distance > 400           | speed = 0                         |
| EBS-04  | testEnemyStopsWhenTooClose()        | Stop chase when distance < 20            | speed = 0                         |
| EBS-05  | testEnemyConditionalJump()          | Validate jump                            | jumpcounter++, velocity = -6      |

---

**4. EnemyTest**

| Test ID | Test Method                           | Purpose                                  | Expected Result         |
|---------|---------------------------------------|------------------------------------------|-------------------------|
| EN-01   | testEnemyTakesDamage()                | Validate damage to enemy                 | health--, velocity = -6 |
| EN-02   | testEnemyInvulnerabilityAfterDamage() | Invalidate damage during Invulnerability | health remain           |
| EN-03   | testEnemyDeathByFalling()             | Fall to death (Y > 720)                  | isDead = true           |
| EN-04   | testEnemyDeathByDamaging()            | Health to death (Health <= 0)            | isDead = true           |

---

**5. PhysicsSystemTest**

| Test ID | Test Method                          | Purpose                                    | Expected Result                               |
|---------|--------------------------------------|--------------------------------------------|-----------------------------------------------|
| PS-01   | testGravityApplied()                 | Validate gracity affect                    | correct velocity change                       |
| PS-02   | testPlayerLandsOnPlatform()          | Validate land on platform                  | onground = true                               |
| PS-03   | testHorizontalMovementAndCollision() | Validate horizontal movement and collision | speed = 0, correct x coordinate               |
| PS-04   | testCeilingCollision()               | Correct respond when celling collision     | correct velocity change, correct y coordinate |

---

**6. PlayerControlSystemTest**

| Test ID | Test Method                | Purpose                          | Expected Result                                             |
|---------|----------------------------|----------------------------------|-------------------------------------------------------------|
| PCS-01  | testJumpCounterIncrement() | Validate jump                    | correct velocity change, correct jumpCounter change         |
| PCS-02  | testHorizontalMovement()   | Validate horizontal movement     | correct speed change                                        |
| PCS-03  | testShootingCooldown()     | Validate and invalidate shooting | correct ammo change, correct objects contain number changed |

---

**7. PlayerTest**

| Test ID | Test Method                               | Purpose                          | Expected Result       |
|---------|-------------------------------------------|----------------------------------|-----------------------|
| PL-01   | testPlayerInitialState()                  | Correct initial player state     | health = 3, ammo = 10 |
| PL-02   | testPlayerTakesDamageAndInvulnerability() | Validate and invalidate hit      | correct health change |
| PL-03   | testPlayerDeathByHealth()                 | Health to death (Health <= 0)    | isDead = true         |
| PL-04   | testPlayerShootingCooldown()              | Validate and invalidate shooting | correct ammo change   |

---

**8. ResourceManagerTest**

| Test ID | Test Method         | Purpose                         | Expected Result   |
|---------|---------------------|---------------------------------|-------------------|
| RM-01   | testImageLoads()    | Validate sprite import          | sprite != null    |
| RM-02   | testSequenceLoads() | Validate sequence sprite import | frames.length = 4 |

---

**9. TileManagerTest**

| Test ID | Test Method                   | Purpose              | Expected Result                 |
|---------|-------------------------------|----------------------|---------------------------------|
| TM-01   | testLoadMapNotEmpty()         | Validate tile import | tile != null                    |
| TM-02   | testTilePositionsAreAligned() | Validate tile align  | tiles coordinate distance is 48 |

---

### Integration Tests

| # | Test Name      | Purpose                                                          | Purpose                                                                                                                                                            |
|---|----------------|------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1 | LoadScene      | Verify scene initializing and all essential game objects loaded. | Scene is not null, contains exactly 14 objects (1 Player + 13 Enemies), MapWidth is 8300, player starts with 3 HP and 10 Ammo. Scene loads more than 50 Platforms. |
| 2 | PlayerMovement | Verify walking & jumping & double jump                           | Correct player coordinate, velocity, jumpCounter, ammo num. Valid bullet creation. Correct scene's objects                                                         |
| 3 | EnemyBehavior  | Enemy chasing and being killed                                   | Correct enemy facing direction, speed, isDeath state. Correct player ammo num.                                                                                     |
| 4 | Physics        | Platform collision                                               | Correct player coordinate, OnGround state, velocity.                                                                                                               |
| 5 | WinCondition   | Win and game restart/reset                                       | Correct isWon state. Correct restart player coordinate, ammo num, speed.                                                                                           |
| 6 | FullGameFlow   | Death after falling off map and full cycle to victory            | Validate game loop logic                                                                                                                                           |

---
##  Test Plan Table - v3

**1. Difficulty System Tests**

| Test ID | Feature            | Purpose                                                  | Expected Result                                     |
|--------:|--------------------|----------------------------------------------------------|-----------------------------------------------------|
|   DS-01 | Load EASY config   | Ensure DifficultyFactory returns correct EASY parameters | enemyCount=5, speed=0.8×, cooldown=0.9×             |
|   DS-02 | Load NORMAL config | Ensure NORMAL parameters load as baseline                | enemyCount=13, speed=1.0×, cooldown=1.0×            |
|   DS-03 | Load HARD config   | Ensure HARD difficulty increases challenge               | enemyCount=17, speed=1.3×, cooldown=1.2×            |
|   DS-04 | Apply difficulty   | GameEngine correctly applies DifficultyConfig            | spawn count & enemy speed reflect config values     |
|   DS-05 | Score multiplier   | Verify scoring multiplier based on difficulty            | EASY=0.8×, NORMAL=1.0×, HARD=1.3× applied correctly |
|   DS-06 | Ammo adjustment    | Verify ammo count changes based on difficulty            | EASY=20, NORMAL=10, HARD=10                         |
|   DS-07 | Cooldown effect    | Shooting cooldown scaled by difficulty                   | adjustedCooldown = base * multiplier                |

DS-07 (Cooldown effect) was verified through gameplay behaviour testing.
The shooting cooldown is stored internally as a derived value and is not directly observable without exposing implementation details.
---

**2. Collectibles + Scoring System Tests**

| Test ID | Feature                | Purpose                                           | Expected Result                        |
|--------:|------------------------|---------------------------------------------------|----------------------------------------|
|   CS-01 | Spawn collectibles     | Ensure correct number spawned based on difficulty | EASY=5, NORMAL=10, HARD=15             |
|   CS-02 | Collect item           | Collectible removed & score increased             | collectible removed, score += 5        |
|   CS-03 | Kill enemy scoring     | Score increases when enemy defeated               | score += 10                            |
|   CS-04 | Score multiplier       | Difficulty multiplier applied to score            | e.g. HARD: +10 → +13                   |
|   CS-05 | Score reset            | ScoreManager resets correctly at game start       | score = 0                              |
|   CS-06 | HUD score display      | ScoreDisplay reflects ScoreManager updates        | HUD shows real-time score              |
|   CS-07 | End-screen score       | Score shown correctly on victory screen           | displays final score from ScoreManager |
|   CS-08 | Collectible collision  | Ensure collision detection triggers collect()     | player overlap → collectible removed   |
|   CS-09 | Invalid double collect | Collectible cannot be collected twice             | score changes only once                |

Coin (Collectible) collision and removal were validated through integration testing, as they depend on spatial interactions 
and scene update cycles that are not suitable for isolated unit testing.
---

**3. Start Screen + Preferences Tests**

| Test ID | Feature                    | Purpose                                                         | Expected Result                                      |
|--------:|----------------------------|-----------------------------------------------------------------|------------------------------------------------------|
|   SP-01 | Default difficulty         | Ensure default dropdown value is NORMAL                         | difficulty=NORMAL                                    |
|   SP-02 | Difficulty selection       | Changing dropdown updates PreferencesManager                    | stored difficulty matches UI selection               |
|   SP-03 | Theme default              | Default theme is BLUE                                           | themeColor=BLUE                                      |
|   SP-04 | Theme selection            | Changing theme updates PreferencesManager                       | stored theme matches selection                       |
|   SP-05 | Music default              | Music toggle default is ON                                      | musicEnabled=true                                    |
|   SP-06 | Music toggle               | Toggling button updates preference & applies on game start      | musicEnabled updated; BGM on/off                     |
|   SP-07 | Preferences persistence    | Preferences retained until Start Game is pressed                | PreferencesManager stores values correctly           |
|   SP-08 | Start Game transition      | Pressing Start loads correct preferences into GameEngine        | GameEngine.initFromPreferences() called successfully |
|   SP-09 | Theme applied in game      | Renderer receives theme setting from preferences                | HUD/buttons show selected color                      |
|   SP-10 | Difficulty applied in game | GameEngine uses selected DifficultyConfig to initialise systems | spawning, speed, cooldown reflect chosen difficulty  |

UI-driven features such as Start Screen transitions, theme application, and difficulty initialisation were tested manually, 
as they depend on JavaFX runtime behaviour.
---
Automated JUnit tests were implemented for all core scoring and difficulty logic.
UI rendering, collision detection, and end-screen behaviour were validated through integration and manual testing as documented in the test plan.

# CI/CD Setup

The final pipeline consists of five jobs arranged into five stages: **Test, CodeQuality, Documentation, Build, and Release**.

```yaml
tags:
  - comp2013_2025_production
````

This tag ensures that the jobs are always executed on the COMP2013 course runner where the four scripts are available and 
where the security restrictions are enforced.

**1. Test stage**

The Test stage runs first and is responsible for executing the automated JUnit test suite. The job is defined as:

```yaml
Test:
  stage: Test
  tags:
    - comp2013_2025_production
  script:
    - test.sh
  artifacts:
    when: always
    reports:
      junit: target/surefire-reports/*.xml
    paths:
      - target/surefire-reports/
```

The `test.sh` script invokes Maven to run all unit tests. The job is configured to run for every push and for every merge request, 
so any change to the codebase is automatically regression-tested. JUnit XML files produced by Maven are collected as artifacts. 
The `reports: junit` section tells GitLab that these files represent test reports, which allows the results to be displayed 
in the GitLab UI and linked to the corresponding pipeline. 
Using `when: always` ensures that the reports are uploaded even if some tests fail, which makes debugging easier.

**2. CodeQuality stage**

The CodeQuality stage performs static analysis on the codebase:

```yaml
CodeQuality:
  stage: CodeQuality
  tags:
    - comp2013_2025_production
  script:
    - quality.sh
  artifacts:
    when: always
    paths:
      - quality-reports/
    reports:
      codequality: quality-reports/gl-code-quality-report.json
```

The `quality.sh` script runs a set of code-quality checks on the runner and produces a Code Climate–compatible report 
in the `quality-reports/` directory. This file is registered as a `codequality` report so that GitLab can surface issues 
directly in merge requests. The job runs on all pushes and merge requests, which means that code style and maintainability 
are continuously monitored alongside functional tests.

**3. Documentation stage**

The Documentation stage is used to automatically generate Javadoc API documentation and push it back to the repository.

```yaml
Documentation:
  stage: Documentation
  tags:
    - comp2013_2025_production
  script:
    - documentation.sh
  rules:
    - if: $CI_PIPELINE_SOURCE == "merge_request_event" &&
          $CI_MERGE_REQUEST_TARGET_BRANCH_NAME == "dev"
      when: on_success
    - when: never
```

The first rule ensures that the job only runs for **merge request pipelines** whose target branch is `dev`. 
The second rule (`when: never`) acts as a fallback to explicitly disable the job for all other pipeline types. 
This configuration guarantees that the Javadoc generation is triggered exactly once in the intended scenario 
and never by accident on `main`, where it would fail due to missing permissions.

When the job runs, `documentation.sh` builds the Javadoc, commits the generated files, and pushes them back to the 
repository using a pre-configured Git token variable. These documentation-updating commits themselves trigger follow-up 
pipelines, but because they are simple push events and no longer match the `rules` condition, the Documentation job is 
correctly skipped in those runs. 

**4. Build stage**

The Build stage compiles the project and produces the executable JAR file:

```yaml
Build:
  stage: Build
  tags:
    - comp2013_2025_production
  script:
    - build.sh
  artifacts:
    paths:
      - target/*.jar
```

The `build.sh` script encapsulates the Maven build commands on the runner. The generated JAR file is stored as a build 
artifact under `target/*.jar`. The Build job runs on every push so that compile-time problems are detected as early as possible. 
It also prepares the artifact that will be consumed by the Release stage.

---

## 5. Release stage

The Release stage models the process of creating a release from a successfully built version:

```yaml
Release:
  stage: Release
  tags:
    - comp2013_2025_production
  script:
    - echo "Generating release"
  needs:
    - job: Build
      optional: true
  rules:
    - if: $CI_PIPELINE_SOURCE == "merge_request_event" &&
        $CI_MERGE_REQUEST_TARGET_BRANCH_NAME == "main"
      when: on_success
    - when: never
  artifacts:
    paths:
      - target/*.jar
```

The Release job is explicitly restricted to merge request pipelines that target the `main` branch.
In all other pipeline contexts, the job is disabled to prevent unintended release generation.
The job declares a dependency on the `Build` job via the `needs` directive. This configuration guarantees that a release can 
only be produced after a successful build, and that the required JAR artifact generated during the build stage is available 
before the release process executes.

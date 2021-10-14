# Battlecode 2021 Scaffold

This is the Battlecode 2021 scaffold, containing an `examplefuncsplayer`. Read https://2021.battlecode.org/getting-started!

### Project Structure

- `README.md`
    This file.
- `build.gradle`
    The Gradle build file used to build and run players.
- `src/`
    Player source code.
- `test/`
    Player test code.
- `client/`
    Contains the client. The proper executable can be found in this folder (don't move this!)
- `build/`
    Contains compiled player code and other artifacts of the build process. Can be safely ignored.
- `matches/`
    The output folder for match files.
- `maps/`
    The default folder for custom maps.
- `gradlew`, `gradlew.bat`
    The Unix (OS X/Linux) and Windows versions, respectively, of the Gradle wrapper. These are nifty scripts that you can execute in a terminal to run the Gradle build tasks of this project. If you aren't planning to do command line development, these can be safely ignored.
- `gradle/`
    Contains files used by the Gradle wrapper scripts. Can be safely ignored.


### Useful Commands

- `./gradlew run`
    Runs a game with the settings in gradle.properties
- `./gradlew update`
    Update to the newest version! Run every so often

## Strategy

**Enlightenment Centers**

- Bid if influence is above a certain threshold (maybe enough influence to make a certain bot?) after creating specified amount of bots
- Bot creation priority: muckrakers, politicians, slanderers?
- need to decide how much influence to allocate each robot

**Muckrakers**

- send muckrakers in first to find as many slanderers as possible
- if a muckraker detects a robot, move closer to it
- if a muckraker finds an enlightment center, set a flag
- if a muckraker finds a politician, set a flag
- if a muckraker finds a muckraker, set a flag

**Slanderers**

- hide slanderers (can they sense other robots?)
- move away from muckraker flags

**Politicians**

- send politicians to flag
- priority: neutral enlightenment center, other politicians, muckrakers, enemy enlightenment centers
	(This may be something we want to change as we see results)
- empower at the destination

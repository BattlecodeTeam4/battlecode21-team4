package robotgamma;

import battlecode.common.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public class Slanderer extends Robot {
    /**
     * @return
     * @throws GameActionException
     */
    static int convertFlag() throws GameActionException {
        if (turnCount >= slaThreshold) {
            if (rc.canSetFlag(turnCount)) {
                rc.setFlag(turnCount);
            }
        }
        return turnCount;
    }

    static Direction moveAway() throws GameActionException {
        // checking if Muckraker's found, if found, then move
        int moveX = 0;
        int moveY = 0;
        Direction direct = randomDirection();
        for (RobotInfo enemy : rc.senseNearbyRobots(senseRadius, enemy)) {
            if (enemy.getType() == RobotType.MUCKRAKER) {
                System.out.println("R u n n i n g  A w a y ! ! !");
                MapLocation enemyLoc = enemy.location;
                if (enemyLoc.x > rc.getLocation().x) {
                    moveX--;
                } else {
                    moveX++;
                }

                if (enemyLoc.y > rc.getLocation().y) {
                    moveY--;
                } else {
                    moveY++;
                }

            }
            MapLocation destination = rc.getLocation().translate(moveX, moveY);
            direct = rc.getLocation().directionTo(destination);
            break;
        }
        return direct;
    }

    /**
     * @throws GameActionException
     */
    static void runSlanderer() throws GameActionException {
        convertFlag();
        tryMove(moveAway());
    }
}
package robotbeta;

import battlecode.common.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public class Slanderer extends Robot {
    /**
     * @throws GameActionException
     */
    static void runSlanderer() throws GameActionException {
        if(turnCount >= slaThreshold)
        {
            if(rc.canSetFlag(turnCount))
            {
                rc.setFlag(turnCount);
            }
        }
        if(senseRadius == 0)
        {
            updateSenseRadius();
        }
        int moveX = 0;
        int moveY = 0;
        // checking if Muckraker's found, if found, then move
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
            Direction direct = rc.getLocation().directionTo(destination);
            tryMove(direct);
            return;
        }
        //move randomly if enemy not detected
        tryMove(randomDirection());
    }
}

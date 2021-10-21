package robotbeta;

import battlecode.common.*;

public class Slanderer extends Robot {
    static void runSlanderer() throws GameActionException {
        int moveX = 0;
        int moveY = 0;
        // checking if Muckraker's found, if found, then move
        for (RobotInfo enemy : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (enemy.getType() == RobotType.MUCKRAKER) {
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
            System.out.println("My next runaway destination is: " + destination);
            Direction direct = rc.getLocation().directionTo(destination);
            tryMove(direct);
        }
        //move randomly if enemy not detected
        if(tryMove(randomDirection())) {
            System.out.println("I moved!");
        }
    }
}

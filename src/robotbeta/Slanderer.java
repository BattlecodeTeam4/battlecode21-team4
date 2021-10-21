package robotbeta;

import battlecode.common.*;

public class Slanderer extends Robot {
    static void runSlanderer() throws GameActionException {
        if(sensorRadius == 0)
        {
            updateSensorRadius();
        }
        int moveX = 0;
        int moveY = 0;
        // checking if Muckraker's found, if found, then move
        for (RobotInfo enemy : rc.senseNearbyRobots(sensorRadius, enemy)) {
            if (enemy.getType() == RobotType.MUCKRAKER) {
                System.out.println("Running Away!");
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
            return;
        }
        //move randomly if enemy not detected
        if(tryMove(randomDirection())) {
            System.out.println("I moved!");
        }
    }
}

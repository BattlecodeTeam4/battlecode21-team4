package robotbeta;

import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

public class Muckraker extends Robot {
    /**
     *
     * Muckraker Flags:
     * 1 - Neutral Enlightenment Centers
     * 2 - Enemy Politician
     * 3 - Enemy Muckraker
     * 4 - Enemy Enlightenment Centers
     *
     * This will later be expanded to communicate location
     * information via flags.
     *
     **/
    static void runMuckraker() throws GameActionException
    {
        //Direction myDirection = null;

//        // Move closer to robots it detects
//        for (MapLocation robotLocation : rc.detectNearbyRobots(actionRadius))
//        {
//            Direction d = rc.getLocation().directionTo(robotLocation);
//            if (rc.getLocation().equals(robotLocation)) {
//                break;
//            }
//            for (int i = 0; i < 2; ++i) {
//                if (rc.isReady()) {
//                    // If the space I am trying to move to is easily passable
//                    // then move
//                    if (rc.canMove(d) && rc.sensePassability(rc.getLocation().add(d)) >= passabilityLimit) {
//                        rc.move(d);
//                    } else if (myDirection == null) {
//                            myDirection = d.rotateRight();
//                    }
//                }
//            }
//        }
        if(actionRadius == 0)
        {
            updateActionRadius();
        }

        if(goingHome) {
            moveToTarget(home);
        } else {
            // Store home location after spawning
            if(!isHomeSet) {
                storeHomeLocation();
                isHomeSet = true;
            }

            // Sense enemy robots
            for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy))
            {
                if (robot.type.canBeExposed())
                {
                    // It's a slanderer... go get them!
                    if (rc.canExpose(robot.location))
                    {
                        System.out.println("e x p o s e d");
                        rc.expose(robot.location);
                        return;
                    }
                }

                // It's a politician
                if (robot.type.canEmpower())
                {
                    rc.setFlag(1);
                    System.out.println("I found a politician and set my flag to 2");
                }

                // It's an enlightenment center
                if (robot.type.canBid())
                {
                    rc.setFlag(4);
                    System.out.println("I found an enemy EC and set my flag to 4");
                }

                // It's a muckraker
                if (robot.type.canExpose())
                {
                    rc.setFlag(3);
                    System.out.println("I found a muckraker and set my flag to 3");
                }
            }

            // Find neutral ECs
            for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, Team.NEUTRAL))
            {
                if (robot.type.canBid())
                {
                    sendLocation(robot.getLocation());
                    System.out.println("I found a neutral EC");
                    goingHome = true;
                }
            }

            // Simple movement and passability check
            if (rc.isReady()) {
                if(tryMove(randomDirection())) {
                    System.out.println("I moved!");
                }
            }
        }


    }
}

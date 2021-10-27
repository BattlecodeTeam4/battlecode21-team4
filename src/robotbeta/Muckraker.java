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
        if(senseRadius == 0)
        {
            updateSenseRadius();
        }

        // Muckraker resets flag if home EC already grabbed its flag
        if(rc.getFlag(homeID) == rc.getFlag(rc.getID())) {
            rc.setFlag(0);
        }

        // Sense neutral robots
        for (RobotInfo robot : rc.senseNearbyRobots(senseRadius))
        {
            if (robot.type.canBid() && ((robot.getTeam() == enemy) || (robot.getTeam() == Team.NEUTRAL)))
            {
                sendLocation(robot.getLocation());
                System.out.println("I found a neutral EC");
            }
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
                System.out.println("I found a politician");
            }

            // It's an enlightenment center
            if (robot.type.canBid())
            {
                sendLocation(robot.getLocation());
                System.out.println("I found an enemy EC");
            }

            // It's a muckraker
            if (robot.type.canExpose())
            {
                System.out.println("I found a muckraker");
            }
        }


        // Simple movement and passability check
        if (rc.isReady()) {
            tryMove(randomDirection());
        }
    }
}

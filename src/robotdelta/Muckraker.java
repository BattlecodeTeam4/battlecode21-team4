package robotdelta;

import battlecode.common.*;

@SuppressWarnings({"RedundantThrows", "unused", "UnusedReturnValue"})
public class Muckraker extends Robot {
    static int lastHoverRoundThresh = 25;
    static int roundSinceLastTarget = 0;

    /**
     * @return int
     * @throws GameActionException
     */
    public static int resetFlag() throws GameActionException {
        // Muckraker resets flag if home EC already grabbed its flag
        if (rc.canGetFlag(homeID)) {
            if (rc.getFlag(homeID) == rc.getFlag(rc.getID())) {
                rc.setFlag(0);
                return 0;
            } else {
                return rc.getFlag(homeID);
            }
        }
        return 0;
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static int scanForTarget() throws GameActionException {
        // Sense neutral robots
        RobotInfo[] targets = rc.senseNearbyRobots(senseRadius);
        for (RobotInfo robot : targets) {
            if (robot.getType().canBid() && ((robot.getTeam() == enemy) || (robot.getTeam() == Team.NEUTRAL))) {
                if (robot.getTeam() == enemy) {
                    sendLocation(robot.getLocation(), 2); // team 2 is enemy
                        target = robot.getLocation();
                } else {
                    sendLocation(robot.getLocation(), 1); // team 1 is neutral
                }
            }
        }

        return targets.length;
    }

    public static int scanAndEmpower() throws GameActionException {
        // Sense enemy robots
        RobotInfo[] targets = rc.senseNearbyRobots(actionRadius, enemy);
        for (RobotInfo robot : targets) {
            if (robot.getType().canBeExposed()) {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.getLocation())) {
                    rc.expose(robot.getLocation());
                }
                if (rc.isReady()) {
                    moveLocation(robot.getLocation());
                }
            }
        }
        return targets.length;
    }

    public static boolean lastTargetMath() throws GameActionException {
        if (roundSinceLastTarget >= lastHoverRoundThresh) {
            roundSinceLastTarget = 0;
            return true;
        } else {
            roundSinceLastTarget += 1;
            return false;
        }
    }

    public static void moveAdjacent() throws GameActionException {
        for(Direction dire : directions)
        {
            MapLocation toTry = rc.getLocation().add(dire);
            if(rc.canSenseLocation(toTry)){
                if(toTry.isAdjacentTo(target) && rc.senseRobotAtLocation(toTry) == null){
                    moveLocation(toTry);
                }
            }
        }
    }

    public static boolean hoverAroundTarget() throws GameActionException {
        if (rc.canSenseLocation(target)) {
            if (rc.senseRobotAtLocation(target).getTeam() != enemy) {
                target = null;
                roundSinceLastTarget = 0;
                moveStraight();
            } else if (!rc.getLocation().isAdjacentTo(target)) {
                findSpot();
            } else if (rc.getLocation().isAdjacentTo(target))
            {
                moveAdjacent();
            }
        } else {
            return moveLocation(target);
        }
        return false;
    }

    public static boolean findSpot() throws GameActionException {
        boolean foundSpot = false;
        if(target != null)
        {
            int senseTotal = 0;
            for (Direction dir : directions) {
                MapLocation toTest = target.add(dir);
                if (rc.canSenseLocation(toTest)) {
                    RobotInfo test = rc.senseRobotAtLocation(toTest);
                    if (test == null) {
                        moveLocation(toTest);
                        foundSpot = true;
                        break;
                    }
                    else if(test.getLocation() == rc.getLocation())
                    {
                        foundSpot = true;
                        break;
                    }
                }
            }
            if (!foundSpot) {
                target = null;
                roundSinceLastTarget = 0;
                moveStraight();
                return false;
            }
        }
        return true;
    }

    public static MapLocation findSlanderer() throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(senseRadius, enemy);
        for (RobotInfo e : enemies){
            if(e.getType().canBeExposed())
            {
                return e.getLocation();
            }
        }
        return null;
    }

    /**
     * @throws GameActionException
     */
    public static void runMuckraker() throws GameActionException {
        resetFlag();
        scanForTarget();
        scanAndEmpower();
        if (target != null) {
            hoverAroundTarget();
        } else if (findSlanderer() != null)
        {
            moveLocation(findSlanderer());
        }
        else moveStraight();
    }
}

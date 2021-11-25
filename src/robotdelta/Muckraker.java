package robotdelta;

import battlecode.common.*;

@SuppressWarnings({"RedundantThrows", "unused", "UnusedReturnValue"})
public class Muckraker extends Robot {
    static int lastHoverRoundThresh = 20;
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
        int targetsCount = 0;
        RobotInfo[] targets = rc.senseNearbyRobots(senseRadius);
        for (RobotInfo robot : targets) {
            if (robot.getType().canBid() && ((robot.getTeam() == enemy) || (robot.getTeam() == Team.NEUTRAL))) {
                if (robot.getTeam() == enemy) {
                    sendLocation(robot.getLocation(), 2); // team 2 is enemy
                    if (lastTargetMath())
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

    public static boolean hoverAroundTarget() throws GameActionException {
        if (rc.canSenseLocation(target)) {
            if (rc.senseRobotAtLocation(target).getTeam() != enemy) {
                target = null;
                roundSinceLastTarget = 0;
                moveStraight();
            } else if (!rc.getLocation().isAdjacentTo(target)) {
                return findSpot();
            }
        } else {
            return moveLocation(target);
        }
        return false;
    }

    public static boolean findSpot() throws GameActionException {
        int senseTotal = 0;
        for (Direction dir : directions) {
            MapLocation toTest = target.add(dir);
            if (rc.canSenseLocation(toTest)) {
                RobotInfo test = rc.senseRobotAtLocation(toTest);
                if (test != null && test.getLocation() != rc.getLocation()) {
                    if (test.getType() == rc.getType() && test.getTeam() == rc.getTeam()) {
                        senseTotal += 1;
                    }
                    moveLocation(toTest);
                }

            }
        }
        if (senseTotal >= 8) {
            target = null;
            roundSinceLastTarget = 0;
            moveStraight();
        }
        return false;
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
        } else moveStraight();
    }
}

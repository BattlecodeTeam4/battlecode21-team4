package robotgamma;

import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

@SuppressWarnings({"RedundantThrows", "unused", "UnusedReturnValue"})
public class Muckraker extends Robot {
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
                sendLocation(robot.getLocation());
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
                    System.out.println("E x p o s i n g . . .");
                    rc.expose(robot.getLocation());
                    System.out.println("E x p o s e d . . .");
                }
                if (rc.isReady()) {
                    moveLocation(robot.getLocation());
                }
            }
        }
        return targets.length;
    }

    /**
     * @throws GameActionException
     */
    public static void runMuckraker() throws GameActionException {
        resetFlag();
        scanForTarget();
        scanAndEmpower();
        moveStraight();
    }
}

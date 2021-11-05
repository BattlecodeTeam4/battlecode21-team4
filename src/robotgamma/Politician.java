package robotgamma;

import battlecode.common.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue"})
public class Politician extends Robot {
    /**
     * @return
     * @throws GameActionException
     */
    public static int resetIfTargetNullAndFlagNotZero() throws GameActionException {
        if (target == null && rc.canGetFlag(rc.getID())) {
            if (rc.getFlag(rc.getID()) != 0) {
                rc.setFlag(0);
            }
        }
        if (rc.canGetFlag(rc.getID())) {
            return rc.getFlag(rc.getID());
        }
        return 0;
    }

    public static MapLocation updateTarget() throws GameActionException {
        if (rc.getFlag(rc.getID()) == 0) {
            if (rc.canGetFlag(homeID)) {
                if (rc.getFlag(homeID) != 0) {
                    target = getLocationFromFlag(rc.getFlag(homeID));
                }
            }
        }
        return target;
    }

    public static void empowerEnemy() throws GameActionException {
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius)) {
            System.out.println("E m p o w e r i n g . . .");
            rc.empower(actionRadius);
            System.out.println("E m p o w e r e d . . .");
        } else {
            System.out.println("A p p r o a c h i n g   T a r g e t . . .");
        }
    }

    public static void targetActions() throws GameActionException {
        if (target != null) {
            if (rc.canSenseLocation(target)) {
                RobotInfo robotTarget = rc.senseRobotAtLocation(target);
                if (robotTarget.getTeam() == rc.getTeam() && robotTarget.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                    sendLocation(target);
                    target = null;
                } else if (robotTarget.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                    empowerEnemy();
                }
            }
        } else {
            empowerEnemy();
        }
    }

    /**
     * @throws GameActionException
     */
    public static void runPolitician() throws GameActionException {
        resetIfTargetNullAndFlagNotZero();
        updateTarget();
        targetActions();
        moveLocation(target);
    }
}

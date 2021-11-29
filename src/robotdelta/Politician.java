package robotdelta;

import battlecode.common.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue"})
public class Politician extends Robot {
    public static int followID;
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

    /**
     * @return
     * @throws GameActionException
     */
    public static MapLocation updateTarget() throws GameActionException {
        if (target == null && targetTeam == 0) {
            if (rc.getFlag(rc.getID()) == 0) {
                if (rc.canGetFlag(homeID)) {
                    if (rc.getFlag(homeID) != 0) {
                        target = getLocationFromFlag(rc.getFlag(homeID));
                        targetTeam = getTeamFromFlag(rc.getFlag(homeID));
                    }
                }
            }
        }
        return target;
    }

    /**
     * @throws GameActionException
     */
    public static void empowerEnemy() throws GameActionException {
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius)) {
            rc.empower(actionRadius);
        }
    }

    /**
     * @throws GameActionException
     */
    public static void targetActions() throws GameActionException {
        if (target != null && targetTeam != 0) {
            if (rc.canSenseLocation(target)) {
                RobotInfo robotTarget = rc.senseRobotAtLocation(target);
                if (robotTarget.getTeam() == rc.getTeam() && robotTarget.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                    sendLocation(target, targetTeam);
                    target = null;
                    targetTeam = 0;
                } else if (robotTarget.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                    empowerEnemy();
                }
            }
        } else {
            empowerEnemy();
        }
    }

    public static void defendHome() throws GameActionException {
        if (homeLoc != null) {
            if (rc.canSenseLocation(homeLoc)) {
                RobotInfo[] list = rc.senseNearbyRobots(actionRadius, enemy);
                if (list.length >= 1) empowerEnemy();
            }
        }
    }

    public static int follow() throws GameActionException {
        RobotInfo[] attackable = rc.senseNearbyRobots(senseRadius, enemy);
        for(RobotInfo a : attackable) {
            if(a.getType() == RobotType.MUCKRAKER || a.getType() == RobotType.POLITICIAN)
            {
                return a.getID();
            }
        }
        return 0;
    }

    /**
     * @throws GameActionException
     */
    public static void runPolitician() throws GameActionException {
        int id = follow();
        MapLocation found;

        defendHome();
        if(id != 0 && rc.canSenseRobot(id))
        {
            found = rc.senseRobot(id).getLocation();
            empowerEnemy();
        }
        else
        {
            found = target;
            resetIfTargetNullAndFlagNotZero();
            targetActions();
            updateTarget();
        }
        moveLocation(found);
    }
}

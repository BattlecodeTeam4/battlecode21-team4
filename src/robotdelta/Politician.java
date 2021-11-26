package robotdelta;

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

    public static void attack() throws GameActionException {
        if(target == null)
        {
            RobotInfo [] enemyList = rc.senseNearbyRobots(senseRadius, enemy);
            for (RobotInfo en : enemyList)
            {
                moveLocation(en.getLocation());
            }
        }
    }

    /**
     * @throws GameActionException
     */
    public static void runPolitician() throws GameActionException {
        defendHome();
        resetIfTargetNullAndFlagNotZero();
        if(rc.getInfluence() >= 100) {
            attack();
            targetActions();
            updateTarget();
        }
        else {
            attack();
            targetActions();
        }
        moveLocation(target);
    }
}

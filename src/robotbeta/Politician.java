package robotbeta;

import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public class Politician extends Robot {
    /**
     * @throws GameActionException
     */
    static void runPolitician() throws GameActionException {
        if(actionRadius == 0)
        {
            updateActionRadius();
        }
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius)) {
            System.out.println("E m p o w e r i n g . . .");
            rc.empower(actionRadius);
            System.out.println("E m p o w e r e d . . .");
            return;
        }

        if (target != null) {
            if(rc.canSenseLocation(target))
            {
                Team curr = rc.senseRobotAtLocation(target).getTeam();
                RobotType currType = rc.senseRobotAtLocation(target).getType();
                if(curr == rc.getTeam() && currType == RobotType.ENLIGHTENMENT_CENTER){
                    rc.setFlag(0);
                    target = null;
                }
            }
        }

        if(target == null && rc.canGetFlag(rc.getID()))
        {
            if(rc.getFlag(rc.getID()) != 0)
            {
                rc.setFlag(0);
            }
        }

        if(rc.getFlag(rc.getID()) == 0)
        {
            if(rc.canGetFlag(homeID)){
                if(rc.getFlag(homeID) != 0) {
                    rc.setFlag(rc.getFlag(homeID));
                    target = getLocationFromFlag(rc.getFlag(rc.getID()));
                }
            }
        }
        moveLocation(target);
    }
}

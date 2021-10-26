package robotbeta;

import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

public class Politician extends Robot {
    static void runPolitician() throws GameActionException {
        if(actionRadius == 0)
        {
            updateActionRadius();
        }
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            rc.empower(actionRadius);
            System.out.println("empowered");
            return;
        }

        if (target != null) {
            if(rc.getLocation().isAdjacentTo(target) && rc.senseRobotAtLocation(target).getTeam() != Team.NEUTRAL)
            {
                rc.setFlag(0);
                target = null;
            }
        }


        if(rc.canGetFlag(homeID)){
            if(rc.getFlag(homeID) != 0) {
                rc.setFlag(rc.getFlag(homeID));
                target = getLocationFromFlag(rc.getFlag(rc.getID()));
            }
        }

        moveLocation(target);
    }
}

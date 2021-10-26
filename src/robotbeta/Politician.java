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

        // Store target location after spawning.
        // Target location comes from home EC flag.
        if(!isTargetSet) {
            for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, rc.getTeam()))
            {
                if (robot.type.canBid() && rc.canGetFlag(robot.ID))
                {
                    target = getLocationFromFlag(rc.getFlag(robot.getID()));
                    System.out.println("I set my target to " + target);
                }
            }
            isTargetSet = true;
        }

        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            rc.empower(actionRadius);
            System.out.println("empowered");
            return;
        }

        if (isTargetSet) {
            moveToTarget(target);
        } else
            tryMove(randomDirection());

    }
}

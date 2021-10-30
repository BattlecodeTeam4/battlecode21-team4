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
        if(target == null && rc.canGetFlag(rc.getID()))
        {
            if(rc.getFlag(rc.getID()) != 0)
            {
                rc.setFlag(0);
            }
        }
        if(actionRadius == 0)
        {
            updateActionRadius();
        }
        if(senseRadius == 0)
        {
            updateSenseRadius();
        }
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);

        if(target != null)
        {
            if(rc.canSenseLocation(target))
            {
                RobotInfo robotTarget = rc.senseRobotAtLocation(target);
                if(robotTarget.getTeam() == rc.getTeam() && robotTarget.getType() == RobotType.ENLIGHTENMENT_CENTER){
                    sendLocation(target);
                    target = null;
                }
                else if(robotTarget.getType() == RobotType.ENLIGHTENMENT_CENTER)
                {
                    if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius))
                    {
                        System.out.println("E m p o w e r i n g . . .");
                        rc.empower(actionRadius);
                        System.out.println("E m p o w e r e d . . .");
                        return;
                    }
                    else
                    {
                        System.out.println("A p p r o a c h i n g   T a r g e t . . .");
                    }
                }
            }
        }

        else if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius)) {
            System.out.println("E m p o w e r i n g . . .");
            rc.empower(actionRadius);
            System.out.println("E m p o w e r e d . . .");
            return;
        }

        if(rc.getFlag(rc.getID()) == 0)
        {
            if(rc.canGetFlag(homeID)){
                if(rc.getFlag(homeID) != 0) {
                    target = getLocationFromFlag(rc.getFlag(homeID));
                }
            }
        }
        moveLocation(target);
    }
}

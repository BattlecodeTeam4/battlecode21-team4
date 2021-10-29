package robotbeta;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

@SuppressWarnings({"RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public class Muckraker extends Robot {

    /**
     * @throws GameActionException
     *
     * Muckraker Flags:
     * 1 - Neutral Enlightenment Centers
     * 2 - Enemy Politician
     * 3 - Enemy Muckraker
     * 4 - Enemy Enlightenment Centers
     *
     * This will later be expanded to communicate location
     * information via flags.
     *
     */
    static void runMuckraker() throws GameActionException
    {
        if(senseRadius == 0)
        {
            updateSenseRadius();
        }
        if(actionRadius == 0)
        {
            updateActionRadius();
        }

        // Muckraker resets flag if home EC already grabbed its flag
        if(rc.canGetFlag(homeID)) {
            if (rc.getFlag(homeID) == rc.getFlag(rc.getID())) {
                rc.setFlag(0);
            }
        }
        else {
            homeID = 0;
            rc.setFlag(0);
        }

        // Sense neutral robots
        for (RobotInfo robot : rc.senseNearbyRobots(senseRadius))
        {
            if (robot.type.canBid() && ((robot.getTeam() == enemy) || (robot.getTeam() == Team.NEUTRAL)))
            {
                sendLocation(robot.getLocation());
            }
        }

        // Sense enemy robots
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.getType().canBeExposed()) {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.getLocation())) {
                    System.out.println("E x p o s i n g . . .");
                    rc.expose(robot.getLocation());
                    System.out.println("E x p o s e d . . .");
                    return;
                }
                else if(rc.isReady())
                {
                    moveLocation(robot.getLocation());
                }
            }
        }
        // Simple movement and passability check
        moveStraight();
    }
}

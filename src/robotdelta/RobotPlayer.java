package robotdelta;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

@SuppressWarnings({"RedundantThrows", "unused"})
public strictfp class RobotPlayer {
    static RobotController rc;
    static int turnCount;
    static boolean robot = true;
    static boolean robotTest = false;

    /**
     * @param rc The RobotController provided to run.
     * @throws GameActionException The Game Action Exception from battlecode.common
     *                             run() is the method that is called when a robot is instantiated in the BattleCode world.
     *                             If this method returns, the robot dies!
     **/
    public static void run(RobotController rc) throws GameActionException {
        RobotPlayer.rc = rc;
        turnCount = 0;

        while (robot) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                RobotType type = rc.getType();
                if (type == RobotType.ENLIGHTENMENT_CENTER) {
                    EnlightenmentCenter.init();
                    EnlightenmentCenter.runEnlightenmentCenter();
                } else if (type == RobotType.POLITICIAN) {
                    Politician.init();
                    Politician.runPolitician();
                } else if (type == RobotType.SLANDERER) {
                    Slanderer.init();
                    Slanderer.runSlanderer();
                } else if (type == RobotType.MUCKRAKER) {
                    Muckraker.init();
                    Muckraker.runMuckraker();
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                if (!robotTest) Clock.yield();

            } catch (Exception e) {
                if (!robotTest) {
                    System.out.println(rc.getType() + " Exception");
                    e.printStackTrace();
                }
            }
            if (robotTest) {
                robot = false;
            }
        }
    }
}

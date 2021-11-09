package robotgamma;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

@SuppressWarnings({"RedundantThrows", "unused", "InfiniteLoopStatement"})
public strictfp class RobotPlayer {
    static RobotController rc;
    static int turnCount;

    /**
     * @param rc The RobotController provided to run.
     * @throws GameActionException The Game Action Exception from battlecode.common
     * run() is the method that is called when a robot is instantiated in the BattleCode world.
     * If this method returns, the robot dies!
     **/
    public static void run(RobotController rc) throws GameActionException {
        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;
        turnCount = 0;

        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                //System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER:
                        EnlightenmentCenter.init();
                        EnlightenmentCenter.runEnlightenmentCenter();
                        break;
                    case POLITICIAN:
                        Politician.init();
                        Politician.runPolitician();
                        break;
                    case SLANDERER:
                        Slanderer.init();
                        Slanderer.runSlanderer();
                        break;
                    case MUCKRAKER:
                        Muckraker.init();
                        Muckraker.runMuckraker();
                        break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }
}

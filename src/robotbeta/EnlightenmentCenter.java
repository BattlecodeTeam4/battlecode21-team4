package robotbeta;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class EnlightenmentCenter extends Robot {

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    static RobotType spawnedRobot = randomSpawnableRobotType();

    static void runEnlightenmentCenter() throws GameActionException {
        //RobotType toBuild = randomSpawnableRobotType();
        //RobotType toBuild = RobotType.POLITICIAN;
        int polInfluence = 50;
        int slaInfluence = 50;
        int muckInfluence = 1;

//        if (rc.canBid(500)) {
//            rc.bid(500);
//        }

        Direction dir = randomDirection();
        switch (spawnedRobot) {
            case MUCKRAKER:
                if (rc.canBuildRobot(RobotType.MUCKRAKER, dir, muckInfluence)) {
                    rc.buildRobot(RobotType.MUCKRAKER, dir, muckInfluence);
                    spawnedRobot = RobotType.POLITICIAN;
                }
                break;
            case POLITICIAN:
                if (rc.canBuildRobot(RobotType.POLITICIAN, dir, polInfluence)) {
                    rc.buildRobot(RobotType.POLITICIAN, dir, polInfluence);
                    spawnedRobot = RobotType.SLANDERER;
                }
                break;
            case SLANDERER:
                if (rc.canBuildRobot(RobotType.SLANDERER, dir, slaInfluence)) {
                    rc.buildRobot(RobotType.SLANDERER, dir, slaInfluence);
                    spawnedRobot = RobotType.MUCKRAKER;
                }
                break;
        }

        if (rc.getInfluence() > 100) {
            rc.bid(rc.getInfluence() - 100);
        }
    }
}

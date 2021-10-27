package robotbeta;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

import java.util.ArrayList;
import java.util.Objects;

public class EnlightenmentCenter extends Robot {

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    static int polInfluence = 35;
    static int slaInfluence = 50;
    static int mucInfluence = 1;

    static int polChance = 50;
    static int slaChance = 25;
    static int mucChance = 25;

    static ArrayList<String> chanceArr = new ArrayList<String>();;

    static ArrayList<Integer> polIDList = new ArrayList<Integer>();
    static ArrayList<Integer> slaIDList = new ArrayList<Integer>();
    static ArrayList<Integer> mucIDList = new ArrayList<Integer>();

    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    static RobotType spawnedRobot = randomSpawnableRobotType();

    static void init() {
        if((polChance + slaChance + mucChance) < 100) {
            System.out.println("Expected Spawn Percentages totaling 100%!");
            return;
        }
        for(int a = 1; a <= polChance; a++) {
            chanceArr.add("pol");
        }
        for(int b = 1; b <= slaChance; b++) {
            chanceArr.add("sla");
        }
        for(int c = 1; c <= mucChance; c++) {
            chanceArr.add("muc");
        }
        if(senseRadius == 0)
        {
            updateSenseRadius();
        }
    }

    static void checkIfExist() {
        mucIDList.removeIf(nxt -> !rc.canGetFlag(nxt));
        polIDList.removeIf(nxt -> !rc.canGetFlag(nxt));
        slaIDList.removeIf(nxt -> !rc.canGetFlag(nxt));
    }

    static void runEnlightenmentCenter() throws GameActionException {
        checkIfExist();

        if(turnCount % 10 == 0) {
            rc.setFlag(0);
        }

        // Scan for new muckraker flags
        for (int id : mucIDList) {
            if (rc.getFlag(id) != 0) {
                rc.setFlag(rc.getFlag(id));
                break;
            }
        }

        int a = (int) (Math.random() * 100);
        if(Objects.equals(chanceArr.get(a), "pol")) {
            spawnedRobot = RobotType.POLITICIAN;
        }
        else if (Objects.equals(chanceArr.get(a), "sla")) {
            spawnedRobot = RobotType.SLANDERER;
        }
        else if (Objects.equals(chanceArr.get(a), "muc")) {
            spawnedRobot = RobotType.MUCKRAKER;
        }
        Direction dir = randomDirection();
        switch (spawnedRobot) {
            case MUCKRAKER:
                if (rc.canBuildRobot(RobotType.MUCKRAKER, dir, mucInfluence)) {
                    rc.buildRobot(RobotType.MUCKRAKER, dir, mucInfluence);
                    mucIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                }
                break;
            case POLITICIAN:
                if (rc.canBuildRobot(RobotType.POLITICIAN, dir, polInfluence)) {
                    rc.buildRobot(RobotType.POLITICIAN, dir, polInfluence);
                    polIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                }
                break;
            case SLANDERER:
                if (rc.canBuildRobot(RobotType.SLANDERER, dir, slaInfluence)) {
                    rc.buildRobot(RobotType.SLANDERER, dir, slaInfluence);
                    slaIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                }
                break;
        }
        if (rc.getInfluence() > 100) {
            rc.bid(rc.getInfluence() - 100);
        }
        else if(rc.getInfluence() > 50){
            rc.bid(1);
        }
    }
}

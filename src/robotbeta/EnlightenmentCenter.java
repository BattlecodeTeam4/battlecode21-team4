package robotbeta;

import battlecode.common.*;

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

    static int polChance = 20;
    static int slaChance = 50;
    static int mucChance = 30;

    static int threshold = 100;
    static double bidThreshold;

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

    static void bidByThreshold(double thresh) throws GameActionException {
        int bid = (int)(rc.getInfluence() * thresh);
        if(bid < 1) bid = 1;
        if (rc.canBid(bid)){
            rc.bid(bid);
        }
    }

    static void spawnRandom (int mucCha, int polCha, int slaCha,
                             int mucInf, int polInf, int slaInf) throws GameActionException {
        chanceArr.clear();
        if((mucCha + polCha + slaCha) < 100) {
            System.out.println("Expected Spawn Percentages totaling 100%!");
            return;
        }
        for(int c = 1; c <= mucCha; c++) {
            chanceArr.add("muc");
        }
        for(int a = 1; a <= polCha; a++) {
            chanceArr.add("pol");
        }
        for(int b = 1; b <= slaCha; b++) {
            chanceArr.add("sla");
        }

        int a = (int) (Math.random() * 100);
        if (Objects.equals(chanceArr.get(a), "pol")) {
            spawnedRobot = RobotType.POLITICIAN;
        } else if (Objects.equals(chanceArr.get(a), "sla")) {
            spawnedRobot = RobotType.SLANDERER;
        } else if (Objects.equals(chanceArr.get(a), "muc")) {
            spawnedRobot = RobotType.MUCKRAKER;
        }
        Direction dir = randomDirection();
        switch (spawnedRobot) {
            case MUCKRAKER:
                if (rc.canBuildRobot(RobotType.MUCKRAKER, dir, mucInf)) {
                    rc.buildRobot(RobotType.MUCKRAKER, dir, mucInf);
                    mucIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                }
                break;
            case POLITICIAN:
                if (rc.canBuildRobot(RobotType.POLITICIAN, dir, polInf)) {
                    rc.buildRobot(RobotType.POLITICIAN, dir, polInf);
                    polIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                }
                break;
            case SLANDERER:
                if (rc.canBuildRobot(RobotType.SLANDERER, dir, slaInf)) {
                    rc.buildRobot(RobotType.SLANDERER, dir, slaInf);
                    slaIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                }
                break;
        }
    }

    static void runEnlightenmentCenter() throws GameActionException {
        checkIfExist();
        bidThreshold = 0.005;

        if(turnCount % 50 == 0) {
            rc.setFlag(0);
        }

        // Scan for new muckraker flags
        for (int id : mucIDList) {
            if (rc.getFlag(id) != 0) {
                rc.setFlag(rc.getFlag(id));
                break;
            }
        }
        RobotInfo[] near = rc.senseNearbyRobots(senseRadius, enemy);
        for(RobotInfo r : near)
        {
            if(r.getType() == RobotType.ENLIGHTENMENT_CENTER)
            {
                int muc = 35;
                int pol = 30;
                int sla = 35;
                spawnRandom(muc, pol, sla, mucInfluence, 10, 10);
                break;
            }
        }
        if(rc.getInfluence() >= threshold) {
            int influence = (int) (rc.getInfluence() * 0.50);
            int muc = mucChance;
            int pol = polChance;
            int sla = slaChance;
            if(rc.getFlag(rc.getID()) != 0)
            {
                pol += 20;
                sla -= 20;
            }
            spawnRandom(muc, pol, sla, mucInfluence, influence, influence);
            bidByThreshold(bidThreshold);
        }
        bidByThreshold(bidThreshold);
    }
}

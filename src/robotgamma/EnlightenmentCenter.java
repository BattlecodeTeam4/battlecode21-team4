package robotgamma;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

import java.util.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public class EnlightenmentCenter extends Robot {
    static final RobotType[] spawnableRobot = {RobotType.POLITICIAN, RobotType.SLANDERER, RobotType.MUCKRAKER,};

    static int polInfluence = 35;
    static int slaInfluence = 50;
    static int mucInfluence = 1;

    static int polChance = 30;
    static int slaChance = 50;
    static int mucChance = 20;

    static int threshold = 100;
    static double bidThreshold = 0.005;
    static int defaultInfGive = 0;

    static int currPolChance = 0;
    static int currSlaChance = 0;
    static int currMucChance = 0;

    static ArrayList<String> chanceArr = null;

    static Set<Integer> polIDList = null;
    static Set<Integer> slaIDList = null;
    static Set<Integer> mucIDList = null;

    static LinkedList<Integer> targetList = null;

    /**
     * @return RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    /**
     * @throws GameActionException
     */
    public static void init() throws GameActionException {
        if (actionRadius == 0) updateActionRadius();
        if (senseRadius == 0) updateSenseRadius();
        if (detectRadius == 0) updateDetectRadius();
        if (enemy == null) enemy = rc.getTeam().opponent();
        if (polIDList == null) polIDList = new HashSet<>();
        if (slaIDList == null) slaIDList = new HashSet<>();
        if (mucIDList == null) mucIDList = new HashSet<>();
        if (chanceArr == null) chanceArr = new ArrayList<>();
        if (targetList == null) targetList = new LinkedList<>();
        influence = rc.getInfluence();
        defaultInfGive = (int) (influence * 0.50);
        currRound = rc.getRoundNum();
        conviction = rc.getConviction();
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static int checkIfExistMuckraker() throws GameActionException {
        mucIDList.removeIf(muc -> !rc.canGetFlag(muc));
        return mucIDList.size();
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static int checkIfExistPolitician() throws GameActionException {
        polIDList.removeIf(pol -> !rc.canGetFlag(pol));
        return polIDList.size();
    }


    /**
     * @return
     * @throws GameActionException
     */
    public static int checkIfExistSlanderer() throws GameActionException {
        Iterator<Integer> sla = slaIDList.iterator();
        while (sla.hasNext()) {
            int id = sla.next();
            if (rc.canGetFlag(id)) {
                if (rc.getFlag(id) >= slaThreshold) {
                    polIDList.add(id);
                    sla.remove();
                }
            } else if (!rc.canGetFlag(id)) {
                sla.remove();
            }
        }
        return slaIDList.size();
    }

    public static RobotType spawnRobot(int mucCha, int polCha, int slaCha) throws GameActionException {
        if (mucCha != currMucChance || polCha != currPolChance || slaCha != currSlaChance) {
            if ((mucCha + polCha + slaCha) != 100) {
                System.out.println("Expected Spawn Percentages totaling 100%!");
                return null;
            } else {
                chanceArr.clear();
            }
            for (int c = 1; c <= mucCha; c++) {
                chanceArr.add("muc");
            }
            for (int a = 1; a <= polCha; a++) {
                chanceArr.add("pol");
            }
            for (int b = 1; b <= slaCha; b++) {
                chanceArr.add("sla");
            }
            currMucChance = mucCha;
            currPolChance = polCha;
            currSlaChance = slaCha;
        }

        RobotType spawnedRobot = null;
        int a = (int) (Math.random() * 100);
        if (Objects.equals(chanceArr.get(a), "pol")) {
            spawnedRobot = RobotType.POLITICIAN;
        } else if (Objects.equals(chanceArr.get(a), "sla")) {
            spawnedRobot = RobotType.SLANDERER;
        } else if (Objects.equals(chanceArr.get(a), "muc")) {
            spawnedRobot = RobotType.MUCKRAKER;
        }
        return spawnedRobot;
    }

    /**
     * @param mucInf
     * @param polInf
     * @param slaInf
     * @return RobotType
     * @throws GameActionException
     */
    public static RobotType buildRobot(RobotType toBuild, int mucInf, int polInf, int slaInf) throws GameActionException {
        Direction dir = randomDirection();
        if (dir != null) {
            switch (Objects.requireNonNull(toBuild)) {
                case MUCKRAKER:
                    if (rc.canBuildRobot(RobotType.MUCKRAKER, dir, mucInf)) {
                        rc.buildRobot(RobotType.MUCKRAKER, dir, mucInf);
                        mucIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                    }
                    else {
                        toBuild = null;
                    }
                    break;
                case POLITICIAN:
                    if (rc.canBuildRobot(RobotType.POLITICIAN, dir, polInf)) {
                        rc.buildRobot(RobotType.POLITICIAN, dir, polInf);
                        polIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                    }
                    else {
                        toBuild = null;
                    }
                    break;
                case SLANDERER:
                    if (rc.canBuildRobot(RobotType.SLANDERER, dir, slaInf)) {
                        rc.buildRobot(RobotType.SLANDERER, dir, slaInf);
                        slaIDList.add(rc.senseRobotAtLocation(rc.adjacentLocation(dir)).getID());
                    }
                    else {
                        toBuild = null;
                    }
                    break;
            }
        }
        if(dir == null)
        {
            toBuild = null;
        }
        return toBuild;
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static int mucScan() throws GameActionException {
        // Scan for new muckraker flags
        // and add to target list if new
        for (int id : mucIDList) {
            if (rc.canGetFlag(id)) {
                int newTarget = rc.getFlag(id);
                if (newTarget != 0) {
                    addTargetToTargetList(newTarget);
                }
            }
        }
        return mucIDList.size();
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static int polScan() throws GameActionException {
        // Scan for new politicians flags
        // and remove from target list if match exists
        for (int id : polIDList) {
            if (rc.canGetFlag(id)) {
                int oldTarget = rc.getFlag(id);
                if (oldTarget != 0 && targetList.contains(oldTarget)) {
                    targetList.removeFirstOccurrence(oldTarget);
                }
            }
        }
        return polIDList.size();
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static int setFlagFromTargetList() throws GameActionException {
        // Set flag to first target in targetList
        if (!targetList.isEmpty()) {
            rc.setFlag(targetList.getFirst());
            return targetList.getFirst();
        } else {
            rc.setFlag(0);
            return 0;
        }
    }

    /**
     * @return enemyInf
     * @throws GameActionException
     */
    public static int senseNearEC() throws GameActionException {
        int enemyInf = 0;
        RobotInfo[] near = rc.senseNearbyRobots(senseRadius, enemy);
        for (RobotInfo r : near) {
            if (r.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                enemyInf = r.getInfluence();
                break;
            }
        }
        return enemyInf;
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static boolean bidByThreshold() throws GameActionException {
        if(currRound <= 100)
        {
            bidThreshold = 0.005;
        }
        else if (currRound <= 400)
        {
            bidThreshold = 0.010;
        }
        else if(currRound <= 750) {
            bidThreshold = 0.0275;
        }
        else if (currRound <= 1000){
            bidThreshold = 0.05;
        }
        else if (currRound <= 1500){
            bidThreshold = 0.075;
        }
        int bid = (int) (rc.getInfluence() * bidThreshold);
        if(bid < 1) bid = 1;
        if (rc.canBid(bid)) {
            rc.bid(bid);
            return true;
        }
        return false;
    }

    /**
     * @throws GameActionException
     */
    public static void cleanData() throws GameActionException {
        if (turnCount % 5 == 0) checkIfExistSlanderer();
        if (turnCount % 50 == 0) checkIfExistPolitician();
        if (turnCount % 100 == 0) checkIfExistMuckraker();
    }

    /**
     * @return
     * @throws GameActionException
     */
    public static RobotType setupProfile() throws GameActionException {
        RobotType toBuild;
        if (influence >= threshold) {
            toBuild = spawnRobot(50, 10, 40);
            return buildRobot(toBuild, mucInfluence, defaultInfGive, defaultInfGive);
        }
        return null;
    }

    public static RobotType nearbyEnemyECProfile(int enemyInfluence) throws GameActionException {
        RobotType toBuild;
        int muc = 50;
        int pol = 50;
        int sla = 0;
        int polInf = 10;
        if (influence >= (enemyInfluence + 50)) {
            polInf = (enemyInfluence + 25);
            pol = 100;
            muc = 0;
        }
        toBuild = spawnRobot(muc, pol, sla);
        return buildRobot(toBuild, mucInfluence, polInf, slaInfluence);
    }

    public static RobotType defaultProfile() throws GameActionException {
        RobotType toBuild;
        if (influence >= threshold) {
            int muc = mucChance;
            int pol = polChance;
            int sla = slaChance;
            if (rc.getFlag(rc.getID()) != 0) {
                pol += 20;
                sla -= 20;
            }
            toBuild = spawnRobot(muc, pol, sla);
            return buildRobot(toBuild, mucInfluence, defaultInfGive, defaultInfGive);
        }
        return null;
    }

    /**
     * @throws GameActionException
     */
    public static void runEnlightenmentCenter() throws GameActionException {
        cleanData();
        mucScan();
        polScan();

        setFlagFromTargetList();

        int nearbyEnemyEC = senseNearEC();

        if (nearbyEnemyEC > 0) {
            nearbyEnemyECProfile(nearbyEnemyEC);
        } else if (nearbyEnemyEC == 0 && currRound < 250) {
            setupProfile();
        } else if (nearbyEnemyEC == 0 && currRound <= 1500) {
            defaultProfile();
        }
        bidByThreshold();
    }

    /**
     * @throws GameActionException
     */
    public static void addTargetToTargetList(int newTarget) throws GameActionException {
        int newTargetTeam = getTeamFromFlag(newTarget);

        if (targetList.isEmpty()) {
            targetList.addFirst(newTarget);
        } else if (!targetList.contains(newTarget)) {
            if (newTargetTeam == 2) { // enemy
                targetList.addLast(newTarget);
            } else { // neutral (1)
                ListIterator<Integer> iter = targetList.listIterator();
                while (iter.hasNext()) {
                    if (getTeamFromFlag(iter.next()) == 2) {
                        targetList.add(iter.previousIndex(), newTarget);
                        break;
                    } else if (!iter.hasNext()) {
                        iter.add(newTarget);
                        break;
                    }
                }
            }
        }
    }
}

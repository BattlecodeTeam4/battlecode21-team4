package robotbeta;

import java.util.*;

import battlecode.common.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public class EnlightenmentCenter extends Robot {

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    static int polInfluence = 35;
    static int slaInfluence = 50;
    static int mucInfluence = 1;

    static int polChance = 30;
    static int slaChance = 50;
    static int mucChance = 20;

    static int threshold = 100;
    static double bidThreshold;

    static int currPolChance = 0;
    static int currSlaChance = 0;
    static int currMucChance = 0;

    static ArrayList<String> chanceArr = new ArrayList<>();

    static Set<Integer> polIDList = new HashSet<>();
    static Set<Integer> slaIDList = new HashSet<>();
    static Set<Integer> mucIDList = new HashSet<>();

    static LinkedList<Integer> targetList = new LinkedList<>();

    /**
     * @return
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    static RobotType spawnedRobot = randomSpawnableRobotType();

    /**
     *
     */
    static void init() {
        if(senseRadius == 0)
        {
            updateSenseRadius();
        }
    }

    /**
     * @throws GameActionException
     */
    static void checkIfExistMuckraker() throws GameActionException {
        mucIDList.removeIf(muc -> !rc.canGetFlag(muc));
    }

    /**
     * @throws GameActionException
     */
    static void checkIfExistPolitician() throws GameActionException {
        polIDList.removeIf(pol -> !rc.canGetFlag(pol));
    }

    /**
     * @throws GameActionException
     */
    static void checkIfExistSlanderer() throws GameActionException {
        Iterator<Integer> sla = slaIDList.iterator();
        while(sla.hasNext())
        {
            int id = sla.next();
            if(rc.canGetFlag(id))
            {
                if(rc.getFlag(id) >= slaThreshold)
                {
                    polIDList.add(id);
                    sla.remove();
                }
            }
            else if(!rc.canGetFlag(id)){
                sla.remove();
            }
        }
    }

    /**
     * @param thresh
     * @throws GameActionException
     */
    static void bidByThreshold(double thresh) throws GameActionException {
        int bid = (int)(rc.getInfluence() * thresh);
        if(bid < 1) bid = 1;
        if (rc.canBid(bid)){
            rc.bid(bid);
        }
    }

    /**
     * @param mucCha
     * @param polCha
     * @param slaCha
     * @param mucInf
     * @param polInf
     * @param slaInf
     * @throws GameActionException
     */
    static void spawnRandom (int mucCha, int polCha, int slaCha,
                             int mucInf, int polInf, int slaInf) throws GameActionException {
        if(mucCha != currMucChance || polCha != currPolChance || slaCha != currSlaChance)
        {
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
            currMucChance = mucCha;
            currPolChance = polCha;
            currSlaChance = slaCha;
            System.out.println("M: " + currMucChance + " P: " + currPolChance + " S: " + currSlaChance);
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

    /**
     * @throws GameActionException
     */
    static void runEnlightenmentCenter() throws GameActionException {
        if(turnCount % 5 == 0)
        {
            checkIfExistSlanderer();
        }
        if(turnCount % 50 == 0)
        {
            checkIfExistPolitician();
        }
        if(turnCount % 100 == 0)
        {
            checkIfExistMuckraker();
        }
        bidThreshold = 0.005;

        // Scan for new muckraker flags
        // and add to target list if new
        for (int id : mucIDList)
        {
            if(rc.canGetFlag(id))
            {
                int newTarget = rc.getFlag(id);
                if (newTarget != 0 && !targetList.contains(newTarget))
                {
                    targetList.addLast(newTarget);
                }
            }
        }

        // Scan for new politicians flags
        // and remove from target list if match exists
        for (int id : polIDList) {
            if(rc.canGetFlag(id)) {
                int oldTarget = rc.getFlag(id);
                if (oldTarget != 0 && targetList.contains(oldTarget)) {
                    targetList.removeFirstOccurrence(oldTarget);
                }
            }
        }

        // Set flag to first target in targetList
        if (!targetList.isEmpty())
        {
            rc.setFlag(targetList.getFirst());
        } else {
            rc.setFlag(0);
        }


        MapLocation attackEC = null;
        RobotInfo[] near = rc.senseNearbyRobots(senseRadius, enemy);
        for(RobotInfo r : near)
        {
            if(r.getType() == RobotType.ENLIGHTENMENT_CENTER)
            {
                attackEC = r.getLocation();
                int goForKill = r.getInfluence();
                int muc = 50;
                int pol = 50;
                int sla = 0;
                int polInf = 10;
                if(rc.getInfluence() >= (r.getInfluence() + 50))
                {
                    polInf = (r.getInfluence() + 25);
                    pol = 100;
                    muc = 0;
                }
                spawnRandom(muc, pol, sla, mucInfluence, polInf, 10);
                break;
            }
        }

        boolean ourTeam = true;
        if(attackEC != null)
        {
            if(rc.canSenseLocation(attackEC))
            {
                if(rc.getTeam() != rc.senseRobotAtLocation(attackEC).getTeam())
                    ourTeam = false;
            }
        }

        if(rc.getInfluence() >= threshold && ourTeam) {
            int influence = (int) (rc.getInfluence() * 0.50);
            if(rc.getRoundNum() <= 250) spawnRandom(50, 10, 40, mucInfluence, influence, influence);
            else{
                int muc = mucChance;
                int pol = polChance;
                int sla = slaChance;
                if(rc.getFlag(rc.getID()) != 0)
                {
                    pol += 20;
                    sla -= 20;
                }
                spawnRandom(muc, pol, sla, mucInfluence, influence, influence);
            }
            if(rc.getRoundNum() >= 750) bidThreshold = 0.05;
            bidByThreshold(bidThreshold);

        }
        if(rc.getRoundNum() >= 750) bidThreshold = 0.05;
        bidByThreshold(bidThreshold);
    }
}

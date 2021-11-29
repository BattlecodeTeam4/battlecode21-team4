package robotdelta;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnlightenmentCenterTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);

    @Test
    public void randomSpawnableRobotType() {
        RobotType rob = EnlightenmentCenter.randomSpawnableRobotType();
        Assert.assertNotNull(rob);
    }

    @Test
    public void initTest() throws GameActionException {
        Robot.rc = mockRC;

        Robot.actionRadius = 0;
        Robot.senseRadius = 0;
        Robot.detectRadius = 0;
        when(mockRC.getType()).thenReturn(RobotType.ENLIGHTENMENT_CENTER);

        Robot.enemy = null;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getTeam().opponent()).thenReturn(Team.B);

        EnlightenmentCenter.slaIDList = null;
        EnlightenmentCenter.polIDList = null;
        EnlightenmentCenter.mucIDList = null;

        when(mockRC.getInfluence()).thenReturn(100);
        when(mockRC.getRoundNum()).thenReturn(100);
        when(mockRC.getConviction()).thenReturn(100);

        EnlightenmentCenter.init();
    }

    @Test
    public void checkIfExistMuckraker() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList.add(200);
        when(mockRC.canGetFlag(200)).thenReturn(true);
        Assert.assertEquals(1, EnlightenmentCenter.checkIfExistMuckraker());
    }

    @Test
    public void checkIfExistPolitician() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.polIDList.add(200);
        EnlightenmentCenter.polIDList.add(300);
        when(mockRC.canGetFlag(200)).thenReturn(true);
        when(mockRC.canGetFlag(300)).thenReturn(true);
        Assert.assertEquals(2, EnlightenmentCenter.checkIfExistPolitician());
    }

    @Test
    public void checkIfExistSlandererRmSlanderer() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.slaThreshold = 290;
        EnlightenmentCenter.slaIDList.add(295);
        when(mockRC.canGetFlag(295)).thenReturn(false);
        when(mockRC.getFlag(295)).thenReturn(295);
        Assert.assertEquals(0, EnlightenmentCenter.checkIfExistSlanderer());
    }

    @Test
    public void checkIfExistSlandererNot() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaThreshold = 290;
        EnlightenmentCenter.slaIDList.add(295);
        when(mockRC.canGetFlag(295)).thenReturn(true);
        when(mockRC.getFlag(295)).thenReturn(295);
        Assert.assertEquals(0, EnlightenmentCenter.checkIfExistSlanderer());
        Assert.assertEquals(1, EnlightenmentCenter.checkIfExistPolitician());
    }

    @Test
    public void checkIfExistSlandererYes() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.slaThreshold = 290;
        EnlightenmentCenter.slaIDList.add(285);
        when(mockRC.canGetFlag(285)).thenReturn(true);
        when(mockRC.getFlag(285)).thenReturn(285);
        Assert.assertEquals(1, EnlightenmentCenter.checkIfExistSlanderer());
    }

    @Test
    public void spawnRobotNot100Test() throws GameActionException {
        Assert.assertNull(EnlightenmentCenter.spawnRobot(50, 50, 50));
    }

    @Test
    public void spawnRobotMucTest() throws GameActionException {
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        Assert.assertEquals(RobotType.MUCKRAKER, EnlightenmentCenter.spawnRobot(100, 0, 0));
    }

    @Test
    public void spawnRobotPolTest() throws GameActionException {
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        Assert.assertEquals(RobotType.POLITICIAN, EnlightenmentCenter.spawnRobot(0, 100, 0));
    }

    @Test
    public void spawnRobotSlaTest() throws GameActionException {
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        Assert.assertEquals(RobotType.SLANDERER, EnlightenmentCenter.spawnRobot(0, 0, 100));
    }

    @Test
    public void createRobotNoDirection() throws GameActionException {
        when(mockRC.getInfluence()).thenReturn(100);
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.MUCKRAKER, d, 10)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(1,
                    Team.A, RobotType.MUCKRAKER, 10, 1, new MapLocation(102, 102)));
        }

        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.mucIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.MUCKRAKER, null, 10, 0, 0);
        Assert.assertNull(test);
    }

    @Test
    public void createMuckraker() throws GameActionException {
        when(mockRC.getInfluence()).thenReturn(100);
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.MUCKRAKER, d, 10)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(1,
                    Team.A, RobotType.MUCKRAKER, 10, 1, new MapLocation(102, 102)));
        }

        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.mucIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.MUCKRAKER, Direction.NORTH, 10, 0, 0);
        Assert.assertEquals(RobotType.MUCKRAKER, test);
    }

    @Test
    public void createMuckrakerNot() throws GameActionException {
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.MUCKRAKER, d, 35)).thenReturn(false);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103)));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.MUCKRAKER, Direction.NORTH, 0, 0, 0);
        Assert.assertNull(test);
    }

    @Test
    public void createPolitician() throws GameActionException {
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.POLITICIAN, d, 35)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103)));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.POLITICIAN, Direction.NORTH, 0, 35, 0);
        Assert.assertEquals(RobotType.POLITICIAN, test);
    }

    @Test
    public void createPoliticianNot() throws GameActionException {
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.POLITICIAN, d, 35)).thenReturn(false);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103)));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.POLITICIAN, Direction.NORTH, 0, 0, 0);
        Assert.assertNull(test);
    }

    @Test
    public void createSlanderer() throws GameActionException {
        when(mockRC.getInfluence()).thenReturn(100);
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.SLANDERER, d, 50)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(0, Team.A, RobotType.SLANDERER, 50, 1, new MapLocation(101, 101)));
        }

        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.SLANDERER, Direction.NORTH, 0, 0, 50);
        Assert.assertEquals(RobotType.SLANDERER, test);
    }

    @Test
    public void createSlandererNot() throws GameActionException {
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.SLANDERER, d, 35)).thenReturn(false);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103)));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.SLANDERER, Direction.NORTH, 0, 0, 0);
        Assert.assertNull(test);
    }

    @Test
    public void setFlagFromTargetListEmptyTest() throws GameActionException {
        EnlightenmentCenter.targetList = new LinkedList<>();
        Assert.assertEquals(0, EnlightenmentCenter.setFlagFromTargetList());
    }

    @Test
    public void setFlagFromTargetListEntryTest() throws GameActionException {
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.targetList.add(100);
        Assert.assertEquals(100, EnlightenmentCenter.setFlagFromTargetList());
    }

    @Test
    public void senseNearECTest() throws GameActionException {
        EnlightenmentCenter.senseRadius = 40;
        Muckraker.enemy = Team.B;
        RobotInfo fakeEnemy = new RobotInfo(100, Team.B, RobotType.ENLIGHTENMENT_CENTER,
                100, 100, new MapLocation(100, 100));
        when(mockRC.senseNearbyRobots(EnlightenmentCenter.senseRadius, Team.B))
                .thenReturn(new RobotInfo[]{fakeEnemy});
        EnlightenmentCenter.rc = mockRC;
        Assert.assertEquals(100, EnlightenmentCenter.senseNearEC());
    }

    @Test
    public void bidByThresholdNot() throws GameActionException {
        EnlightenmentCenter.currRound = 100;
        when(mockRC.getInfluence()).thenReturn(1);
        when(mockRC.canBid(1)).thenReturn(false);
        EnlightenmentCenter.rc = mockRC;
        Assert.assertFalse(EnlightenmentCenter.bidByThreshold());

    }

    @Test
    public void bidByThreshold100() throws GameActionException {
        EnlightenmentCenter.currRound = 100;
        when(mockRC.getInfluence()).thenReturn(1);
        when(mockRC.canBid(1)).thenReturn(true);
        EnlightenmentCenter.rc = mockRC;
        Assert.assertTrue(EnlightenmentCenter.bidByThreshold());
    }

    @Test
    public void bidByThreshold400() throws GameActionException {
        EnlightenmentCenter.currRound = 400;
        when(mockRC.getInfluence()).thenReturn(1);
        when(mockRC.canBid(1)).thenReturn(true);
        EnlightenmentCenter.rc = mockRC;
        Assert.assertTrue(EnlightenmentCenter.bidByThreshold());
    }

    @Test
    public void bidByThreshold750() throws GameActionException {
        EnlightenmentCenter.currRound = 750;
        when(mockRC.getInfluence()).thenReturn(1);
        when(mockRC.canBid(1)).thenReturn(true);
        EnlightenmentCenter.rc = mockRC;
        Assert.assertTrue(EnlightenmentCenter.bidByThreshold());
    }

    @Test
    public void bidByThreshold1000() throws GameActionException {
        EnlightenmentCenter.currRound = 1000;
        when(mockRC.getInfluence()).thenReturn(1);
        when(mockRC.canBid(1)).thenReturn(true);
        EnlightenmentCenter.rc = mockRC;
        Assert.assertTrue(EnlightenmentCenter.bidByThreshold());
    }

    @Test
    public void bidByThreshold1500() throws GameActionException {
        EnlightenmentCenter.currRound = 1500;
        when(mockRC.getInfluence()).thenReturn(1);
        when(mockRC.canBid(1)).thenReturn(true);
        EnlightenmentCenter.rc = mockRC;
        Assert.assertTrue(EnlightenmentCenter.bidByThreshold());
    }

    @Test
    public void bidByThresholdLargeInfluence() throws GameActionException {
        EnlightenmentCenter.currRound = 1500;
        when(mockRC.getInfluence()).thenReturn(1000);
        when(mockRC.canBid(75)).thenReturn(true);
        EnlightenmentCenter.rc = mockRC;
        //Assert.assertTrue(EnlightenmentCenter.bidByThreshold());
    }

    @Test
    public void cleanDataTurn1Test() throws GameActionException {
        EnlightenmentCenter.turnCount = 1;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.cleanData();
    }

    @Test
    public void cleanDataTurn5Test() throws GameActionException {
        EnlightenmentCenter.turnCount = 5;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.cleanData();
    }

    @Test
    public void cleanDataTurn50Test() throws GameActionException {
        EnlightenmentCenter.turnCount = 50;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.cleanData();
    }

    @Test
    public void cleanDataTurn100Test() throws GameActionException {
        EnlightenmentCenter.turnCount = 100;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.cleanData();
    }

    @Test
    public void setupProfileTestNull() throws GameActionException {
        EnlightenmentCenter.influence = 50;
        Assert.assertNull(EnlightenmentCenter.setupProfile());
    }

    @Test
    public void setupProfileTestN() throws GameActionException {
        EnlightenmentCenter.influence = 1000;
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        //Assert.assertNotNull(EnlightenmentCenter.setupProfile());
    }

    @Test
    public void nearbyEnemyECProfileTest() throws GameActionException {
        EnlightenmentCenter.influence = 1000;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        Assert.assertNull(EnlightenmentCenter.nearbyEnemyECProfile(500));
    }

    @Test
    public void defaultProfileTest() throws GameActionException {
        EnlightenmentCenter.influence = 1000;
        when(mockRC.getID()).thenReturn(100);
        when(mockRC.getFlag(100)).thenReturn(100);
        EnlightenmentCenter.rc = mockRC;
        Assert.assertNull(EnlightenmentCenter.defaultProfile());
    }

    @Test
    public void defaultProfileNullTest() throws GameActionException {
        EnlightenmentCenter.influence = 10;
        Assert.assertNull(EnlightenmentCenter.defaultProfile());
    }

    @Test
    public void balanceProfile2DefaultTest() throws GameActionException {
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            EnlightenmentCenter.mucIDList.add(i);
        }
        for (int i = 0; i < 60; i++) {
            EnlightenmentCenter.polIDList.add(i);
        }
        for (int i = 0; i < 60; i++) {
            EnlightenmentCenter.slaIDList.add(i);
        }
        EnlightenmentCenter.balanceProfile();
    }

    @Test
    public void balanceProfilePolSlaTest() throws GameActionException {
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            EnlightenmentCenter.mucIDList.add(i);
        }
        for (int i = 0; i < 40; i++) {
            EnlightenmentCenter.polIDList.add(i);
        }
        for (int i = 0; i < 40; i++) {
            EnlightenmentCenter.slaIDList.add(i);
        }
        EnlightenmentCenter.balanceProfile();
    }

    @Test
    public void endGameProfilePolSlaTest() throws GameActionException {
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            EnlightenmentCenter.mucIDList.add(i);
        }
        for (int i = 0; i < 40; i++) {
            EnlightenmentCenter.polIDList.add(i);
        }
        for (int i = 0; i < 40; i++) {
            EnlightenmentCenter.slaIDList.add(i);
        }
        EnlightenmentCenter.endGameProfile();
    }

    @Test
    public void endGameProfilePolSlaGreaterTest() throws GameActionException {
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            EnlightenmentCenter.mucIDList.add(i);
        }
        for (int i = 0; i < 60; i++) {
            EnlightenmentCenter.polIDList.add(i);
        }
        for (int i = 0; i < 60; i++) {
            EnlightenmentCenter.slaIDList.add(i);
        }
        EnlightenmentCenter.endGameProfile();
    }

    @Test
    public void runEnlightenmentCenterTest() throws GameActionException {
        EnlightenmentCenter.turnCount = 100;
        EnlightenmentCenter.currRound = 100;
        EnlightenmentCenter.senseRadius = 40;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        EnlightenmentCenter.currMucChance = 0;
        EnlightenmentCenter.currPolChance = 0;
        EnlightenmentCenter.currSlaChance = 0;
        EnlightenmentCenter.influence = 1000;
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(EnlightenmentCenter.senseRadius, Team.B)).thenReturn(new RobotInfo[]{});
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.runEnlightenmentCenter();
    }

    @Test
    public void runEnlightenmentCenter250PlusTest() throws GameActionException {
        EnlightenmentCenter.turnCount = 100;
        EnlightenmentCenter.currRound = 500;
        EnlightenmentCenter.senseRadius = 40;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(EnlightenmentCenter.senseRadius, Team.B)).thenReturn(new RobotInfo[]{});
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.runEnlightenmentCenter();
    }

    @Test
    public void runEnlightenmentCenter500PlusPlusTest() throws GameActionException {
        EnlightenmentCenter.turnCount = 100;
        EnlightenmentCenter.currRound = 750;
        EnlightenmentCenter.senseRadius = 40;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(EnlightenmentCenter.senseRadius, Team.B)).thenReturn(new RobotInfo[]{});
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.runEnlightenmentCenter();
    }

    @Test
    public void runEnlightenmentCenter1000PlusPlusTest() throws GameActionException {
        EnlightenmentCenter.turnCount = 100;
        EnlightenmentCenter.currRound = 1250;
        EnlightenmentCenter.senseRadius = 40;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(EnlightenmentCenter.senseRadius, Team.B)).thenReturn(new RobotInfo[]{});
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.runEnlightenmentCenter();
    }

    @Test
    public void runEnlightenmentCenterNearEnemyECTest() throws GameActionException {
        EnlightenmentCenter.turnCount = 100;
        EnlightenmentCenter.currRound = 500;
        EnlightenmentCenter.senseRadius = 40;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.chanceArr = new ArrayList<>();
        Muckraker.enemy = Team.B;
        RobotInfo fakeEnemy = new RobotInfo(100, Team.B, RobotType.ENLIGHTENMENT_CENTER,
                100, 100, new MapLocation(100, 100));
        when(mockRC.senseNearbyRobots(EnlightenmentCenter.senseRadius, Team.B))
                .thenReturn(new RobotInfo[]{fakeEnemy});
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.runEnlightenmentCenter();
    }

    @Test
    public void addTargetToTargetListEmptyTest() throws GameActionException {
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.addTargetToTargetList(45668);
    }

    @Test
    public void addTargetToTargetListEnemyTest() throws GameActionException {
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.targetList.add(29284);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.addTargetToTargetList(45668);
    }

    @Test
    public void addTargetToTargetListNeutralTest() throws GameActionException {
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.targetList.add(45668);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.addTargetToTargetList(29284);
    }

    @Test
    public void addTargetToTargetListFilledTest() throws GameActionException {
        EnlightenmentCenter.targetList = new LinkedList<>();
        System.out.println(EnlightenmentCenter.sendLocation(new MapLocation( 24678, 24678), 1));
        EnlightenmentCenter.targetList.add(29542);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.addTargetToTargetList(29284);
    }

    @Test
    public void polScanTest() throws GameActionException {
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.polIDList.add(100);
        EnlightenmentCenter.targetList = new LinkedList<>();
        EnlightenmentCenter.targetList.add(200);
        when(mockRC.canGetFlag(100)).thenReturn(true);
        when(mockRC.getFlag(100)).thenReturn(200);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polScan();
    }

    @Test
    public void mucScanTest() throws GameActionException {
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList.add(100);
        EnlightenmentCenter.targetList = new LinkedList<>();
        when(mockRC.canGetFlag(100)).thenReturn(true);
        when(mockRC.getFlag(100)).thenReturn(200);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.mucScan();

    public void polScanZeroTest() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList= new HashSet<>();
        EnlightenmentCenter.polIDList.add(300);
        when(mockRC.canGetFlag(300))
                .thenReturn(true);
        when(mockRC.getFlag(300))
                .thenReturn(0);
        int result = EnlightenmentCenter.polScan();
        Assert.assertEquals(1, result);
    }
}
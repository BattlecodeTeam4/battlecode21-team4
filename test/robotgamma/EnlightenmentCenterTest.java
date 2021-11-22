package robotgamma;

import battlecode.common.*;
import org.junit.*;
import org.mockito.*;

import java.util.HashSet;
import java.util.Map;

import static org.mockito.Mockito.*;

public class EnlightenmentCenterTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);

    @Test
    public void randomSpawnableRobotType() {
        RobotType rob = EnlightenmentCenter.randomSpawnableRobotType();
        Assert.assertNotNull(rob);
    }

    @Test
    public void checkIfExistMuckraker() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList.add(200);
        when(mockRC.canGetFlag(200)).thenReturn(true);
        Assert.assertEquals(1,EnlightenmentCenter.checkIfExistMuckraker());
    }

    @Test
    public void checkIfExistPolitician() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList= new HashSet<>();
        EnlightenmentCenter.polIDList.add(200);
        EnlightenmentCenter.polIDList.add(300);
        when(mockRC.canGetFlag(200)).thenReturn(true);
        when(mockRC.canGetFlag(300)).thenReturn(true);
        Assert.assertEquals(2 ,EnlightenmentCenter.checkIfExistPolitician());
    }

    @Test
    public void checkIfExistSlandererRmSlanderer() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList= new HashSet<>();
        EnlightenmentCenter.slaThreshold = 290;
        EnlightenmentCenter.slaIDList.add(295);
        when(mockRC.canGetFlag(295)).thenReturn(false);
        when(mockRC.getFlag(295)).thenReturn(295);
        Assert.assertEquals(0,EnlightenmentCenter.checkIfExistSlanderer());
    }

    @Test
    public void checkIfExistSlandererNot() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList= new HashSet<>();
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaThreshold = 290;
        EnlightenmentCenter.slaIDList.add(295);
        when(mockRC.canGetFlag(295)).thenReturn(true);
        when(mockRC.getFlag(295)).thenReturn(295);
        Assert.assertEquals(0,EnlightenmentCenter.checkIfExistSlanderer());
        Assert.assertEquals(1,EnlightenmentCenter.checkIfExistPolitician());
    }

    @Test
    public void checkIfExistSlandererYes() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList= new HashSet<>();
        EnlightenmentCenter.slaThreshold = 290;
        EnlightenmentCenter.slaIDList.add(285);
        when(mockRC.canGetFlag(285)).thenReturn(true);
        when(mockRC.getFlag(285)).thenReturn(285);
        Assert.assertEquals(1,EnlightenmentCenter.checkIfExistSlanderer());
    }

    @Test
    public void bidByThreshold() throws GameActionException {
        when(mockRC.getInfluence()).thenReturn(10);
        when(mockRC.canBid(anyInt())).thenReturn(false);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.bidByThreshold();
        verify(mockRC, times(1)).canBid(anyInt());
    }
    @Test
    public void createMuckraker() throws  GameActionException {
        when(mockRC.getInfluence()).thenReturn(100);
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.MUCKRAKER, d , 10)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(1, Team.A, RobotType.MUCKRAKER, 10, 1, new MapLocation(102,102)));
        }

        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.mucIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.MUCKRAKER, 10,0,0);
        Assert.assertEquals(RobotType.MUCKRAKER, test);
    }

    @Test
    public void createMuckrakerNot() throws GameActionException {
        for(Direction d : EnlightenmentCenter.directions){
            when(mockRC.canBuildRobot(RobotType.POLITICIAN, d, 35)).thenReturn(false);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103) ));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.POLITICIAN, 0, 35, 0);
        Assert.assertEquals(null, test);
    }

    @Test
    public void createPolitician() throws GameActionException {
        for(Direction d : EnlightenmentCenter.directions){
            when(mockRC.canBuildRobot(RobotType.POLITICIAN, d, 35)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103) ));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.POLITICIAN, 0, 35, 0);
        Assert.assertEquals(RobotType.POLITICIAN, test);
    }

    @Test
    public void createPoliticianNot() throws GameActionException {
        for(Direction d : EnlightenmentCenter.directions){
            when(mockRC.canBuildRobot(RobotType.POLITICIAN, d, 35)).thenReturn(false);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103) ));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.POLITICIAN, 0, 35, 0);
        Assert.assertEquals(null, test);
    }

    @Test
    public void createSlanderer() throws  GameActionException {
        when(mockRC.getInfluence()).thenReturn(100);
        for (Direction d : EnlightenmentCenter.directions) {
            when(mockRC.canBuildRobot(RobotType.SLANDERER, d , 50)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(0, Team.A, RobotType.SLANDERER, 50, 1, new MapLocation(101,101)));
        }

        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.SLANDERER, 0,0,50);
        Assert.assertEquals(RobotType.SLANDERER, test);
    }

    @Test
    public void createSlandererNot() throws GameActionException {
        for(Direction d : EnlightenmentCenter.directions){
            when(mockRC.canBuildRobot(RobotType.POLITICIAN, d, 35)).thenReturn(false);
            when(mockRC.senseRobotAtLocation(mockRC.adjacentLocation(d))).thenReturn(new RobotInfo(2, Team.A, RobotType.POLITICIAN, 35, 1, new MapLocation(103, 103) ));
        }
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList = new HashSet<>();

        RobotType test = EnlightenmentCenter.buildRobot(RobotType.POLITICIAN, 0, 35, 0);
        Assert.assertEquals(null, test);
    }

    @Test
    public void spawnRandom() {
    }

    @Test
    public void runEnlightenmentCenter() {
    }
}
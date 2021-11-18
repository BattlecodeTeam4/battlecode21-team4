package robotdelta;

import battlecode.common.*;
import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class RobotTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation loc = new MapLocation(100, 100);
    int neutralTeam = 1;
    int enemyTeam = 2;

    @Test
    public void initTest() throws GameActionException {
        Robot.rc = mockRC;

        Robot.actionRadius = 0;
        Robot.senseRadius = 0;
        Robot.detectRadius = 0;
        when(mockRC.getType()).thenReturn(RobotType.MUCKRAKER);

        Robot.enemy = null;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getTeam().opponent()).thenReturn(Team.B);

        Robot.homeLoc = new MapLocation(1, 1);

        Robot.homeID = 10800;
        when(mockRC.canGetFlag(Robot.homeID)).thenReturn(false);

        when(mockRC.getInfluence()).thenReturn(100);
        when(mockRC.getRoundNum()).thenReturn(100);
        when(mockRC.getConviction()).thenReturn(100);

        Robot.init();
    }

    @Test
    public void sendLocationTest() throws GameActionException {
        when(mockRC.canSetFlag(0)).thenReturn(true);
        Robot.rc = mockRC;
        Assert.assertEquals(29284, Robot.sendLocation(loc, 1));
    }
    @Test
    public void getLocationFromFlagTest() throws GameActionException {
        //Since RC is fake we have to define variables returned from method!
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        when(mockRC.getLocation()).thenReturn(loc);

        //Have to set variable in Robot to mockRC otherwise nullptr!
        Robot.rc = mockRC;

        MapLocation result = Robot.getLocationFromFlag(12900);

        //12900 is flag for MapLocation(100, 100)!
        Assert.assertEquals(loc, result);
    }

    @Test
    public void getNeutralTeamFromFlagTest() throws GameActionException{
        when(mockRC.canSetFlag(29284)).thenReturn(true);
        Robot.rc = mockRC;
        int resultNeutral = Robot.getTeamFromFlag(29284);
        Assert.assertEquals(resultNeutral, neutralTeam);
    }

    @Test
    public void getEnemyTeamFromFlagTest() throws GameActionException{
        when(mockRC.canSetFlag(45668)).thenReturn(true);
        Robot.rc = mockRC;
        int resultEnemy = Robot.getTeamFromFlag(45668);
        Assert.assertEquals(resultEnemy, enemyTeam);
    }

    @Test
    public void updateActionRadiusTest() {
        Robot.rc = mockRC;
        when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        Robot.updateActionRadius();
        verify(mockRC, times(1)).getType();
    }

    @Test
    public void updateSenseRadiusTest() {
        Robot.rc = mockRC;
        when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        Robot.updateSenseRadius();
        verify(mockRC, times(1)).getType();
    }

    @Test
    public void updateDetectRadiusTest() {
        Robot.rc = mockRC;
        when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        Robot.updateDetectRadius();
        verify(mockRC, times(1)).getType();
    }

   @Test
    public void randomDirectionTest() {
        Robot.rc = mockRC;
        Direction dir = robotgamma.Robot.randomDirection();
        Assert.assertNotEquals(dir, Direction.CENTER);
    }

    @Test
    public void tryMoveTest() throws GameActionException {
        Robot.rc = mockRC;
        when(mockRC.canMove(any())).thenReturn(false);
        Assert.assertFalse(Robot.tryMove(Direction.EAST));
    }

    @Test
    public void moveLocationAtCenterTest() throws GameActionException {
        when(mockRC.getLocation()).thenReturn(new MapLocation(100, 100));
        Robot.rc = mockRC;
        Assert.assertFalse(Robot.moveLocation(loc));
    }
    @Test
    public void moveLocationNoTargetTest() throws GameActionException {
        when(mockRC.getLocation()).thenReturn(new MapLocation(101, 100));
        Robot.rc = mockRC;
        Assert.assertTrue(Robot.moveLocation(null));
    }

    @Test
    public void moveLocationTryMoveTest() throws GameActionException {
        when(mockRC.getLocation()).thenReturn(new MapLocation(200, 200));
        when(mockRC.canMove(Direction.SOUTHWEST)).thenReturn(true);
        Robot.rc = mockRC;
        Assert.assertTrue(Robot.moveLocation(loc));
    }

    @Test
    public void pathfindingTest() throws GameActionException {
        Robot.rc = mockRC;
        Assert.assertFalse(Robot.pathfinding(Direction.NORTH));
    }

    @Test
    public void findHomeTest() throws GameActionException {
        Robot.rc = mockRC;
        Robot.homeLoc = null;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getLocation()).thenReturn(loc);
        for (int i = Robot.directions.length; --i >= 0; ) {
            MapLocation parseLoc = loc.add(Robot.directions[i]);
            when(mockRC.onTheMap(parseLoc)).thenReturn(true);
            when(mockRC.senseRobotAtLocation(parseLoc)).thenReturn(new RobotInfo(100, Team.A,
                    RobotType.ENLIGHTENMENT_CENTER,100, 100, new MapLocation(200, 200)));
        }
        Robot.findHome();
    }

    @Test
    public void moveStraightTest() throws GameActionException {
        Robot.rc = mockRC;
        when(mockRC.isReady()).thenReturn(true);
        Robot.moveStraight();
    }
}
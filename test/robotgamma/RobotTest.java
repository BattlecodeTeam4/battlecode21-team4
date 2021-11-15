package robotgamma;

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
    public void sendLocationTest() throws GameActionException {
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        Robot.rc = mockRC;
        Robot.sendLocation(loc, 1);
        verify(mockRC, times(1)).canSetFlag(anyInt());
    }
    @Test
    public void getLocationFromFlagTest() throws GameActionException {
        //Since RC is fake we have to define variables returned from method!
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        when(mockRC.getLocation()).thenReturn(new MapLocation(100, 100));

        //Have to set variable in Robot to mockRC otherwise nullptr!
        Robot.rc = mockRC;

        MapLocation result = Robot.getLocationFromFlag(12900);

        //12900 is flag for MapLocation(100, 100)!
        Assert.assertEquals(loc, result);
    }

    @Test
    public void getTeamFromFlagTest() throws GameActionException{
        when(mockRC.canSetFlag(29284)).thenReturn(true);
        Robot.rc = mockRC;
        int resultNeutral = Robot.getTeamFromFlag(29284);
        Assert.assertEquals(resultNeutral, neutralTeam);

        when(mockRC.canSetFlag(45668)).thenReturn(true);
        Robot.rc = mockRC;
        int resultEnemy = Robot.getTeamFromFlag(45668);
        Assert.assertEquals(resultEnemy, enemyTeam);
    }

    @Test
    public void updateActionRadiusTest() {
        robotgamma.Robot.rc = mockRC;
        int actionRadius = 0;
        when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        Robot.updateActionRadius();
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
        verify(mockRC, times(0)).move(Direction.EAST);
        Assert.assertFalse(Robot.tryMove(Direction.EAST));
    }

    @Test
    public void pathfindingTest() throws GameActionException {
        Robot.rc = mockRC;
        when(mockRC.canMove(any())).thenReturn(false);
        verify(mockRC, times(0)).move(Direction.EAST);
        Assert.assertFalse(Robot.pathfinding(Direction.EAST));
    }

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
}
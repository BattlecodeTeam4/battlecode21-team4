package robotbeta;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;

import java.util.Map;

import static org.mockito.Mockito.*;

public class RobotTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation flag = new MapLocation(100, 100);
    MapLocation dummy = new MapLocation(0, 1);
    RobotInfo info = new RobotInfo(69,Team.B,RobotType.SLANDERER,1,1,dummy);
    RobotInfo[] infoArr = new RobotInfo[1];

    @Test
    public void tryMuckraker() throws GameActionException {
        infoArr[0] = info;
        Muckraker.rc = mockRC;
        Muckraker.turnCount = 0;
        Muckraker.homeID = 0;
        Muckraker.senseRadius = 30;
        Muckraker.actionRadius = 12;
        Muckraker.enemy = Team.B;
        Muckraker.homeLoc = new MapLocation(0, 0);
        when(Muckraker.rc.getType()).thenReturn(RobotType.MUCKRAKER);
        when(Muckraker.rc.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(infoArr);
        when(Muckraker.rc.senseNearbyRobots(Muckraker.actionRadius, Muckraker.enemy)).thenReturn(infoArr);
        System.out.println(Muckraker.rc.getType());
        Muckraker.runMuckraker();
    }

    @Test
    public void sendLocationTest() throws GameActionException {
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        Robot.rc = mockRC;

        Robot.sendLocation(flag);
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
        Assert.assertEquals(flag, result);
    }
}
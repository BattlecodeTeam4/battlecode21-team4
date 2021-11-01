package robotbeta;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;

public class RobotTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation flag = new MapLocation(100, 100);

    @Test
    public void tryMoveTest() throws GameActionException {

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
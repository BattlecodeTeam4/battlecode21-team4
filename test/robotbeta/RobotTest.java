package robotbeta;

import static org.junit.Assert.*;
import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
public class RobotTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);


    @Test
    public void testMoveLocation() throws GameActionException {
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        when(mockRC.getLocation()).thenReturn(new MapLocation(100, 100));

        // the new loc needs to move to
        MapLocation newLoc = new MapLocation(110, 110);

        // Try the function
        Robot.rc = mockRC;
        Robot.moveLocation(newLoc);
        MapLocation result = Robot.getLocationFromFlag(12900);

        // Check if robot with flag 12900 is moved to the new loc
        Assert.assertEquals(newLoc, result);
    }
    @Test
    public void testSanity() {
        assertEquals(2, 1+1);
    }

}

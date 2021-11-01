package robotbeta;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;

public class EnlightenmentCenterTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);

    @Test
    public void sendLocationTest() throws GameActionException {
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        MapLocation flag = new MapLocation(100, 100);
        Robot.rc = mockRC;
        Robot.sendLocation(flag);
    }
}
package robotgamma;

import battlecode.common.*;
import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class PoliticianTest {

    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation flag = new MapLocation(100, 100);

    @Test
    public void runPoliticianTest() throws GameActionException {
        //Politician.rc = mockRC;
        //when(mockRC.canSetFlag(13068)).thenReturn(false);
        //when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        //when(mockRC.canEmpower(9)).thenReturn(false);
        //when(mockRC.getType().actionRadiusSquared).thenReturn(9);
        //Politician.runPolitician();
    }
}
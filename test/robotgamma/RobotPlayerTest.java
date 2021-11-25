package robotgamma;

import battlecode.common.RobotController;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

public class RobotPlayerTest {

    @Mock
    RobotController mockRC = mock(RobotController.class);
    RobotPlayer robot = mock(RobotPlayer.class);

   /* @Test
    public void runTest() throws GameActionException {
        when(mockRC.canSetFlag(13068)).thenReturn(false);
        when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        when(mockRC.canEmpower(9)).thenReturn(false);
        //when(mockRC.getType().actionRadiusSquared).thenReturn(9);
        when(mockRC.getTeam()).thenReturn(Team.B);
        RobotPlayer.run(mockRC);
        verify(robot, times(1)).run(mockRC);
    } */
}
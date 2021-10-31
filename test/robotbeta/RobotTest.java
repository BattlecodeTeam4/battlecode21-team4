package robotbeta;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;
import static robotbeta.Robot.randomDirection;

public class RobotTest {

    @Test
    public void tryMovetest() throws GameActionException {
        Robot robot = mock(Robot.class);
        final Direction[] directions = {
                Direction.NORTH,
                Direction.NORTHEAST,
                Direction.EAST,
                Direction.SOUTHEAST,
                Direction.SOUTH,
                Direction.SOUTHWEST,
                Direction.WEST,
                Direction.NORTHWEST,
        };
        Direction dir = randomDirection();
        doNothing().when(robot).tryMove(dir);
        doReturn(false).when(robot).tryMove(dir);
        assertEquals(false, robot.tryMove(dir));
    }
}
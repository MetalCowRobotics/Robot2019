package frc.commands;

import frc.lib14.MCRCommand;
import frc.systems.Climber;

public class ClimbCommand extends TimedCommand implements MCRCommand {
    private boolean firstTime = true;
    private Climber climber = Climber.getInstance();

    public enum LEGS {
        front, back
    };

    public enum DIRECTIONS {
        extend, retract
    };

    private LEGS leg;
    private DIRECTIONS direction;

    public ClimbCommand(LEGS leg, DIRECTIONS direction) {
        super();
        setParameters(leg, direction);
    }

    public ClimbCommand(LEGS leg, DIRECTIONS direction, int timeoutSeconds) {
        super();
        setParameters(leg, direction);
        setTargetTime(timeoutSeconds);
    }

    private void setParameters(LEGS leg, DIRECTIONS direction) {
        this.leg = leg;
        this.direction = direction;
    }

    public void run() {
        if (firstTime) {
            firstTime = false;
            startTimer();
            articulateClimber();
        }
    }

    private void articulateClimber() {
        switch (leg) {
        case front:
            if (DIRECTIONS.extend==direction) {
                climber.extendFrontLegs();
            } else {
                climber.retractFrontLegs();
            }
            break;
        case back:
            if (DIRECTIONS.extend==direction) {
                climber.extendBackLegs();
            } else {
                climber.retractBackLegs();
            }
            break;
        default:
            break;
        }
    }

    private void end() {
        endTimer();
    }

    public boolean isFinished() {
        if (timerUp()) {
            end();
            return true;
        }
        return !firstTime;
    }
}
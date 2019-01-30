package frc.commands;

import frc.commands.ClimbCommand.DIRECTIONS;
import frc.commands.ClimbCommand.LEGS;
import frc.lib14.MCRCommand;

public class DeployFrontLegs extends TimedCommand implements MCRCommand {
    MCRCommand command = new ClimbCommand(LEGS.front, DIRECTIONS.extend);

    public DeployFrontLegs(int timeoutSeconds) {
        command = new ClimbCommand(LEGS.front, DIRECTIONS.extend, timeoutSeconds);
    }

    public void run() {
        command.run();
    }

    public boolean isFinished() {
        return command.isFinished();
    }
}
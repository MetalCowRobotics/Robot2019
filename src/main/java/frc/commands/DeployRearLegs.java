package frc.commands;

import frc.commands.ClimbCommand.DIRECTIONS;
import frc.commands.ClimbCommand.LEGS;
import frc.lib14.MCRCommand;

public class DeployRearLegs implements MCRCommand {
    MCRCommand command = new ClimbCommand(LEGS.back, DIRECTIONS.extend);

    public DeployRearLegs(int timeoutSeconds) {
        command = new ClimbCommand(LEGS.back, DIRECTIONS.extend, timeoutSeconds);
    }

    public void run() {
        command.run();
      //  System.out.println("Deploy Rear Legs");
    }

    public boolean isFinished() {
        return command.isFinished();
    }
}
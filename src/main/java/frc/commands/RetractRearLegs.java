package frc.commands;

import frc.commands.ClimbCommand.DIRECTIONS;
import frc.commands.ClimbCommand.LEGS;
import frc.lib14.MCRCommand;

public class RetractRearLegs implements MCRCommand {
    MCRCommand command;
    
    public RetractRearLegs() {
        command = new ClimbCommand(LEGS.back, DIRECTIONS.retract);
    }
    
    public RetractRearLegs(int timeoutSeconds) {
        command = new ClimbCommand(LEGS.back, DIRECTIONS.retract, timeoutSeconds);
    }
    
    public void run() {
        command.run();
    //    System.out.println("Retract Rear Legs");
    }
    
    public boolean isFinished() {
        return command.isFinished();
    }
    }
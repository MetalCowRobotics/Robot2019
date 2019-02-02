package frc.commands;

import frc.commands.ClimbCommand.DIRECTIONS;
import frc.commands.ClimbCommand.LEGS;
import frc.lib14.MCRCommand;

public class RetractFrontLegs extends TimedCommand implements MCRCommand {
    MCRCommand command;
    
    public RetractFrontLegs() {
        command = new ClimbCommand(LEGS.front, DIRECTIONS.retract);
    }
    
    public RetractFrontLegs(int timeoutSeconds) {
        command = new ClimbCommand(LEGS.front, DIRECTIONS.retract, timeoutSeconds);
    }
    
    public void run() {
        command.run();
        System.out.println("Retract Front Legs");
    }
    
    public boolean isFinished() {
        return command.isFinished();
    }
    }
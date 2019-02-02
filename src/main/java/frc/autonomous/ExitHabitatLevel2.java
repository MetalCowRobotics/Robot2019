package frc.autonomous;

import frc.commands.CommandPause;
import frc.commands.DeployFrontLegs;
import frc.commands.DeployRearLegs;
import frc.commands.DriveBackwardsStraight;
import frc.commands.DriveStraightInches;
import frc.commands.RetractFrontLegs;
import frc.commands.RetractRearLegs;
import frc.lib14.MCRCommand;
import frc.lib14.ParallelCommands;
import frc.lib14.SequentialCommands;

public class ExitHabitatLevel2 implements MCRCommand {
    MCRCommand mission;
    boolean firstTime = true;
    public ExitHabitatLevel2() {
        mission = new SequentialCommands(
            new CommandPause(5),
            new DeployRearLegs(1),
            //TODO: drive straight number of inches both times or use DriveToSensor
            new DriveBackwardsStraight(24,3),
            new DeployFrontLegs(1),
            // version 1
            new RetractRearLegs(),
            new DriveBackwardsStraight(10,3),
            new RetractFrontLegs()
            //version 2
             // new DriveStraightInches(10,3),
            //new ParallelCommands(
               // new RetractRearLegs(),
               // new RetractFrontLegs()
           // )
        );
    }

    @Override
    public void run() {
        mission.run();
        System.out.println("Mission2");
    }
    @Override
    public boolean isFinished() {
        return mission.isFinished();
    }
}
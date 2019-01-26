package frc.autonomous;

import frc.commands.CommandPause;
import frc.commands.DriveStraightInches;
import frc.lib14.MCRCommand;
import frc.lib14.SequentialCommands;

public class ExitHabitatLevel1 implements MCRCommand {
    MCRCommand mission;
    boolean firstTime = true;
    public ExitHabitatLevel1() {
        mission = new SequentialCommands(
            new CommandPause(5),
            new DriveStraightInches(48,3)
        );
    }

    @Override
    public void run() {
        mission.run();
    }
    @Override
    public boolean isFinished() {
        return mission.isFinished();
    }

} 
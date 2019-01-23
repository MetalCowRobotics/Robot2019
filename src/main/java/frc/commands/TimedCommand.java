package frc.commands;

import edu.wpi.first.wpilibj.Timer;

abstract class TimedCommand {
    private Timer timer = new Timer();
    private double targetTime = 3;

    protected double setTargetTime(double seconds) {
        this.targetTime = seconds;
    }

    protected void startTimer() {
        timer.reset();
        timer.start();
    }

protected void endTimer() {
	timer.stop;
}

    protected boolean timerUp() {
        return timer.get() > targetTime;
    }
}
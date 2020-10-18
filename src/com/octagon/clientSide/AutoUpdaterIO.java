package com.octagon.clientSide;

import com.octagon.Controller;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AutoUpdaterIO {
    Timer timer;
    Controller controller = new Controller();

    public void runChecker(){
        timer = new Timer();
        timer.schedule(timerTask,600000);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            controller.launchSync();
            if(controller.statusAutoUpdate){
                AutoUpdaterIO autoUpdaterIO = new AutoUpdaterIO();
                autoUpdaterIO.runChecker();
            }
        }
    };
}

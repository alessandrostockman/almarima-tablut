package it.unibo.almarima.tablut.local.control;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;
import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;

public class OfflineThread extends Thread {

    private OfflineAgent agent;
    private int maxGames;
    private String path;
    private List<String> logBuffer;

    public OfflineThread(OfflineAgent agent, int games, String twoHeurName) {
        this.agent = agent;
        this.maxGames = games;
        this.path = twoHeurName;
        this.logBuffer = new ArrayList<>();
    }

    public void run(){
        int games = 0;
        String p;
        while (games < this.maxGames) {
            try {
                p=path.concat("_"+games);
                this.agent.execute(p);
                return;
            } catch (AgentStoppedException e) { 
                if (e instanceof GameFinishedException) {
                    GameFinishedException gameReport = (GameFinishedException) e;
                    this.logBuffer.add("Winner " + gameReport.getWinner());
                }
                games++;
            }
        }

        String baseDirectory = "src/main/java/it/unibo/almarima/tablut/local/stats_history";
		Path reportlogPath = Paths.get(baseDirectory);
		reportlogPath = reportlogPath.toAbsolutePath();

		Logger loggReport = Logger.getLogger("ReportLog");

        if (logBuffer.size() > 0) {
            try {
                File logDir = new File(reportlogPath.toString());
                if (!logDir.exists()) {
                    logDir.mkdirs();
                }
    
                reportlogPath = Paths.get(reportlogPath.toString() + File.separator + "report_" + new Date().getTime() + ".txt");
                
                File log = new File(reportlogPath.toString());
                if (!log.exists()) {
                    log.createNewFile();
                }
                FileHandler fh = null;
                fh = new FileHandler(reportlogPath.toString(), true);
                loggReport.addHandler(fh);
                fh.setFormatter(new SimpleFormatter());
                loggReport.setLevel(Level.FINE);
                for (String s : this.logBuffer) {
                    loggReport.fine(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

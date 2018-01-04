package fr.pavnay.rabbits.ui;

import java.util.List;

import javax.swing.SwingWorker;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import fr.pavnay.rabbits.engine.HuntEngine;
import fr.pavnay.rabbits.model.enums.Status;

/**
 * 
 * This worker provides the loop to play HuntEngine.step() until the status is different to RUNNING
 *
 */
public class HuntWorker extends SwingWorker<Status, String> {

	private static final Logger logger = Logger.getLogger(HuntWorker.class);
	
	public final static long SLEEP_TIME = 500;
	
	private HuntEngine engine;
	private MainFrame mainFrame;
	
	public HuntWorker(HuntEngine engine, MainFrame mainFrame ) {
		this.engine = engine;
		this.mainFrame = mainFrame;
		HuntLogAppender appender = new HuntLogAppender(this);
		LogManager.getLogger("fr.pavnay.rabbits").addAppender(appender);
	}
	
	public void appendLog(String log) {
		publish(log);
	}
	
	/**
	 * Provides some hunt event from logs to UI
	 */
	@Override
	protected void process(List<String> logs) {
		for(String log : logs) {
			mainFrame.report(log);
		}
	}
	
	@Override
	protected Status doInBackground() throws Exception {
		Status status = engine.getStatus();
		try {
		while( status == Status.RUNNING ) {
			status = engine.step();
			Thread.sleep(SLEEP_TIME);
		}
		}catch( Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug("Hunt ended : " + engine.getStatus());
		return engine.getStatus();
	}
	
	@Override
	protected void done() {
		super.done();
		logger.debug("Done - " + engine.getStatus());
	}
	
	/**
	 * 
	 * A log appender to retrieve hunt logs and give them to UI.
	 *
	 */
	public class HuntLogAppender extends AppenderSkeleton {
	    private final HuntWorker worker;

	    public HuntLogAppender( HuntWorker slurperWorker) {
	        this.worker = slurperWorker;
	    }
	    protected void append(LoggingEvent event) 
	    {
	        if(event.getLevel().equals(Level.INFO)){
	        	worker.appendLog(event.getMessage().toString());
	        }
	    }
	    public void close() 
	    {
	    }
	    public boolean requiresLayout() 
	    {
	        return false;
	    }
	}
	
}

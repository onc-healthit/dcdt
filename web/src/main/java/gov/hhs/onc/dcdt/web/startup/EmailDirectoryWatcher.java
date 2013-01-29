package gov.hhs.onc.dcdt.web.startup;

import gov.hhs.onc.dcdt.web.mail.decrypt.DecryptController;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * Processes email file directory on application
 * start-up, cycling throw all the emails and passing each
 * to the decryptor controller.
 * Launches thread that watches the directory and processes
 * the emails as they are moved into the directory.
 * @author joelamy
 *
 */
public class EmailDirectoryWatcher implements Runnable {

    private volatile Thread blinker;
	private final Path dir;
	private final WatchService watcher;
	private final String sentDir;
	private final String failedDir;
	
	private final static Logger LOGGER = Logger.getLogger(EmailDirectoryWatcher.class);

	public EmailDirectoryWatcher(String path) throws IOException {
		super();
		this.watcher = FileSystems.getDefault().newWatchService();
		this.dir = Paths.get(path);
		dir.register(watcher, ENTRY_CREATE);
		sentDir = ConfigInfo.getConfigProperty("SentDirectory");
		failedDir = ConfigInfo.getConfigProperty("FailedDirectory");
	}

	// Technique for safe thread control
	// http://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
	public void start() {
        blinker = new Thread(this);
		blinker.setDaemon(true);
        blinker.start();
		
		LOGGER.debug("Directory watcher started.");
    }

    public void stopRunning() {
		try
		{
			this.watcher.close();
		}
		catch (IOException e)
		{
			LOGGER.error(e);
		}
		
        Thread tmpBlinker = blinker;

        // Setting to null will stop the thread if it's running freely
        blinker = null;

        // If not, interrupt
        if (tmpBlinker != null) {
            tmpBlinker.interrupt();
			
			try
			{
				tmpBlinker.join();
			}
			catch (InterruptedException e)
			{
				LOGGER.error(e);
			}
        }
		
		LOGGER.debug("Directory watcher stopped.");
    }

	public void run() {
		File[] files = dir.toFile().listFiles();
		// Run once through the folder
		LOGGER.debug("First time through the folder: " + dir);
		for (File file : files) {
			LOGGER.debug("Found email file: "
				+ file.getName());
			DecryptController dc =
				new DecryptController(file.getName());

			if (!file.isDirectory()) {
				try {
					// TODO: replace with long-term solution
					Thread.sleep(5000);
					
					dc.processMessage();
					moveFile(file, sentDir);
				} catch (Exception e) {
					LOGGER.error("Problem with email file.", e);
					moveFile(file, failedDir);
				}
			}
		}

		// Go into a loop to watch the folder
		processEvents();
	}

    /**
     * Process all events for the key queued to the watcher.
     * http://docs.oracle.com/javase/tutorial/essential/io/examples/Email.java
     */
	@SuppressWarnings("unchecked")
    private void processEvents() {
 
    	// Endless loop
		LOGGER.debug("Watching the folder for new files");
		
		// http://www.forward.com.au/javaProgramming/HowToStopAThread.html
		// TODO: Review this for thread safety and try/catch
		// (i.e. wrap whole thing in try for InterruptedException?)
		Thread thisThread = Thread.currentThread();
        while (blinker == thisThread) {
 
            // Wait for key to be signaled that it's available
            WatchKey key;

            try {
            	// This is the one thing that can get stuck, so we need to be able to interrupt it
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            // Get any events that have queued up
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
 
                if (kind == OVERFLOW) {
                    continue;
                }
 
                //The filename is the context of the event.
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();
 
    			LOGGER.debug("Found email file: " + filename);
    			DecryptController dc = new DecryptController(filename.toString());

    			File nextFile = new File(dir.toString() + File.separatorChar + File.separatorChar
    				+ filename.toString());

    			LOGGER.debug("NextFile: " + nextFile.getAbsolutePath());

    			if (!nextFile.isDirectory()) {
    				try {
						// TODO: replace with long-term solution
						Thread.sleep(5000);
						
    					dc.processMessage();
    					moveFile(nextFile, sentDir);
    				} catch (Exception e) {
    					LOGGER.error("Problem with email file.", e);
    					moveFile(nextFile, failedDir);
    				}
    			}
            }
 
            //Reset the key -- this step is critical if you want to receive
            //further watch events. If the key is no longer valid, the directory
            //is inaccessible so exit the loop.
            boolean valid = key.reset();
            if (!valid) {
            	break;
            }
        }
    }

    private void moveFile(File file, String moveDir){
    	try {
    		LOGGER.debug("----Moving File---");
    		LOGGER.debug("Current Location: " + file.getAbsolutePath());
    		LOGGER.debug("Moving Location: " + moveDir);

    		File movedFile = new File(moveDir
    			+ File.separatorChar + file.getName());

    		FileUtils.copyFile(file, movedFile);
    		if(!file.delete())
    			FileUtils.forceDelete(file);
    		
    		if (!file.exists() && movedFile.exists()) {
    			LOGGER.debug("File moved to: "
    				+ movedFile.getAbsolutePath());
    		} else {
    			LOGGER.error("Problem moving file from "
    				+ file.getAbsolutePath() + " to "
    				+ movedFile.getAbsolutePath());
    		}
    	} catch (IOException e) {
			LOGGER.error(e);
		}
    }

}

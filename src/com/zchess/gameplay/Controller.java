package com.zchess.gameplay;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.zchess.ui.GameFrame;


public class Controller {

	public static final Logger logger = Logger.getLogger(Controller.class.getName());
	private static FileHandler fileTxt;
	private static MyFormatter formatterTxt;
	public static GameFrame gFrame;

	public static void main(String[] a) {
		setupLogger();
		gFrame = new GameFrame();
	}
	
	 public static void setupLogger() {
		 for(Handler iHandler:logger.getParent().getHandlers())
	        {
	        logger.getParent().removeHandler(iHandler);
	        }
		  try {
			fileTxt = new FileHandler("logs/log.txt");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		 formatterTxt = new MyFormatter();
		  fileTxt.setFormatter(formatterTxt);
		  logger.addHandler(fileTxt);
	  }
	
}

class MyFormatter extends Formatter 
{   
    public MyFormatter() { super(); }

    @Override 
    public String format(final LogRecord record) 
    {
    	StringBuilder sb = new StringBuilder();
          sb.append(" ")
            .append(record.getLevel().getLocalizedName())
            .append(": ")
            .append(formatMessage(record))
            .append("\n");
          return sb.toString();
    }
}
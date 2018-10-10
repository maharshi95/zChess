package com.zchess.gameplay;

public class GameMode {
	
	public static final String BLITZ = "Blitz";
	public static final String LIGHTENING= "Lightening";
	public static final String RAPID = "Rapid";
	public static final String ARMAGEDDON = "Armageddon";
	public static final String NO_CLOCK = "No Clock";
	public static final String INVALID_TYPE = "Invalid Type";
	
	public static final String BLITZ_INFO = "Blits Mode: Each Player will be given 5 minutes initially\nWith each move 2 seconds will be imparted";
	public static final String LIGHTENING_INFO = "Lightening Mode: Each Player will be given 1 minute initially\nWith each move 2 seconds will be imparted";
	public static final String RAPID_INFO = "Blits Mode: Each Player will be given 15 minutes initially";
	public static final String ARMAGEDDON_INFO = "Armageddon Mode: White gets 6 minutes, Black gets 5 minutes\nWhite loses if he doesnt win, Black wins even if game is draw";
	public static final String NO_CLOCK_INFO = "No Clock Mode: Player may take as much time as they want";
	
	public static String info(String mode) {
		
		if(mode.equals(BLITZ))
			return BLITZ_INFO;
		
		else if(mode.equals(LIGHTENING))
			return LIGHTENING_INFO;
		
		else if(mode.equals(RAPID))
			return RAPID_INFO;
		
		else if(mode.equals(ARMAGEDDON))
			return ARMAGEDDON_INFO;
		
		else
			return NO_CLOCK_INFO;
	}
}

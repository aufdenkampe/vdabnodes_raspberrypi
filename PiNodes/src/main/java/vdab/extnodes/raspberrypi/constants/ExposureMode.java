package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ExposureMode {
	public static final int AUTO = 1;
	public static final int MANUAL = 0;
	public static final int NIGHT = 2;
	public static final int BACKLIGHT = 3;
	public static final int SPOTLIGHT = 4;
	public static final int SPORTS = 5;
	public static final int ANTISHAKE = 6;	
	
	private static AFEnum s_ExposureModeEnum = new AFEnum("ExposureMode")
	.addEntry(AUTO, "Auto")
	.addEntry(MANUAL, "Manual")
	.addEntry(NIGHT, "Night")
	.addEntry(BACKLIGHT, "Backlight")
	.addEntry(SPOTLIGHT, "Spotlight")
	.addEntry(SPORTS, "Sports")
	.addEntry(ANTISHAKE, "Antishake");
	public static AFEnum getEnum(){
		return  s_ExposureModeEnum  ;
	}
}

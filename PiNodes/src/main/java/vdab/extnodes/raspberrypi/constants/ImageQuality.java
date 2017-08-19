package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ImageQuality {
	public static final int LOW = 1;
	public static final int MEDIUM = 2;
	public static final int HIGH = 3;
	private static AFEnum s_ImageQualityEnum = new AFEnum("ImageQuality")
	.addEntry(LOW, "Low")
	.addEntry(MEDIUM, "Medium")
	.addEntry(HIGH,"High");
	public static AFEnum getEnum(){
		return  s_ImageQualityEnum  ;
	}
}

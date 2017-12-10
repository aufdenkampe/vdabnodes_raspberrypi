package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class SensorMode {
	public static final int STANDARD = 0;
	public static final int ZOOM = 1;

	private static AFEnum s_SensorModeEnum = new AFEnum("SensorMode")
	.addEntry(STANDARD, "Standard")
	.addEntry(ZOOM, "Zoom");

	public static AFEnum getEnum(){
		return  s_SensorModeEnum  ;
	}
}

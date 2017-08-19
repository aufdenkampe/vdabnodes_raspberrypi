package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class CameraISO {
	public static final int ISO50 = 50;
	public static final int ISO100 = 100;
	public static final int ISO200 = 200;
	public static final int ISO400 = 400;
	public static final int ISO800 = 800;
	private static AFEnum s_CameraISOEnum = new AFEnum("CameraISO")
	.addEntry(ISO50, " 50")
	.addEntry(ISO100, "100")
	.addEntry(ISO200, "200")
	.addEntry(ISO400, "400")
	.addEntry(ISO800, "800");
	public static AFEnum getEnum(){
		return  s_CameraISOEnum  ;
	}
}

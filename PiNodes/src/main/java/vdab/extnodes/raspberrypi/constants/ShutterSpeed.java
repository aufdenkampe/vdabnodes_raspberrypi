package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ShutterSpeed {
	public static final int X1000MS = 1000000;
	public static final int X250MS = 1000000/4;
	public static final int X125MS = 1000000/8;
	
	private static AFEnum s_ShutterSpeedEnum = new AFEnum("ShutterSpeed")
	.addEntry(X1000MS, "1 sec")
	.addEntry(X250MS, "1/4 sec")
	.addEntry(X125MS, "1/8 sec");

	public static AFEnum getEnum(){
		return  s_ShutterSpeedEnum ;
	}
}

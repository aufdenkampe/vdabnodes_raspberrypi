package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ShutterSpeed {
	public static final int X4000MS = 4000000;
	public static final int X2000MS = 2000000;
	public static final int X1000MS = 1000000;
	public static final int X500MS = 1000000/2;
	public static final int X250MS = 1000000/4;
	public static final int X125MS = 1000000/8;
	public static final int X67MS = 1000000/15;
	public static final int X33MS = 1000000/30;
	public static final int X17MS = 1000000/60;
	public static final int X8MS = 1000000/125;
	public static final int X4MS = 1000000/250;
	public static final int X2MS = 1000000/500;
	public static final int X1MS = 1000000/1000;
	
	private static AFEnum s_ShutterSpeedEnum = new AFEnum("ShutterSpeed")
	.addEntry(X4000MS, "4 sec")
	.addEntry(X2000MS, "2 sec")
	.addEntry(X1000MS, "1 sec")
	.addEntry(X500MS, "1/2 sec")
	.addEntry(X250MS, "1/4 sec")
	.addEntry(X125MS, "1/8 sec")
	.addEntry(X67MS, "1/15 sec")
	.addEntry(X33MS, "1/30 sec")
	.addEntry(X17MS, "1/60 sec")
	.addEntry(X8MS, "1/125 sec")
	.addEntry(X4MS, "1/250 sec")
	.addEntry(X2MS, "1/500 sec")
	.addEntry(X1MS, "1/1000 sec");

	public static AFEnum getEnum(){
		return  s_ShutterSpeedEnum ;
	}
}

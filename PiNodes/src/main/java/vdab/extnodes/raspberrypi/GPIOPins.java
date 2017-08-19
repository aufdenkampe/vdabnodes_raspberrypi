package vdab.extnodes.raspberrypi;

import com.lcrc.af.datatypes.AFEnum;
public class GPIOPins {
	public final static int GPIO0 	= 0;
	public final static int GPIO1 	= 1;
	public final static int GPIO2 	= 2;
	public final static int GPIO3  	= 3;		
	public final static int GPIO4 	= 4;
	public final static int GPIO5 	= 5;
	public final static int GPIO6 	= 6;
	public final static int GPIO7  	= 7;
	private static AFEnum s_EnumPiPorts = new AFEnum("PiPorts")
	.addEntry(GPIO0 , "GPIO 0")
	.addEntry(GPIO1 , "GPIO 1")
	.addEntry(GPIO2 , "GPIO 2")
	.addEntry(GPIO3	, "GPIO 3")
	.addEntry(GPIO4 , "GPIO 4")
	.addEntry(GPIO5 , "GPIO 5")
	.addEntry(GPIO6 , "GPIO 6")
	.addEntry(GPIO7	, "GPIO 7");
	public static AFEnum getEnum(){
		return s_EnumPiPorts ;
	}
}

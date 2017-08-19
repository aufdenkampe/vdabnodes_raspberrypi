package vdab.extnodes.raspberrypi;

import com.lcrc.af.datatypes.AFEnum;
public class GpioOutputType {
	public final static int TOGGLE	= 0;
	public final static int SETSTATE= 1;
	public final static int BLINK	= 2;
	private static AFEnum s_EnumOutputTypes = new AFEnum("GpioOutputTypes")
	.addEntry(TOGGLE, "Toggle")
	.addEntry(SETSTATE , "Set State")
	.addEntry(BLINK , "Blink");
	public static AFEnum getEnum(){
		return s_EnumOutputTypes ;
	}
}

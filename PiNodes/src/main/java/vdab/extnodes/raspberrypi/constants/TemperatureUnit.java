package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class TemperatureUnit {
	public static final int C	= 1;
	public static final int F	= 2;
	public static final int K	= 3;


	private static AFEnum s_EnumTemperatureUnit = new AFEnum("TemperatureUnit")
	.addEntry(C, "Celsius")
	.addEntry(F, "Fahrenheit")
;	public static AFEnum getEnum(){
		return s_EnumTemperatureUnit;
	}
}

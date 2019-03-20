package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class A2DVoltageRange {
		public static final int ADS1115 = 1;
	    protected static final int  ADS1x15_REG_CONFIG_PGA_6_144V   = 0x0000;  // +/-6.144V range
	    protected static final int  ADS1x15_REG_CONFIG_PGA_4_096V   = 0x0200;  // +/-4.096V range
	    protected static final int  ADS1x15_REG_CONFIG_PGA_2_048V   = 0x0400;  // +/-2.048V range (default)
	    protected static final int  ADS1x15_REG_CONFIG_PGA_1_024V   = 0x0600;  // +/-1.024V range
	    protected static final int  ADS1x15_REG_CONFIG_PGA_0_512V   = 0x0800;  // +/-0.512V range
	    protected static final int  ADS1x15_REG_CONFIG_PGA_0_256V   = 0x0A00;  // +/-0.256V range

		private static AFEnum s_A2DVoltageRange = new AFEnum("A2DVoltageRange")
		.addEntry( ADS1x15_REG_CONFIG_PGA_6_144V, "+/-6.144V")
		.addEntry( ADS1x15_REG_CONFIG_PGA_4_096V ,"+/-4.096V")
		.addEntry(  ADS1x15_REG_CONFIG_PGA_2_048V ,"+/-2.048V")
		.addEntry(  ADS1x15_REG_CONFIG_PGA_1_024V ,"+/-1.024V")
		.addEntry(  ADS1x15_REG_CONFIG_PGA_0_512V ,"+/-0.512V")
		.addEntry(  ADS1x15_REG_CONFIG_PGA_0_256V ,"+/-0.256V")
		;

		public static AFEnum getEnum(){
			return s_A2DVoltageRange ;
		}
}

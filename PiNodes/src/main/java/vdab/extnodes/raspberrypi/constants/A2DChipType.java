package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class A2DChipType {
		public static final int ADS1115 = 1;

		private static AFEnum s_A2DChipType = new AFEnum("A2DChipType")
		.addEntry(ADS1115, "ADS1115")
		;

		public static AFEnum getEnum(){
			return s_A2DChipType ;
		}
}

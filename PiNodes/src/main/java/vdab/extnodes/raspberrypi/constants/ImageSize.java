package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ImageSize {
	public static final int XSMALL = 0;
	public static final int SMALL = 1;
	public static final int MEDIUM = 2;
	public static final int LARGE = 3;
	public static final int XLARGE = 4;

	private static AFEnum s_ImageSizeEnum = new AFEnum("ImageSize")
	.addEntry(XSMALL, "XSmall 216x162")
	.addEntry(SMALL, "Small 432x324")
	.addEntry(MEDIUM, "Medium 864x648")
	.addEntry(LARGE, "Large 1296x972")
	.addEntry(XLARGE,"Maximum");
	public static AFEnum getEnum(){
		return  s_ImageSizeEnum  ;
	}
}

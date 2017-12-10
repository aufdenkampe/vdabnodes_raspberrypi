package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ImageFileType {
	public static final int JPG = 0;
	public static final int GIF = 1;
	public static final int BMP = 2;
	public static final int PNG = 3;

	private static AFEnum s_ImageFileTypeEnum = new AFEnum("ImageFileType")
	.addEntry(JPG, "jpg")
	.addEntry(GIF, "gif")
	.addEntry(BMP, "bmp")
	.addEntry(PNG, "png");
	public static AFEnum getEnum(){
		return  s_ImageFileTypeEnum  ;
	}
}

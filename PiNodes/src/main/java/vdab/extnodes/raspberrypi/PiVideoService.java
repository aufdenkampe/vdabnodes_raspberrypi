package vdab.extnodes.raspberrypi;

import vdab.extnodes.raspberrypi.constants.ImageSize;

import com.lcrc.af.constants.IconCategory;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.file.FileUtility;

public class PiVideoService extends PiCameraService {
	private AFFile c_OutFile;
	
	public Integer get_IconCode(){
		return  IconCategory.NODE_VIDEO;
	}
	// SUPPORTING Methods --------------------------------------
	protected void buildCommand(){
			StringBuilder sb = new StringBuilder("raspivid -t 5000");
		
			sb.append(" -ISO ").append(get_ISO()) ;
			sb.append(" -br ").append(get_Brightness()) ;
			sb.append(" -co ").append(get_Contrast()) ;			
		
			sb.append(" -o ");
			String filename = FileUtility.buildFilenameFromTemplate(get_Filename(), null);
			c_OutFile = new AFFile(filename,"h264");
			sb.append(c_OutFile.getFilePath());
			String cmd = sb.toString();
			logInfo("Command build CMD="+cmd);
			set_Command(cmd);
		}	
}

/*LICENSE*
 * Copyright (C) 2013 - 2018 MJA Technology LLC 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
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

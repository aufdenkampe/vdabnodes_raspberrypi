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
package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ExposureMode {
	public static final int AUTO = 1;
	public static final int MANUAL = 0;
	public static final int NIGHT = 2;
	public static final int BACKLIGHT = 3;
	public static final int SPOTLIGHT = 4;
	public static final int SPORTS = 5;
	public static final int ANTISHAKE = 6;	
	
	private static AFEnum s_ExposureModeEnum = new AFEnum("ExposureMode")
	.addEntry(AUTO, "Auto")
	.addEntry(MANUAL, "Manual")
	.addEntry(NIGHT, "Night")
	.addEntry(BACKLIGHT, "Backlight")
	.addEntry(SPOTLIGHT, "Spotlight")
	.addEntry(SPORTS, "Sports")
	.addEntry(ANTISHAKE, "Antishake");
	public static AFEnum getEnum(){
		return  s_ExposureModeEnum  ;
	}
}

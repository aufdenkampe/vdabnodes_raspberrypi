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

public class CameraISO {
	public static final int ISO50 = 50;
	public static final int ISO100 = 100;
	public static final int ISO200 = 200;
	public static final int ISO400 = 400;
	public static final int ISO800 = 800;
	private static AFEnum s_CameraISOEnum = new AFEnum("CameraISO")
	.addEntry(ISO50, " 50")
	.addEntry(ISO100, "100")
	.addEntry(ISO200, "200")
	.addEntry(ISO400, "400")
	.addEntry(ISO800, "800");
	public static AFEnum getEnum(){
		return  s_CameraISOEnum  ;
	}
}

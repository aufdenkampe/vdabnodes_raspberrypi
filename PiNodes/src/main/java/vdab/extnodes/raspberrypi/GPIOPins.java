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

import com.lcrc.af.datatypes.AFEnum;
public class GPIOPins {
	public final static int GPIO0 	= 0;
	public final static int GPIO1 	= 1;
	public final static int GPIO2 	= 2;
	public final static int GPIO3  	= 3;		
	public final static int GPIO4 	= 4;
	public final static int GPIO5 	= 5;
	public final static int GPIO6 	= 6;
	public final static int GPIO7  	= 7;
	private static AFEnum s_EnumPiPorts = new AFEnum("PiPorts")
	.addEntry(GPIO0 , "GPIO 0")
	.addEntry(GPIO1 , "GPIO 1")
	.addEntry(GPIO2 , "GPIO 2")
	.addEntry(GPIO3	, "GPIO 3")
	.addEntry(GPIO4 , "GPIO 4")
	.addEntry(GPIO5 , "GPIO 5")
	.addEntry(GPIO6 , "GPIO 6")
	.addEntry(GPIO7	, "GPIO 7");
	public static AFEnum getEnum(){
		return s_EnumPiPorts ;
	}
}

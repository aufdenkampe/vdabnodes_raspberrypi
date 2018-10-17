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

import java.util.ArrayList;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class GPIOUtility {
	public static Pin[] createPinArray(String[] pinLabels){
		ArrayList<Pin> l = new ArrayList<Pin>();
		 for (String pinLabel: pinLabels){
			int code = GPIOPins.getEnum().getCode(pinLabel);
			switch (code) {
			case GPIOPins.GPIO0:
				l.add(RaspiPin.GPIO_00);
				break;
			case GPIOPins.GPIO1:
				l.add(RaspiPin.GPIO_01);
				break;
			case GPIOPins.GPIO2:
				l.add(RaspiPin.GPIO_02);
				break;
			case GPIOPins.GPIO3:
				l.add(RaspiPin.GPIO_03);
				break;
			case GPIOPins.GPIO4:
				l.add(RaspiPin.GPIO_04);
				break;
			case GPIOPins.GPIO5:
				l.add(RaspiPin.GPIO_05);
				break;
			case GPIOPins.GPIO6:
				l.add( RaspiPin.GPIO_06);
				break;
			case GPIOPins.GPIO7:
				l.add( RaspiPin.GPIO_07);
				break;
			}		 
		 }
		return l.toArray(new Pin[l.size()]);
	}
}

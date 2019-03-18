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
import com.lcrc.af.AnalysisPolledSource;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class BlinkSource extends AnalysisPolledSource {
	private GpioPinDigitalOutput c_Led1;
	private GpioPinDigitalOutput c_Led2;
	private GpioController c_Gpio;
	@Override
	public void processData() {
		if (c_Led1 != null)
			c_Led1.blink(200, getRateInMillis()/2);
		if (c_Led2 != null)
			c_Led2.blink(500, getRateInMillis());
	}
	private long getRateInMillis(){
		return get_PollRate().longValue() * 1000L;
	}
	public void _start(){

		super._start();
		try {
		c_Gpio  = GpioFactory.getInstance();
		logInfo("BLINK SOURCE 1");
		// provision gpio pin #01 & #03 as an output pins and blink
		c_Led1 = c_Gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
		logInfo("BLINK SOURCE 2");
		c_Led2 = c_Gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
		}
		catch (Exception e) {
			setError("Unable to initialize Analog inputs e>"+e);
			e.printStackTrace();
			_disable();
		}	
		
	}
	public void _stop(){
		c_Gpio.shutdown();
	}


}

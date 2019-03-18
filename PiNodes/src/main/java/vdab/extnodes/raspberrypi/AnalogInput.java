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
import com.lcrc.af.AnalysisData;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.input.AnalysisInput;
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider.ProgrammableGainAmplifierValue;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.i2c.I2CBus;

public class AnalogInput extends AnalysisInput {
	private GpioController c_Gpio;
	private GpioPinAnalogInput[] c_Inputs;
	private ADS1115GpioProvider c_GpioProvider ;
	private Integer c_MonitorInterval = Integer.valueOf(500);
	// ATTRIBUTES
	public Integer get_MonitorInterval(){
		return c_MonitorInterval;
	}
	public void set_MonitorInterval(Integer interval){
		c_MonitorInterval = interval;
	}
	public void _start(){
		super._start();
		try {
			c_Gpio  = GpioFactory.getInstance();
		
			c_GpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);
			c_Inputs = new GpioPinAnalogInput[4];
			c_Inputs[0] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A0, "MyAnalogInput-A0");
			c_Inputs[1] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A1, "MyAnalogInput-A1");
			c_Inputs[2] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A2, "MyAnalogInput-A2");
			c_Inputs[3] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A3, "MyAnalogInput-A3");
			c_GpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);
			c_GpioProvider.setEventThreshold(500, ADS1115Pin.ALL);
		
			int interval = c_MonitorInterval.intValue();
			c_GpioProvider.setMonitorInterval(interval > 50 ? interval: 50);
	        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
	        {
	            @Override
	            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
	            {
	                // RAW value
	                double value = event.getValue();
	                // percentage
	                double percent =  ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);
	                // approximate voltage ( *scaled based on PGA setting )
	                double v = c_GpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * (percent/100);
	                publish(new AnalysisEvent(AnalogInput.this, new AnalysisData("Voltage"+event.getPin(),v)));
	            }
	        };
	        c_Inputs[0].addListener(listener);
	        c_Inputs[1].addListener(listener);
	        c_Inputs[2].addListener(listener);
	        c_Inputs[3].addListener(listener);
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

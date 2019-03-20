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
import java.io.IOException;

import vdab.extnodes.raspberrypi.constants.A2DChipType;
import vdab.extnodes.raspberrypi.constants.A2DVoltageRange;

import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisDataDef;
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
import com.pi4j.io.i2c.I2CFactory;


public class AnalogInput extends AnalysisInput {
	static {
		A2DChipType.getEnum();
		A2DVoltageRange.getEnum();
	}
	private GpioController c_Gpio;
	private GpioPinAnalogInput[] c_Inputs;
	private ADS1115GpioProvider c_GpioProvider ;
	private Integer c_MonitorInterval = Integer.valueOf(100);
	private Integer c_A2DChip;
	private Integer c_ChangeThreshold = Integer.valueOf(50);
	private Integer c_VoltageRange;
	// ATTRIBUTES

	public void set_A2DChip(Integer chip){
		c_A2DChip = chip;
	}
	public Integer get_A2DChip(){
		return c_A2DChip;
	}
	public void set_VoltageRange(Integer range){
		c_VoltageRange = range;
	}
	public Integer get_VoltageRange(){
		return c_VoltageRange;
	}
	public AnalysisDataDef def_VoltageRange(AnalysisDataDef theDataDef){
		if (c_A2DChip == null || c_A2DChip.intValue() != A2DChipType.ADS1115)
			theDataDef.disable();		
		return theDataDef;
	}
	public void set_ChangeThreshold(Integer threshold){
		c_ChangeThreshold = threshold;
	}
	public Integer get_ChangeThreshold(){
		return c_ChangeThreshold;
	}
	public Integer get_MonitorInterval(){
		return c_MonitorInterval;
	}
	public void set_MonitorInterval(Integer interval){
		c_MonitorInterval = interval;
	}
	public String get_AvailableBusNumber(){
		try {
			int[] nos = I2CFactory.getBusIds();
			StringBuilder sb = new StringBuilder();
			for (int no: nos){
				sb.append(no);
				sb.append(",");
			}
			return sb.toString();
		}
		catch (IOException e) {
			return "e>"+e;
		}
	}

	public void _start(){
		super._start();
		try {
			c_Gpio  = GpioFactory.getInstance();
		
			c_GpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);
			c_Inputs = new GpioPinAnalogInput[4];
			c_Inputs[0] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A0, "Voltage_0");
			c_Inputs[1] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A1, "Voltage_1");
			c_Inputs[2] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A2, "Voltage_2");
			c_Inputs[3] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1115Pin.INPUT_A3, "Voltage_3");
			c_GpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);
		
		
			c_GpioProvider.setEventThreshold(c_ChangeThreshold.doubleValue(), ADS1115Pin.ALL);
	
			c_GpioProvider.setMonitorInterval(c_MonitorInterval.intValue());
	        
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
	                publish(new AnalysisEvent(AnalogInput.this, new AnalysisData(event.getPin().getName(),Double.valueOf(v))));
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
		super._stop();
	}
}

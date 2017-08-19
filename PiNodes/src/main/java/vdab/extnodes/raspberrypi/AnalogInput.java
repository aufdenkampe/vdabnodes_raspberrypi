package vdab.extnodes.raspberrypi;
import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.input.AnalysisInput;
import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
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
	private ADS1015GpioProvider c_GpioProvider ;
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
			logInfo("ANALOG INPUT 1");
			c_GpioProvider = new ADS1015GpioProvider(I2CBus.BUS_1, ADS1015GpioProvider.ADS1015_ADDRESS_0x48);
			logInfo("ANALOG INPUT 2");
			c_Inputs = new GpioPinAnalogInput[4];
			c_Inputs[0] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1015Pin.INPUT_A0, "MyAnalogInput-A0");
			c_Inputs[1] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1015Pin.INPUT_A1, "MyAnalogInput-A1");
			c_Inputs[2] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1015Pin.INPUT_A2, "MyAnalogInput-A2");
			c_Inputs[3] =	c_Gpio.provisionAnalogInputPin(c_GpioProvider, ADS1015Pin.INPUT_A3, "MyAnalogInput-A3");
			c_GpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_6_144V, ADS1015Pin.ALL);
			c_GpioProvider.setEventThreshold(1, ADS1015Pin.ALL);
			logInfo("ANALOG INPUT 3");
			c_GpioProvider.setMonitorInterval(c_MonitorInterval.intValue());
	        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
	        {
	            @Override
	            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
	            {
	                // RAW value
	                double value = event.getValue();
	                // percentage
	                double percent =  ((value * 100) / ADS1015GpioProvider.ADS1015_RANGE_MAX_VALUE);
	                // approximate voltage ( *scaled based on PGA setting )
	                double v = c_GpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * (percent/100);
	                publish(new AnalysisEvent(AnalogInput.this, new AnalysisData("Voltage"+event.getPin(),v)));
	            }
	        };
			logInfo("ANALOG INPUT 4");
	        c_Inputs[0].addListener(listener);
	        c_Inputs[1].addListener(listener);
	        c_Inputs[2].addListener(listener);
	        c_Inputs[3].addListener(listener);
			logInfo("ANALOG INPUT 5");
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

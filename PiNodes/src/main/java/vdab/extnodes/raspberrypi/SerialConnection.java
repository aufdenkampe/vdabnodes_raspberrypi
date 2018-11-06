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
import java.util.HashMap;
import java.util.Map.Entry;


import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisObject;
import com.lcrc.af.datatypes.AFEnum;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;

public class SerialConnection extends AnalysisObject implements SerialDataEventListener {
	public static String[] DEFAULT_DEVICES= new String[0];
	private static SerialConnection s_SampleSerialConnection = new SerialConnection();
	private static HashMap<String, SerialConnection> s_DeviceSerialConnection_map = new HashMap<String,SerialConnection>();
	public static SerialConnection getSerialConnection (String device){
		SerialConnection con = s_DeviceSerialConnection_map.get(device);
		if (con == null){
			con = new SerialConnection(device);
			// Copy the default attributes
			con.copyAttributes(s_SampleSerialConnection, new String[] {"BaudRate","StopBits","DataBits","Parity","FlowControl"});
		}
		return con;
	}
	public static boolean hasAvailableDevice(String device){
		return s_DeviceSerialConnection_map.get(device) != null;
	}

	public static String[] getAllSerialDevices(){
		ArrayList<String> l = new ArrayList<String>();
		for (Entry<String, SerialConnection> entry: s_DeviceSerialConnection_map.entrySet()){
			l.add(entry.getKey());
		}
		return l.toArray( new String[l.size()]);
	}

	public static SerialConnection getSampleConnection(){
		return s_SampleSerialConnection;
	}
	
	private String c_SerialDevice;
	
	private Integer c_DataBits;
	private Integer c_StopBits;
	private Integer c_BaudRate;
	private Integer c_FlowControl;
	private Integer c_Parity;

	private ArrayList<SerialTarget> c_SerialTargetList = new ArrayList<SerialTarget>();
	private ArrayList<SerialSource> c_SerialSourceList = new ArrayList<SerialSource>();
		
	private Serial c_Serial;	
	private SerialDataEventListener c_SerialDataEventListener ;
	
	private static AFEnum s_EnumDataBits = new AFEnum("PI4J_DataBits", DataBits.values(), "getValue");
	private static AFEnum s_EnumBaud = new AFEnum("PI4J_Baud", Baud.values(), "getValue");
	private static AFEnum s_EnumParity= new AFEnum("PI4J_Parity", Parity.values(), "getIndex");
	private static AFEnum s_EnumStopBits = new AFEnum("PI4J_StopBits", StopBits.values(), "getValue");
	private static AFEnum s_EnumFlowControl = new AFEnum("PI4J_FlowControl", FlowControl.values(), "getIndex");
	
	private static AnalysisDataDef[] s_SerialConnection_ddefs = new AnalysisDataDef[]{
	
		AnalysisObject.getClassAttributeDataDef(SerialConnection.class,"DataBits")
				.setRequired().setEnum(s_EnumDataBits).setDefaultValue(Integer.valueOf(8)).setEditOrder(12),
		AnalysisObject.getClassAttributeDataDef(SerialConnection.class,"StopBits")
				.setRequired().setEnum(s_EnumStopBits).setDefaultValue(Integer.valueOf(1)).setEditOrder(13),
		AnalysisObject.getClassAttributeDataDef(SerialConnection.class,"Parity")
				.setRequired().setEnum(s_EnumParity).setDefaultValue("NONE").setEditOrder(15),
		AnalysisObject.getClassAttributeDataDef(SerialConnection.class,"FlowControl")
				.setRequired().setEnum(s_EnumFlowControl).setDefaultValue("NONE").setEditOrder(14),
		AnalysisObject.getClassAttributeDataDef(SerialConnection.class,"BaudRate")
				.setRequired().setEnum(s_EnumBaud).setDefaultValue(Integer.valueOf(9600)).setEditOrder(11)};
	public SerialConnection(){
		c_SerialDevice ="SAMPLE";
	}
	public SerialConnection(String device){
		c_SerialDevice = device;	
	    s_DeviceSerialConnection_map.put(c_SerialDevice, this);    
	}
	public void init(){	
		if (this == s_SampleSerialConnection){
			AnalysisObject.logError(">>>>>SerialConnection.init()", "Tried to initialize the sample SerialConnection");
			return;
		}
		if (c_Serial == null){
			c_Serial = SerialFactory.createInstance();
		
			
		c_Serial.addListener(this);
		}
		AnalysisObject.logInfo(">>>>>SerialConnection.init()", "Created a serial instance CLASS="+c_Serial.getClass().getName());
	}

	public synchronized void open() throws Exception{	
		if (this == s_SampleSerialConnection){
			AnalysisObject.logError("SerialConnection.open()", "Tried to open the sample SerialConnection");
			return;
		}
		if (!c_Serial.isOpen()){
			c_Serial.open(c_SerialDevice, c_BaudRate, c_DataBits, c_Parity, c_StopBits, c_FlowControl);
			AnalysisObject.logInfo(">>>>>SerialConnection.open()", "Opened serial port CLASS="+c_Serial.getClass().getName());
		}
	
	}

	public void close(){		
		if (c_Serial != null){
			try {
				c_Serial.close();
			} 
			catch (Exception e) {
				AnalysisObject.logError("SerialConnection.close()", "Exception trying to close the serial connection e>"+e);
	
			}		
		}
	}

	public void addTarget(SerialTarget at) {
		
		if (c_SerialTargetList.contains(at))
			return;
		
		try {
			at.addDelegatedAttribute("BaudRate", this);
			at.addDelegatedAttribute("DataBits", this);
			at.addDelegatedAttribute("StopBits", this);
			at.addDelegatedAttribute("FlowControl", this);	
			at.addDelegatedAttribute("Parity", this);
			at.addDelegatedAttribute("PortSettings", this);	
			c_SerialTargetList.add(at);
		}
		catch (Exception e){
			at.setError("Unable to add this target and set delegates e>"+e);
		}
	}

	public void addInput(SerialSource as) {
		
		if (c_SerialSourceList.contains(as))
			return;
		
		try {
			as.addDelegatedAttribute("BaudRate", this);
			as.addDelegatedAttribute("DataBits", this);
			as.addDelegatedAttribute("StopBits", this);
			as.addDelegatedAttribute("FlowControl", this);	
			as.addDelegatedAttribute("Parity", this);	
			as.addDelegatedAttribute("PortSettings", this);	
			c_SerialSourceList.add(as);
		}
		catch (Exception e){
			as.setError("Unable to add this Source and set delegates e>"+e);
		}
	}
	public void removeTarget(SerialTarget at){
		if (c_SerialTargetList.contains(at)){
			at.logInfo("Removing serial target from Connection TARGET="+at);
			c_SerialTargetList.remove(at);
		}
		if (isUnattached())
			close();	
	}
	public void removeInput(SerialSource as){
		if (c_SerialSourceList.contains(as)){
			as.logInfo("Removing serial input from Connection INPUT="+as);
			c_SerialSourceList.remove(as);
		}
		if (isUnattached())
			close();
	}
	public boolean isUnattached(){
		return c_SerialSourceList.size() <= 0 && c_SerialTargetList.size() <= 0;
	}
	public boolean isRunning(){
		for (SerialSource as:c_SerialSourceList){
			if (as.isRunning())
				return true;
		}
		for (SerialTarget at:c_SerialTargetList){
			if (at.isRunning())
				return true;
		}
		return false;
	}
	public String get_PortSettings(){
		StringBuilder sb = new StringBuilder();
		if (c_BaudRate != null)
			sb.append(c_BaudRate).append(" ");
		if (c_DataBits != null) 
			sb.append(c_DataBits);
		if (c_FlowControl != null){
			String fc = s_EnumFlowControl.getLabel(c_FlowControl);
			sb.append(fc.charAt(0));			
		}
		if (c_StopBits != null)
			sb.append(c_StopBits);
		return sb.toString();
	}
	public Integer get_BaudRate(){
		return c_BaudRate;
	}
	public void set_BaudRate(Integer rate){
		c_BaudRate = rate;
	}

	public Integer get_DataBits(){
		return c_DataBits;
	}
	public void set_DataBits(Integer bits){
		c_DataBits = bits;
	}

	public Integer get_StopBits(){
		return c_StopBits;
	}
	public void set_StopBits(Integer bits){
		c_StopBits = bits;
	}

	public Integer get_FlowControl(){
		return c_FlowControl;
	}
	public void set_FlowControl(Integer control){
		c_FlowControl = control;
	}

	public Integer get_Parity(){
		return c_Parity;
	}
	public void set_Parity(Integer parity){
		c_Parity = parity;
	}

	private boolean isOpen(){
		if (c_Serial == null)
			return false;
		
		return c_Serial.isOpen();
	}
	public synchronized void  write(SerialTarget atFrom, String data) throws Exception {
		if (isOpen()){
			if (atFrom.isTraceLogging())
				atFrom.logTrace(">>>> Calling Serial Write DATA="+data);
			c_Serial.write(data);
		}
	}
	public synchronized void  writeln(SerialTarget atFrom, String data) throws Exception {
		if (isOpen()){
			if (atFrom.isTraceLogging())
				atFrom.logTrace(">>>> Calling Serial Writeln DATA="+data);
			c_Serial.writeln(data);
		}
	}
	@Override
	public void dataReceived(SerialDataEvent ev) {
		// push data to listener
		
		try {		
			if (c_SerialSourceList.size() > 0 && c_SerialSourceList.get(0).isTraceLogging()) 
				c_SerialSourceList.get(0).logTrace(">>>> Received Data to DATA="+ev.getAsciiString());
	
			for (SerialSource as: c_SerialSourceList)
					as.publishIndividualEvent(ev.getAsciiString());
			
		} catch (Throwable e) {
			AnalysisObject.logWarning("SerialInput.handler()"," Failed publishing data e>"+e);					
		}
	}
}

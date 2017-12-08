package vdab.extnodes.raspberrypi;

import vdab.extnodes.raspberrypi.constants.CameraISO;
import vdab.extnodes.raspberrypi.constants.ImageQuality;
import vdab.extnodes.raspberrypi.constants.ImageSize;

import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.AnalysisTrigger;
import com.lcrc.af.constants.TriggerCode;
import com.lcrc.af.constants.IconCategory;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.datatypes.LogMessage;
import com.lcrc.af.file.FileUtility;
import com.lcrc.af.servicehandlers.ServiceHandler_Command;
import com.lcrc.af.service.CommandService ;

public class PiCameraService extends CommandService  {
	static {
		ImageSize.getEnum();
		ImageQuality.getEnum();
		CameraISO.getEnum();
	}
	private static final String EVENTNAME_IMAGEFILECREATED = "ImageFileCreated";

	private static String s_ImageFileName_Default = "IMAGE_${DateTime}";
	
	private String c_Filename = s_ImageFileName_Default;
	private ServiceHandler_Command c_EventHandler;
	private Integer c_ImageSize ;
	private Integer c_ImageQuality ;
	private Integer c_ISO = Integer.valueOf(200);
	private Integer c_Contrast = Integer.valueOf(0);
	private Integer c_Brightness = Integer.valueOf(50);
	private String c_OtherOptions;

	private AFFile c_OutFile ;
	private Integer c_FileNo = Integer.valueOf(0);
	public Integer get_IconCode(){
		return  IconCategory.NODE_CAMERA;
	}
	public String get_Filename(){
		return c_Filename;
	}
	public void set_Filename(String name){
		c_Filename = name;
	}	
	public  void set_ISO(Integer iso){
		c_ISO = iso;
	}
	public  Integer get_ISO(){
		return c_ISO;
	}
	public void set_Brightness(Integer brightness){
		c_Brightness = brightness;
	}
	public Integer get_Brightness(){
		return c_Brightness ;
	}
	public void set_Contrast(Integer contrast){
		c_Contrast = contrast;
	}
	public Integer get_Contrast(){
		return c_Contrast;
	}
	public void set_ImageSize(Integer size){
		c_ImageSize = size;
	}
	public Integer get_ImageSize(){
		return c_ImageSize;
	}
	public void set_ImageQuality(Integer quality){
		c_ImageQuality = quality;
	}
	public Integer get_ImageQuality(){
		return c_ImageQuality;
	}
	public void set_OtherOptions(String options){
		c_OtherOptions = options;	
	}
	public String get_OtherOptions(){
		return c_OtherOptions;	
	}
	public void serviceFailed(AnalysisEvent ae, int code) {
		publish(new AnalysisTrigger(this, TriggerCode.FAILED, code));
	}
	//HACKALERT - definitely should be matched up with the original event.
	public void serviceCompleted(AnalysisEvent ae) {
		publish(new AnalysisEvent(this, new AnalysisData( EVENTNAME_IMAGEFILECREATED, c_OutFile)));		
	}

	public synchronized void processEvent(AnalysisEvent ev){	
		buildCommand();
		c_EventHandler = new ServiceHandler_Command(this, ev);
	}
	
	// SUPPORTING Methods --------------------------------------
	protected void buildCommand(){
			StringBuilder sb = new StringBuilder("raspistill -n -t 10");
		
			switch (c_ImageSize.intValue()){
			
			case ImageSize.XSMALL:
				sb.append(" -w 216 -h 162");
				break;
				
			case ImageSize.SMALL:
				sb.append(" -w 432 -h 324");
				break;
				
			case ImageSize.MEDIUM:
				sb.append(" -w 864 -h 648");
				break;
				
			case ImageSize.LARGE:
				sb.append(" -w 1296 -h 972");
				break;
				
			case ImageSize.XLARGE:
			default:
				break;
			}

			switch (c_ImageQuality.intValue()){
			case ImageQuality.LOW:
				sb.append(" -q 30");
				break;
			case ImageQuality.MEDIUM:
				sb.append(" -q 70");
				break;
			case ImageQuality.HIGH:
			default:
				sb.append(" -q 95");
				break;	
			}
			
			sb.append(" -ISO ").append(c_ISO);
			sb.append(" -br ").append(c_Brightness);
			sb.append(" -co ").append(c_Contrast);			
		
			if (c_OtherOptions != null)
				sb.append(" ").append(c_OtherOptions);
				
			sb.append(" -o ");
			String filename = FileUtility.buildFilenameFromTemplate(c_Filename, c_FileNo);
			c_FileNo++;
			c_OutFile = new AFFile(filename,"jpg");
			sb.append(c_OutFile.getFilePath());
			String cmd = sb.toString();

			set_Command(cmd);
		}	
}

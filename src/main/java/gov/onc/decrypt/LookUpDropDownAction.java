package gov.onc.decrypt;

import gov.onc.startup.ConfigInfo;
import org.apache.log4j.Logger;


import gov.onc.decrypt.LookUpDropDownActionForm;


import java.io.File;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



/**
 * Action Class to get the drop down Values from the config properties
 * @author SandeepBhanoori
 *
 */
public class LookUpDropDownAction extends Action {

	private Logger log = Logger.getLogger("certDiscoveryLogger");
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
        throws Exception {
		
	     final String configHome = "";
		 final String CONFIG_PROP_FILE = "config.properties";
		 final String dts500 , dts501 , dts502, dts505, dts515, dts506, dts507, dts517, dts511, dts520,dts512;

		 log.debug("");
	     LookUpDropDownActionForm ludf = (LookUpDropDownActionForm) form;	 

		 
		try {
			   // Load in config files
			ConfigInfo.loadConfigProperties(configHome
				+ File.separatorChar + CONFIG_PROP_FILE);
	
			 log.debug("got the properties file" +CONFIG_PROP_FILE);
	    	}	
		   			
	catch (Exception e) {
		log.error("IO Error - Loading config files in LookupDropDown Action.\n"
				+ e.getMessage());
	}		
		
		
         /* Stuffing the email into the bean */
		
		dts500 = ConfigInfo.getConfigProperty("DTS_500_EMAIL");
		ludf.setDts500(dts500);
		dts501 = ConfigInfo.getConfigProperty("DTS_501_EMAIL");
		ludf.setDts501(dts501);
		dts502 = ConfigInfo.getConfigProperty("DTS_502_EMAIL");
		ludf.setDts502(dts502);
		dts505 = ConfigInfo.getConfigProperty("DTS_505_EMAIL");
		ludf.setDts505(dts505);
		dts515 = ConfigInfo.getConfigProperty("DTS_515_EMAIL");
		ludf.setDts515(dts515);
		dts506 = ConfigInfo.getConfigProperty("DTS_506_EMAIL");
		ludf.setDts506(dts506);
		dts507 = ConfigInfo.getConfigProperty("DTS_507_EMAIL");
		ludf.setDts507(dts507);
		dts517 = ConfigInfo.getConfigProperty("DTS_517_EMAIL");
		ludf.setDts517(dts517);
		dts511 = ConfigInfo.getConfigProperty("DTS_511_EMAIL");
		ludf.setDts511(dts511);
		dts520 = ConfigInfo.getConfigProperty("DTS_520_EMAIL");
		ludf.setDts520(dts520);
		dts512 = ConfigInfo.getConfigProperty("DTS_512_EMAIL");
		ludf.setDts512(dts512);
		
		log.debug("Completed setting Config Properties");
		
		return null;
	}
}

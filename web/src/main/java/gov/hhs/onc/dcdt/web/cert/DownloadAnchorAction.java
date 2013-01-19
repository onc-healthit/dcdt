package gov.hhs.onc.dcdt.web.cert;

import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

public class DownloadAnchorAction extends DownloadAction
{
	private final static String ANCHOR_CERT_CONFIG_PROP_NAME = "CertAnchorLocation";
	private final static String ANCHOR_CERT_FILE_NAME_HEADER_KEY = "Content-disposition";
	private final static String ANCHOR_CERT_FILE_NAME_HEADER_VALUE_PREFIX = "attachment;filename=";
	private final static String ANCHOR_CERT_MIME_TYPE = "application/x-x509-ca-cert";
	
	private Logger log = Logger.getLogger("certDiscoveryLogger");
	
	@Override
	protected StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		File anchorCertFile = new File(ConfigInfo.getConfigProperty(ANCHOR_CERT_CONFIG_PROP_NAME));
		
		if (!anchorCertFile.exists())
		{
			// TODO: throw an appropriate exception instead of attempting the download anyway
			
			log.error("Unable to find anchor certificate file: " + anchorCertFile.getAbsolutePath());
		}
		
		response.setHeader(ANCHOR_CERT_FILE_NAME_HEADER_KEY, 
			ANCHOR_CERT_FILE_NAME_HEADER_VALUE_PREFIX + anchorCertFile.getName()); 
		
		return new FileStreamInfo(ANCHOR_CERT_MIME_TYPE, anchorCertFile);
	}
}
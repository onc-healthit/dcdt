package gov.hhs.onc.dcdt.testcases.discovery.results.sender.impl;

import gov.hhs.onc.dcdt.config.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import java.nio.charset.Charset;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.ModelMap;

public class DiscoveryTestcaseResultSenderServiceImpl extends AbstractToolMailSenderService implements DiscoveryTestcaseResultSenderService {
    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY = "discoveryTestcase";
    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT = MODEL_ATTR_NAME_TESTCASE_DISCOVERY + "Result";

    public DiscoveryTestcaseResultSenderServiceImpl(Charset mailEnc, VelocityEngine velocityEngine, InstanceMailAddressConfig fromConfig,
        String mimeMailMsgBeanName) {
        super(mailEnc, velocityEngine, fromConfig, mimeMailMsgBeanName);
    }

    @Override
    public void send(MailAddress to, DiscoveryTestcase discoveryTestcase, DiscoveryTestcaseResult discoveryTestcaseResult) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY, discoveryTestcase);
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT, discoveryTestcaseResult);

        this.send(to, modelMap, modelMap);
    }
}

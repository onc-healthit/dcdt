package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
import java.util.List;

public interface DiscoveryTestcaseService extends ToolBeanService<DiscoveryTestcase, DiscoveryTestcaseDao> {
    public List<DiscoveryTestcase> processDiscoveryTestcases() throws CryptographyException;
}

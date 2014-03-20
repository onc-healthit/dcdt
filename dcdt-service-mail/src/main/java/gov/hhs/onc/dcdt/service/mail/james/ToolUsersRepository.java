package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import java.util.List;
import org.apache.james.user.api.UsersRepository;

public interface ToolUsersRepository extends ToolBean, UsersRepository {
    public List<InstanceMailAddressConfig> getMailAddressConfigs();

    public void setMailAddressConfigs(List<InstanceMailAddressConfig> mailAddrConfigs);
}

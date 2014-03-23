package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.james.user.api.UsersRepository;
import org.springframework.context.ApplicationContextAware;

public interface ToolUsersRepository extends ApplicationContextAware, ToolBean, UsersRepository {
    public boolean hasMailAddressConfigs();

    @Nullable
    public List<InstanceMailAddressConfig> getMailAddressConfigs();
}

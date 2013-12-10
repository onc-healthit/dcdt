package gov.hhs.onc.dcdt.data;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.sql.DataSource;

public interface ToolBeanDataSource<T extends ToolBean> extends DataSource {
    public String getDriverClassName();

    public void setDriverClassName(String driverClassName);

    public String getPassword();

    public void setPassword(String password);

    public String getUrl();

    public void setUrl(String url);

    public String getUsername();

    public void setUsername(String username);
}

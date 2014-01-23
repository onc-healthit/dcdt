package gov.hhs.onc.dcdt.data.impl;

import gov.hhs.onc.dcdt.data.types.ToolUserType;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class ToolLocalSessionFactoryBean extends LocalSessionFactoryBean {
    private List<ToolUserType<?, ?, ?>> userTypes;

    @Override
    protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder localSessionFactoryBuilder) {
        if (this.hasUserTypes()) {
            for (ToolUserType<?, ?, ?> userType : this.userTypes) {
                localSessionFactoryBuilder.registerTypeOverride(userType, userType.getKeys());
            }
        }

        return super.buildSessionFactory(localSessionFactoryBuilder);
    }

    public boolean hasUserTypes() {
        return !CollectionUtils.isEmpty(this.userTypes);
    }

    @Nullable
    public List<ToolUserType<?, ?, ?>> getUserTypes() {
        return this.userTypes;
    }

    public void setUserTypes(@Nullable List<ToolUserType<?, ?, ?>> userTypes) {
        this.userTypes = userTypes;
    }
}

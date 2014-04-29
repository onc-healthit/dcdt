package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.config.MailGatewayCredentialConfig;
import gov.hhs.onc.dcdt.service.mail.james.ToolUsersRepository;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.james.user.api.UsersRepositoryException;
import org.apache.james.user.api.model.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ToolUsersRepositoryImpl extends AbstractToolBean implements ToolUsersRepository {
    private static class ToolJamesUserNamePredicate extends AbstractToolPredicate<InstanceMailAddressConfig> {
        protected String userName;

        public ToolJamesUserNamePredicate(String userName) {
            this.userName = userName;
        }

        @Override
        protected boolean evaluateInternal(InstanceMailAddressConfig mailAddrConfig) throws Exception {
            return Objects.equals(ToolUsersRepositoryImpl.getUserName(mailAddrConfig), this.userName);
        }
    }

    private static class ToolJamesUserNameTransformer extends AbstractToolTransformer<InstanceMailAddressConfig, String> {
        public final static ToolJamesUserNameTransformer INSTANCE = new ToolJamesUserNameTransformer();

        @Nullable
        @Override
        protected String transformInternal(InstanceMailAddressConfig mailAddrConfig) throws Exception {
            return ToolUsersRepositoryImpl.getUserName(mailAddrConfig);
        }
    }

    private static class ToolJamesUser implements Comparable<ToolJamesUser>, User {
        private ImmutablePair<String, String> pair;

        public ToolJamesUser(String name, String pass) {
            this.pair = new ImmutablePair<>(name, pass);
        }

        @Override
        public boolean verifyPassword(String pass) {
            return Objects.equals(pass, this.getPassword());
        }

        @Override
        @SuppressWarnings({ "NullableProblems" })
        public int compareTo(ToolJamesUser user) {
            return user.getUserName().compareTo(this.getUserName());
        }

        @Override
        public String getUserName() {
            return this.pair.getLeft();
        }

        public String getPassword() {
            return this.pair.getRight();
        }

        @Override
        public boolean setPassword(String pass) {
            return false;
        }
    }

    private AbstractApplicationContext appContext;
    private List<InstanceMailAddressConfig> mailAddrConfigs;

    @Override
    public boolean isProcessed(String recipientMailAddrStr) {
        InstanceMailAddressConfig recipientMailAddrConfig = CollectionUtils.find(this.mailAddrConfigs, new ToolJamesUserNamePredicate(recipientMailAddrStr));

        return ((recipientMailAddrConfig != null) && recipientMailAddrConfig.isProcessed());
    }

    @Override
    public void removeUser(String userName) throws UsersRepositoryException {
        throw new UsersRepositoryException(String.format("Unable to remove James user (name=%s) from James users repository (class=%s).", userName,
            ToolClassUtils.getName(this)));
    }

    @Override
    public void updateUser(User user) throws UsersRepositoryException {
        throw new UsersRepositoryException(String.format("Unable to update James user (name=%s) in James users repository (class=%s).", user.getUserName(),
            ToolClassUtils.getName(this)));
    }

    @Override
    public void addUser(String userName, String password) throws UsersRepositoryException {
        throw new UsersRepositoryException(String.format("Unable to add James user (name=%s) to James users repository (class=%s).", userName,
            ToolClassUtils.getName(this)));
    }

    @Override
    public boolean test(String userName, String pass) throws UsersRepositoryException {
        User user = this.getUserByName(userName);

        return ((user != null) && user.verifyPassword(pass));
    }

    @Nullable
    @Override
    public User getUserByName(String userName) throws UsersRepositoryException {
        InstanceMailAddressConfig mailAddrConfig = CollectionUtils.find(this.mailAddrConfigs, new ToolJamesUserNamePredicate(userName));

        return ((mailAddrConfig != null) ? getUser(mailAddrConfig) : null);
    }

    @Override
    public boolean contains(String userName) throws UsersRepositoryException {
        return getUserNames(this.mailAddrConfigs).contains(userName);
    }

    @Override
    public int countUsers() throws UsersRepositoryException {
        return getUserNames(this.mailAddrConfigs).size();
    }

    @Override
    public Iterator<String> list() throws UsersRepositoryException {
        return getUserNames(this.mailAddrConfigs).iterator();
    }

    @Override
    public boolean supportVirtualHosting() throws UsersRepositoryException {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.mailAddrConfigs = ToolBeanFactoryUtils.getBeansOfType(this.appContext, InstanceMailAddressConfig.class);
    }

    private static Collection<String> getUserNames(List<InstanceMailAddressConfig> mailAddrConfigs) {
        return CollectionUtils.select(CollectionUtils.collect(mailAddrConfigs, ToolJamesUserNameTransformer.INSTANCE), PredicateUtils.notNullPredicate());
    }

    @Nullable
    private static ToolJamesUser getUser(InstanceMailAddressConfig mailAddrConfig) {
        if (mailAddrConfig == null) {
            return null;
        }

        MailGatewayCredentialConfig mailGatewayCredConfig = mailAddrConfig.getGatewayCredentialConfig();

        // noinspection ConstantConditions
        return ((mailGatewayCredConfig.hasId() && mailGatewayCredConfig.hasSecret()) ? new ToolJamesUser(getUserName(mailAddrConfig),
            mailGatewayCredConfig.getSecret()) : null);
    }

    @Nullable
    private static String getUserName(InstanceMailAddressConfig mailAddrConfig) {
        MailGatewayCredentialConfig mailGatewayCredConfig = mailAddrConfig.getGatewayCredentialConfig();

        // noinspection ConstantConditions
        return (mailGatewayCredConfig.hasId() ? mailGatewayCredConfig.getId().toAddress() : null);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public boolean hasMailAddressConfigs() {
        return !CollectionUtils.isEmpty(this.mailAddrConfigs);
    }

    @Nullable
    @Override
    public List<InstanceMailAddressConfig> getMailAddressConfigs() {
        return this.mailAddrConfigs;
    }
}

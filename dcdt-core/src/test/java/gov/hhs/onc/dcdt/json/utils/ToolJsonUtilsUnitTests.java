package gov.hhs.onc.dcdt.json.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.json.ToolJsonException;
import gov.hhs.onc.dcdt.json.impl.ToolObjectMapper;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.net.InetAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;

@Test(groups = { "dcdt.test.unit.json.all", "dcdt.test.unit.json.utils.all", "dcdt.test.unit.json.utils.json" })
public class ToolJsonUtilsUnitTests extends AbstractToolUnitTests {
    @JsonSubTypes({ @Type(MailAddressImpl.class) })
    private static interface ToolTestJsonBean extends ToolBean {
        @JsonProperty(value = "domainName", required = true)
        public Name getDomainName();

        public void setDomainName(Name domainName);

        @JsonProperty(value = "ipAddr", required = true)
        public InetAddress getIpAddress();

        public void setIpAddress(InetAddress ipAddr);

        @JsonProperty(value = "mailAddr", required = true)
        public MailAddress getMailAddress();

        public void setMailAddress(MailAddress mailAddr);
    }

    @Component("toolTestJsonBeanImpl")
    @JsonTypeName("testJsonBean")
    private static class ToolTestJsonBeanImpl extends AbstractToolBean implements ToolTestJsonBean {
        @Value("${dcdt.test.json.domain.name}")
        private Name domainName;

        @Value("${dcdt.test.json.ip.addr}")
        private InetAddress ipAddr;

        @Value("${dcdt.test.json.mail.addr}")
        private MailAddress mailAddr;

        @Override
        public Name getDomainName() {
            return this.domainName;
        }

        @Override
        public void setDomainName(Name domainName) {
            this.domainName = domainName;
        }

        @Override
        public InetAddress getIpAddress() {
            return this.ipAddr;
        }

        @Override
        public void setIpAddress(InetAddress ipAddr) {
            this.ipAddr = ipAddr;
        }

        @Override
        public MailAddress getMailAddress() {
            return this.mailAddr;
        }

        @Override
        public void setMailAddress(MailAddress mailAddr) {
            this.mailAddr = mailAddr;
        }
    }

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolObjectMapper objMapper;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolTestJsonBean testJsonBean;

    private String testJsonBeanJson;

    @Test(dependsOnMethods = { "testToJson" })
    public void testFromJson() throws ToolJsonException {
        ToolTestJsonBean testJsonBeanDeserialized = ToolJsonUtils.fromJson(this.objMapper, this.testJsonBeanJson, ToolTestJsonBean.class);
        Assert.assertEquals(testJsonBeanDeserialized.getDomainName(), this.testJsonBean.getDomainName(), "Domain names do not match.");
        Assert.assertEquals(testJsonBeanDeserialized.getIpAddress(), this.testJsonBean.getIpAddress(), "IP addresses do not match.");
        Assert.assertEquals(testJsonBeanDeserialized.getMailAddress(), this.testJsonBean.getMailAddress(), "Mail addresses do not match.");
    }

    @Test
    public void testToJson() throws ToolJsonException {
        this.testJsonBeanJson = ToolJsonUtils.toJson(this.objMapper, this.testJsonBean);
        this.assertJsonContainsStringValue(this.testJsonBean.getDomainName().toString());
        this.assertJsonContainsStringValue(this.testJsonBean.getIpAddress().getHostAddress());
        this.assertJsonContainsStringValue(this.testJsonBean.getMailAddress().toAddress());
    }

    private void assertJsonContainsStringValue(String valueStr) {
        valueStr = ToolStringUtils.quote(valueStr);
        Assert.assertTrue(StringUtils.contains(this.testJsonBeanJson, valueStr),
            String.format("JSON does not contain value string (%s):\n%s", valueStr, this.testJsonBeanJson));
    }
}

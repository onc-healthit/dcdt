package gov.hhs.onc.dcdt.testcases.discovery.mail.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingJsonDto;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseMailMappingJsonDto")
@JsonTypeName("discoveryTestcaseMailMapping")
@Lazy
@Scope("prototype")
public class DiscoveryTestcaseMailMappingJsonDtoImpl extends AbstractToolBeanJsonDto<DiscoveryTestcaseMailMapping> implements
    DiscoveryTestcaseMailMappingJsonDto {
    private String directAddr;
    private String resultsAddr;
    private String msg;

    public DiscoveryTestcaseMailMappingJsonDtoImpl() {
        super(DiscoveryTestcaseMailMapping.class, DiscoveryTestcaseMailMappingImpl.class);
    }

    @Nullable
    @Override
    public String getDirectAddress() {
        return this.directAddr;
    }

    @Override
    public void setDirectAddress(@Nullable String directAddr) {
        this.directAddr = directAddr;
    }

    @Nullable
    @Override
    public String getResultsAddress() {
        return this.resultsAddr;
    }

    @Override
    public void setResultsAddress(@Nullable String resultsAddr) {
        this.resultsAddr = resultsAddr;
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public void setMessage(@Nullable String msg) {
        this.msg = msg;
    }
}

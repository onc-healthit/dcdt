package gov.hhs.onc.dcdt.discovery.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.CertificateLookupStep;
import gov.hhs.onc.dcdt.discovery.steps.CertificateValidationStep;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;

public class CertificateDiscoveryServiceImpl extends AbstractToolBean implements CertificateDiscoveryService {
    private List<CertificateDiscoveryStep> defaultSteps;

    public CertificateDiscoveryServiceImpl(List<CertificateDiscoveryStep> defaultSteps) {
        this.defaultSteps = defaultSteps;
    }

    @Override
    public List<CertificateDiscoveryStep> discoverCertificates(MailAddress directAddr) {
        return this.discoverCertificates(this.defaultSteps, directAddr, true);
    }

    @Override
    public List<CertificateDiscoveryStep> discoverCertificates(List<CertificateDiscoveryStep> steps, MailAddress directAddr, boolean processAllSteps) {
        Map<BindingType, MailAddress> directAddrBoundMap = new EnumMap<>(BindingType.class);
        MailAddress directAddrBound;
        Class<? extends CertificateDiscoveryStep> stepClass;
        BindingType stepBindingType;
        boolean certDiscovered = false, skipStep;
        List<CertificateDiscoveryStep> processedSteps = new ArrayList<>(steps.size());
        CertificateDiscoveryStep processedStep;

        for (CertificateDiscoveryStep step : steps) {
            if (!ToolClassUtils.isAssignable((stepClass = step.getClass()), CertificateValidationStep.class) && certDiscovered) {
                continue;
            }

            processedSteps.add((processedStep = ObjectUtils.clone(step)));

            skipStep = false;

            if (!directAddrBoundMap.containsKey((stepBindingType = step.getBindingType()))) {
                if ((directAddrBound = directAddr.forBindingType(stepBindingType)) == null) {
                    processedStep.getExecutionMessages().add(
                        String.format("Direct address cannot be converted to binding type (%s): %s", stepBindingType.name(), directAddr.toAddress()));

                    skipStep = true;
                }

                if (!skipStep) {
                    directAddrBoundMap.put(stepBindingType, directAddrBound);
                }
            }

            boolean isCertLookupStep = ToolClassUtils.isAssignable(stepClass, CertificateLookupStep.class);

            if (!skipStep && !processedStep.execute(processedSteps, directAddrBoundMap.get(stepBindingType)) && (!processAllSteps || !isCertLookupStep)) {
                break;
            }

            certDiscovered = (!skipStep && isCertLookupStep && ((CertificateLookupStep<?, ?, ?, ?>) processedStep).hasCertificateInfos());
        }

        return processedSteps;
    }
}

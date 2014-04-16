package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator;
import gov.hhs.onc.dcdt.testcases.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractToolTestcaseProcessor<T extends ToolTestcaseDescription, U extends ToolTestcaseConfig, V extends ToolTestcaseResult, W extends ToolTestcase<T, U>, X extends ToolTestcaseSubmission<T, U, W>>
    implements ToolTestcaseProcessor<T, U, V, W, X> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected CertificateDiscoveryService certDiscoveryService;

    @Autowired
    protected CertificateInfoValidator certInfoValidator;

    protected AbstractApplicationContext appContext;

    @Override
    public V generateTestcaseResult(X submission, List<ToolTestcaseStep> certDiscoverySteps) throws ToolTestcaseResultException {
        return null;
    }

    @Override
    public int getErrorStepPosition(U config, V result) {
        int stepPosition = 0;

        if (result.isSuccessful()) {
            return stepPosition;
        }

        List<ToolTestcaseStep> configSteps = config.getConfigSteps();
        List<ToolTestcaseStep> infoSteps = result.getInfoSteps();

        // noinspection ConstantConditions
        for (stepPosition = 0; stepPosition < configSteps.size(); stepPosition++) {
            // noinspection ConstantConditions
            if (stepPosition < infoSteps.size()) {
                ToolTestcaseStep configStep = configSteps.get(stepPosition);
                ToolTestcaseStep infoStep = infoSteps.get(stepPosition);

                if (configStep.isSuccessful() != infoStep.isSuccessful()) {
                    return stepPosition + 1;
                }
            } else {
                return stepPosition + 1;
            }
        }

        return stepPosition;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) applicationContext;
    }
}

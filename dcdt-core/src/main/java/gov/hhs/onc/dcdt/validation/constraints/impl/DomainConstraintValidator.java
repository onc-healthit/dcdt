package gov.hhs.onc.dcdt.validation.constraints.impl;

import gov.hhs.onc.dcdt.validation.constraints.Domain;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("domainConstraintValidator")
@Scope("prototype")
public class DomainConstraintValidator extends AbstractToolConstraintValidator<Domain, CharSequence> {
}

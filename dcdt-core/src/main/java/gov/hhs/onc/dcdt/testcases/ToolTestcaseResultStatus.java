package gov.hhs.onc.dcdt.testcases;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolTestcaseResultStatus")
@Lazy
@Scope("prototype")
public enum ToolTestcaseResultStatus {
    PASS("pass"), OPTIONAL("opt"), FAIL("fail");

    private String beanName;
    private String name;

    ToolTestcaseResultStatus(String name) {
        this.name = name;
    }

    public boolean isPass() {
        return this.isPass(false);
    }

    public boolean isPass(boolean includeOpt) {
        return (this == PASS) || (includeOpt && this.isOptional());
    }

    public boolean isOptional() {
        return this == OPTIONAL;
    }

    public boolean isFail() {
        return this == FAIL;
    }

    public String getName() {
        return this.name;
    }
}

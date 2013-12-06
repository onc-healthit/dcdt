package gov.hhs.onc.dcdt.validation.constraints.io.impl;

import gov.hhs.onc.dcdt.io.utils.ToolFileUtils;
import gov.hhs.onc.dcdt.validation.constraints.io.FileExists;
import java.nio.file.Path;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("fileExistsConstraintValidator")
@Scope("prototype")
public class FileExistsConstraintValidator extends AbstractToolFileConstraintValidator<FileExists> {
    @Override
    protected boolean isValidPath(Path path, ConstraintValidatorContext validatorContext) {
        boolean constraintAnnoPathExists = this.constraintAnno.value(), pathExists = ToolFileUtils.exists(this.constraintAnno.followLinks(), path);

        return (constraintAnnoPathExists && pathExists) || (!constraintAnnoPathExists && !pathExists);
    }
}

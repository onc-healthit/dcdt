package gov.hhs.onc.dcdt.validation.constraints.io.impl;

import gov.hhs.onc.dcdt.io.utils.ToolFileUtils;
import gov.hhs.onc.dcdt.validation.constraints.io.FileType;
import java.nio.file.Path;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("fileTypeConstraintValidator")
@Scope("prototype")
public class FileTypeConstraintValidator extends AbstractToolFileConstraintValidator<FileType> {
    @Override
    protected boolean isValidPath(Path path, ConstraintValidatorContext validatorContext) {
        return ToolFileUtils.isType(this.constraintAnno.followLinks(), this.constraintAnno.value(), path);
    }
}

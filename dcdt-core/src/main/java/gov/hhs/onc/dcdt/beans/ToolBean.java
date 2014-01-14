package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import javax.annotation.Nullable;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

@JsonAutoDetect(getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE, fieldVisibility = Visibility.NONE)
@JsonTypeInfo(use = Id.NAME)
public interface ToolBean extends BeanFactoryAware, BeanNameAware, InitializingBean {
    public boolean hasBeanId();

    @Nullable
    public Serializable getBeanId();

    public String getBeanName();
}

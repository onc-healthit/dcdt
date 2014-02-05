package gov.hhs.onc.dcdt.web.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Documented
@Inherited
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
@ResponseBody
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface JsonController {
}

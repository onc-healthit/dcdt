package gov.hhs.onc.dcdt.web.filter.impl;

import java.nio.charset.Charset;
import javax.servlet.ServletException;
import org.springframework.web.filter.CharacterEncodingFilter;

public class ToolCharacterEncodingFilter extends AbstractContentFilter<CharacterEncodingFilter> {
    private Charset enc;

    public ToolCharacterEncodingFilter(Charset enc) {
        super(new CharacterEncodingFilter());

        this.enc = enc;
    }

    @Override
    protected MutableFilterConfig initInternal(MutableFilterConfig filterConfig) throws ServletException {
        this.filter.setEncoding(this.enc.name());

        return super.initInternal(filterConfig);
    }

    public Charset getEncoding() {
        return enc;
    }
}

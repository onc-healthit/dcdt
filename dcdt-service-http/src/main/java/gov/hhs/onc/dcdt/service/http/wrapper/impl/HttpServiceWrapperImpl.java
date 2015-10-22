package gov.hhs.onc.dcdt.service.http.wrapper.impl;

import gov.hhs.onc.dcdt.service.http.HttpService;
import gov.hhs.onc.dcdt.service.http.impl.HttpServiceImpl;
import gov.hhs.onc.dcdt.service.http.wrapper.HttpServiceWrapper;
import gov.hhs.onc.dcdt.service.wrapper.impl.AbstractToolServiceWrapper;

public class HttpServiceWrapperImpl extends AbstractToolServiceWrapper<HttpService> implements HttpServiceWrapper {
    public HttpServiceWrapperImpl(String ... args) {
        super(HttpService.class, HttpServiceImpl.class, args);
    }

    public static void main(String ... args) {
        new HttpServiceWrapperImpl(args).start();
    }
}

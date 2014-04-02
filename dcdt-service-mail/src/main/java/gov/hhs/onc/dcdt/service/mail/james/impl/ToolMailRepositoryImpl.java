package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.service.mail.james.ToolMailRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import org.apache.mailet.Mail;

public class ToolMailRepositoryImpl extends AbstractToolNamedBean implements ToolMailRepository {
    private Map<String, Mail> mailMap = new HashMap<>();

    @Override
    public boolean unlock(String key) throws MessagingException {
        return true;
    }

    @Override
    public boolean lock(String key) throws MessagingException {
        return true;
    }

    @Nullable
    @Override
    public Mail retrieve(String key) throws MessagingException {
        return this.mailMap.get(key);
    }

    @Override
    public void remove(Collection<Mail> mails) throws MessagingException {
        for (Mail mail : mails) {
            this.remove(mail);
        }
    }

    @Override
    public void remove(Mail mail) throws MessagingException {
        this.remove(getKey(mail));
    }

    @Override
    public void remove(String key) throws MessagingException {
        this.mailMap.remove(key);
    }

    @Override
    public void store(Mail mail) throws MessagingException {
        this.mailMap.put(getKey(mail), mail);
    }

    @Override
    public Iterator<String> list() throws MessagingException {
        return this.mailMap.keySet().iterator();
    }

    private static String getKey(Mail mail) throws MessagingException {
        return mail.getMessage().getMessageID();
    }
}

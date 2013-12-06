package gov.hhs.onc.dcdt.tx.services.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.tx.services.ToolBeanService;
import gov.hhs.onc.dcdt.beans.ToolBeanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class ToolBeanServiceImpl<T extends ToolBean> implements ToolBeanService<T> {

    @Autowired
    ToolBeanDao toolBeanDao;

    @Override
    public List<T> getBeansById(Iterable<? extends Serializable> beanIdValues) {
        return toolBeanDao.getBeansById(beanIdValues);
    }

    @Override
    public boolean containsBeans(Iterable<? extends Serializable> beanIdValues) {
        return toolBeanDao.containsBeans(beanIdValues);
    }

    @Override
    public List<T> addBeans(Iterable<T> beans) {
        return toolBeanDao.addBeans(beans);
    }

    @Override
    public List<T> updateBeans(Iterable<T> beans) {
        return toolBeanDao.updateBeans(beans);
    }

    @Override
    public List<T> removeBeans(Iterable<T> beans) {
        return toolBeanDao.removeBeans(beans);
    }
}

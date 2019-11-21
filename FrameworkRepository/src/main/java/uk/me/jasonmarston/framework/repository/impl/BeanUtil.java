package uk.me.jasonmarston.framework.repository.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(
    		final ApplicationContext applicationContext) 
    		throws BeansException {
        context = applicationContext;
    }

    public static AutowireCapableBeanFactory getAutowireCapableBeanFactory() {
        return context.getAutowireCapableBeanFactory();
    }
}
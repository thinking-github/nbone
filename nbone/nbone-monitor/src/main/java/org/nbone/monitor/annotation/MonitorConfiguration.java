package org.nbone.monitor.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Set;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-14
 */
@Configuration
public class MonitorConfiguration implements ImportAware {

    private static final Logger logger = LoggerFactory.getLogger(MonitorConfiguration.class);

    private AnnotationAttributes enableMonitor;

    protected Set<Class<? extends Throwable>> ignoresException;


    @Autowired(required = false)
    void setConfigurer(MonitorConfigurer configurer) {
        if (configurer == null) {
            return;
        }
        logger.info("initialize MonitorConfigurer... ");
        useMonitorConfigurer(configurer);
    }

    protected void useMonitorConfigurer(MonitorConfigurer config) {
        addIgnoresException(config.ignoresException());
    }

    @Bean
    public MonitorAnnotationParser monitorAnnotationParser() {
        logger.info("initialize monitorAnnotationParser... ");

        MonitorAnnotationParser monitorAnnotationParser = new MonitorAnnotationParser();
        if (this.ignoresException != null) {
            monitorAnnotationParser.setIgnoresException(ignoresException);
        }
        return monitorAnnotationParser;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        logger.info("initialize setImportMetadata... ");

        this.enableMonitor = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableMonitor.class.getName(), false));
        if (this.enableMonitor == null) {
            throw new IllegalArgumentException(
                    "@EnableMonitor is not present on importing class " + importMetadata.getClassName());
        }

        Class<?>[] ignores = enableMonitor.getClassArray("ignores");
        addIgnoresException(ignores);

    }


    private void addIgnoresException(Class<?>[] ignores) {
        if (ignoresException == null && ignores != null) {
            ignoresException = new HashSet<>(ignores.length + 4);
        }

        for (Class<?> ignore : ignores) {
            ignoresException.add((Class<? extends Throwable>) ignore);
        }

        if (logger.isInfoEnabled()) {
            for (Class<?> ex : ignores) {
                logger.info("ignoresException: " + ex.getName());
            }
        }
    }
}

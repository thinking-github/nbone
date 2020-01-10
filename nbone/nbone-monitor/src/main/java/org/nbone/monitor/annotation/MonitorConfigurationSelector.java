package org.nbone.monitor.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-14
 */
public class MonitorConfigurationSelector extends AdviceModeImportSelector<EnableMonitor> {

    private static final String CONFIGURATION_CLASS_NAME = MonitorConfiguration.class.getName();

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{CONFIGURATION_CLASS_NAME};
    }
}

package org.springframework.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-04
 */
@Configuration
public class EnvironmentConfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public int get(String env, int present, int other) {
        return getValue(env, present, other);
    }

    public long get(String env, long present, long other) {
        return getValue(env, present, other);
    }

    public String get(String env, String present, String other) {
        return getValue(env, present, other);
    }


    public <T> T getValue(String env, T present, T other) {
        for (String activeProfile : environment.getActiveProfiles()) {
            if (activeProfile.equals(env)) {
                return present;
            }
        }
        return other;
    }

    public <T> T getValue(String env1, T present1, String env2, T present2, T other) {
        for (String activeProfile : environment.getActiveProfiles()) {
            if (activeProfile.equals(env1)) {
                return present1;
            }
            if (activeProfile.equals(env2)) {
                return present2;
            }
        }
        return other;
    }
}

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

    /**
     * 根据环境变量返回变量值
     *
     * @param env     environment variable
     * @param env2    environment variable
     * @param present env/env2 used value
     * @param other   other environment variable used value
     * @param <T>     返回泛型
     * @return 根据环境变量返回
     */
    public <T> T getValue(String env, String env2, T present, T other) {
        for (String activeProfile : environment.getActiveProfiles()) {
            if (activeProfile.equals(env)) {
                return present;
            }
            if (activeProfile.equals(env2)) {
                return present;
            }
        }
        return other;
    }

    /**
     * 根据环境变量返回变量值
     *
     * @param env1     environment variable
     * @param present1 env used value
     * @param env2     environment variable
     * @param present2 env2 used value
     * @param other    other environment variable used value
     * @param <T>      返回泛型
     * @return 根据环境变量返回
     */
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

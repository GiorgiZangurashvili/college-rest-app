package dev.omedia.boot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CollegeRestAppConfig {
    @Value("${minimumAge}")
    private int studentMinAge;

    @Value("${maximumAllowedRegistration}")
    private int maximumAllowedRegistration;

    @Value("${allowedDaysToRegisterBeforeStart}")
    private int allowedDaysToRegisterBeforeStart;

    @Value("${allowedDaysToRegisterAfterStart}")
    private int allowedDaysToRegisterAfterStart;

    @Value("${minimumGradeToPass}")
    private int minimumGradeToPass;

    @Value("${minimumGradeToGet}")
    private int minimumGradeToGet;

    @Value("${maximumGradeToGet}")
    private int maximumGradeToGet;
}

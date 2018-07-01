package dvoraka.codegen.test;

import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@ToString
@Service
public class ServiceGenerator {

    private final String basePackageName;

    private final String serviceName;
    private final String packageName;

    private final String baseInterfaceName;
    private final String baseInterfacePackageName;

    private final String serviceInterfaceName;


    public ServiceGenerator() {
        basePackageName = "test";

        serviceName = "cool";
        packageName = serviceName;

        baseInterfaceName = "BaseInterface";
        baseInterfacePackageName = basePackageName + "." + "common.service";

        serviceInterfaceName = StringUtils.capitalize(serviceName) + "Service";
    }
}

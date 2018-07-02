package dvoraka.codegen.test;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.IOException;

@ToString
@Service
public class ServiceGenerator {

    private final String basePackageName;

    private final String serviceName;
    private final String packageName;
    private final String servicePackageName;

    private final String baseInterfaceName;
    private final String baseInterfacePackageName;

    private final String serviceInterfaceName;
    private final String serviceImplementationName;


    public ServiceGenerator() {
        basePackageName = "test";

        serviceName = "cool";
        packageName = basePackageName + "." + serviceName;
        servicePackageName = packageName + "." + "service";

        baseInterfaceName = "BaseInterface";
        baseInterfacePackageName = basePackageName + "." + "common.service";

        serviceInterfaceName = StringUtils.capitalize(serviceName) + "Service";
        serviceImplementationName = "Default" + serviceInterfaceName;
    }

    public void generate() throws IOException {

        JavaFile javaFile;

        // service interface
        TypeSpec serviceInterface = TypeSpec.interfaceBuilder(serviceInterfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(baseInterfacePackageName, baseInterfaceName))
                .build();

        javaFile = JavaFile.builder(servicePackageName, serviceInterface)
                .build();

        javaFile.writeTo(System.out);
        System.out.println("***");

        // service implementation
        TypeSpec serviceImpl = TypeSpec.classBuilder(serviceImplementationName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(servicePackageName, serviceInterfaceName))
                .addAnnotation(Service.class)
                .build();

        javaFile = JavaFile.builder(servicePackageName, serviceImpl)
                .build();

        javaFile.writeTo(System.out);
        System.out.println("***");
        System.out.println("===");
    }

    private String buildPackage(String... packages) {

        StringBuilder packageName = new StringBuilder();
        for (String name : packages) {
            packageName.append(name);
            packageName.append(".");
        }

        packageName.deleteCharAt(packageName.length() - 1);

        return packageName.toString();
    }
}

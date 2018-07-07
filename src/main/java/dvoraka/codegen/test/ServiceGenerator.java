package dvoraka.codegen.test;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private final String serverClassName;
    private final String serverBaseClasName;


    public ServiceGenerator() {
        basePackageName = "test";

        serviceName = "cool";
        packageName = basePackageName + "." + serviceName;
        servicePackageName = buildPackage(packageName, "service");

        baseInterfaceName = "BaseInterface";
        baseInterfacePackageName = buildPackage(basePackageName, "common.service");

        serviceInterfaceName = StringUtils.capitalize(serviceName) + "Service";
        serviceImplementationName = "Default" + serviceInterfaceName;

        serverClassName = StringUtils.capitalize(serviceName) + "Server";
        serverBaseClasName = "AbstractServer";
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

        // server
        TypeSpec server = TypeSpec.classBuilder(serverClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get(packageName, serverBaseClasName))
                .addAnnotation(Service.class)
                .build();

        javaFile = JavaFile.builder(packageName, server)
                .build();

        javaFile.writeTo(System.out);
        System.out.println("***");

        // service 2 implementation
        Class<?> clazz = DirectoryStream.class;

        Method methods[] = clazz.getDeclaredMethods();

        // find super interface methods
        System.out.println(findMethods(clazz));

        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (Method m : methods) {

            // skip default methods
            if (m.isDefault()) {
                continue;
            }

            // return type
            Type retType = m.getGenericReturnType();

            // parameters
            Type[] parTypes = m.getGenericParameterTypes();
            List<ParameterSpec> parSpecs = new ArrayList<>();
            for (Type parType : parTypes) {
                ParameterSpec parSpec = ParameterSpec.builder(parType, "par")
                        .build();

                parSpecs.add(parSpec);
            }

            // exceptions
            Type[] exceptions = m.getGenericExceptionTypes();
            List<TypeName> exceptionTypes = new ArrayList<>();
            for (Type type : exceptions) {
                exceptionTypes.add(TypeName.get(type));
            }

            MethodSpec spec = MethodSpec.methodBuilder(m.getName())
                    .addAnnotation(Override.class)
                    .returns(retType)
                    .addParameters(parSpecs)
                    .addExceptions(exceptionTypes)
                    .build();

            methodSpecs.add(spec);
        }

        TypeSpec serviceImpl2 = TypeSpec.classBuilder("Impl")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(clazz)
                .addAnnotation(Service.class)
                .addMethods(methodSpecs)
                .build();

        javaFile = JavaFile.builder(servicePackageName, serviceImpl2)
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

    public List<Method> findMethods(Class<?> clazz) {
        if (clazz.getInterfaces().length == 0) {
            return Arrays.asList(clazz.getDeclaredMethods());
        }

        List<Method> methods = new ArrayList<>();
        for (Class<?> cls : clazz.getInterfaces()) {
            methods.addAll(findMethods(cls));
        }

        return methods;
    }
}

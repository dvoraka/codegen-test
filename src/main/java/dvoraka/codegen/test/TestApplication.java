package dvoraka.codegen.test;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.lang.model.element.Modifier;
import java.io.Serializable;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {

            TypeSpec serviceInterface = TypeSpec.interfaceBuilder("BaseInterface")
                    .build();

            JavaFile javaFile = JavaFile.builder("com.example.helloworld", serviceInterface)
                    .build();

            javaFile.writeTo(System.out);
            System.out.println("***");

            serviceInterface = TypeSpec.interfaceBuilder("ServiceInterface")
                    .addSuperinterface(Serializable.class)
                    .build();

            javaFile = JavaFile.builder("com.example.helloworld", serviceInterface)
                    .build();

            javaFile.writeTo(System.out);
            System.out.println("***");

            serviceInterface = TypeSpec.interfaceBuilder("ServiceInterface")
                    .addSuperinterface(ClassName.get("com.example.helloworld.interface", "TestInterface"))
                    .build();

            javaFile = JavaFile.builder("com.example.helloworld", serviceInterface)
                    .build();

            javaFile.writeTo(System.out);
            System.out.println("***");

            MethodSpec main = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(String[].class, "args")
                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                    .build();

            TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(main)
                    .build();

            javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                    .build();

            javaFile.writeTo(System.out);
        };
    }
}

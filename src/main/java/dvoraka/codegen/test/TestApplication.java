package dvoraka.codegen.test;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.lang.model.element.Modifier;

@SpringBootApplication
public class TestApplication {

    @Autowired
    private ServiceGenerator serviceGenerator;


    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {

            System.out.println(serviceGenerator);
            serviceGenerator.generate();

            TypeSpec serviceInterface = TypeSpec.interfaceBuilder("BaseInterface")
                    .build();

            JavaFile javaFile = JavaFile.builder("com.example.helloworld", serviceInterface)
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

            Directory baseDir = new Directory.DirectoryBuilder()
                    .dirType(DirType.BASE)
                    .parent(null)
                    .build();

            Directory services = new Directory.DirectoryBuilder()
                    .dirType(DirType.SERVICES)
                    .parent(baseDir)
                    .build();

            System.out.println(baseDir);
        };
    }
}

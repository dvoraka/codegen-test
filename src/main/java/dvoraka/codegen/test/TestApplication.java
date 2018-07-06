package dvoraka.codegen.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@SpringBootApplication
public class TestApplication {

    @Autowired
    private ServiceGenerator serviceGenerator;
    @Autowired
    private ObjectMapper objectMapper;


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

            Directory baseDir = new Directory.DirectoryBuilder("dvoraka.cool")
                    .dirType(DirType.BASE)
                    .parent(null)
                    .build();

            Directory services = new Directory.DirectoryBuilder("service")
                    .dirType(DirType.SERVICES)
                    .parent(baseDir)
                    .build();

            Directory services2 = new Directory.DirectoryBuilder("nestedservice")
                    .dirType(DirType.SERVICES)
                    .parent(services)
                    .build();

            System.out.println(baseDir);

            processDirs(baseDir);

            String json = objectMapper.writeValueAsString(baseDir);
            Files.write(Paths.get("test.json"), json.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        };
    }

    public void processDirs(Directory root) {
        if (root.getChildren().isEmpty()) {
            processLeaf(root);
        } else {
            for (Directory dir : root.getChildren()) {
                processDirs(dir);
            }
            process(root);
        }
    }

    public void process(Directory directory) {
        System.out.println("Processing " + directory.getName() + "...");
    }

    public void processLeaf(Directory directory) {
        try {
            Files.createDirectories(Paths.get(pkg2path(directory.getPackageName())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        process(directory);
    }

    public String pkg2path(String packageName) {
        return packageName.replace('.', '/');
    }
}

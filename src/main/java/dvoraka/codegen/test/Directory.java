package dvoraka.codegen.test;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Directory {

    private String name;
    private String className;

    @JsonBackReference
    private Directory parent;

    private DirType dirType;

    @JsonManagedReference
    private List<Directory> children;


    public Directory() {
        children = new ArrayList<>();
    }

    public void addChildren(Directory directory) {
        children.add(directory);
    }

    @JsonIgnore
    public String getPackageName() {
        if (getParent() == null) {
            return getName();
        } else {
            return getParent().getPackageName() + "." + getName();
        }
    }

    boolean isRoot() {
        return parent == null;
    }

    public static final class DirectoryBuilder {

        private String name;
        private String className;
        private Directory parent;
        private DirType dirType;


        public DirectoryBuilder(String name) {
            this.name = name;
        }

        public DirectoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DirectoryBuilder className(String className) {
            this.className = className;
            return this;
        }

        public DirectoryBuilder parent(Directory parent) {
            this.parent = parent;
            return this;
        }

        public DirectoryBuilder dirType(DirType dirType) {
            this.dirType = dirType;
            return this;
        }

        public Directory build() {
            Directory directory = new Directory();
            directory.dirType = this.dirType;
            directory.name = this.name;
            directory.className = this.className;
            directory.parent = this.parent;

            if (directory.dirType != DirType.BASE) {
                directory.parent.addChildren(directory);
            }

            return directory;
        }
    }

    @Override
    public String toString() {
        return "Directory{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", parent=" + (parent == null ? "null" : parent.getName()) +
                ", dirType=" + dirType +
                ", children=" + children +
                '}';
    }
}

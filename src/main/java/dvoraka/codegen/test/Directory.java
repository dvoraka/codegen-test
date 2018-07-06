package dvoraka.codegen.test;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Directory {

    private String name;

    private Directory parent;

    private DirType dirType;

    private List<Directory> children;


    public Directory() {
        children = new ArrayList<>();
    }

    public void addChildren(Directory directory) {
        children.add(directory);
    }

    @Override
    public String toString() {
        return "Directory{" +
                "name='" + name + '\'' +
                ", parent=" + (parent == null ? "null" : parent.getName()) +
                ", dirType=" + dirType +
                ", children=" + children +
                '}';
    }

    public static final class DirectoryBuilder {

        private String name;
        private Directory parent;
        private DirType dirType;


        public DirectoryBuilder name(String name) {
            this.name = name;
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
            directory.parent = this.parent;

            if (directory.dirType != DirType.BASE) {
                directory.parent.addChildren(directory);
            }

            return directory;
        }
    }
}

package dvoraka.codegen.test;

public enum DirType {
    BASE,
    SERVICE_ABSTRACT(true),
    SERVICE,
    SERVICE_IMPL;

    private final boolean abstractType;


    DirType() {
        this(false);
    }

    DirType(boolean abstractType) {
        this.abstractType = abstractType;
    }

    public boolean isAbstractType() {
        return abstractType;
    }
}

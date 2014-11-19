package com.zenplanner;

import org.apache.commons.lang.NotImplementedException;
import railo.commons.io.res.Resource;
import railo.runtime.SourceFile;

import java.io.File;
import java.net.URI;

public class MockSourceFile implements SourceFile {

    private File root;
    private Resource physicalFile;

    public MockSourceFile(File root, Resource physicalFile) {
        this.root = root;
        this.physicalFile = physicalFile;
    }

    @Override
    public Resource getPhyscalFile() {
        return physicalFile;
    }

    @Override
    public String getDisplayPath() {
        throw new NotImplementedException();
    }

    @Override
    public String getFullClassName() {
        try {
            URI root = this.root.toURI();
            URI file = new File(this.physicalFile.getAbsolutePath()).toURI();
            String className = root.relativize(file).getPath();
            className = className.replace(".", "_").replace("/", ".") + "$cf";
            return className;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getClassName() {
        throw new NotImplementedException();
    }

    @Override
    public String getPackageName() {
        throw new NotImplementedException();
    }

    @Override
    public String getRealPathAsVariableString() {
        throw new NotImplementedException();
    }

    @Override
    public Resource getFile() {
        return physicalFile;
    }
}

package com.zenplanner;

import org.apache.commons.lang.NotImplementedException;
import railo.commons.io.res.Resource;
import railo.runtime.Mapping;
import railo.runtime.Page;
import railo.runtime.PageSource;
import railo.runtime.SourceFile;
import railo.runtime.config.ConfigWeb;
import railo.runtime.exp.PageException;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class MockSourceFile implements SourceFile, PageSource {

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

    // ---

    @Override
    public Page loadPage(ConfigWeb config) throws PageException {
        throw new NotImplementedException();
    }

    @Override
    public Page loadPage(ConfigWeb config, Page defaultValue) throws PageException {
        throw new NotImplementedException();
    }

    @Override
    public String getRealpath() {
        throw new NotImplementedException();
    }

    @Override
    public String getFullRealpath() {
        throw new NotImplementedException();
    }

    @Override
    public String getClazz() {
        throw new NotImplementedException();
    }

    @Override
    public String getFileName() {
        throw new NotImplementedException();
    }

    @Override
    public String getJavaName() {
        throw new NotImplementedException();
    }

    @Override
    public String getComponentName() {
        throw new NotImplementedException();
    }

    @Override
    public Mapping getMapping() {
        throw new NotImplementedException();
    }

    @Override
    public boolean exists() {
        throw new NotImplementedException();
    }

    @Override
    public boolean physcalExists() {
        throw new NotImplementedException();
    }

    @Override
    public String[] getSource() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public PageSource getRealPage(String realPath) {
        throw new NotImplementedException();
    }

    @Override
    public void setLastAccessTime(long lastAccess) {
        throw new NotImplementedException();
    }

    @Override
    public long getLastAccessTime() {
        throw new NotImplementedException();
    }

    @Override
    public void setLastAccessTime() {
        throw new NotImplementedException();
    }

    @Override
    public int getAccessCount() {
        throw new NotImplementedException();
    }

    // ----
}

package com.zenplanner;

import railo.commons.io.res.Resource;
import railo.runtime.Mapping;
import railo.runtime.Page;
import railo.runtime.PageSource;
import railo.runtime.config.ConfigWeb;
import railo.runtime.exp.PageException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;

public class MockPageSource extends MockSourceFile implements PageSource {

    public MockPageSource(File root, Resource file) {
        super(root, file);
    }

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
        return getPhyscalFile().exists();
    }

    @Override
    public boolean physcalExists() {
        return getPhyscalFile().exists();
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
}

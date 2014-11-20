package com.zenplanner;

import org.apache.commons.lang.NotImplementedException;
import railo.commons.io.res.Resource;
import railo.runtime.Mapping;
import railo.runtime.PageContext;
import railo.runtime.PageSource;
import railo.runtime.config.Config;
import railo.runtime.dump.DumpData;
import railo.runtime.dump.DumpProperties;

import java.io.File;
import java.io.IOException;

public class MockMapping implements Mapping {

    private File root;

    public MockMapping(File root) {
        this.root = root;
    }

    @Override
    public ClassLoader getClassLoaderForArchive() {
        throw new NotImplementedException();
    }

    @Override
    public ClassLoader getClassLoaderForPhysical(boolean reload) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public Resource getPhysical() {
        throw new NotImplementedException();
    }

    @Override
    public String getVirtualLowerCase() {
        throw new NotImplementedException();
    }

    @Override
    public String getVirtualLowerCaseWithSlash() {
        throw new NotImplementedException();
    }

    @Override
    public Resource getArchive() {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasArchive() {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasPhysical() {
        throw new NotImplementedException();
    }

    @Override
    public Resource getClassRootDirectory() {
        throw new NotImplementedException();
    }

    @Override
    public PageSource getPageSource(String realPath) {
        File file = new File(root, realPath);
        MockResource res = new MockResource(file);
        MockPageSource source = new MockPageSource(root, res);
        return source;
    }

    @Override
    public PageSource getPageSource(String path, boolean isOut) {
        throw new NotImplementedException();
    }

    @Override
    public void check() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isHidden() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isPhysicalFirst() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isReadonly() {
        throw new NotImplementedException();
    }

    @Override
    public String getStrArchive() {
        throw new NotImplementedException();
    }

    @Override
    public String getStrPhysical() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isTrusted() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isTopLevel() {
        throw new NotImplementedException();
    }

    @Override
    public String getVirtual() {
        throw new NotImplementedException();
    }

    @Override
    public Config getConfig() {
        throw new NotImplementedException();
    }

    @Override
    public DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties properties) {
        throw new NotImplementedException();
    }
}

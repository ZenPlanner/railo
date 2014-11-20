package com.zenplanner;

import org.apache.commons.lang.NotImplementedException;
import railo.commons.io.res.Resource;
import railo.runtime.CFMLFactory;
import railo.runtime.Mapping;
import railo.runtime.config.ConfigImpl;
import railo.runtime.config.ConfigServer;
import railo.runtime.config.ConfigServerImpl;
import railo.runtime.exp.PageException;
import railo.runtime.monitor.RequestMonitor;
import railo.transformer.library.function.FunctionLib;
import railo.transformer.library.tag.TagLib;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

public class MockConfig extends ConfigImpl {
    private static final CFMLFactory factory = null;
    private static final Resource configDir = null;
    private static final Resource configFile = null;
    private static final TagLib[] tlds = null;
    private static final FunctionLib[] flds = null;

    public MockConfig() {
        super(factory, configDir, configFile);
    }

    @Override
    public void setMappings(Mapping[] mappings) {
        super.setMappings(mappings);
    }

    @Override
    protected ConfigServerImpl getConfigServerImpl() {
        throw new NotImplementedException();
    }

    @Override
    public String getUpdateType() {
        throw new NotImplementedException();
    }

    @Override
    public URL getUpdateLocation() {
        throw new NotImplementedException();
    }

    @Override
    public Resource getRootDirectory() {
        throw new NotImplementedException();
    }

    @Override
    public ConfigServer getConfigServer(String password) throws PageException {
        throw new NotImplementedException();
    }

    @Override
    public ConfigServer getConfigServer() {
        throw new NotImplementedException();
    }

    @Override
    public int getLoginDelay() {
        throw new NotImplementedException();
    }

    @Override
    public boolean getLoginCaptcha() {
        throw new NotImplementedException();
    }

    @Override
    public Resource getConfigServerDir() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isMonitoringEnabled() {
        throw new NotImplementedException();
    }

    @Override
    public RequestMonitor[] getRequestMonitors() {
        return new RequestMonitor[0];
    }
}

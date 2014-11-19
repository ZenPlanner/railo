package com.zenplanner;

import railo.commons.io.res.Resource;
import railo.runtime.SourceFile;
import railo.runtime.config.ConfigImpl;
import railo.runtime.config.ConfigServer;
import railo.runtime.config.ConfigServerImpl;
import railo.runtime.exp.PageException;
import railo.runtime.monitor.RequestMonitor;
import railo.transformer.bytecode.Page;
import railo.transformer.cfml.tag.CFMLTransformer;
import railo.transformer.library.function.FunctionLib;
import railo.transformer.library.tag.TagLib;

import java.io.File;
import java.net.URL;

public class App {

    public static void main(String[] args) throws Exception {

        MockConfig config = new MockConfig();
        CFMLTransformer cfmlTransformer = new CFMLTransformer();
        File root = new File("C:\\websites\\studio.zenplanner.local\\zen\\");
        File file = new File("C:\\websites\\studio.zenplanner.local\\zen\\tags\\radio.cfm");
        MockResource physicalFile = new MockResource(file);
        SourceFile source = new MockSourceFile(root, physicalFile);
        TagLib[] tld = new TagLib[] {};
        FunctionLib[] fld = new FunctionLib[] {};

        Page page = cfmlTransformer.transform(config,source,config.getTLDs(),config.getFLDs());
        System.out.println(page);
    }


}

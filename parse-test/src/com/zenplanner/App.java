package com.zenplanner;

import org.apache.commons.lang.NotImplementedException;
import railo.commons.io.res.Resource;
import railo.runtime.Mapping;
import railo.runtime.SourceFile;
import railo.runtime.config.ConfigImpl;
import railo.runtime.config.ConfigServer;
import railo.runtime.config.ConfigServerImpl;
import railo.runtime.exp.PageException;
import railo.runtime.monitor.RequestMonitor;
import railo.transformer.bytecode.Body;
import railo.transformer.bytecode.Page;
import railo.transformer.bytecode.ScriptBody;
import railo.transformer.bytecode.statement.Condition;
import railo.transformer.bytecode.statement.tag.Tag;
import railo.transformer.bytecode.statement.tag.TagBase;
import railo.transformer.bytecode.statement.tag.TagScript;
import railo.transformer.cfml.tag.CFMLTransformer;
import railo.transformer.library.function.FunctionLib;
import railo.transformer.library.tag.TagLib;

import java.io.File;
import java.net.URL;

public class App {

    public static void main(String[] args) throws Exception {
        File root = new File("C:\\websites\\studio.zenplanner.local\\");

        MockConfig config = new MockConfig();
        config.setMappings(new Mapping[]{
                new MockMapping(root)
        });

        CFMLTransformer cfmlTransformer = new CFMLTransformer();
        File file = new File("C:\\websites\\studio.zenplanner.local\\zenplanner\\qq\\pricing\\schedules\\index.cfm");
        MockResource physicalFile = new MockResource(file);
        SourceFile source = new MockSourceFile(root, physicalFile);
        TagLib[] tld = new TagLib[] {};
        FunctionLib[] fld = new FunctionLib[] {};

        Page page = cfmlTransformer.transform(config,source,config.getTLDs(),config.getFLDs());
        for(Object obj : page.getStatements()) {
            if(obj instanceof TagScript) {
                TagScript tag = (TagScript)obj;
                Body body = tag.getBody();
                if(body instanceof ScriptBody) {
                    ScriptBody script = (ScriptBody)body;
                    for(Object stmt : script.getStatements()) {
                        if(stmt instanceof Condition) {
                            Condition con = (Condition)stmt;
                            System.out.println(con);
                        }
                    }
                } else {
                    throw new NotImplementedException("Unknown tag: " + body.getClass());
                }
            } else if(obj instanceof TagBase) {
                TagBase tag = (TagBase)obj;
            }
            else {
                throw new NotImplementedException("Unknown tag: " + obj.getClass());
            }
        }
    }


}

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
import railo.transformer.bytecode.Statement;
import railo.transformer.bytecode.statement.Condition;
import railo.transformer.bytecode.statement.HasBody;
import railo.transformer.bytecode.statement.PrintOut;
import railo.transformer.bytecode.statement.tag.*;
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
        processObj(page);
    }

    public static void processObj(Object obj) {
        if(obj instanceof Body) {
            System.out.println("Body=" + obj);
            processBody((Body) obj);
        } else if(obj instanceof HasBody) {
            if(obj instanceof TagBase) {
                processTagBase((TagBase)obj);
            } else if(obj instanceof TagImport) {
                System.out.println("TagImport=" + obj);
            } else if(obj instanceof TagInclude) {
                System.out.println("TagInclude=" + obj);
            } else if(obj instanceof TagIf) {
                System.out.println("TagIf=" + obj);
            } else if(obj instanceof TagOutput) {
                System.out.println("TagOutput=" + obj);
            } else if(obj instanceof TagParam) {
                System.out.println("TagParam=" + obj);
            } else {
                throw new RuntimeException("Unknown tag: " + obj.getClass());
            }
            processBody(((HasBody)obj).getBody());
        } else if(obj instanceof PrintOut) {
            PrintOut po = (PrintOut)obj;
            String str = po.getExpr().toString().trim();
            if(str.length() > 0) {
                System.out.println("PrintObj: " + str);
            }
        }
        else {
            throw new RuntimeException("Unknown tag: " + obj.getClass());
        }
    }

    public static void processTagBase(TagBase tag) {
        String name = tag.getFullname();
        if (name.equals("suppresswhitespace")) {
            System.out.println(tag);
        } else if(name.equals("cfprocessingdirective")) {
            System.out.println(tag);
        } else if(name.equals("cfimport")) {
            System.out.println(tag);
        } else if(name.equals("cfinclude")) {
            System.out.println(tag);
        } else if(name.equals("cfif")) {
            System.out.println(tag);
        } else if(name.equals("skin:template")) { // TODO: Custom tags?!?
            System.out.println(tag);
        } else if(name.equals("wf:wireframe")) { // TODO: Custom tags?!?
            System.out.println(tag);
        } else if(name.equals("wf:field")) { // TODO: Custom tags?!?
            System.out.println(tag);
        } else if(name.equals("wf:controller")) { // TODO: Custom tags?!?
            System.out.println(tag);
        } else if(name.equals("wf:action")) { // TODO: Custom tags?!?
            System.out.println(tag);
        } else if(name.equals("wf:button")) { // TODO: Custom tags?!?
            System.out.println(tag);
        } else if(name.equals("cfoutput")) {
            System.out.println(tag);
        } else if(name.equals("cfparam")) {
            System.out.println(tag);
        } else if(name.equals("cfelse")) {
            System.out.println(tag);
        }
        else {
            throw new RuntimeException("Unknown tag: " + tag.getFullname());
        }
    }

    public static void processBody(Body body) {
        if(body == null) {
            return;
        }
        for(Object obj : body.getStatements()) {
            processObj(obj);
        }
    }
}

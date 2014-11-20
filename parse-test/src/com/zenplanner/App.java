package com.zenplanner;

import org.h2.util.StringUtils;
import railo.runtime.Mapping;
import railo.runtime.SourceFile;
import railo.transformer.bytecode.Body;
import railo.transformer.bytecode.Page;
import railo.transformer.bytecode.statement.HasBody;
import railo.transformer.bytecode.statement.PrintOut;
import railo.transformer.bytecode.statement.tag.*;
import railo.transformer.cfml.tag.CFMLTransformer;
import railo.transformer.library.function.FunctionLib;
import railo.transformer.library.tag.TagLib;

import java.io.File;

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

        Page page = cfmlTransformer.transform(config, source, config.getTLDs(), config.getFLDs());
        processObj(page);
    }

    public static void processObj(Object obj) {
        if (obj instanceof Body) {
            System.out.println("Body=" + obj);
            processBody((Body) obj);
        } else if (obj instanceof HasBody) {
            if (obj instanceof TagBase) {
                processTagBase((TagBase) obj);
            } else {
                throw new RuntimeException("Unknown tag: " + obj.getClass());
            }
            processBody(((HasBody) obj).getBody());
        } else if (obj instanceof PrintOut) {
            PrintOut po = (PrintOut) obj;
            String str = po.getExpr().toString().trim();
            if (str.length() > 0) {
                System.out.println("PrintObj: " + str);
            }
        } else {
            throw new RuntimeException("Unknown tag: " + obj.getClass());
        }
    }

    public static void processTagBase(TagBase tag) {
        String name = tag.getFullname();
        if (tag instanceof TagImport) {
            TagImport ti = (TagImport) tag;
            Attribute prefix = ti.getAttribute("prefix");
            Attribute taglib = ti.getAttribute("taglib");
            System.out.println("TagImport\n\tprefix=" + prefix + "\n\ttaglib=" + taglib);
        } else if (tag instanceof TagInclude) {
            TagInclude ti = (TagInclude)tag;
            Attribute template = ti.getAttribute("template");
            System.out.println("TagInclude\n\ttemplate=" + template);
        } else if (tag instanceof TagIf) {
            TagIf ti = (TagIf)tag;
            Attribute condition = ti.getAttribute("condition");
            System.out.println("TagIf\n\tcondition=" + condition);
        } else if (tag instanceof TagOutput) {
            System.out.println("TagOutput=" + tag);
        } else if (tag instanceof TagParam) {
            System.out.println("TagParam=" + tag);
        } else if (StringUtils.equals("cfprocessingdirective", name)) {
            System.out.println(tag);
        } else if (StringUtils.equals("cfelse", name)) {
            System.out.println(tag);
        } else if (name.contains(":")) { // TODO: Custom tags
            String txt = "";
            for(Object key : tag.getAttributes().keySet()) {
                txt += "\n\t" + key + "=" + tag.getAttribute((String)key);
            }
            System.out.println("CustomTag " + txt);
        } else {
            throw new RuntimeException("Unknown tag: " + tag + " name=" + name);
        }
    }

    public static void processBody(Body body) {
        if (body == null) {
            return;
        }
        for (Object obj : body.getStatements()) {
            processObj(obj);
        }
    }
}

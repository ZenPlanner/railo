package com.zenplanner;

import org.h2.util.StringUtils;
import railo.runtime.Mapping;
import railo.runtime.SourceFile;
import railo.transformer.bytecode.Body;
import railo.transformer.bytecode.Page;
import railo.transformer.bytecode.Statement;
import railo.transformer.bytecode.statement.HasBody;
import railo.transformer.bytecode.statement.PrintOut;
import railo.transformer.bytecode.statement.tag.*;
import railo.transformer.cfml.tag.CFMLTransformer;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class App {

    private static Map<String,Set<String>> map = new TreeMap<String, Set<String>>();

    public static void main(String[] args) throws Exception {
        String rootPath = args[0];

        File root = new File(rootPath);

        MockConfig config = new MockConfig();
        config.setMappings(new Mapping[]{
                new MockMapping(root)
        });

        CFMLTransformer cfmlTransformer = new CFMLTransformer();
        File file = new File(root, "zenplanner\\qq\\pricing\\schedules\\index.cfm");
        MockResource physicalFile = new MockResource(file);
        SourceFile source = new MockSourceFile(root, physicalFile);

        Page page = cfmlTransformer.transform(config, source, config.getTLDs(), config.getFLDs());
        recurse(page);
    }

    private static void recurse(Statement stmt) {
        process(stmt);

        if(stmt instanceof Body) {
            recurse((Body)stmt);
        }
        if(stmt instanceof HasBody) {
            recurse(((HasBody)stmt).getBody());
        }
    }

    private static void recurse(Body body) {
        if(body == null) {
            return;
        }
        for(Object obj : body.getStatements()) {
            Statement stmt = (Statement)obj;
            recurse(stmt);
        }
    }

    private static void process(Statement stmt) {
        if(stmt instanceof PrintOut) {
            PrintOut po = (PrintOut) stmt;
            String str = po.getExpr().toString().trim();
            if (str.length() > 0) {
                System.out.println("PrintObj:\n\t" + str);
            }
            return;
        }
        if (stmt instanceof TagImport) {
            TagImport ti = (TagImport) stmt;
            Attribute prefix = ti.getAttribute("prefix");
            Attribute taglib = ti.getAttribute("taglib");
            System.out.println("TagImport\n\tprefix=" + prefix + "\n\ttaglib=" + taglib);
            return;
        }
        if (stmt instanceof TagInclude) {
            TagInclude ti = (TagInclude)stmt;
            Attribute template = ti.getAttribute("template");
            System.out.println("TagInclude\n\ttemplate=" + template);
            return;
        }
        if (stmt instanceof TagIf) {
            TagIf ti = (TagIf)stmt;
            Attribute condition = ti.getAttribute("condition");
            System.out.println("TagIf\n\tcondition=" + condition);
            return;
        }
        if (stmt instanceof TagOutput) {
            System.out.println("TagOutput:\n\t" + stmt);
            return;
        }
        if (stmt instanceof TagParam) {
            System.out.println("TagParam:\n\t" + stmt);
            return;
        }
        if(stmt instanceof Tag) {
            processTag((Tag)stmt);
            return;
        }
        throw new RuntimeException("Unknown statement: " + stmt);
    }

    private static void processTag(Tag tag) {
        String name = tag.getFullname();
        if (StringUtils.equals("cfprocessingdirective", name)) {
            System.out.println("CfProcessingDirective:\n\t" + tag);
            return;
        }
        if (StringUtils.equals("cfelse", name)) {
            System.out.println("CfElse:\n\t" + tag);
            return;
        }
        if (name.contains(":")) { // TODO: Custom tags
            String txt = "";
            for(Object key : tag.getAttributes().keySet()) {
                txt += "\n\t" + key + "=" + tag.getAttribute((String)key);
            }
            System.out.println("CustomTag " + txt);
            return;
        }
        throw new RuntimeException("Unknown tag: " + tag + " name=" + name);
    }

}

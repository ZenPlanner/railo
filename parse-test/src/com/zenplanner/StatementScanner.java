package com.zenplanner;

import org.h2.util.StringUtils;
import railo.transformer.bytecode.Body;
import railo.transformer.bytecode.Page;
import railo.transformer.bytecode.Statement;
import railo.transformer.bytecode.statement.HasBody;
import railo.transformer.bytecode.statement.PrintOut;
import railo.transformer.bytecode.statement.tag.*;
import railo.transformer.library.tag.TagLibTag;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StatementScanner {

    private final Set<String> references;

    public StatementScanner(Set<String> references) {
        this.references = references;
    }

    public void process(Statement stmt) {
        recurse(stmt, null);
    }

    private void recurse(Statement stmt, Context context) {
        process(stmt, context);

        if(stmt instanceof Body) {
            recurse((Body)stmt, context);
        }
        if(stmt instanceof HasBody) {
            recurse(((HasBody)stmt).getBody(), context);
        }
    }

    private void recurse(Body body, Context context) {
        if(body == null) {
            return;
        }
        context = new Context(context);
        for(Object obj : body.getStatements()) {
            Statement stmt = (Statement)obj;
            recurse(stmt, context);
        }
    }

    private void process(Statement stmt, Context context) {
        if(stmt instanceof Page) {
            Page p = (Page)stmt;
            System.out.println("Page:\n\t" + p);
            return;
        }
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
            context.addImport(prefix.getValue().toString(), taglib.getValue().toString());
            System.out.println("TagImport\n\tprefix=" + prefix + "\n\ttaglib=" + taglib);
            return;
        }
        if (stmt instanceof TagInclude) {
            TagInclude ti = (TagInclude)stmt;
            Attribute template = ti.getAttribute("template");
            references.add(template.getValue().toString());
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

    private void processTag(Tag tag) {
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
            TagLibTag tlt = tag.getTagLibTag().getTagLib().getAppendixTag(null);
            String fileName = tag.getAppendix() + ".cfm";
            String path = (String)tlt.getAttribute("__custom_tag_path").getDefaultValue() + "/";
            path += fileName;
            references.add(path);
            System.out.println("CustomTag " + txt);
            return;
        }
        throw new RuntimeException("Unknown tag: " + tag + " name=" + name);
    }

    // ------------------------------------------- Context ------------------------------------------------------------
    private static class Context {

        private final Context parent;
        private final Map<String,String> imports = new HashMap<String, String>();

        public Context(Context parent) {
            this.parent = parent;
        }

        public void addImport(String prefix, String taglib) {
            prefix = prefix.toLowerCase();
            if(imports.containsKey(prefix)) {
                throw new RuntimeException("Prefix is already defined: " + prefix);
            }
            imports.put(prefix, taglib);
        }

        public String getTagLib(String prefix) {
            prefix = prefix.toLowerCase();
            if(imports.containsKey(prefix)) {
                return imports.get(prefix);
            }
            if(parent != null) {
                return parent.getTagLib(prefix);
            }
            return null;
        }
    }
}

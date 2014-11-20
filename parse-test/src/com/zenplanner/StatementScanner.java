package com.zenplanner;

import railo.transformer.bytecode.Body;
import railo.transformer.bytecode.Page;
import railo.transformer.bytecode.Statement;
import railo.transformer.bytecode.expression.Expression;
import railo.transformer.bytecode.expression.var.*;
import railo.transformer.bytecode.statement.Condition;
import railo.transformer.bytecode.statement.ExpressionStatement;
import railo.transformer.bytecode.statement.HasBody;
import railo.transformer.bytecode.statement.PrintOut;
import railo.transformer.bytecode.statement.tag.*;
import railo.transformer.library.tag.TagLibTag;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StatementScanner {

    private final Set<String> references;

    public StatementScanner(Set<String> references) {
        this.references = references;
    }

    public void scan(Statement stmt) throws Exception {
        processStatement(stmt);
    }

    private void processStatement(Statement stmt) throws Exception {
        if(stmt instanceof Page) {
            Page p = (Page)stmt;
            processBody(p);
            return;
        }
        if(stmt instanceof PrintOut) {
            return; // No recursion
        }
        if (stmt instanceof TagImport) {
            TagImport ti = (TagImport) stmt;
            processBody(ti.getBody());
            return;
        }
        if (stmt instanceof TagInclude) {
            TagInclude ti = (TagInclude)stmt;
            Attribute template = ti.getAttribute("template");
            references.add(template.getValue().toString().toLowerCase());
            processBody(ti.getBody());
            return;
        }
        if (stmt instanceof TagIf) {
            TagIf ti = (TagIf)stmt;
            processBody(ti.getBody());
            return;
        }
        if (stmt instanceof TagOutput) {
            TagOutput ti = (TagOutput)stmt;
            processBody(ti.getBody());
            return;
        }
        if (stmt instanceof TagParam) {
            TagParam tp = (TagParam)stmt;
            processBody(tp.getBody());
            return;
        }
        if (stmt instanceof ExpressionStatement) {
            ExpressionStatement es = (ExpressionStatement)stmt;
            Field f = ExpressionStatement.class.getDeclaredField("expr");
            f.setAccessible(true);
            Expression exp = (Expression)f.get(es);
            processExpression(exp);
            return;
        }
        if(stmt instanceof Tag) {
            processTag((Tag)stmt);
            return;
        }
        if(stmt instanceof Condition) {
            Condition con = (Condition)stmt;
            for(Body body : con.getBodies()) {
                processBody(body);
            }
            return;
        }
        throw new RuntimeException("Unknown statement: " + stmt);
    }

    private void processBody(Body body) throws Exception {
        if(body == null) {
            return;
        }
        for(Object obj : body.getStatements()) {
            Statement stmt = (Statement)obj;
            processStatement(stmt);
        }
    }

    private void processExpression(Expression exp) {
        if(exp instanceof Assign) {
            Assign ass = (Assign)exp;
            Expression child = ass.getValue();
            processExpression(child);
            return;
        }
        if(exp instanceof Variable) {
            Variable var = (Variable)exp;
            for(Object obj : var.getMembers()) {
                processMember((Member) obj);
            }
            return;
        }
        throw new RuntimeException("Unknown Expression: " + exp);
    }

    private void processMember(Member mem) {
        if(mem instanceof BIF) {
            BIF bif = (BIF)mem;
            String className = bif.getClassName();
            if("railo.runtime.functions.other.CreateObject".equalsIgnoreCase(className)) {
                Argument[] args = bif.getArguments();
                String arg0 = args[0].getValue().toString();
                if(!"component".equalsIgnoreCase(arg0)) {
                    return;
                }
                String arg1 = args[0].getValue().toString();
                return;
            }
            return;
        }
        if(mem instanceof DataMember) {
            return;
        }
        if(mem instanceof UDF) {
            return;
        }
        throw new RuntimeException("Unknown Member: " + mem);
    }

    private void processTag(Tag tag) {
        String name = tag.getFullname();
        if(name == null) {
            return; // Null tag hack for pure CFCs
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
            //System.out.println("CustomTag " + txt);
            return;
        }
    }

}

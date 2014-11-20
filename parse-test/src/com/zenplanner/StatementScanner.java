package com.zenplanner;

import railo.transformer.bytecode.Body;
import railo.transformer.bytecode.Page;
import railo.transformer.bytecode.Statement;
import railo.transformer.bytecode.cast.CastBoolean;
import railo.transformer.bytecode.cast.CastString;
import railo.transformer.bytecode.expression.Expression;
import railo.transformer.bytecode.expression.var.*;
import railo.transformer.bytecode.expression.var.Argument;
import railo.transformer.bytecode.literal.LitBoolean;
import railo.transformer.bytecode.literal.LitDouble;
import railo.transformer.bytecode.literal.LitString;
import railo.transformer.bytecode.op.*;
import railo.transformer.bytecode.statement.*;
import railo.transformer.bytecode.statement.tag.*;
import railo.transformer.library.tag.TagLibTag;

import java.lang.reflect.Field;
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
        Class<?> clazz = stmt.getClass();
        if (clazz == Page.class) {
            Page p = (Page) stmt;
            processBody(p);
            return;
        }
        if (clazz == PrintOut.class) {
            PrintOut po = (PrintOut)stmt;
            processExpression(po.getExpr());
            return;
        }
        if (clazz == TagImport.class) {
            TagImport ti = (TagImport) stmt;
            processBody(ti.getBody());
            return;
        }
        if (clazz == TagInclude.class) {
            TagInclude ti = (TagInclude) stmt;
            Attribute template = ti.getAttribute("template");
            references.add(template.getValue().toString().toLowerCase());
            processBody(ti.getBody());
            return;
        }
        if (clazz == TagIf.class) {
            TagIf ti = (TagIf) stmt;
            processBody(ti.getBody());
            return;
        }
        if (clazz == TagOutput.class) {
            TagOutput ti = (TagOutput) stmt;
            processBody(ti.getBody());
            return;
        }
        if (clazz == TagParam.class) {
            TagParam tp = (TagParam) stmt;
            processBody(tp.getBody());
            return;
        }
        if (clazz == ExpressionStatement.class) {
            ExpressionStatement es = (ExpressionStatement) stmt;
            Field f = ExpressionStatement.class.getDeclaredField("expr");
            f.setAccessible(true);
            Expression exp = (Expression) f.get(es);
            processExpression(exp);
            return;
        }
        if (clazz == Condition.class) {
            Condition con = (Condition) stmt;
            for (Body body : con.getBodies()) {
                processBody(body);
            }
            return;
        }
        if(clazz == TagComponent.class) {
            TagComponent comp = (TagComponent)stmt;
            Attribute attr = comp.getAttribute("extends");
            if(attr == null) {
                return;
            }
            String superClass = attr.getValue().toString().toLowerCase();
            references.add(superClass);
            processBody(comp.getBody());
            return;
        }
        if (clazz == Tag.class) {
            Tag tag = (Tag)stmt;
            processTag(tag);
            processBody(tag.getBody());
            return;
        }
        if(clazz == Switch.class) {
            Switch sw = (Switch)stmt;
            for(Body body : sw.getBodies()) {
                processBody(body);
            }
            return;
        }
        if (stmt instanceof Body) {
            Body tag = (Body)stmt;
            processBody(tag);
            return;
        }
        if (stmt instanceof HasBody) {
            HasBody tag = (HasBody)stmt;
            processBody(tag.getBody());
            return;
        }
        if(stmt instanceof Return) {
            return;
        }
        if(stmt instanceof TryCatchFinally) {
            TryCatchFinally tcf = (TryCatchFinally)stmt;
            for(Body body : tcf.getBodies()) {
                processBody(body);
            }
            return;
        }
        throw new RuntimeException("Unknown statement: " + stmt);
    }

    private void processBody(Body body) throws Exception {
        if (body == null) {
            return;
        }
        for (Object obj : body.getStatements()) {
            Statement stmt = (Statement) obj;
            processStatement(stmt);
        }
    }

    private void processExpression(Expression exp) throws Exception {
        Class<?> clazz = exp.getClass();
        if (clazz == Assign.class || clazz == OpVariable.class) {
            Assign ass = (Assign) exp;
            Expression child = ass.getValue();
            processExpression(child);
            return;
        }
        if (clazz == Variable.class) {
            Variable var = (Variable) exp;
            for (Object obj : var.getMembers()) {
                processMember((Member) obj);
            }
            return;
        }
        if(clazz == OpContional.class) {
            OpContional op = (OpContional)exp;
            Field cont = OpContional.class.getDeclaredField("cont");
            cont.setAccessible(true);
            processExpression((Expression)cont.get(op));

            processLAndR(exp);

            return;
        }
        if(clazz == OPDecision.class) {
            processLAndR(exp);
            return;
        }
        if(clazz == OpString.class) {
            processLAndR(exp);
            return;
        }
        if(clazz == OpDouble.class) {
            processLAndR(exp);
            return;
        }
        if(clazz == OpBool.class) {
            processLAndR(exp);
            return;
        }
        if(clazz == CastString.class) {
            CastString cast = (CastString)exp;
            processExpression(cast.getExpr());
            return;
        }
        if(clazz == OpNegate.class) {
            Field expr = exp.getClass().getDeclaredField("expr");
            expr.setAccessible(true);
            processExpression((Expression)expr.get(exp));
            return;
        }
        if(clazz == OpNegateNumber.class) {
            Field expr = exp.getClass().getDeclaredField("expr");
            expr.setAccessible(true);
            processExpression((Expression)expr.get(exp));
            return;
        }
        if(clazz == CastBoolean.class) {
            Field expr = exp.getClass().getDeclaredField("expr");
            expr.setAccessible(true);
            processExpression((Expression)expr.get(exp));
            return;
        }
        if(clazz == LitString.class) {
            return;
        }
        if(clazz == LitBoolean.class) {
            return;
        }
        if(clazz == LitDouble.class) {
            return;
        }
        throw new RuntimeException("Unknown Expression: " + exp);
    }

    private void processLAndR(Object op) throws Exception {
        Field left = op.getClass().getDeclaredField("left");
        left.setAccessible(true);
        processExpression((Expression)left.get(op));

        Field right = op.getClass().getDeclaredField("right");
        right.setAccessible(true);
        processExpression((Expression)right.get(op));
    }

    private void processMember(Member mem) {
        Class<?> clazz = mem.getClass();
        if (clazz == BIF.class) {
            BIF bif = (BIF) mem;
            String className = bif.getClassName();
            if ("railo.runtime.functions.other.CreateObject".equalsIgnoreCase(className)) {
                Argument[] args = bif.getArguments();
                String arg0 = args[0].getValue().toString();
                if (!"component".equalsIgnoreCase(arg0)) {
                    return;
                }
                String arg1 = args[1].getValue().toString().toLowerCase();
                references.add(arg1);
                return;
            }
            return;
        }
        if (clazz == DataMember.class) {
            return;
        }
        if (clazz == UDF.class) {
            return;
        }
        throw new RuntimeException("Unknown Member: " + mem);
    }

    private void processTag(Tag tag) {
        String name = tag.getFullname();
        if (name == null) {
            return; // Null tag hack for pure CFCs
        }
        if (!name.contains(":")) {
            return;
        }

        // Custom tags
        String txt = "";
        for (Object key : tag.getAttributes().keySet()) {
            txt += "\n\t" + key + "=" + tag.getAttribute((String) key);
        }
        TagLibTag tlt = tag.getTagLibTag().getTagLib().getAppendixTag(null);
        String fileName = tag.getAppendix() + ".cfm";
        String path = tlt.getAttribute("__custom_tag_path").getDefaultValue() + "/";
        path += fileName;
        references.add(path);
    }

}

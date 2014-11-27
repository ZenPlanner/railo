package com.zenplanner;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import railo.transformer.bytecode.Body;
import railo.transformer.bytecode.Page;
import railo.transformer.bytecode.Statement;
import railo.transformer.bytecode.cast.CastBoolean;
import railo.transformer.bytecode.cast.CastString;
import railo.transformer.bytecode.expression.ExprString;
import railo.transformer.bytecode.expression.Expression;
import railo.transformer.bytecode.expression.var.Argument;
import railo.transformer.bytecode.expression.var.*;
import railo.transformer.bytecode.literal.LitBoolean;
import railo.transformer.bytecode.literal.LitDouble;
import railo.transformer.bytecode.literal.LitString;
import railo.transformer.bytecode.op.*;
import railo.transformer.bytecode.statement.*;
import railo.transformer.bytecode.statement.tag.*;
import railo.transformer.library.tag.TagLibTag;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

public class StatementScanner {

    private final TitanGraph graph;
    private final File root;
    private final File file;
    private final File folder;
    private final String path;
    private final Vertex vertex;
    private final Map<String,Vertex> map;

    public StatementScanner(TitanGraph graph, Map<String,Vertex> map, File root, File file) {
        this.graph = graph;
        this.map = map;
        this.root = root;
        this.file = file;
        this.folder = file.getParentFile();

        this.path = StatementScanner.makeRelative(root, file);
        this.vertex = addOrGet(graph, this.path);
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
            addRef(template.getValue());
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
            addRef(attr.getValue());
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
        if(clazz == Function.class) {
            Function func = (Function)stmt;

            // Add return value
            Field returnField = Function.class.getDeclaredField("returnType");
            returnField.setAccessible(true);
            ExprString returnExpr = (ExprString)returnField.get(func);
            addRef(returnExpr);

            // TODO: process arguments

            // Process body
            processBody(func.getBody());
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
                addRef(args[1].getValue());
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

    private void addRef(Expression exp) {
        String ref = getName(exp);
        if("string".equals(ref)) {
            return;
        }
        if("any".equals(ref)) {
            return;
        }
        if("query".equals(ref)) {
            return;
        }
        if("number".equals(ref)) {
            return;
        }
        if("array".equals(ref)) {
            return;
        }
        if("boolean".equals(ref)) {
            return;
        }
        if("integer".equals(ref)) {
            return;
        }
        if("struct".equals(ref)) {
            return;
        }
        if("void".equals(ref)) {
            return;
        }
        if("numeric".equals(ref)) {
            return;
        }
        if(ref.toUpperCase() != ref) { // Hack to signify NULL or DYNAMIC
            String resolved = resolvePath(ref);
            if(resolved == null) {
                System.out.println("Parsing " + file.getPath() + " file not found: " + ref);
                return;
            }
            ref = resolved;
        }

        Vertex child = addOrGet(graph, ref);
        graph.addEdge(null, this.vertex, child, "child");
    }

    private String resolvePath(String ref) {
        ref = normalize(ref);
        File relative = new File(folder, ref);
        if(relative.exists()) {
            ref = makeRelative(root, relative);
            ref = normalize(ref);
            return ref;
        }
        File absolute = new File(root, ref);
        if(absolute.exists()) {
            ref = makeRelative(root, absolute);
            ref = normalize(ref);
            return ref;
        }
        return null;
    }

    private static String normalize(String ref) {
        String ext;
        if (ref.endsWith(".cfm")) {
            ext = ".cfm";
            ref = ref.substring(0, ref.length() - 4);
        } else if (ref.endsWith(".cfc")) {
            ext = ".cfc";
            ref = ref.substring(0, ref.length() - 4);
        } else {
            ext = ".cfc";
        }
        normalizeFolderSeperator(ref);
        ref = ref.replace('.', '/');
        while(ref.contains("//")) {
            ref = ref.replaceAll("//","/");
        }
        ref = ref + ext;
        return ref;
    }

    private static String normalizeFolderSeperator(String path) {
        return path.replace('\\', '/');
    }

    private String getName(Expression exp) {
        if(exp == null) {
            return "NULL";
        }
        if(exp instanceof CastString) {
            return "DYNAMIC";
        }
        if(exp instanceof LitString) {
            LitString lit = (LitString)exp;
            return lit.getString().toLowerCase();
        }
        if(exp instanceof OpString) {
            return "DYNAMIC";
        }
        throw new RuntimeException("Unknown type: " + exp);
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

        Vertex child = addOrGet(graph, path);
        graph.addEdge(null, this.vertex, child, "child");
    }

    public static String makeRelative(File root, File child) {
        String path = root.toURI().relativize(child.toURI()).getPath().toLowerCase();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    private Vertex addOrGet(TitanGraph graph, String path) {
        if(map.containsKey(path)) {
            return map.get(path);
        }
        Vertex vert = graph.addVertex(path);
        vert.setProperty("path", path);
        map.put(path, vert);
        return vert;
    }

}

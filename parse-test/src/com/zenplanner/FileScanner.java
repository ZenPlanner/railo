package com.zenplanner;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.io.FilenameUtils;
import railo.runtime.SourceFile;
import railo.runtime.config.ConfigImpl;
import railo.runtime.exp.TemplateException;
import railo.transformer.bytecode.Page;
import railo.transformer.cfml.tag.CFMLTransformer;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FileScanner {

    private final TitanGraph graph;
    private final Map<String,Vertex> map;

    public FileScanner(TitanGraph graph, Map<String,Vertex> map) {
        this.graph = graph;
        this.map = map;
    }

    public void scan(ConfigImpl config, CFMLTransformer parser, File root) throws Exception {
        scan(config, parser, root, root);
    }

    private void scan(ConfigImpl config, CFMLTransformer parser, File root, File file) throws Exception {
        if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                String ext = FilenameUtils.getExtension(child.getName());
                if(child.isFile() && !"cfm".equals(ext) && !"cfc".equals(ext)) {
                    continue;
                }
                if("WEB-INF".equals(child.getName()) || "mxunit".equals(child.getName())) {
                    continue; // TODO: Un hard code
                }
                scan(config, parser, root, child);
            }
        } else {
            MockResource physicalFile = new MockResource(file);
            SourceFile source = new MockSourceFile(root, physicalFile);
            Page page;
            try {
                page = parser.transform(config, source, config.getTLDs(), config.getFLDs());
            } catch (TemplateException ex) {
                System.out.println(ex.getMessage() + ": " + file + ":" + ex.getLine());
                return;
            }  catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            new StatementScanner(graph, map, root, file).scan(page); // TODO: better OO
        }
    }

}

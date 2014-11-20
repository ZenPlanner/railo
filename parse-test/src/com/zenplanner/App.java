package com.zenplanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.h2.util.StringUtils;
import railo.commons.io.FileUtil;
import railo.commons.lang.StringUtil;
import railo.runtime.Mapping;
import railo.runtime.SourceFile;
import railo.runtime.config.ConfigImpl;
import railo.transformer.bytecode.Page;
import railo.transformer.cfml.tag.CFMLTransformer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class App {

    public static void main(String[] args) throws Exception {
        String rootPath = args[0];

        File root = new File(rootPath);
        MockConfig config = new MockConfig();
        config.setMappings(new Mapping[]{
                new MockMapping(root)
        });
        CFMLTransformer parser = new CFMLTransformer();
        Map<String,Set<String>> map = new FileScanner().scan(config, parser, root);
        System.out.println("Scanned " + map.size() + " files");
        write(new File("out.gv"), map);
    }

    public static void write(File file, Map<String,Set<String>> map) throws Exception {
        FileWriter w = null;
        BufferedWriter writer = null;
        try {
            w = new FileWriter(file);
            writer = new BufferedWriter(w);
            writer.write("digraph abstract {\n");
            for(Map.Entry<String,Set<String>> entry : map.entrySet()) {
                String parent = entry.getKey();
                Set<String> children = entry.getValue();
                for (String child : children) {
                    writer.write(FileScanner.gvSafe(parent) + " -> " + FileScanner.gvSafe(child) + ";\n");
                }
            }
            writer.write("}\n");
            writer.flush();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if(w != null) {
                w.close();
            }
            if(writer != null) {
                writer.close();
            }
        }
    }

}

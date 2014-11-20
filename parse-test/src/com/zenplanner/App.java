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

import java.io.File;
import java.util.HashSet;
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
        CFMLTransformer parser = new CFMLTransformer();
        process(config, parser, root, root);
        System.out.println("Scanned " + map.size() + " files");
    }

    private static void process(ConfigImpl config, CFMLTransformer parser, File root, File file) throws Exception {
        if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                String ext = FilenameUtils.getExtension(child.getName());
                if(child.isFile() && !"cfm".equals(ext) && !"cfc".equals(ext)) {
                    continue;
                }
                process(config, parser, root, child);
            }
        } else {
            MockResource physicalFile = new MockResource(file);
            SourceFile source = new MockSourceFile(root, physicalFile);
            Page page;
            try {
                page = parser.transform(config, source, config.getTLDs(), config.getFLDs());
            }  catch (Exception ex) {
                System.out.println("Error parsing file: " + file);
                return;
            }

            Set<String> refs = new HashSet<String>();
            new StatementScanner(refs).scan(page);
            map.put(file.getName(), refs);
            System.out.println(refs.size() + " references in " + file);
        }
    }

}

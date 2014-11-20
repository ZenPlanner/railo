package com.zenplanner;

import org.apache.commons.io.FilenameUtils;
import railo.runtime.SourceFile;
import railo.runtime.config.ConfigImpl;
import railo.transformer.bytecode.Page;
import railo.transformer.cfml.tag.CFMLTransformer;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FileScanner {

    private final Map<String,Set<String>> map = new TreeMap<String, Set<String>>();

    public FileScanner() {

    }

    public Map<String,Set<String>> scan(ConfigImpl config, CFMLTransformer parser, File root) throws Exception {
        scan(config, parser, root, root);
        return map; // TODO: better OO
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
            }  catch (Exception ex) {
                System.out.println("Error parsing file: " + file);
                return;
            }

            Set<String> refs = new HashSet<String>();
            new StatementScanner(refs, root, file).scan(page); // TODO: better OO
            String path = gvSafe(StatementScanner.makeRelative(root, file));
            map.put(path, refs);
            //System.out.println(refs.size() + " references in " + file);
        }
    }

    public static String gvSafe(String parent) {
        parent = parent.replace('/', '_');
        parent = parent.replace('.', '_');
        parent = parent.replace('-', '_');
        return parent;
    }

}

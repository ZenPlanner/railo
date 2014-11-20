package com.zenplanner;

import org.apache.commons.lang.NotImplementedException;
import railo.commons.io.res.Resource;
import railo.commons.io.res.ResourceProvider;
import railo.commons.io.res.filter.ResourceFilter;
import railo.commons.io.res.filter.ResourceNameFilter;

import java.io.*;
import java.net.URI;

public class MockResource implements Resource {

    private File file;

    public MockResource(File file) {
        this.file = file;
    }

    @Override
    public boolean isReadable() {
        throw new NotImplementedException();
    }

    @Override
    public boolean canRead() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isWriteable() {
        throw new NotImplementedException();
    }

    @Override
    public boolean canWrite() {
        throw new NotImplementedException();
    }

    @Override
    public void remove(boolean force) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public boolean delete() {
        throw new NotImplementedException();
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public Resource getAbsoluteResource() {
        throw new NotImplementedException();
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public Resource getCanonicalResource() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public String getCanonicalPath() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getParent() {
        throw new NotImplementedException();
    }

    @Override
    public Resource getParentResource() {
        throw new NotImplementedException();
    }

    @Override
    public String getReal(String realpath) {
        throw new NotImplementedException();
    }

    @Override
    public Resource getRealResource(String realpath) {
        throw new NotImplementedException();
    }

    @Override
    public String getPath() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isAbsolute() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isDirectory() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public boolean isHidden() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isArchive() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isSystem() {
        throw new NotImplementedException();
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public long length() {
        throw new NotImplementedException();
    }

    @Override
    public String[] list() {
        throw new NotImplementedException();
    }

    @Override
    public String[] list(ResourceNameFilter filter) {
        throw new NotImplementedException();
    }

    @Override
    public String[] list(ResourceFilter filter) {
        throw new NotImplementedException();
    }

    @Override
    public Resource[] listResources() {
        throw new NotImplementedException();
    }

    @Override
    public Resource[] listResources(ResourceFilter filter) {
        throw new NotImplementedException();
    }

    @Override
    public Resource[] listResources(ResourceNameFilter filter) {
        throw new NotImplementedException();
    }

    @Override
    public boolean renameTo(Resource dest) {
        throw new NotImplementedException();
    }

    @Override
    public void moveTo(Resource dest) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public boolean setLastModified(long time) {
        throw new NotImplementedException();
    }

    @Override
    public boolean setReadOnly() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setWritable(boolean writable) {
        throw new NotImplementedException();
    }

    @Override
    public boolean setReadable(boolean readable) {
        throw new NotImplementedException();
    }

    @Override
    public boolean createNewFile() {
        throw new NotImplementedException();
    }

    @Override
    public void createFile(boolean createParentWhenNotExists) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public boolean mkdir() {
        throw new NotImplementedException();
    }

    @Override
    public boolean mkdirs() {
        throw new NotImplementedException();
    }

    @Override
    public void createDirectory(boolean createParentWhenNotExists) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file); // TODO: Make sure this is closed again?
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void copyTo(Resource res, boolean append) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void copyFrom(Resource res, boolean append) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public ResourceProvider getResourceProvider() {
        throw new NotImplementedException();
    }

    @Override
    public int getMode() {
        throw new NotImplementedException();
    }

    @Override
    public void setMode(int mode) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void setHidden(boolean value) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void setSystem(boolean value) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void setArchive(boolean value) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void setAttribute(short attribute, boolean value) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public boolean getAttribute(short attribute) {
        throw new NotImplementedException();
    }
}

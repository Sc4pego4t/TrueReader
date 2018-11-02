package ru.scapegoats.truereader.model;

import java.io.File;
import java.io.Serializable;

public class Book implements Serializable {

    public enum FileTypes implements Serializable {
        FB2, EPUB, PDF, DJVU;
    }

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileTypes getFileType() {
        return fileType;
    }

    public void setFileType(FileTypes fileType) {
        this.fileType = fileType;
    }

    private FileTypes fileType;

    public Book(File file, FileTypes fileType) {
        this.file = file;
        this.fileType = fileType;
    }


}

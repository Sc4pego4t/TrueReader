package ru.scapegoats.truereader.model;

import java.io.File;
import java.io.Serializable;

public class Book {

    public enum file_types{
        FB2, EPUB, DPF, DJVU;
    }

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public file_types getFileType() {
        return fileType;
    }

    public void setFileType(file_types fileType) {
        this.fileType = fileType;
    }

    private file_types fileType;

    public Book(File file, file_types fileType){
        this.file = file;
        this.fileType = fileType;
    }



}

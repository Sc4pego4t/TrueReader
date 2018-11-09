package ru.scapegoats.truereader.activities.filebrowser.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.core.ZipFile;




import androidx.core.content.ContextCompat;
import ru.scapegoats.truereader.activities.books.BookActivity;
import ru.scapegoats.truereader.activities.books.BookPresenter;
import ru.scapegoats.truereader.model.Book;

public class OnItemClickListener implements View.OnClickListener {

    private File file;
    private FileBrowserRVAdapter adapter;
    private String extension;

    OnItemClickListener(File file, FileBrowserRVAdapter adapter, String extension) {
        this.file = file;
        this.adapter = adapter;
        this.extension = extension;
    }

    @Override
    public void onClick(View view) {
        if (file.isDirectory()) {
            adapter.enterInFolder(file);
            adapter.addToPathHistory(file);
        } else if (FileBrowserRVAdapter.readableFormats.contains(extension)) {

            Book book = new Book(file, Book.FileTypes.valueOf(extension.toUpperCase()));

            Intent intent = new Intent(adapter.activity, BookActivity.class);

            intent.putExtra(BookPresenter.BOOK_INFO, book);

            adapter.activity.startActivity(intent);
        } else if (FileBrowserRVAdapter.archivedFormats.contains(extension)) {
            //if our clicked file is a archive we need to extract it and then open
            if (ContextCompat.checkSelfPermission(adapter.activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                File newDirWhereToExtract = new File(file.getParent()
                        + File.separator
                        + getFileNameWithoutExtension(file));

                if (!newDirWhereToExtract.exists()) {
                    newDirWhereToExtract.mkdir();
                }

                String source = file.getAbsolutePath();
                String destination = newDirWhereToExtract.getAbsolutePath();
                String password = "password";

                try {
                    ZipFile zipFile = new ZipFile(source);
                    if (zipFile.isEncrypted()) {
                        //TODO ADD DIALOG TO ENCRYPTED FILES
                        zipFile.setPassword(password);
                    }
                    zipFile.extractAll(destination);
                } catch (ZipException e) {
                    e.printStackTrace();
                }
                adapter.addToPathHistory(newDirWhereToExtract);
                adapter.enterInFolder(newDirWhereToExtract);
            }
        }
    }

    private String getFileNameWithoutExtension(File file) {
        String[] splitedName = file.getName().split(FileBrowserRVAdapter.DIVIDER);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < splitedName.length - 1; i++) {
            result.append(splitedName[i]);
        }
        return result.toString();
    }
}

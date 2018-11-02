package ru.scapegoats.truereader.activities.filebrowser.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

                try {
                    ZipFile zip = new ZipFile(file);
                    Enumeration entries = zip.entries();
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    System.out.println(entry.getName());

                    //create folder where to extract if he doesn't exist
                    File newDirWhereToExtract = new File(file.getParent()
                            + File.separator
                            + getFileNameWithoutExtension(file));

                    if (!newDirWhereToExtract.exists()) {
                        newDirWhereToExtract.mkdir();
                    }

                    //if extracted file is directory
                    // then extract all inner files and folder
                    if (entry.isDirectory()) {
                        new File(newDirWhereToExtract, entry.getName()).mkdirs();
                    } else {
                        //if it's file then just copy it in our new folder
                        write(zip.getInputStream(entry),
                                new BufferedOutputStream(new FileOutputStream(
                                        new File(newDirWhereToExtract, entry.getName()))));
                    }

                    adapter.enterInFolder(newDirWhereToExtract);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //
    private static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        out.close();
        in.close();
    }

    private String getFileNameWithoutExtension(File file) {
        String[] splitedName = file.getName().split(FileBrowserRVAdapter.DIVIDER);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < splitedName.length - 2; i++) {
            result.append(splitedName[i]);
        }
        return result.toString();
    }
}

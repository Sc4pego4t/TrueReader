package ru.scapegoats.truereader.activities.books.booktypes;

import android.text.SpannableString;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import ru.scapegoats.truereader.activities.books.booktypes.tools.PageDivider;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.ProgressDialog;

public class EPUB implements TextableFormats {

    private BaseActivity activity;
    private Book book;
    //TODO RESTORE text after orientation changes

    public EPUB(BaseActivity activity, Book book) {
        this.activity = activity;
        this.book = book;
    }
    @Override
    public void createAdapter(ProgressDialog progressDialog) {

            Disposable disposable=Single.fromCallable(()->{
                EpubReader epubReader = new EpubReader();
                nl.siegmann.epublib.domain.Book
                        epubBook = epubReader.readEpub(new FileInputStream(book.getFile()));

                StringBuilder builder=new StringBuilder();
                for(Resource resource: epubBook.getContents()){
                    Document document= Jsoup.parse(new String(resource.getData()));
                    //stepThrough(document.body(),null);
                    builder.append(toText(document));
                }
                return builder;
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((stringBuilder ->
                            new PageDivider(activity
                                    ,new SpannableString(stringBuilder)
                                    ,progressDialog)
                                    .createAdapter()));


    }

    private static List<String> delimeters
            = new ArrayList<>(Arrays.asList("p","span"));

    private static String toText(Element element) {

        final StringBuilder stringBuilder = new StringBuilder();
        new NodeTraversor(new NodeVisitor() {
            public void head(Node node, int depth) {
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                        stringBuilder.append(textNode.text());

                } else if (node instanceof Element) {
                    if(delimeters.contains(node.nodeName()))
                        stringBuilder.append("\n\t\t\t\t");
                }
            }

            public void tail(Node node, int depth) {
            }
        }).traverse(element);

        return stringBuilder.toString();
    }
}

package ru.scapegoats.truereader.activities.books.booktypes;

import android.text.SpannableString;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.scapegoats.truereader.activities.books.booktypes.tools.PageDivider;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.ProgressDialog;

public class HTML implements TextableFormats {

    private BaseActivity activity;
    private Book book;
    //TODO RESTORE text after orientation changes

    public HTML(BaseActivity activity, Book book) {
        this.activity = activity;
        this.book = book;
    }

    @Override
    public void createAdapter(ProgressDialog progressDialog) {
        Disposable disposable= Single.fromCallable(()->{

            //find charset encoding
            FileInputStream fis = new FileInputStream(book.getFile());

            UniversalDetector detector = new UniversalDetector(null);

            byte[] buf = new byte[4096];
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();

            String encoding = detector.getDetectedCharset();
            if (encoding != null) {
                Log.e("Detected encoding = ", encoding);
            } else {
                Log.e("No encoding detected.", "((");
                return null;
            }

            detector.reset();
            StringBuilder builder=new StringBuilder();
                Document document= Jsoup.parse(book.getFile(),encoding);
                //stepThrough(document.body(),null);
                builder.append(toText(document));
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

    private static List<String> delimiters
            = new ArrayList<>(Arrays.asList("p","span","br"));

    private static String toText(Element element) {

        final StringBuilder stringBuilder = new StringBuilder();
        new NodeTraversor(new NodeVisitor() {
            public void head(Node node, int depth) {
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    stringBuilder.append(textNode.text());

                } else if (node instanceof Element) {
                    Log.e("Tag",node.nodeName());
                    if(delimiters.contains(node.nodeName()))
                        stringBuilder.append("\n\t\t\t\t");
                }
            }

            public void tail(Node node, int depth) {
            }
        }).traverse(element);

        return stringBuilder.toString();
    }
}

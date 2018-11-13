package ru.scapegoats.truereader.activities.books.booktypes;

import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.booktypes.tools.PageDivider;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.ProgressDialog;

public class FB2 implements TextableFormats {

    private BaseActivity activity;
    private Book book;
    //TODO RESTORE text after orientation changes

    public FB2(BaseActivity activity, Book book) {
        this.activity = activity;
        this.book = book;
    }

    //TODO handle disposable, make it possible to cancel the task
    @Override
    public void createAdapter() {
        ProgressDialog progressDialog=new ProgressDialog(activity);
        progressDialog.show();
        Disposable disposable=Completable.fromCallable(()->{
                readWholeXmlFile(book.getFile());
                return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{

                    new PageDivider(activity,new SpannableString(spannable),progressDialog)
                            .createAdapter();
                    spannable=null;
                });
    }

    //TODO compute in another thread
    private void readWholeXmlFile(File file){
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            try {
                Document document = builder.parse(file);
                stepThrough(document.getDocumentElement(),null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            Log.e("exc", e.toString());
        }
    }

    //fb2 xml tag in end of the file, it useless so skip this content
    private static List<String> TagsWithUselessContent
            = new ArrayList<>(Arrays.asList("binary","id","program-used","sup",
            "genre","lang","src-lang","document-info","myheader","title","epigraph"));
    private static String descr="description";

    //read xml in order
    private SpannableStringBuilder spannable=new SpannableStringBuilder("\n\n\t\t\t\t");


    private int innertyCounter;
    private boolean isInP=false;


    private void stepThrough (Node start, String parentTag) {

        String tag=parentTag;
        if(start instanceof Element) {
            tag = ((Element) start).getTagName();
            if(TagsWithUselessContent.contains(tag)){
                return;
            }
            if(tag.equals(descr)){
                getXmlDescription(start,null);
                return;
            }
            if(tag.equals("p")){
                innertyCounter=0;
                isInP=true;
            }
        }



        if (start.getNodeValue()!= null && !start.getNodeValue().isEmpty()) {
            //Log.e("tag=",tag+"/"+start.getNodeValue());
            int startPos=spannable.length();
            if(isInP && innertyCounter==0) {
                isInP=false;
                innertyCounter++;
                spannable.append("\n\t\t\t\t");
            }
            spannable.append(start.getNodeValue());
            int endPos=spannable.length();
            switch (tag){
                case "emphasis":
                    spannable.setSpan(new StyleSpan(Typeface.ITALIC),startPos
                            ,endPos,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case "strong":
                    spannable.setSpan(new StyleSpan(Typeface.BOLD)
                            ,startPos,endPos,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            }

        }
        for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling()) {
            stepThrough(child,tag);
        }
    }


    private void appendBold(String text){
        int start=spannable.length();
        spannable.append(text);

        spannable.setSpan(new StyleSpan(Typeface.BOLD)
                ,start,spannable.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendItalic(String text){
        int start=spannable.length();
        spannable.append(text);
        spannable.setSpan(new StyleSpan(Typeface.ITALIC)
                ,start,spannable.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.append("\n\n\t\t\t\t");
    }

    private void getXmlDescription(Node start,String parentTag){
        String tag=parentTag;
        if(start instanceof Element) {
            tag = ((Element) start).getTagName();

            if(parentTag!=null) {
                if(parentTag.equals("title-info") && tag.equals("author")){
                    getAuthor(start);
                }
                if(tag.equals("p")){
                    tag=parentTag;
                }
            }
        }


        if (start.getNodeValue()!= null && !start.getNodeValue().isEmpty()) {
            switch (tag){
                case "book-title":
                    appendBold(start.getNodeValue());
                    spannable.append("\n\n\t\t\t\t");
                    break;
                case "annotation":
                    appendItalic(start.getNodeValue());
                    break;
            }
        }

        for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling()) {
            getXmlDescription(child,tag);
        }

    }

    private void getAuthor(Node start){
        for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling()) {
            appendBold(child.getFirstChild().getNodeValue()+" ");
        }
        spannable.append("\n\n\t\t\t\t");
    }
}

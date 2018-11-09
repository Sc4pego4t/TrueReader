package ru.scapegoats.truereader.activities.books.booktypes;

import android.text.Html;
import android.util.Log;
import android.widget.TextView;

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

import ru.scapegoats.truereader.activities.books.viewpager.MyPagerAdapter;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;

public class FB2 extends TextableBooks {

    public FB2(BaseActivity activity, float textSize, Book book) {
        super(activity, textSize, book);
    }

    @Override
    String getBookTextContent() {
        readWholeXmlFile(book.getFile());
        try {
            return fileReader.toString();
        } finally {
            fileReader=null;
        }
    }

    private void readWholeXmlFile(File file){
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            try {
                Document document = builder.parse(file);
                stepThrough(document.getDocumentElement());

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
            = new ArrayList<>(Arrays.asList("binary","id","program-used",
            "genre","lang","src-lang","document-info","myheader","title","epigraph"));
    private static String descr="description";
    //read xml in order
    private StringBuilder fileReader = new StringBuilder();

    int counter=0;
    boolean isBody;
    private void stepThrough (Node start) {
        if(start instanceof Element) {
            String tag = ((Element) start).getTagName();
            if(TagsWithUselessContent.contains(tag)){
                return;
            }
            if(tag.equals(descr)){
                getXmlDescription(start);
                return;
            }
//            Log.e("tag",tag);
        }
        counter++;
        if (start.getNodeValue()!= null && !start.getNodeValue().isEmpty()) {
            fileReader.append(start.getNodeValue()).append("\n\t\t\t\t");
        }
        for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling()) {
            stepThrough(child);
        }

    }
    private void getXmlDescription(Node descr){
        NodeList list=descr.getChildNodes();

        for (Node child = descr.getFirstChild(); child != null; child = child.getNextSibling()) {
            if(child instanceof Element) {
                String tag = ((Element) child).getTagName();
//                Log.e("tag",tag);
                switch (tag){
                    case "title-info":getXmlDescription(child);break;
                    case "document-info":return;
                    case "author":getAuthorName(child);break;
                    case "book-title":makeBoldAndWhitespaced(child.getFirstChild().getNodeValue());break;
                    case "annotation":makeItalicAndWhitespaced(child
                            .getFirstChild().getFirstChild().getNodeValue());break;
                }
            }
        }
    }

    //TODO italic?

    private void makeItalicAndWhitespaced(String string){
        String builder = string +
                "\n\n\t\t\t";
        fileReader.append(builder);
    }

    //TODO bold?
    private void makeBoldAndWhitespaced(String string){
        String builder = string +
                "\n\n\t\t\t";
        fileReader.append(builder);
    }

    private void getAuthorName(Node author){
        StringBuilder builder=new StringBuilder("\n\n\t\t\t");
        for (Node child = author.getFirstChild(); child != null; child = child.getNextSibling()) {
            builder.append(child.getFirstChild().getNodeValue()).append("   ");
        }
        makeBoldAndWhitespaced(builder.toString());
    }

}

package ru.scapegoats.truereader.activities.filebrowser;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.BookActivity;
import ru.scapegoats.truereader.activities.books.BookPresenter;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;

public class FileBrowserRVAdapter extends RecyclerView.Adapter<FileBrowserRVAdapter.ViewHolder> {

    static private  String DIVIDER="\\.";
    static private final String PATH_SPLITTER = "/";
    static private List<String> readableFormats= new ArrayList<>(
            Arrays.asList("fb2","djvu","pdf","epub"));

    private List<File> fileList;
    private LinearLayout llFilePathHistory;
    private BaseActivity acivity;

    FileBrowserRVAdapter(File file, LinearLayout llFilePathHistory, BaseActivity context){
        fileList = sortAndReturnList(Arrays.asList(file.listFiles()));
        this.llFilePathHistory=llFilePathHistory;
        this.acivity=context;
        addToPathHistory(file);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView icon;
        LinearLayout itemContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
            icon=itemView.findViewById(R.id.icon);
            itemContainer=itemView.findViewById(R.id.item);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File currentFile = fileList.get(position);
        holder.tv.setText(currentFile.getName());

        String format = isReadableFile(currentFile);
        if(currentFile.isDirectory()){
            holder.icon.setImageDrawable(
                    acivity.getResources().getDrawable(R.drawable.ic_folder_black));
        } else if(currentFile.isFile()){


            //if format is unreadable
            if(format!=null){
                holder.icon.setImageDrawable(
                        acivity.getResources().getDrawable(R.drawable.ic_closed_book));
            } else {
                holder.icon.setImageDrawable(
                        acivity.getResources().getDrawable(R.drawable.ic_file));
            }
        }

        //listener on file or folder click
        holder.itemContainer.setOnClickListener((view)->{
            if (currentFile.isDirectory()) {
                enterInFolder(currentFile);
                addToPathHistory(currentFile);
            } else if(format!=null){

                Book book = new Book(currentFile, Book.FileTypes.valueOf(format.toUpperCase()));

                Intent intent=new Intent(acivity, BookActivity.class);

                intent.putExtra(BookPresenter.BOOK_INFO,book);

                acivity.startActivity(intent);
                //TODO START READ ACTIVITY
            }
        });
    }

    //check is it appropriate file to read
    //if format readable return it like a string
    //else return null
    private String isReadableFile(File file){
        boolean apprFormat=false;
        String[] ar = file.getName().split(DIVIDER);
        String str=null;
        //is file have extensions
        try {
            str=ar[ar.length-1].toLowerCase();
            //if it's extension that we can read
            if(readableFormats.contains(str))
                apprFormat=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(apprFormat) {
            return str;
        } else{
            return null;
        }
    }



    //sort file by alphabet
    //TODO sort by folders and files?
    private ArrayList<File> sortAndReturnList(List<File> list){
        Set<File> set = new TreeSet<>(list);
        return new ArrayList<>(set);
    }

    private void enterInFolder(File file){
        fileList = sortAndReturnList(Arrays.asList(file.listFiles()));
        notifyDataSetChanged();
    }



    private List<TextView> hist = new ArrayList<>();

    List<TextView> getHist() {
        return new ArrayList<>(hist);
    }

    private void addToPathHistory(File file){

        TextView spl=new TextView(acivity);
        spl.setText(PATH_SPLITTER);

        TextView newTv=new TextView(acivity);
        newTv.setText( android.text.Html.fromHtml("<u>" + file.getName() + "</u>"));
        newTv.setTextColor(acivity.getResources().getColor(R.color.colorPrimaryDark));
        newTv.setTag(file);
        hist.add(spl);
        hist.add(newTv);

        List<TextView> curHist=new ArrayList<>(hist);

        newTv.setOnClickListener((view -> {
            enterInFolder(file);

            for (int i = hist.size()-1; i >= curHist.size(); i--){
                llFilePathHistory.removeViewAt(i);
                hist.remove(i);
            }

        }));

        //TODO CHANGE sizes
        newTv.setTextSize(20);
        spl.setTextSize(20);

        llFilePathHistory.addView(spl);
        llFilePathHistory.addView(newTv);

    }

    void histBack(){
        int border = hist.size();
        int i;
        for(i = border-1; i>=border-2; i--){
            llFilePathHistory.removeViewAt(i);
            hist.remove(i);
        }
        enterInFolder((File)llFilePathHistory.getChildAt(i).getTag());
    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }
}

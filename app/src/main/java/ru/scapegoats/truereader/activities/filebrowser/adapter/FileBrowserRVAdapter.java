package ru.scapegoats.truereader.activities.filebrowser.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
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
import ru.scapegoats.truereader.modules.BaseActivity;

public class FileBrowserRVAdapter extends RecyclerView.Adapter<FileBrowserRVAdapter.ViewHolder> implements Parcelable {


    static String DIVIDER = "\\.";
    static private final String PATH_SPLITTER = "/";
    static List<String> readableFormats = new ArrayList<>(
            Arrays.asList("fb2", "djvu", "pdf", "epub", "txt","html"));

    //TODO read 7z?
    static List<String> archivedFormats = new ArrayList<>(
            Arrays.asList("zip"));

    private List<File> fileList;
    private LinearLayout llFilePathHistory;
    BaseActivity activity;

    public FileBrowserRVAdapter(File file, LinearLayout llFilePathHistory, BaseActivity context) {
        fileList = sortAndReturnList(Arrays.asList(file.listFiles()));
        this.llFilePathHistory = llFilePathHistory;
        this.activity = context;
        addToPathHistory(file);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView icon;
        LinearLayout itemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            icon = itemView.findViewById(R.id.icon);
            itemContainer = itemView.findViewById(R.id.item);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File currentFile = fileList.get(position);
        holder.tv.setText(currentFile.getName());

        String extension = getExtension(currentFile);
        if (currentFile.isDirectory()) {
            holder.icon.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_folder_black));
        } else if (readableFormats.contains(extension)) {
            holder.icon.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_closed_book));
        } else if (archivedFormats.contains(extension)) {
            holder.icon.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_archive));
        } else {
            holder.icon.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_file));
        }

        //listener on file or folder click
        holder.itemContainer.setOnClickListener(
                new OnItemClickListener(currentFile, this, extension));
    }


    private String getExtension(File fileName) {
        String[] ar = fileName.getName().split(DIVIDER);
        return ar[ar.length - 1].toLowerCase();
    }


    //sort file by alphabet
    private List<File> sortAndReturnList(List<File> list) {
        Set<File> setDirs = new TreeSet<>();
        Set<File> setFiles = new TreeSet<>();

        //sort by folders and files
        for (File el : list) {
            if (el.isDirectory()) {
                setDirs.add(el);
            } else {
                setFiles.add(el);
            }
        }
        //folders comes first
        List<File> result = new ArrayList<>(setDirs);
        result.addAll(setFiles);
        return result;
    }

    void enterInFolder(File file) {
        fileList = sortAndReturnList(Arrays.asList(file.listFiles()));

        Log.e("FILEs", fileList.toString());
        notifyDataSetChanged();
    }


    private List<File> pathHistory = new ArrayList<>();

    public List<File> getHist() {
        return new ArrayList<>(pathHistory);
    }

    private void addViewToHistoryBar(File file, View.OnClickListener onClickListener) {

        TextView newTv = new TextView(activity);
        newTv.setText(android.text.Html.fromHtml("/<u>" + file.getName() + "</u>"));
        newTv.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        newTv.setTag(file);

        //TODO change size
        newTv.setTextSize(20);

        newTv.setOnClickListener(onClickListener);
        newTv.setId(pathHistory.size());

        llFilePathHistory.addView(newTv);
    }

    void addToPathHistory(File file) {
        pathHistory.add(file);
        List<File> currentHistory = new ArrayList<>(pathHistory);

        //not lambda because you should to generate exact listener for this
        addViewToHistoryBar(file, view -> {
            llFilePathHistory.removeAllViews();
            pathHistory = new ArrayList<>();
            for (File localFile : currentHistory) {
                addToPathHistory(localFile);
            }
            enterInFolder(currentHistory.get(currentHistory.size() - 1));
        });
    }

    public void histBack() {

        int border = pathHistory.size() - 1;
        llFilePathHistory.removeViewAt(border);
        pathHistory.remove(border);

        Log.e("INFO", ((TextView) llFilePathHistory.getChildAt(border - 1)).getText().toString());
        enterInFolder((File) llFilePathHistory.getChildAt(border - 1).getTag());
    }

    //parcelable methods

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.pathHistory);
    }

    private FileBrowserRVAdapter(Parcel in) {
        this.pathHistory = new ArrayList<>();
        in.readList(this.pathHistory, File.class.getClassLoader());
    }

    //invoke this function after parse the parcelable
    public void restoreProgress() {
        List<File> temp = new ArrayList<>(pathHistory);
        pathHistory = new ArrayList<>();
        llFilePathHistory.removeAllViews();
        for (File el : temp) {
            addToPathHistory(el);
        }

        Log.e("WHAT", temp.get(temp.size() - 1).getName());
        enterInFolder(temp.get(temp.size() - 1));
    }

    public void setLlFilePathHistory(LinearLayout llFilePathHistory) {
        this.llFilePathHistory = llFilePathHistory;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public static final Parcelable.Creator<FileBrowserRVAdapter> CREATOR = new Parcelable.Creator<FileBrowserRVAdapter>() {
        @Override
        public FileBrowserRVAdapter createFromParcel(Parcel source) {
            return new FileBrowserRVAdapter(source);
        }

        @Override
        public FileBrowserRVAdapter[] newArray(int size) {
            return new FileBrowserRVAdapter[size];
        }
    };
}

package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contextmenupresentationdemo.MainActivity;
import com.example.contextmenupresentationdemo.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Chapter;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterHolder>{
    private List<Chapter> chapters;
    private Context context;
    private Set<String> selectedChapters = new HashSet<>();

    public ChapterAdapter(Context context, List<Chapter> chapters) {
        this.context = context;
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_chapter, parent, false);
        return new ChapterHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterHolder holder, int position) {
        holder.imv_ava.setImageResource(chapters.get(position).getAva());
        holder.tv_title.setText(chapters.get(position).getTitle());
        holder.tv_des.setText(chapters.get(position).getDes());
        holder.checkbox.setChecked(selectedChapters.contains(chapters.get(position).getTitle()));
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public void clearSelections() {
        selectedChapters.clear();
        notifyDataSetChanged(); // Notify the adapter to refresh
    }

    class ChapterHolder extends RecyclerView.ViewHolder{
        ImageView imv_ava;
        TextView tv_title;
        TextView tv_des;
        CompoundButton checkbox;

        public ChapterHolder(@NonNull View itemView){
            super(itemView);
            imv_ava = itemView.findViewById(R.id.imv_ava);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_des = itemView.findViewById(R.id.tv_des);
            checkbox = itemView.findViewById(R.id.checkbox);

            itemView.setOnLongClickListener(v -> {
                itemView.showContextMenu();
                return true;
            });

            itemView.setOnClickListener(v -> {
                boolean isSelected = !selectedChapters.contains(tv_title.getText().toString());
                if (isSelected) {
                    selectedChapters.add(tv_title.getText().toString());
                } else {
                    selectedChapters.remove(tv_title.getText().toString());
                }

                ((MainActivity) context).onItemSelected(tv_title.getText().toString(), isSelected);
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}

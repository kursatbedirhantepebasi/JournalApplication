package com.example.journalapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.journalapp.models.Journal;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListJournalAdapter extends RecyclerView.Adapter<ListJournalAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Journal> journalrList;
    private ArrayList<Journal> journalrListAll;
    Context context;

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Journal> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredList.addAll(journalrListAll);
            }
            else {
                for (Journal journal:journalrListAll){
                    if(journal.title.toLowerCase().contains(constraint.toString().toLowerCase())
                            || journal.tags.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(journal);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            journalrList.clear();
            journalrList.addAll((Collection<? extends Journal>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, subTitle, date, location, tags;
        public ImageView mainImg;


        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            subTitle = (TextView) view.findViewById(R.id.subTitle);
            tags = (TextView) view.findViewById(R.id.tags);
            date = (TextView) view.findViewById(R.id.date);
            location = (TextView) view.findViewById(R.id.location);
            mainImg = (ImageView) view.findViewById(R.id.mainImg);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goJournalDetail(v);
                }
            });
        }

        public void goJournalDetail(View v){
            Intent intent = new Intent(v.getContext(), AddJournal.class);
            intent.putExtra("journalId", journalrList.get(getAdapterPosition()).journalId);
            v.getContext().startActivity(intent);
        }

    }

    public ListJournalAdapter (ArrayList<Journal> journalrList) {

        this.journalrList = journalrList;
        this.journalrListAll = new ArrayList<>(journalrList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_journal_adapter, parent, false);
        this.context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(journalrList.get(position).title);
        holder.subTitle.setText(journalrList.get(position).subTitle);
        holder.tags.setText(journalrList.get(position).tags);
        holder.date.setText(journalrList.get(position).date);
        holder.location.setText(journalrList.get(position).location);
       // holder.mainImg.setImageBitmap(Uri.parse(journalrList.get(position).images.get(0)));
        if(journalrList.get(position).images.size()>0){
            holder.mainImg.setImageBitmap(createImage(Uri.parse(journalrList.get(position).images.get(0))));
        }
    }

    public Bitmap createImage(final Uri imageUri){
        try {
            final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            return selectedImage;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int getItemCount() {
        return journalrList.size();
    }
}

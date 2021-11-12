package com.example.flickr10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.CommentViewHolder>{

    Context context;
    ArrayList<Comment> comments;


    public CommentsRecyclerViewAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsRecyclerViewAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment, parent, false);

        return new CommentsRecyclerViewAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsRecyclerViewAdapter.CommentViewHolder holder, int position) {
        holder.content.setText(comments.get(position).getContent());
        holder.author.setText(comments.get(position).getAuthorname());
        holder.date.setText(comments.get(position).getDateCreate().toString());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView date;
        TextView content;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.comment_name);
            date = itemView.findViewById(R.id.comment_date);
            content = itemView.findViewById(R.id.comment_content);
        }
    }
}

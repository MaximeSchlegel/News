package com.centrale.news.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.centrale.news.DataModels.Article;
import com.centrale.news.DataModels.ArticlesList;
import com.centrale.news.R;
import com.centrale.news.fragments.ArticlePreviewListFragment;

import java.io.InputStream;

public class ArticlePreviewListAdapter
        extends RecyclerView.Adapter<ArticlePreviewListAdapter.ViewHolder> {

    private ArticlesList articlesList;
    private Context context;
    private ArticlePreviewListFragment.OnListFragmentInteractionListener listener;


    public class ViewHolder
            extends RecyclerView.ViewHolder {

        private View view;
        private ProgressBar progressBar;
        private ImageView image;
        private TextView title ;
        private TextView author;
        private TextView date;

        @SuppressLint("StaticFieldLeak")
        private class DownloadAndDisplayImageTask extends AsyncTask<Article, Void, Bitmap> {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
            }

            @Override
            protected Bitmap doInBackground(Article... articles) {
                String url = articles[0].getImageUrl();
                Bitmap img = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    img = BitmapFactory.decodeStream(in);
                    articles[0].setImage(img);
                } catch (Exception e) {
                    Log.d("DownloadImage", "Download Failed");
                    Log.d("DownloadImage", e.getMessage());
                    img = BitmapFactory.decodeResource(context.getResources(), R.mipmap.no_image_available);
                }
                return img;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                image.setImageBitmap(result);
                image.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            image = (ImageView) itemView.findViewById(R.id.articleImage);
            title = (TextView) itemView.findViewById(R.id.articleTitle);
            author = (TextView) itemView.findViewById(R.id.articleAuthor);
            date = (TextView) itemView.findViewById(R.id.articleDate);
        }

        public void bindArticle(int position) {
            Article article = ArticlesList.getInstance().get(position);
            if (article.getImage() == null) {
                new DownloadAndDisplayImageTask().execute(article);
            } else {
                image.setImageBitmap(article.getImage());
                progressBar.setVisibility(View.GONE);
            }
            title.setText(article.getTitle());
            author.setText(article.getAuthor());
            date.setText(article.getPublicationDate());
        }
    }


    public ArticlePreviewListAdapter(Context context, ArticlePreviewListFragment.OnListFragmentInteractionListener listener) {
        this.articlesList = ArticlesList.getInstance();
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_article_preview_list_item_1, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_article_preview_list_item_2, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindArticle(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onArticleClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}

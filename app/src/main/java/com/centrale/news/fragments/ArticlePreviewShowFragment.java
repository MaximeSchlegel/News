package com.centrale.news.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.centrale.news.DataModels.Article;
import com.centrale.news.DataModels.ArticlesList;
import com.centrale.news.DataModels.Source;
import com.centrale.news.DataModels.SourcesList;
import com.centrale.news.R;


public class ArticlePreviewShowFragment extends Fragment {

    Article article;
    private OnShowFragmentInteractionListener listener;


    public interface OnShowFragmentInteractionListener {
        void onOpenArticle(Article article);
        void onCloseFragment();
    }

    public ArticlePreviewShowFragment() {
        // Required empty public constructor
    }

    public static ArticlePreviewShowFragment newInstance(int postion) {
        ArticlePreviewShowFragment fragment = new ArticlePreviewShowFragment();
        fragment.article = ArticlesList.getInstance().get(postion);

        Log.d("ArticlePreviewShow", "Created");

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_preview_show, container, false);

        // get all the elements to set up
        TextView titleTextView = (TextView) view.findViewById(R.id.articleTitle);
        ImageView imageView = (ImageView) view.findViewById(R.id.articleImage);
        TextView sourceTextView = (TextView) view.findViewById(R.id.articleSource);
        TextView authorTextView = (TextView) view.findViewById(R.id.articleAuthor);
        TextView dateTextView = (TextView) view.findViewById(R.id.articleDate);
        TextView contentTextView = (TextView) view.findViewById(R.id.articleContent);

        Button backBtn = (Button) view.findViewById(R.id.backBtn);
        Button readMoreBtn = (Button) view.findViewById(R.id.readMoreBtn);

        //set up the elements
        titleTextView.setText(article.getTitle());

        if (article.getImage() != null) {
            imageView.setImageBitmap(article.getImage());
        } else {
            imageView.setVisibility(View.GONE);
        }

        Source source = SourcesList.getInstance().get(article.getSourceId());
        if (source != null) {
            sourceTextView.setText(source.getName());
        } else {
            view.findViewById(R.id.sourceLabel).setVisibility(View.GONE);
        }

        if (!article.getAuthor().equals("null")) {
            authorTextView.setText(article.getAuthor());
        } else {
            view.findViewById(R.id.authorLayout).setVisibility(View.GONE);
        }

        if (!article.getPublicationDate().equals("null")) {
            dateTextView.setText(article.getPublicationDate());
        } else {
            view.findViewById(R.id.dateLayout).setVisibility(View.GONE);
        }

        if(!article.getContent().equals("null")) {
            contentTextView.setText(article.getContent());
        } else {
            contentTextView.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseFragment();
            }
        });

        if (!article.getArticleUrl().equals("null")) {
            readMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOpenArticle(article);
                }
            });
        } else {
            readMoreBtn.setVisibility(View.GONE);
        }

        //check if the article have all the element

        Log.d("ArticlePreviewShow", "View Created");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShowFragmentInteractionListener) {
            listener = (OnShowFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        Log.d("ArticlePreviewShow", "listener attached");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

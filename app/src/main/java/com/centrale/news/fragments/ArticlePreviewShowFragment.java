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
        ImageView imageView = (ImageView) view.findViewById(R.id.articleImage);
        TextView titleTextView = (TextView) view.findViewById(R.id.articleTitle);
        TextView authorTextView = (TextView) view.findViewById(R.id.articleAuthor);
        TextView dateTextView = (TextView) view.findViewById(R.id.articleDate);
        TextView contentTextView = (TextView) view.findViewById(R.id.articleContent);

        Button backBtn = (Button) view.findViewById(R.id.backBtn);
        Button readMoreBtn = (Button) view.findViewById(R.id.readMoreBtn);

        //set up the elements
        imageView.setImageBitmap(article.getImage());
        titleTextView.setText(article.getTitle());
        authorTextView.setText(article.getAuthor());
        dateTextView.setText(article.getPublicationDate());
        contentTextView.setText(article.getContent());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseFragment();
            }
        });

        readMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOpenArticle(article);
            }
        });

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

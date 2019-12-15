package com.centrale.news.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centrale.news.R;
import com.centrale.news.adapters.ArticlePreviewListAdapter;


public class ArticlePreviewListFragment extends Fragment {

    private ArticlePreviewListAdapter adapter;
    private OnListFragmentInteractionListener listener;


    public interface OnListFragmentInteractionListener {
        void onArticleClicked(int position);
        void onTop();
        void onBottom();
    }


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticlePreviewListFragment() {
    }

    public static ArticlePreviewListFragment newInstance() {
        ArticlePreviewListFragment fragment = new ArticlePreviewListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ArticlePreviewList", "Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_article_preview_list, container, false);
        Context context = view.getContext();

        adapter = new ArticlePreviewListAdapter(listener);

        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(adapter);
        view.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        if(!view.canScrollVertically(-1)) {
                            listener.onTop();
                        }
                        if(!view.canScrollVertically(1)) {
                            listener.onBottom();
                        }
                    }
                }
        );

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}

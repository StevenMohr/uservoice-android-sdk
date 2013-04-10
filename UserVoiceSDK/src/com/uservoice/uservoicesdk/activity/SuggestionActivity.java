package com.uservoice.uservoicesdk.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uservoice.uservoicesdk.R;
import com.uservoice.uservoicesdk.Session;
import com.uservoice.uservoicesdk.model.Comment;
import com.uservoice.uservoicesdk.model.Suggestion;
import com.uservoice.uservoicesdk.rest.Callback;
import com.uservoice.uservoicesdk.ui.PaginatedAdapter;

public class SuggestionActivity extends ListActivity {
	
	private View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		headerView = getLayoutInflater().inflate(R.layout.suggestion_layout, null);
		getListView().addHeaderView(headerView);
		
		setListAdapter(new PaginatedAdapter<Comment>(this, android.R.layout.simple_list_item_1, new ArrayList<Comment>()) {

			@Override
			protected void search(String query, Callback<List<Comment>> callback) {
			}

			@Override
			protected int getTotalNumberOfObjects() {
				return Session.getInstance().getSuggestion().getNumberOfComments();
			}

			@Override
			protected void customizeLayout(View view, Comment model) {
				TextView textView = (TextView) view.findViewById(android.R.id.text1);
				textView.setText(model.getText());
			}

			@Override
			protected void loadPage(int page, Callback<List<Comment>> callback) {
				Comment.loadComments(Session.getInstance().getSuggestion(), page, callback);
			}
		});
		
		updateView();
		
		getModelAdapter().loadMore();
	}
	
	
	private void updateView() {
		Suggestion suggestion = Session.getInstance().getSuggestion();
		
		getTextView(R.id.suggestion_details_title).setText(suggestion.getTitle());
		
		TextView status = getTextView(R.id.suggestion_details_status);
		if (suggestion.getStatus() != null) {
			status.setVisibility(View.VISIBLE);
			status.setText(Html.fromHtml(String.format("<font color='%s'>%s</font>", suggestion.getStatusColor(), suggestion.getStatus())));
		} else {
			status.setVisibility(View.GONE);
		}
		
		getTextView(R.id.suggestion_details_text).setText(suggestion.getText());
		getTextView(R.id.suggestion_details_creator).setText(String.format("Posted by %s on %s", suggestion.getCreatorName(), DateFormat.getDateInstance().format(suggestion.getCreatedAt())));
		
		if (suggestion.getAdminResponseText() == null) {
			headerView.findViewById(R.id.suggestion_details_admin_response).setVisibility(View.GONE);
		} else {
			headerView.findViewById(R.id.suggestion_details_admin_response).setVisibility(View.VISIBLE);
			getTextView(R.id.suggestion_details_admin_name).setText(suggestion.getAdminResponseUserName());
			getTextView(R.id.suggestion_details_admin_response_date).setText(DateFormat.getDateInstance().format(suggestion.getAdminResponseCreatedAt()));
			getTextView(R.id.suggestion_details_admin_response_text).setText(suggestion.getAdminResponseText());
		}
		
		getTextView(R.id.suggestion_details_vote_count).setText(String.format("%d votes � %d comments", suggestion.getNumberOfVotes(), suggestion.getNumberOfComments()));
	}
	
	private TextView getTextView(int id) {
		return (TextView) headerView.findViewById(id);
	}


	@SuppressWarnings("unchecked")
	protected PaginatedAdapter<Comment> getModelAdapter() {
		return (PaginatedAdapter<Comment>) getListAdapter();
	}

}
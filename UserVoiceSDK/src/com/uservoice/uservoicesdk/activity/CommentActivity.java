package com.uservoice.uservoicesdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.uservoice.uservoicesdk.R;
import com.uservoice.uservoicesdk.Session;
import com.uservoice.uservoicesdk.model.Comment;
import com.uservoice.uservoicesdk.model.Suggestion;
import com.uservoice.uservoicesdk.ui.DefaultCallback;

public class CommentActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_layout);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}
	
	public void postComment() {
		Suggestion suggestion = Session.getInstance().getSuggestion();
		EditText editText = (EditText) findViewById(R.id.comment_edit_text);
		String text = editText.getText().toString();
		Comment.createComment(suggestion, text, new DefaultCallback<Comment>(this) {
			@Override
			public void onModel(Comment comment) {
				Toast.makeText(CommentActivity.this, "Your comment has been posted", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.post_comment) {
	    	postComment();
	    	return true;
	    } else {
            return super.onOptionsItemSelected(item);
	    }
	}

}
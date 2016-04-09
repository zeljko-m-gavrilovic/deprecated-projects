package com.yc.cepelin;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.cepelin.model.Comment;
import com.yc.cepelin.service.Callback;
import com.yc.cepelin.service.DataService;

public class CommentsActivity extends ListActivity {

	private int itemId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.comments);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			itemId = bundle.getInt(MainTabActivity.ID);
		}

		Button commentAdd = (Button) findViewById(R.id.comment_add);
		commentAdd.setOnClickListener(new OpenDialogListener());
	}

	@Override
	protected void onResume() {
		super.onResume();
		DataService dataService = DataService.getInstance(this);
		dataService.getComments(itemId, new GetCommentsCallback());
	}

	private class GetCommentsCallback implements Callback<ArrayList<Comment>> {

		public void onFinish(ArrayList<Comment> result) {
			CommentsActivity.this.findViewById(R.id.pleaseWait).setVisibility(
					View.GONE);
			setListAdapter(new CommentListAdapter(CommentsActivity.this, result));
		}

	}

	private class CommentListAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Comment> comments;

		public CommentListAdapter(Context context, ArrayList<Comment> comments) {
			this.context = context;
			this.comments = comments;
		}

		public int getCount() {
			return comments.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout view;

			view = (convertView == null) ? view = (LinearLayout) View.inflate(
					context, R.layout.comment, null)
					: (LinearLayout) convertView;

			Comment comment = comments.get(position);

			TextView mComment = (TextView) view.findViewById(R.id.comment_text);
			mComment.setText(comment.getText());

			RatingBar mStars = (RatingBar) view
					.findViewById(R.id.comment_rating);
			mStars.setRating((float) comment.getRating());

			return view;
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		CommentListAdapter adapter = (CommentListAdapter) getListAdapter();
		Comment comment = adapter.comments.get(position);

		AlertDialog dialog = new AlertDialog.Builder(CommentsActivity.this)
				.create();

		dialog.setTitle(R.string.title_comment_dialog);
		dialog.setMessage(comment.getText());
		dialog.setButton(getString(R.string.button_ok),
				new OnDialogClickListener());

		dialog.show();
	}

	private class OnDialogClickListener implements
			DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}

	}

	private class OpenDialogListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Dialog dialog = new Dialog(CommentsActivity.this);
			dialog.setContentView(R.layout.item_comment);
			dialog.setTitle(getString(R.string.add_comment));
			Button confirmButton = (Button) dialog
					.findViewById(R.id.itemAddCommentButtonConfirm);
			confirmButton.setOnClickListener(new AddCommentListener(dialog));

			Button cancelButton = (Button) dialog
					.findViewById(R.id.itemCancelCommentButton);
			cancelButton.setOnClickListener(new CancelCommentListener(dialog));
			dialog.show();
		}
	}

	private class AddCommentListener implements View.OnClickListener {
		private Dialog dialog;

		AddCommentListener(Dialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onClick(View v) {
			EditText commentView = (EditText) dialog
					.findViewById(R.id.itemCommentEditView);
			String comment = commentView.getText().toString();
			if ("".equals(comment)) {
				comment = null;
			}

			RatingBar ratingBar = (RatingBar) dialog
					.findViewById(R.id.itemRatingBar);
			float userRating = ratingBar.getRating();

			DataService.getInstance(CommentsActivity.this).addComment(itemId,
					userRating, comment, new CommentCallback(dialog));
		}
	}

	private class CancelCommentListener implements View.OnClickListener {
		private Dialog dialog;

		CancelCommentListener(Dialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}

	}

	private class CommentCallback implements Callback<Integer> {
		Dialog dialog;

		public CommentCallback(Dialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onFinish(Integer result) {
			dialog.dismiss();
			Toast.makeText(CommentsActivity.this,
					CommentsActivity.this.getText(R.string.comment_added),
					Toast.LENGTH_SHORT).show();
			DataService dataService = DataService
					.getInstance(CommentsActivity.this);
			dataService.getComments(itemId, new GetCommentsCallback());
		}

	}
}

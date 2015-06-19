package com.apptalks.plainolnotes;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apptalks.plainolnotes.data.NoteItem;
import com.apptalks.plainolnotes.data.NotesDataSource;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ListActivity {

	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	ArrayAdapter<NoteItem> adapter;
	private int currentNodeId;
	protected Object mActionMode;
	private NotesDataSource datasource;
	List<NoteItem> notesList;
	public int selectedItem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)

	{

		super.onCreate(savedInstanceState);
		// registerForContextMenu(getListView());

		final ListView lv = getListView();
		lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
		datasource = new NotesDataSource(this);
		lv.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// Inflate the menu for the CAB
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.main, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					SparseBooleanArray selected = lv.getCheckedItemPositions();
					for (int i = selected.size() - 1; i >= 0; i--) {
						NoteItem note = notesList.get(selected.keyAt(i));
						datasource.remove(note);
						refreshDisplay();
					}
					adapter.notifyDataSetChanged();
					mode.finish(); // Action picked, so close the CAB
					return true;
				case R.id.action_share:
					shareVia();
					mode.finish();
					return true;
				default:
					return false;
				}

			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {

			}
		});

		refreshDisplay();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_create) {
			createNote();
		}

		return super.onOptionsItemSelected(item);
	}

	public void refreshDisplay() {

		notesList = datasource.findAll();
		adapter = new ArrayAdapter<NoteItem>(this, R.layout.list_item_layout,
				R.id.note_Text, notesList);
		setListAdapter(adapter);

	}

	private void createNote() {
		NoteItem note = NoteItem.getNew();
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra("key", note.getKey());
		intent.putExtra("text", note.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		NoteItem note = notesList.get(position);
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra("key", note.getKey());
		intent.putExtra("text", note.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			NoteItem note = new NoteItem();
			note.setKey(data.getStringExtra("key"));
			note.setText(data.getStringExtra("text"));
			datasource.update(note);
			refreshDisplay();
		}
	}

	public void shareVia() {

		NoteItem note = notesList.get(currentNodeId);

		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, note.getText().toString());
		intent.setType("text/plain");
		startActivity(Intent.createChooser(intent, "Share Via"));
	}

}
package com.apptalks.plainolnotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.EditText;
import com.apptalks.plainolnotes.MainActivity;

import com.apptalks.plainolnotes.data.NoteItem;

public class NoteEditorActivity extends ActionBarActivity {

	private NoteItem note;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_editor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// launcher icon is transformed into a options button
		// having a id of home

		
		Intent intent = this.getIntent();
		note = new NoteItem();
		note.setKey(intent.getStringExtra("key"));
		note.setText(intent.getStringExtra("text"));

		EditText et = (EditText) findViewById(R.id.noteText);

		et.setText(note.getText());
		et.setSelection(note.getText().length());
	}

	private void saveAndFinish() {
		EditText et = (EditText) findViewById(R.id.noteText);
		String noteText = et.getText().toString();
		final Intent i1 = new Intent(this, MainActivity.class);

		if (noteText.equals("")) 
		{
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					NoteEditorActivity.this);
			builder1.setMessage("Please provide any message. Note should not be Blank");
			builder1.setCancelable(false);
			builder1.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) 
						{
 							dialog.dismiss();
						}
					});

			builder1.setPositiveButton("Ok, Got it!",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(i1);
						}
					});
			AlertDialog alert11 = builder1.create();
			alert11.show();

		}
		else
		{
		

			Intent intent = new Intent();
			intent.putExtra("key", note.getKey());
			intent.putExtra("text", noteText);
			setResult(RESULT_OK, intent);
			finish();
		
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			saveAndFinish();
		}
		return false;
	}

	@Override
	public void onBackPressed() {

		saveAndFinish();

	}
}

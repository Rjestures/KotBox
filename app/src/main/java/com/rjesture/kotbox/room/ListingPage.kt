package com.rjesture.kotbox.room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rjesture.kotbox.R
import com.rjesture.kotbox.room.adapter.NoteAdapter
import com.rjesture.kotbox.room.database.entity.Note
import com.rjesture.kotbox.room.database.viewModel.NoteViewModel
import com.rjesture.kotbox.showToast
import kotlinx.android.synthetic.main.activity_listing_page.*

const val ADD_NOTE_REQUEST = 1
const val EDIT_NOTE_REQUEST = 2

class ListingPage : AppCompatActivity() {
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var viewModel: NoteViewModel
    private lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_page)
        makeInits()
        setRecyclerView()
        setListeners()
    }

    private fun setListeners() {
        button_add_note.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        // swipe listener
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = noteAdapter.getNotesAt(viewHolder.adapterPosition)
                viewModel.delete(note)
            }

        }).attachToRecyclerView(rv_roomList)
    }

    private fun makeInits() {
        mActivity = this@ListingPage
        viewModel = ViewModelProviders.of(this)[NoteViewModel::class.java]
        viewModel.getAllNotes().observe(this, Observer {
            Log.i("Notes observed", "$it")
            noteAdapter.submitList(it)
        })

    }

    private fun setRecyclerView() {
        rv_roomList.layoutManager = LinearLayoutManager(this)
        rv_roomList.setHasFixedSize(true)
        noteAdapter = NoteAdapter { clickedNote ->
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra(EXTRA_ID, clickedNote.id)
            intent.putExtra(EXTRA_TITLE, clickedNote.title)
            intent.putExtra(EXTRA_DESCRIPTION, clickedNote.description)
            intent.putExtra(EXTRA_PRIORITY, clickedNote.priority)
        }
        rv_roomList.adapter = noteAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val title: String = data.getStringExtra(EXTRA_TITLE).toString()
            val description: String = data.getStringExtra(EXTRA_DESCRIPTION).toString()
            val priorityId: Int = data.getIntExtra(EXTRA_PRIORITY, -1)
            viewModel.insert(Note(title,description,priorityId))
            showToast(mActivity, "Note Inserted")

        } else if (data != null && requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val id = data.getIntExtra(EXTRA_ID, -1)
            if (id == -1) {
                showToast(mActivity, "Note couldn't be inserted")
                return
            }
            val title: String = data.getStringExtra(EXTRA_TITLE).toString()
            val description: String = data.getStringExtra(EXTRA_DESCRIPTION).toString()
            val priority: Int = data.getIntExtra(EXTRA_PRIORITY, -1)
            viewModel.update(Note(title, description, priority, id))
            showToast(mActivity, "Note updated!!")
        } else {
            showToast(mActivity, "Note not saved")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                viewModel.deleteAllNotes()
                showToast(mActivity, "All notes deleted")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}




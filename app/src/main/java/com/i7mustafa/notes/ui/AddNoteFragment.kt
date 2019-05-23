package com.i7mustafa.notes.ui


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.i7mustafa.notes.R
import com.i7mustafa.notes.db.Note
import com.i7mustafa.notes.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddNoteFragment : BaseFragment() {

    private var noteM: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            noteM = AddNoteFragmentArgs.fromBundle(it).note
            etTitle.setText(noteM?.title)
            etNote.setText(noteM?.note)
        }

        delete_Floating.setOnClickListener {
            if (noteM != null)
                deleteNote()
            else
                context?.toast("Can't deleting")

        }

        save_Floating.setOnClickListener {view->
            val title = etTitle.text.toString().trim()
            val note = etNote.text.toString().trim()

            if (title.isEmpty()){
                etTitle.error = "Title required"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            if (note.isEmpty()){
                etNote.error = "Note required"
                etNote.requestFocus()
                return@setOnClickListener
            }

            launch {

                context?.let{

                    val mNote = Note(title, note)

                    if (noteM == null){
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved!")
                    }else{
                        mNote.id = noteM!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note Update!")
                    }

                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)

                }
            }
        }
    }

    private fun deleteNote(){

        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You can't undo that!")
            setPositiveButton("YES") { _  ,  _ ->

                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(noteM!!)
                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view!!).navigate(action)
                }
            }
            setNegativeButton("NO") { _, _ ->

                cancel()
            }
        }.create().show()
    }
}

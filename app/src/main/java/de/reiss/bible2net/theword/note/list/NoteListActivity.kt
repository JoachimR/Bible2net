package de.reiss.bible2net.theword.note.list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.note.export.NoteExportActivity
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.hideKeyboard
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.note_list_activity.*

class NoteListActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, NoteListActivity::class.java)

    }

    private val searchManager: SearchManager by lazy {
        App.component.searchManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_list_activity)
        setSupportActionBar(note_list_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.note_list_fragment) == null) {
            replaceFragmentIn(
                    container = R.id.note_list_fragment,
                    fragment = NoteListFragment.createInstance())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_list, menu)

        menu.findItem(R.id.menu_note_list_search).apply {

            (actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                setIconifiedByDefault(true) // always show reading-glass symbol or X symbol
                setOnCloseListener {
                    search("")
                    false
                }
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        hideKeyboard()
                        search(query ?: "")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        search(newText ?: "")
                        return true
                    }

                })
            }

            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

                override fun onMenuItemActionExpand(item: MenuItem?) = true // expands view

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    search("")
                    return true // collapses view
                }
            })

        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_note_list_export -> {
                    startActivity(NoteExportActivity.createIntent(this))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onNewIntent(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent?.action) {
            search(intent.getStringExtra(SearchManager.QUERY) ?: "")
        }
    }

    private fun search(query: String) {
        (findFragmentIn(R.id.note_list_fragment) as? NoteListFragment)?.applyFilter(query)
    }

}
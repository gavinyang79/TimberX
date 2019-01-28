package com.naman14.timberx.ui.fragments

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.naman14.timberx.R
import com.naman14.timberx.ui.adapters.SongsAdapter
import com.naman14.timberx.ui.widgets.RecyclerItemClickListener
import com.naman14.timberx.util.*
import com.naman14.timberx.models.Song
import com.naman14.timberx.ui.listeners.SortMenuListener
import kotlinx.android.synthetic.main.layout_recyclerview.*
import kotlin.collections.ArrayList

class SongsFragment : MediaItemFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recyclerview, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = SongsAdapter().apply {
            showHeader = true

            popupMenuListener = mainViewModel.popupMenuListener

            sortMenuListener = object : SortMenuListener {
                override fun shuffleAll() {
                    ArrayList(songs!!).shuffled().apply {
                        mainViewModel.mediaItemClicked(this[0],
                                getExtraBundle(toSongIDs(), "All songs"))
                    }
                }

                override fun sortAZ() {
                    defaultPrefs(activity!!).edit {
                        putString(Constants.SONG_SORT_ORDER, SongSortOrder.SONG_A_Z)
                    }
                    mediaItemFragmentViewModel.reloadMediaItems()
                }

                override fun sortDuration() {
                    defaultPrefs(activity!!).edit {
                        putString(Constants.SONG_SORT_ORDER, SongSortOrder.SONG_DURATION)
                    }
                    mediaItemFragmentViewModel.reloadMediaItems()
                }

                override fun sortYear() {
                    defaultPrefs(activity!!).edit {
                        putString(Constants.SONG_SORT_ORDER, SongSortOrder.SONG_YEAR)
                    }
                    mediaItemFragmentViewModel.reloadMediaItems()
                }

                override fun sortZA() {
                    defaultPrefs(activity!!).edit {
                        putString(Constants.SONG_SORT_ORDER, SongSortOrder.SONG_Z_A)
                    }
                    mediaItemFragmentViewModel.reloadMediaItems()
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        mediaItemFragmentViewModel.mediaItems.observe(this,
                Observer<List<MediaBrowserCompat.MediaItem>> { list ->
                    val isEmptyList = list?.isEmpty() ?: true
                    if (!isEmptyList) {
                        adapter.updateData(list as ArrayList<Song>)
                    }
                })

        recyclerView.addOnItemClick(object : RecyclerItemClickListener.OnClickListener {
            override fun onItemClick(position: Int, view: View) {
                adapter.getSongForPosition(position)?.let { song ->
                    mainViewModel.mediaItemClicked(song,
                            getExtraBundle(adapter.songs!!.toSongIDs(), "All songs"))
                }
            }
        })
    }


}

package com.example.fragments

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.fragments.details.DetailsFragment
import com.example.fragments.list.ListFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ListFragment>(R.id.main)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.actions.collect { action ->
                        when(action) {
                            is MainAction.ShowDetailsFragment -> {
                                supportFragmentManager.commit {
                                    Log.d(TAG, "adding details fragment")
                                    setReorderingAllowed(true)
                                    val fragment = DetailsFragment.newInstance(action.noteId)
                                    add(R.id.main, fragment, DETAILS_TAG)
                                    addToBackStack(DETAILS_TAG)
                                }
                            }
                            MainAction.RemoveDetailsFragment -> {
                                supportFragmentManager.commit {
                                    supportFragmentManager.findFragmentByTag(DETAILS_TAG)?.let {
                                        remove(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val DETAILS_TAG = "Details fragment"
        const val TAG = "MainActivity"
    }
}
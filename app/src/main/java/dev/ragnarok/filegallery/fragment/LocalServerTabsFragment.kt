package dev.ragnarok.filegallery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.ragnarok.filegallery.R
import dev.ragnarok.filegallery.activity.ActivityFeatures
import dev.ragnarok.filegallery.activity.ActivityUtils.supportToolbarFor
import dev.ragnarok.filegallery.fragment.base.BaseFragment
import dev.ragnarok.filegallery.listener.OnSectionResumeCallback
import dev.ragnarok.filegallery.model.SectionItem
import dev.ragnarok.filegallery.settings.Settings
import dev.ragnarok.filegallery.util.Utils

class LocalServerTabsFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(
            R.layout.fragment_local_media_server_tabs,
            container,
            false
        ) as ViewGroup
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager: ViewPager2 = view.findViewById(R.id.fragment_tabs_pager)
        viewPager.offscreenPageLimit = 1
        viewPager.setPageTransformer(
            Utils.createPageTransform(
                Settings.get().main().getViewpager_page_transform()
            )
        )
        val adapter = Adapter(this)
        setupViewPager(viewPager, adapter)
        TabLayoutMediator(
            view.findViewById(R.id.fragment_local_media_server_tabs),
            viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (adapter.tFragments[position]) {
                LOCAL_SERVER_AUDIO -> tab.text =
                    getString(R.string.local_server_audio)
                LOCAL_SERVER_PHOTO -> tab.text =
                    getString(R.string.local_server_photo)
                LOCAL_SERVER_VIDEO -> tab.text =
                    getString(R.string.local_server_video)
            }
        }.attach()
    }

    private fun dCreateFragment(option_menu: Int): Fragment {
        return when (option_menu) {
            LOCAL_SERVER_AUDIO -> {
                AudiosLocalServerFragment()
            }
            LOCAL_SERVER_PHOTO -> PhotosLocalServerFragment()
            LOCAL_SERVER_VIDEO -> {
                VideosLocalServerFragment()
            }
            else -> throw UnsupportedOperationException()
        }
    }

    private fun setupViewPager(viewPager: ViewPager2, adapter: Adapter) {
        adapter.addFragment(LOCAL_SERVER_AUDIO)
        adapter.addFragment(LOCAL_SERVER_PHOTO)
        adapter.addFragment(LOCAL_SERVER_VIDEO)
        viewPager.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val actionBar = supportToolbarFor(this)
        actionBar?.setTitle(R.string.local_media_server)
        actionBar?.subtitle = null
        if (requireActivity() is OnSectionResumeCallback) {
            (requireActivity() as OnSectionResumeCallback).onSectionResume(SectionItem.LOCAL_SERVER)
        }
        ActivityFeatures.Builder()
            .begin()
            .setBarsColored(requireActivity(), true)
            .build()
            .apply(requireActivity())
    }

    private inner class Adapter(fragmentActivity: Fragment) :
        FragmentStateAdapter(fragmentActivity) {
        val tFragments: MutableList<Int> = ArrayList()
        fun addFragment(fragment: Int) {
            tFragments.add(fragment)
        }

        override fun createFragment(position: Int): Fragment {
            return dCreateFragment(tFragments[position])
        }

        override fun getItemCount(): Int {
            return tFragments.size
        }
    }

    companion object {
        const val LOCAL_SERVER_AUDIO = 0
        const val LOCAL_SERVER_PHOTO = 1
        const val LOCAL_SERVER_VIDEO = 2
    }
}
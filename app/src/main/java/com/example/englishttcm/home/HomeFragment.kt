/* NAM NV created on 22:08 12-4-2023 */
package com.example.englishttcm.home

import android.util.Log
import android.view.ViewGroup
import com.example.englishttcm.OnItemClickListener
import com.example.englishttcm.R
import com.example.englishttcm.playzone.adapter.PlayZoneAdapter
import com.example.englishttcm.storyquote.adapter.StoryQuoteAdapter
import com.example.englishttcm.learnzone.adapter.StudyZoneAdapter
import com.example.englishttcm.base.BaseFragment
import com.example.englishttcm.databinding.FragmentHomeBinding
import com.example.englishttcm.learnzone.view.LearnDetailFragment
import com.example.englishttcm.playzone.model.GamePlayMode
import com.example.englishttcm.storyquote.model.StoryQuote
import com.example.englishttcm.learnzone.model.Study
import com.google.firebase.auth.FirebaseUser

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var firebaseUser: FirebaseUser

    private lateinit var listStudyTitle: ArrayList<Study>
    private lateinit var listPlayMode: ArrayList<GamePlayMode>
    private lateinit var listStoryQuote: ArrayList<StoryQuote>

    override fun getLayout(container: ViewGroup?): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        firebaseUser = data as FirebaseUser
        Log.i("id", firebaseUser.uid)
        setLearnData()
        setPlayMode()
        setStoryData()
        Log.i("list", listStudyTitle.size.toString())
        binding.rcvStudyZone.adapter = StudyZoneAdapter(requireContext(),listStudyTitle, object : OnItemClickListener{
            override fun onItemClick(data: Any?) {
                val studyItem = data as Study
                callback.showFragment(HomeFragment::class.java, LearnDetailFragment::class.java, R.anim.slide_in, R.anim.slide_out, studyItem, true)
                //notify(studyItem.title)
            }
        })

        binding.rcvPlayZone.adapter = PlayZoneAdapter(requireContext(), listPlayMode, object : OnItemClickListener{
            override fun onItemClick(data: Any?) {
                val gameMode = data as GamePlayMode
            }
        })

        binding.rcvStoryQuote.adapter = StoryQuoteAdapter(requireContext(), listStoryQuote, object : OnItemClickListener{
            override fun onItemClick(data: Any?) {
                val storyQuote = data as StoryQuote
                notify(storyQuote.title)
            }
        })
    }

    private fun setStoryData() {
        listStoryQuote = arrayListOf()
        listStoryQuote.add(StoryQuote("Hello"))
        listStoryQuote.add(StoryQuote("Goodbye"))
    }

    private fun setPlayMode() {
        listPlayMode = arrayListOf()
        listPlayMode.add(GamePlayMode(R.drawable.bg_study_zone_green, "Mul-Choice", R.drawable.multiple_choice))
        listPlayMode.add(GamePlayMode(R.drawable.header_home_background, "Fill Blank", R.drawable.fill_blank))
    }

    private fun setLearnData() {
        listStudyTitle = arrayListOf()
        listStudyTitle.add(Study(R.drawable.bg_study_zone_green, "Vocabulary", R.drawable.img_vocabulary))
        listStudyTitle.add(Study(R.drawable.bg_study_zone_pink, "Passive", R.drawable.img_vocabulary))
        listStudyTitle.add(Study(R.drawable.bg_study_zone_yellow, "Active", R.drawable.img_vocabulary))
        listStudyTitle.add(Study(R.drawable.header_home_background, "Relative Pronoun", R.drawable.img_vocabulary))
        listStudyTitle.add(Study(R.drawable.bg_study_zone_purple, "Past Simple", R.drawable.img_vocabulary))
    }
}
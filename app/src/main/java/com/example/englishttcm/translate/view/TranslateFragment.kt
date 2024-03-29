package com.example.englishttcm.translate.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.speech.RecognizerIntent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.englishttcm.R
import com.example.englishttcm.base.BaseFragment
import com.example.englishttcm.databinding.FragmentTranslateBinding
import com.example.englishttcm.translate.viewmodel.TranslateViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale


class TranslateFragment : BaseFragment<FragmentTranslateBinding>(),OnItemSelectedListener {
    private lateinit var translateViewModel: TranslateViewModel


    override fun getLayout(container: ViewGroup?): FragmentTranslateBinding =
        FragmentTranslateBinding.inflate(layoutInflater, container, false)

    private var srcLangId = 0
    private var tarLangId = 1
    override fun initViews() {
        translateViewModel = ViewModelProvider(this)[TranslateViewModel::class.java]
        val sourceSpnAdpt = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            arrayOf("Tiếng Anh", "Tiếng Việt")
        )
        sourceSpnAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnSource.adapter = sourceSpnAdpt
        binding.spnSource.onItemSelectedListener = this
        val targetSpnAdpt = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            arrayOf("Tiếng Anh", "Tiếng Việt")
        )
        targetSpnAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnTarget.adapter = targetSpnAdpt
        binding.spnTarget.onItemSelectedListener = this
        binding.spnSource.setSelection(0)
        binding.spnTarget.setSelection(1)
        binding.btnTranslate.setOnClickListener {
            loading(true)
            val sourceText = binding.edSourcetext.text.toString()
            translateViewModel.getTranslateText(srcLangId,tarLangId,sourceText).observe(viewLifecycleOwner){
                binding.tvTargettext.visibility = View.VISIBLE
                binding.tvTargettext.text = it
                loading(false)
            }
        }
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        binding.ivExchange.setOnClickListener {
            swapSpinnerContent()
        }
        binding.ivVoice.setOnClickListener {
            speak()
        }



    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            R.id.spn_source -> {
                if (p2 == tarLangId) {
                    tarLangId = srcLangId
                }
                srcLangId = p2
            }
            R.id.spn_target -> {
                if (p2 == srcLangId) {
                    srcLangId = tarLangId
                }
                tarLangId = p2
            }
        }
        binding.spnSource.setSelection(srcLangId)
        binding.spnTarget.setSelection(tarLangId)
//        binding.edSourcetext.setText("")
//        binding.tvTargettext.text = ""
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onResume() {
        super.onResume()
        val fab = activity?.findViewById<FloatingActionButton>(R.id.fabTranslate)
        fab!!.visibility = View.INVISIBLE
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnTranslate.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnTranslate.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
    private fun swapSpinnerContent() {
        val sourceAdapter = binding.spnSource.adapter
        val sourceSelectedIndex = binding.spnSource.selectedItemPosition
        val targetAdapter = binding.spnTarget.adapter
        val targetSelectedIndex = binding.spnTarget.selectedItemPosition

        binding.spnSource.adapter = targetAdapter
        binding.spnSource.setSelection(targetSelectedIndex)

        binding.spnTarget.adapter = sourceAdapter
        binding.spnTarget.setSelection(sourceSelectedIndex)
    }

    private val speechToTextLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.edSourcetext.setText(result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0).toString())
        }
    }

    private fun speak() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start speaking")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.US)
        speechToTextLauncher.launch(intent)
    }
}




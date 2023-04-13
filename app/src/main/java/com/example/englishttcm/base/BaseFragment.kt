/* NAM NV created on 22:11 13-4-2023 */
package com.example.englishttcm.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.englishttcm.OnActionCallback

abstract class BaseFragment<VM: ViewBinding>: Fragment(){

    protected lateinit var mContext: Context
    protected lateinit var binding: VM
    lateinit var callback: OnActionCallback
    var data: Any? = null

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getLayout(container)
        return binding.root
    }

    abstract fun getLayout(container: ViewGroup?): VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    abstract fun initViews()

    fun notify(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}
package com.example.myapplication.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.*
import com.example.myapplication.network.Result
import com.example.myapplication.ui.EditProfilActivity
import com.example.myapplication.ui.LoginActivity
import com.example.myapplication.ui.SessionManager
import com.example.myapplication.ui.TentangKamiActivity
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.example.myapplication.ui.viewmodel.SettingVMF
import com.example.myapplication.ui.viewmodel.SettingViewModel
import com.example.myapplication.util.Utility
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.DecimalFormat


class SettingFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    private lateinit var photoProfile: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtHp: TextView
    private lateinit var txtUangMasuk: TextView
    private lateinit var txtUangKeluar: TextView
    private lateinit var btnEditProdil: RelativeLayout
    private lateinit var btnTentangKami: RelativeLayout
    private lateinit var btnLogout: RelativeLayout
    private lateinit var shimmer1: ShimmerFrameLayout
    private lateinit var shimmer2: ShimmerFrameLayout
    private lateinit var profile: LinearLayout
    private lateinit var stats: RelativeLayout
    private lateinit var swipe: SwipeRefreshLayout
    private val settingVM by viewModels<SettingViewModel>{
        SettingVMF.getInstance(requireActivity())
    }

    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(requireActivity())
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        sessionManager = SessionManager(activity)
        photoProfile = view.findViewById(R.id.photoProfile)
        txtName = view.findViewById(R.id.txtName)
        txtHp = view.findViewById(R.id.txtHp)

        shimmer1 = view.findViewById(R.id.shimmer1)
        shimmer2 = view.findViewById(R.id.shimmer2)
        profile = view.findViewById(R.id.profile)
        swipe = view.findViewById(R.id.swipe)

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
        }

        homeVM.getUser().observe(requireActivity()){
            when(it){
                is Result.Error -> {
                    txtName.text = "Halo, guess !"
                    txtHp.text = "XXXXXXXXXXX"
                    Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                    Log.d("error", it.error + "user")
                }
                Result.Loading -> {
                    startShimmer()
                    txtName.text = "Halo, guest !"
                    txtHp.text = "XXXXXXXXXXX"
                }
                is Result.Success -> {
                    stopShimmer()
                    txtName.text =  "Halo, " + it.data.name + "!"
                    txtHp.text = it.data.phoneNumber
                }
            }
        }

        btnEditProdil = view.findViewById(R.id.btnEditProfil)
        btnTentangKami = view.findViewById(R.id.btnTentangKami)
        btnLogout = view.findViewById(R.id.btnLogout)

        btnEditProdil.setOnClickListener{
            val intent = Intent(activity, EditProfilActivity::class.java)
            startActivity(intent)
        }
        btnTentangKami.setOnClickListener{
            val intent = Intent(activity, TentangKamiActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            lifecycleScope.launch {
                homeVM.logout()
                settingVM.logout()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

        }

        return view
    }

    override fun onResume() {
        super.onResume()
//        getUserProfile()
//        getUserStats()
    }

//    private fun getUserProfile() {
//        startShimmer()
//        val token = sessionManager.getToken()
//
//        AndroidNetworking.get("https://dompetku-api.vercel.app/api/user/getprofile")
//            .addHeaders("Authorization", "Bearer $token")
//            .setTag("profile")
//            .setPriority(Priority.LOW)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response", response.toString())
//
//                    val getJsonObject: JSONObject = response.getJSONObject("data")
//                    val user = getJsonObject.getJSONObject("user")
//
//                    if(response.getString("success").equals("true")) {
//                        setProfile(user)
//
//                        stopShimmer()
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    if(jsonObject.getString("code").equals("401")) {
//                        val intent = Intent(activity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            })
//    }

//    private fun getUserStats() {
//        startShimmer()
//        val token = sessionManager.getToken()
//
//        AndroidNetworking.get("https://dompetku-api.vercel.app/api/user/getstats")
//            .addHeaders("Authorization", "Bearer $token")
//            .setTag("profile")
//            .setPriority(Priority.LOW)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response", response.toString())
//                    val data: JSONObject = response.getJSONObject("data")
//
//                    if(response.getString("success").equals("true")) {
//                        val decimalFormat = DecimalFormat("#,###")
//                        txtUangMasuk.text = "Rp. ${decimalFormat.format(data.getInt("uangMasuk"))}"
//                        txtUangKeluar.text = "Rp. ${decimalFormat.format(data.getInt("uangKeluar"))}"
//
//                        stopShimmer()
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    Log.d("error", error.toString())
//                }
//            })
//    }

    private fun setProfile(user: JSONObject) {
        txtName.text = user.getString("name")
        txtHp.text = user.getString("nohp")

        // load image
        Picasso.get()
            .load(user.getString("image"))
            .into(photoProfile)
    }

    private fun startShimmer() {
        shimmer1.visibility = View.VISIBLE
        shimmer1.startShimmer()

        shimmer2.visibility = View.VISIBLE
        shimmer2.startShimmer()

        profile.visibility = View.GONE
        stats.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmer1.visibility = View.GONE
        shimmer1.stopShimmer()

        shimmer2.visibility = View.GONE
        shimmer2.stopShimmer()

        profile.visibility = View.VISIBLE
        stats.visibility = View.VISIBLE
    }
}
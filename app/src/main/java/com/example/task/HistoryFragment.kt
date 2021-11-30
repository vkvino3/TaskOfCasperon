package com.example.task

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.task.databinding.FragmentMyHistoryBinding
import com.example.task.databinding.ItemMyHistoryBinding
import com.example.task.helper.ApiInterface
import com.example.task.helper.OnItemClicked
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment(private  var onItemClicked: OnItemClicked) : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val TAG = HistoryFragment::class.java.simpleName
    }

    lateinit var apiInterface: ApiInterface
    lateinit var binding: FragmentMyHistoryBinding
    var listItem=ArrayList<HistoryItem>()
    lateinit var adapter: HistoryAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       binding=DataBindingUtil.inflate(inflater,R.layout.fragment_my_history,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiInterface=ApiClient.getClient().create(ApiInterface::class.java)
        adapter=HistoryAdapter(requireContext(),listItem)
        binding.rvBookings.layoutManager=LinearLayoutManager(requireContext())
        binding.rvBookings.setHasFixedSize(false)
        binding.rvBookings.adapter=adapter
        adapter.notifyDataSetChanged()
        binding.swipeRefreshLay.setOnRefreshListener(this)
        getMyHistory()
    }

    fun getMyHistory(){
        if(AppUtils.isNetworkAvailable(requireContext())){
            val call=apiInterface.getDataActivity(AppUtils.USER_TOKEN)
            call.enqueue(object : Callback<DataResponse> {
                override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                    if(response.isSuccessful && response.body()!=null){
                        listItem.clear()
                        listItem.addAll(response.body()!!.content.response.history)
                        adapter.notifyDataSetChanged()
                        binding.swipeRefreshLay.isRefreshing=false
                    }
                }

                override fun onFailure(call: Call<DataResponse>, t: Throwable) {

                }

            })
        }else{
            AppUtils.showNoInternetSnack(requireActivity(),binding.rvBookings,false)
            binding.swipeRefreshLay.isRefreshing=false
        }

    }

   inner class HistoryAdapter(private var context: Context,private  var listItem: ArrayList<HistoryItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_my_history,parent,false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val historyItem=listItem[position]
            if(holder is HistoryViewHolder){
                holder.itemBinding.title.text=historyItem.rentalId
                holder.itemBinding.txtLocation.text=historyItem.startedStation
                holder.itemBinding.txtAddress.text=historyItem.startedPakingLane
                Glide.with(context).load(historyItem.vehicle.carIcon).into(holder.itemBinding.itemImage)
                holder.itemBinding.txtDateTime.text=historyItem.startedAt
                holder.itemBinding.txtTotalTime.text=historyItem.fareDetails.totalTime
                holder.itemBinding.txtTotalcost.text=historyItem.fareDetails.totalCost
                holder.itemBinding.downloadReceipt.setOnClickListener {
                    onItemClicked.downloadReceipt(historyItem)
                }
            }
        }

        override fun getItemCount(): Int {
           return listItem.size
        }

       inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
           var itemBinding: ItemMyHistoryBinding= ItemMyHistoryBinding.bind(view.rootView)
       }

    }

    override fun onRefresh() {
        binding.swipeRefreshLay.isRefreshing=true
        getMyHistory()
    }
}

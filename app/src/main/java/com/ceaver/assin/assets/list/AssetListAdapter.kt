package com.ceaver.assin.assets.list

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ceaver.assin.MyApplication
import com.ceaver.assin.R
import com.ceaver.assin.assets.Asset
import com.ceaver.assin.extensions.resIdByName
import com.ceaver.assin.extensions.toCurrencyString
import kotlin.random.Random

class AssetListAdapter(private val onClickListener: AssetListFragment.OnItemClickListener) : RecyclerView.Adapter<AssetListAdapter.ViewHolder>() {

    companion object {
        val CONTEXT_MENU_GROUP_ID = Random.nextInt()
        val CONTEXT_MENU_DEPOSIT_ITEM_ID = Random.nextInt()
        val CONTEXT_MENU_WITHDRAW_ITEM_ID = Random.nextInt()
        val CONTEXT_MENU_INTENTION_ITEM_ID = Random.nextInt()
    }

    var assets: List<Asset> = ArrayList()
    var currentLongClickAsset: Asset? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.asset_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(assets[position], onClickListener)
        holder.itemView.setOnLongClickListener { currentLongClickAsset = assets[position]; false }
    }

    override fun getItemCount() = assets.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu!!.add(CONTEXT_MENU_GROUP_ID, CONTEXT_MENU_DEPOSIT_ITEM_ID, 0, "Deposit")
            menu.add(CONTEXT_MENU_GROUP_ID, CONTEXT_MENU_WITHDRAW_ITEM_ID, 1, "Withdraw")
            menu.add(CONTEXT_MENU_GROUP_ID, CONTEXT_MENU_INTENTION_ITEM_ID, 2, "Intention")
        }

        fun bindItem(asset: Asset, onClickListener: AssetListFragment.OnItemClickListener) {
            (view.findViewById(R.id.assetImageView) as ImageView).setImageResource(getImageIdentifier(asset.symbol))
            (view.findViewById(R.id.assetNameTextView) as TextView).text = asset.name
            (view.findViewById(R.id.assetBalanceTextView) as TextView).text = "${asset.amount} ${asset.symbol}"
            (view.findViewById(R.id.assetBtcValueTextView) as TextView).text = asset.btcValue.toCurrencyString(asset.symbol) + " " + "BTC"
            (view.findViewById(R.id.assetUsdValueTextView) as TextView).text = asset.usdValue.toCurrencyString(asset.symbol) + " " + "USD"

            view.setOnCreateContextMenuListener(this)
            itemView.setOnClickListener { onClickListener.onItemClick(asset) }
        }

        private fun getImageIdentifier(symbol: String): Int {
            val identifier = MyApplication.appContext!!.resIdByName(symbol.toLowerCase(), "drawable")
            return if (identifier == 0) R.drawable.unknown else identifier
        }
    }
}
package com.gfd.home

import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import com.gfd.common.ui.fragment.BaseMvpFragment
import com.gfd.common.utils.GlideImageLoader
import com.gfd.home.adapter.VideoListAdapter
import com.gfd.home.common.Concant
import com.gfd.home.entity.BinnerData
import com.gfd.home.entity.VideoItemData
import com.gfd.home.entity.VideoListData
import com.gfd.home.injection.DaggerVideoComponent
import com.gfd.home.injection.VideoModule
import com.gfd.home.mvp.VideoListContract
import com.gfd.home.mvp.presenter.VedioPresenter
import com.github.jdsjlzx.ItemDecoration.DividerDecoration
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_home.*
import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration






/**
 * @Author : 郭富东
 * @Date ：2018/8/2 - 17:55
 * @Email：878749089@qq.com
 * @descriptio：
 */
class HomeFragment : BaseMvpFragment<VedioPresenter>(), VideoListContract.View {

    /** GridView 列表的列数 */
    private val GRID_COLUMNS = 3
    /** 轮播图切换时间 */
    private val BANNER_TIME = 3 * 1000
    private val videoDatas = ArrayList<VideoItemData>()
    private lateinit var mVideoAdapter: VideoListAdapter
    private lateinit var mLRecyclerViewAdapter: LRecyclerViewAdapter
    private lateinit var mBanner: Banner

    override fun injectComponent() {
        DaggerVideoComponent.builder().activityComponent(mActivityComponent)
                .videoModule(VideoModule(this)).build().inject(this)

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        val layoutManager = GridLayoutManager(activity, GRID_COLUMNS)
        mRecyclerView.layoutManager = layoutManager
        mVideoAdapter = VideoListAdapter(activity)
        mLRecyclerViewAdapter = LRecyclerViewAdapter(mVideoAdapter)
        mRecyclerView.adapter = mLRecyclerViewAdapter
        //添加Head View
        val headView = LayoutInflater.from(context).inflate(R.layout.head_home, null, false)
        mBanner = headView.findViewById(R.id.mBanner)
        mLRecyclerViewAdapter.addHeaderView(headView)
        //设置分割线
        val spacing = resources.getDimensionPixelSize(R.dimen.dp_4)
        mRecyclerView.addItemDecoration(SpacesItemDecoration.newInstance(
                spacing, spacing, layoutManager.spanCount, resources.getColor(R.color.colorItemDecoration)))

    }


    override fun initData() {
        mPresenter.getVideoList()
    }

    override fun showVideoList(data: VideoListData) {
        videoDatas.addAll(data.videoList)
        //设置轮播图数据
        setBanner(data.bannerUrls)
        //设置列表数据
        mLRecyclerViewAdapter.setSpanSizeLookup(object : LRecyclerViewAdapter.SpanSizeLookup {

            override fun getSpanSize(gridLayoutManager: GridLayoutManager?, position: Int): Int {
                val type = data.videoList.get(position).type
                if(type == Concant.ITEM_TYPE_TITLE){
                    return GRID_COLUMNS
                }else{
                    return 1
                }
            }

        })
        mVideoAdapter.addAll(data.videoList)
        mLRecyclerViewAdapter.notifyDataSetChanged()

    }

    /**
     * 设置轮播图数据
     * @param data VideoListData
     */
    private fun setBanner(data: List<BinnerData>) {
        val bannerImages = ArrayList<String>()
        val titles = ArrayList<String>()
        for (bannerUrl in data) {
            bannerImages.add(bannerUrl.imgUrl)
            titles.add(bannerUrl.name)
        }
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
        mBanner.setBannerTitles(titles)
        mBanner.setImageLoader(GlideImageLoader())
        mBanner.setImages(bannerImages)
        mBanner.setDelayTime(BANNER_TIME)
        mBanner.setBannerTitles(titles)
        mBanner.isAutoPlay(true)
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
        mBanner.setBannerAnimation(Transformer.Default)
        mBanner.start()
    }


    override fun onStop() {
        super.onStop()
        //结束轮播
        mBanner.stopAutoPlay()
    }


}
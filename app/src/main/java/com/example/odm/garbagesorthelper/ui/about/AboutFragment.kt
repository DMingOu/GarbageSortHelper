package com.example.odm.garbagesorthelper.ui.about

//import com.example.odm.garbagesorthelper.BR

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.CacheDiskUtils
import com.blankj.utilcode.util.CleanUtils
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.databinding.FragmentAboutBinding
import com.example.odm.garbagesorthelper.model.entity.ProvinceInfo
import com.example.odm.garbagesorthelper.utils.SharePreferencesUtil
import com.orhanobut.logger.Logger
import com.xuexiang.xui.utils.DensityUtils
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView
import com.xuexiang.xui.widget.grouplist.XUIGroupListView
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener
import com.xuexiang.xui.widget.toast.XToast


/**
 * description: 我的页面View层
 * author: ODM
 * date: 2019/9/18
 */
class AboutFragment : BaseFragment() {
    private var mBinding: FragmentAboutBinding? = null
    private var aboutViewModel: AboutViewModel? = null
    private var dialogBuilder: MaterialDialog.Builder? = null

    private lateinit var mGroupListView : XUIGroupListView

    private lateinit var itemWithCityPicker : XUICommonListItemView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewDataBinding(inflater, container)
        initView()
        initData()
        initGroupListView()
        return mBinding?.root
    }

    override val layoutId: Int
        get() = R.layout.fragment_about

    override fun initViewDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        aboutViewModel = ViewModelProviders.of(this).get(AboutViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mBinding?.setLifecycleOwner(this)
    }

    private fun initData() {
//        aboutViewModel?.versionName?.value = aboutViewModel?.version
    }

    private fun initGroupListView() {

        val itemWithCheckUpdate: XUICommonListItemView = mGroupListView.createItemView(
                ContextCompat.getDrawable(context!!, R.drawable.check_update_64),
                getString(R.string.update_version),
                getString(R.string.version_latest),
                XUICommonListItemView.HORIZONTAL,
                XUICommonListItemView.ACCESSORY_TYPE_NONE)

        itemWithCityPicker = mGroupListView.createItemView(getString(R.string.chose_address))
        itemWithCityPicker.setImageDrawable(ContextCompat.getDrawable(requireContext() ,R.drawable.city_pick_64))
        itemWithCityPicker.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        itemWithCityPicker.orientation = XUICommonListItemView.VERTICAL
        itemWithCityPicker.detailText = aboutViewModel?.getRetainAddressValue()

        val itemWithCleanCache: XUICommonListItemView = mGroupListView.createItemView(getString(R.string.clean_cache))
        itemWithCleanCache.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        itemWithCleanCache.setImageDrawable(ContextCompat.getDrawable(requireContext() ,R.drawable.clean_cache_64))

        val itemWithWelComeAnimation: XUICommonListItemView = mGroupListView.createItemView(getString(R.string.skip_welcome_animation))
        itemWithWelComeAnimation.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
        itemWithWelComeAnimation.switch.isChecked = SharePreferencesUtil.getInstance().getBoolean("isSkipWelcomeAnimation")
        itemWithWelComeAnimation.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            SharePreferencesUtil.getInstance().put("isSkipWelcomeAnimation" , isChecked)
        }
/*        val itemWithCustom: XUICommonListItemView = mGroupListView.createItemView("Item 6")
        itemWithCustom.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        val loadingView = MiniLoadingView(activity)
        itemWithCustom.addAccessoryCustomView(loadingView)*/

        val checkUpdateListener = View.OnClickListener { v ->
            if (v is XUICommonListItemView) {

                XToast.info(requireContext(),getString(R.string.version_latest)).show()
            }
        }

        val cleanCacheListener = View.OnClickListener {
            if(it is XUICommonListItemView) {
                aboutViewModel?.cleanCache(Glide.get(requireContext()))
                XToast.success(requireContext() ,getString(R.string.cache_clean_completed)).show()
            }
        }
        val cityPickerListener = View.OnClickListener { v->
            if( v is XUICommonListItemView) {
                showPickerView(false)
            }
        }
        val itemWithAboutAuthor: XUICommonListItemView = mGroupListView.createItemView(
                ContextCompat.getDrawable(requireContext(), R.drawable.about_author_64),
                getString(R.string.about_author),
                null,
                XUICommonListItemView.HORIZONTAL,
                XUICommonListItemView.ACCESSORY_TYPE_CHEVRON)
        val aboutAuthorListener = View.OnClickListener {
            if(it is XUICommonListItemView) {
                showAuthorDialog()
            }
        }

        val size = DensityUtils.dp2px(context, 20f)
        XUIGroupListView.newSection(context)
//                .setDescription("Section 1 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithCheckUpdate, checkUpdateListener)
                .addItemView(itemWithCityPicker, cityPickerListener)
                .addItemView(itemWithCleanCache, cleanCacheListener)
                .addItemView(itemWithAboutAuthor, aboutAuthorListener)

                .addTo(mGroupListView)
        XUIGroupListView.newSection(context)
                .setTitle("")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithWelComeAnimation, null)
                .addTo(mGroupListView)
    }



    private fun initView() {
        if (mBinding != null) {
            dialogBuilder = MaterialDialog.Builder(context!!)
            mBinding?.tvAboutAppVersioncode?.text = aboutViewModel?.versionName?.value

        }
        mGroupListView = mBinding?.glvAbout!!
    }

    // 弹出选择器
    private fun showPickerView(isDialog: Boolean) {

/*        if (aboutViewModel?.mHasLoaded ?: false) {
            Logger.d("正在加载地址数据")
            return
        }*/
//        val defaultSelectOptions: IntArray = getDefaultCity()
        val cityPickerOptions: OptionsPickerView<Any> = OptionsPickerBuilder(
                context, OnOptionsSelectListener { options1, options2, options3, v ->
                    //返回的分别是三个级别的选中位置
                    val chosenString: String = aboutViewModel?.options1Items?.get(options1).toString() + "-" +
                            aboutViewModel?.options2Items?.get(options1)?.get(options2) + "-" +
                            aboutViewModel?.options3Items?.get(options1)?.get(options2)?.get(options3)
                    //更改当前的城市
                    addressPicked(chosenString)
                               })
                .setTitleText(getString(R.string.chose_address))
                .setDividerColor(Color.BLACK) //切换选项时，还原到第一项
                .isRestoreItem(true) //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .isDialog(isDialog)
//                .setSelectOptions(defaultSelectOptions[0], defaultSelectOptions[1], defaultSelectOptions[2])
                .build<Any>()

        //设置 三级选择器
        cityPickerOptions.setPicker(aboutViewModel?.options1Items?.toList(), aboutViewModel?.options2Items?.toList(), aboutViewModel?.options3Items?.toList())
        cityPickerOptions.show()
    }

    private fun addressPicked(address : String) {
        itemWithCityPicker.detailText = address
        aboutViewModel?.retainAddressPickedValue(address)
    }



    private fun showAuthorDialog() {
        if (dialogBuilder != null) {
            dialogBuilder?.title("我是谁?")
                    ?.content(resources.getString(R.string.author_introduction_self) + "\n" + resources.getString(R.string.welcome_contact_to_me) + "\n" + resources.getString(R.string.my_email_address))
                    ?.positiveText(R.string.known)
                    ?.positiveColor(resources.getColor(R.color.bottom_navigation_about))
                    ?.show()
        }
    }

    private fun showIntroductionDialog() {
        if (dialogBuilder != null) {
            dialogBuilder?.title("功能介绍")
                    ?.icon(resources.getDrawable(R.drawable.module_about_dialog_icon_list))
                    ?.content("1、搜索关键词，获取关键词对应的垃圾分类"
                            + "\n" + "2、物体拍照识别,获取对应的垃圾分类" + "\n" + "3、语音识别关键词，获取关键词对应的垃圾分类" + "\n" + "4、学习垃圾分类的基础知识与必要性")
                    ?.positiveText(R.string.known)
                    ?.show()
        }
    }
}
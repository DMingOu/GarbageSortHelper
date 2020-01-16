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
        aboutViewModel?.versionName?.value = aboutViewModel?.version
    }

    private fun initGroupListView() {

        val itemWithCheckUpdate: XUICommonListItemView = mGroupListView.createItemView(
                ContextCompat.getDrawable(context!!, R.drawable.check_update_64),
                "检查最新版本",
                "当前已是最新版本",
                XUICommonListItemView.HORIZONTAL,
                XUICommonListItemView.ACCESSORY_TYPE_NONE)

        val itemWithCityPicker: XUICommonListItemView = mGroupListView.createItemView("选择当前城市")
        itemWithCityPicker.setImageDrawable(ContextCompat.getDrawable(requireContext() ,R.drawable.city_pick_64))
        itemWithCityPicker.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        itemWithCityPicker.orientation = XUICommonListItemView.VERTICAL
        itemWithCityPicker.detailText = "未选择"

        val itemWithCleanCache: XUICommonListItemView = mGroupListView.createItemView("清除缓存")
        itemWithCleanCache.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        itemWithCleanCache.setImageDrawable(ContextCompat.getDrawable(requireContext() ,R.drawable.clean_cache_64))

        val itemWithWelComeAnimation: XUICommonListItemView = mGroupListView.createItemView("关闭启动页动画")
        itemWithWelComeAnimation.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_SWITCH
        itemWithWelComeAnimation.switch.isChecked = SharePreferencesUtil.getInstance().getBoolean("isSkipWelcomeAnimation")
        Logger.d("现在是否跳过动画 " + SharePreferencesUtil.getInstance().getBoolean("isSkipWelcomeAnimation"))
        itemWithWelComeAnimation.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            SharePreferencesUtil.getInstance().put("isSkipWelcomeAnimation" , isChecked)
        }
/*        val itemWithCustom: XUICommonListItemView = mGroupListView.createItemView("Item 6")
        itemWithCustom.accessoryType = XUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        val loadingView = MiniLoadingView(activity)
        itemWithCustom.addAccessoryCustomView(loadingView)*/

        val checkUpdateListener = View.OnClickListener { v ->
            if (v is XUICommonListItemView) {

                XToast.info(requireContext(),"当前版本已是最新版本").show()
            }
        }

        val cleanCacheListener = View.OnClickListener {
            if(it is XUICommonListItemView) {
                //Todo 清除缓存的操作
                XToast.success(requireContext() ,"已清除缓存！").show()
            }
        }
        val cityPickerListener = View.OnClickListener { v->
            if( v is XUICommonListItemView) {
                Logger.d("选择城市")
                itemWithCityPicker.detailText = "我已经选择城市"
            }
        }
        val itemWithAboutAuthor: XUICommonListItemView = mGroupListView.createItemView(
                ContextCompat.getDrawable(requireContext(), R.drawable.about_author_64),
                "关于开发者",
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

            mBinding?.ralAboutAppAuthor?.setOnClickListener { v: View? -> showAuthorDialog() }
            mBinding?.ralAboutAppIntroduction?.setOnClickListener { v: View? -> showIntroductionDialog() }
            mBinding?.ralAboutAppUpdate?.setOnClickListener { v: View? -> Toast.makeText(context, "已经是最新版本了", Toast.LENGTH_SHORT).show() }
        }
        mGroupListView = mBinding?.glvAbout!!
    }


    private fun showPickerView(isDialog: Boolean) {
        // 弹出选择器
        if (aboutViewModel?.mHasLoaded ?: false) {
            Logger.d("正在加载地址数据")
            return
        }
//        val defaultSelectOptions: IntArray = getDefaultCity()
        val cityPickerOptions: OptionsPickerView<Any> = OptionsPickerBuilder(
                context, OnOptionsSelectListener { options1, options2, options3, v ->
                    //返回的分别是三个级别的选中位置
                    val tx: String = aboutViewModel?.options1Items?.get(options1).toString() + "-" +
                            aboutViewModel?.options2Items?.get(options1)?.get(options2) + "-" +
                            aboutViewModel?.options3Items?.get(options1)?.get(options2)?.get(options3)
                            Logger.d("选中了") })
                .setTitleText("城市选择")
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



    private fun showAuthorDialog() {
        if (dialogBuilder != null) {
            dialogBuilder?.title("我是谁?")
                    ?.content("我是来自广东工业大学的客户端开发者DMingO,独立开发了垃圾分类小助手APP。" + "\n" + resources.getString(R.string.welcome_contact_to_me) + "\n" + resources.getString(R.string.my_email_address))
                    ?.positiveText(R.string.known)
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
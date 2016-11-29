package com.mikyou.tools;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ScrollViewConflictWithListViewUtils {
	/**
	 * 为了解决ScrollView和ListView的冲突
	 * 需要动态设置ListView的高度
	 * */
	public static void setListViewHeight(ListView lv){
		if (lv==null) {
			return ;
		}
		ListAdapter adapter=lv.getAdapter();//获得ListView的适配器对象
		if (adapter==null) {
			return ;
		}
		int totalHeight=0;//用于记录最后累加后的ListView的总高度。
		for (int i = 0; i < adapter.getCount(); i++) {//遍历整个适配器中的Item的View
			View listItem=adapter.getView(i, null, lv);
			listItem.measure(0, 0);//为每一个listItem测量高度
			totalHeight+=listItem.getMeasuredHeight();//累加每个已测量listItem的高度
		}
		LayoutParams params=lv.getLayoutParams();//先得到LayoutParams对象，接着就是修改该对象中的height的属性值
		params.height=totalHeight+(lv.getDividerHeight()*(lv.getCount()-1));//listItem的总高度+所有divider(分割线)的高度之和=最后的ListView的总高度
		lv.setLayoutParams(params);//最后重新设置LayoutParams对象
	}
}

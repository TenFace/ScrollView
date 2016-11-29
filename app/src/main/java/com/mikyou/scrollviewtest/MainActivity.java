package com.mikyou.scrollviewtest;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikyou.myview.MyScrollView;
import com.mikyou.myview.MyScrollView.OnScrollViewListener;
import com.mikyou.tools.ScrollViewConflictWithListViewUtils;
import com.mikyou.tools.SystemStatusManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnScrollViewListener {
    private ListView mListView;
    private LinearLayout centerBuy, topBuy, linear;
    private RelativeLayout include_header_home;
    private MyScrollView mScrollView;
    private ViewGroup mParentView;
    private TextView textView;
    private TextView test;
    private PopupWindow popupWindow;
    private ListView listView;

    private List<String> strs = new ArrayList<String>();
    private MyPupListAdapter pupListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTranslucentStatus();
        setContentView(R.layout.activity_main);
        initView();
        initListView();
        initData();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    include_header_home.setVisibility(View.GONE);
                    if (popupWindow == null) {
                        popupWindow = new PopupWindow(MainActivity.this);
                        popupWindow.setContentView(listView);
                        // PopupWindow必须设置高度和宽度
                        popupWindow.setWidth(linear.getWidth()); // 设置宽度
                        popupWindow.setHeight(800); // 设置popWin 高度
                        popupWindow.showAsDropDown(linear, 0, linear.getHeight());
                        popupWindow.setOutsideTouchable(true);// 设置后点击其他位置PopupWindow将消失
                    } else if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    } else {
                        popupWindow.showAsDropDown(textView);
                }
            }

        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    include_header_home.setVisibility(View.GONE);
                if (popupWindow == null) {
                    popupWindow = new PopupWindow(MainActivity.this);
                    popupWindow.setContentView(listView);
                    // PopupWindow必须设置高度和宽度
                    popupWindow.setWidth(linear.getWidth()); // 设置宽度
                    popupWindow.setHeight(800); // 设置popWin 高度
                    popupWindow.showAsDropDown(linear, 0, linear.getHeight());
                    popupWindow.setOutsideTouchable(true);// 设置后点击其他位置PopupWindow将消失
                } else if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(textView);
                }
            }
        });
    }

    private void setTranslucentStatus() {//沉浸标题栏效果
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);
        tintManager.setNavigationBarTintEnabled(true);
    }

    private void initData() {
        initListViewData();
    }

    private void initListViewData() {
        mListView.setAdapter(new MyAdapter());
        /**
         * 解决ScrollView与ListView冲突的问题
         * 该方法需要在setAdapter之后调用
         * */
        ScrollViewConflictWithListViewUtils.setListViewHeight(mListView);
    }

    private void initView() {
        registerAllViewId();
        registerAllViewEvent();
    }

    private void registerAllViewEvent() {
        mScrollView.setOnScrollViewListener(this);
        //但布局的状态以及布局中某些控件的可见性发生改变时，会触发回调
        mParentView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mScrollView.getScrollY());//这一步很关键，因为在这里我们手动调用了onScroll方法
                //开始时初始化界面没滑动，mScrollView.getScrollY()为0,所以此时优先调用一次onScroll方法
                //如果我们不在初始化界面监听并手动调用该方法的话，onScroll方法，只会在滑动的时候产生回调
                //一开始并不会产生回调，也就会导致topBuy（顶部的布局）和centerBuy(中间的布局)无法在第一时间内整个界面一开始并没有滑动就重合
                //就会看见有两个相同的布局，一个在顶部，一个在中间左右，而我们一旦滑动就会触发回调onScroll方法，此时Math.max(scrollY,centerBuy.getTop)
                //取最大值肯定就是centerBuy.getTop(),那么接着利用layout方法，重新绘制topBuy（顶部布局），使得顶部布局正好与中间布局重合在一起，从而使得
                //所以，如果一开始我们就手动调用一次	onScroll(mScrollView.getScrollY());就能在第一时间内使得两个布局重合，给人感觉就是一个布局，实际上是
                //两个布局，只是重新绘制了顶部布局位置，使它重叠在中间布局上面，给人感觉就是顶部布局消失了一样
            }
        });
    }

    private void registerAllViewId() {
        mListView = (ListView) findViewById(R.id.listview);
        centerBuy = (LinearLayout) findViewById(R.id.center_buy);
        topBuy = (LinearLayout) findViewById(R.id.top_buy);
        mScrollView = (MyScrollView) findViewById(R.id.myscrollview);
        mParentView = (ViewGroup) findViewById(R.id.parent_layout);
        textView = (TextView) findViewById(R.id.textView);
        test = (TextView) findViewById(R.id.test);
        include_header_home = (RelativeLayout) findViewById(R.id.include_header_home);
        linear = (LinearLayout) findViewById(R.id.linear);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this, R.layout.home_list_item, null);
            return view;
        }
    }

    @Override
    public void onScroll(int scrollY) {
        System.out.println("ScrollY---->" + scrollY);
        System.out.println("centerBuy_getTop中间布局离ScrollView距离:------->" + centerBuy.getTop());
        int topBuyParentTop = Math.max(scrollY, centerBuy.getTop());
        System.out.println("topBuy顶部布局离ScrollView顶部距离:" + topBuyParentTop);
        topBuy.layout(0, topBuyParentTop, topBuy.getWidth(), topBuyParentTop + topBuy.getHeight());
        include_header_home.setVisibility(View.VISIBLE);

    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        for (int i = 0; i < 10; i++) {
            strs.add("第" + i + "条记录");
        }
        listView = new ListView(this);
        if (pupListAdapter == null) {
            pupListAdapter = new MyPupListAdapter();
            listView.setAdapter(pupListAdapter);
        } else {
            pupListAdapter.notifyDataSetChanged();
        }
        listView.setBackgroundResource(R.color.level2_countdown_item_bg);
        listView.setDividerHeight(2);
        listView.setVerticalScrollBarEnabled(false);
    }

    private class MyPupListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return strs.size();
        }

        @Override
        public Object getItem(int position) {
            return strs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null) {
                view = View.inflate(MainActivity.this, R.layout.list_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv_listitem_delete);
                holder.tv = (TextView) view.findViewById(R.id.tv_listitem_content);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.tv.setText(strs.get(position));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//					et.setText(strs.get(position));
                    popupWindow.dismiss();
                }
            });
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 从List中移除
                    strs.remove(position);
                    // 更新ListView
                    pupListAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }

    }

    private class ViewHolder {
        private TextView tv;
        private ImageView iv;
    }


}

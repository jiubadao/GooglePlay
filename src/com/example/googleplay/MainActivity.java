package com.example.googleplay;

import com.astuetz.PagerSlidingTabStripExtends;
import com.example.googleplay.base.BaseActivity;
import com.example.googleplay.base.BaseFragment;
import com.example.googleplay.factory.FragmentFactory;
import com.example.googleplay.holder.MeunHolder;
import com.example.googleplay.utils.UIUtils;
import com.example.googleplay.views.DepthPageTransformer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends BaseActivity
{
	// tab导航栏
	private PagerSlidingTabStripExtends mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	// tab导航栏标题
	private String[] mMainTitles;
	// 实现了侧滑菜单效果的控件
	private DrawerLayout mDrawerLayout;
	// ActionBar的开关
	private ActionBarDrawerToggle mToggle;
	// 左侧菜单滚动条
	private ScrollView mMeun;
	private ActionBar mActionBar;

	/** 初始化view */
	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		mPagerSlidingTabStrip = (PagerSlidingTabStripExtends) findViewById(R.id.mian_tabs);
		mViewPager = (ViewPager) findViewById(R.id.mian_pager);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawlayout);
		// 左侧抽屉
		mMeun = (ScrollView) findViewById(R.id.main_meun);
	}

	/** 初始ActionBar */
	@Override
	public void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setLogo(R.drawable.ic_launcher);
		mActionBar.setTitle("应用市场");
		// 设置显示返回按钮
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		// 初始化开关
		initActionBarToggle();
	}

	/** 初始化开关 */
	private void initActionBarToggle() {
		// 使用ActionBarDrawerToggle作为监听器
		mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_am, R.string.open,
				R.string.close);
		// 设置mDrawerLayout拖动的监听
		mDrawerLayout.setDrawerListener(mToggle);

		// 同步状态
		// Synchronize the state of the drawer indicator/affordance with the
		// linked DrawerLayout.
		// mToggle.syncState();
	}

	/** 初始化Data */
	@Override
	public void initData() {
		// 获取导航栏标题数据
		mMainTitles = UIUtils.getStringArr(R.array.main_titles);
		// 将viewPager绑定适配器
		MainFragmentStatePagerAdapter adapter = new MainFragmentStatePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		// Bind the tabs to the ViewPager
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		// 给ViewPager设置动画
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
		// 左侧菜单
		MeunHolder meunHolder = new MeunHolder(mDrawerLayout);
		mMeun.addView(meunHolder.getHolderView());
		meunHolder.setDataAndRefreshHolderView(null);
	}

	/** 设置监听事件 */
	@Override
	public void initListener() {
		// 给mTabs导航栏设置监听事件
		mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// 获取缓存中的fragment
				BaseFragment fragment = FragmentFactory.getFragment(position);
				// 如果fragment不为空，触发加载数据
				if (fragment != null) {
					// 加载数据
					fragment.getLoadingPager().loadData();
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			/**
			 * 使用ActionBarDrawerToggle的onOptionsItemSelected方法。
			 * 该方法activity的onOptionsItemSelected方法中根据传递进来的menu item做了
			 * 在activity的onOptionsItemSelected方法中判断点击事件是否来自于app图标，
			 * 然后用DrawerLayout.closeDrawer和DrawerLayout.openDrawer来隐藏与展开
			 */
			// mToggle控制打开关闭菜单,home被选中的时候，mToggle也被选中
			mToggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * FragmentStatePagerAdapter ---> fragment 填充mViewPager
	 * 未缓存fragment，其实它缓存了fragment状态
	 */
	class MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter
	{

		public MainFragmentStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = FragmentFactory.getFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
			if (mMainTitles != null) {
				return mMainTitles.length;
			}
			return 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mMainTitles[position];
		}

	}

	/**
	 * PagerAdapter ---> view 填充mViewPager
	 */
	class MainPagerAdapter extends PagerAdapter
	{

		@Override
		public int getCount() {
			if (null != mMainTitles) {
				return mMainTitles.length;
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TextView tv = new TextView(UIUtils.getContext());
			container.addView(tv);
			return tv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		// 该方法需要重写，默认返回null，报空指针异常！
		@Override
		public CharSequence getPageTitle(int position) {
			return mMainTitles[position];
		}
	}

	/**
	 * FragmentPagerAdapter ---> fragment 填充mViewPager
	 * 缓存了fragment，如果fragment比较多，就不建议使用
	 */
	class MainFragmentPagerAdapter extends FragmentPagerAdapter
	{

		public MainFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 创建fragment
			Fragment fragment = FragmentFactory.getFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
			if (mMainTitles != null) {
				return mMainTitles.length;
			}
			return 0;
		}

		// 该方法需要重写，默认返回null，报空指针异常！
		@Override
		public CharSequence getPageTitle(int position) {
			return mMainTitles[position];
		}

	}

}

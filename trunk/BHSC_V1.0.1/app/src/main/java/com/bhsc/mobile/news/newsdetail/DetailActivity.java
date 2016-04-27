package com.bhsc.mobile.news.newsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bhsc.mobile.R;

/**
 * Created by lynn on 15-9-19.
 */
public class DetailActivity extends Activity {
    private final String TAG = DetailActivity.class.getName();

    public static final String INTENT_KEY_NEWSID = "newsId";


    /**
     * 新闻ID
     */
    private String mNewsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        Intent intent = getIntent();
        if (intent != null) {
            mNewsId = intent.getStringExtra(INTENT_KEY_NEWSID);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        initData();
        initView();
    }

    private void initView() {
    }

    private void initData() {
    }

    private void goBack() {
        finish();
    }

    private void support() {
    }

    private void opposition() {
    }

    private void collect() {
    }

    private void discuss() {
    }

//    private void onNewsItemClick() {
//        Intent intent = new Intent();
//        intent.setClass(this, DetailActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    private void onDiscussItemClick() {
//        Intent intent = new Intent();
//        intent.setClass(this, DiscussDetailActivity.class);
//        startActivity(intent);
//    }
//
//    private void onEventMainThread(SupportEvent event) {
//        if (event.getState() == SupportEvent.STATE_NEGATIVE) {
//            mViews.activity_newsdetail_support_icon.setBackgroundResource(R.mipmap.btn_support_big_normal);
//        } else {
//            mViews.activity_newsdetail_support_icon.setBackgroundResource(R.mipmap.btn_support_big_press);
//        }
//    }
//
//    private void onEventMainThread(CollectEvent event) {
//        if (event.getState() == SupportEvent.STATE_NEGATIVE) {
//            mViews.activity_newsdetail_collect_icon.setBackgroundResource(R.mipmap.btn_collect_normal);
//        } else {
//            mViews.activity_newsdetail_collect_icon.setBackgroundResource(R.mipmap.btn_collect_press);
//        }
//    }
//
//    private void onEventMainThread(OppEvent event) {
//        if (event.getState() == SupportEvent.STATE_NEGATIVE) {
//            mViews.activity_newsdetail_opposition_icon.setBackgroundResource(R.mipmap.btn_opposition_normal);
//        } else {
//            mViews.activity_newsdetail_opposition_icon.setBackgroundResource(R.mipmap.btn_opposition_press);
//        }
//    }
//
//    private void onEventMainThread(RelatedNewsEvent event) {
//        List<Data_DB_News> newsList = event.getNewsList();
//        L.i(TAG, "初始化新闻列表:" + newsList.size());
//        if (newsList.size() > 0) {
//            mViews.activity_newsdetail_related_news.setVisibility(View.VISIBLE);
//            mViews.activity_newsdetail_news_list_divider.setVisibility(View.VISIBLE);
//        }
//        mNewsList.addAll(newsList);
//        mNewsAdapter.notifyDataSetChanged();
//    }
//
//    private void onEventMainThread(DiscussEvent event) {
//        L.i(TAG, "discuss event");
//        if (event.getAction() == DiscussEvent.ACTION_GET_ALL_DISCUSS) {
//            L.i(TAG, "get all discuss");
//            List<Data_DB_Discuss> discusses = (List<Data_DB_Discuss>) event.getExtra();
//            if (discusses.size() > 0) {
//                mDiscusses.clear();
//                mDiscusses.addAll(discusses);
//                mHotDiscussAdapter.notifyDataSetChanged();
//            }
//        } else if (event.getAction() == DiscussEvent.ACTION_PUBLISH_SUCCESS) {
//            L.i(TAG, "publish success!");
//            NewsPresenter.getInstance(this).getAllDiscuss(mNews);
//        }
//    }
//
//    private void onEventMainThread(HotDiscussEvent event) {
//        List<Data_DB_Discuss> discusses = event.getDiscusses();
//        L.i(TAG, "初始化评论列表:" + discusses.size());
//        if (discusses.size() > 0) {
//            mViews.activity_newsdetail_hot_discuss.setVisibility(View.VISIBLE);
//        }
//        mDiscusses.clear();
//        mDiscusses.addAll(discusses);
//        mHotDiscussAdapter.notifyDataSetChanged();
//    }
//
//    private void showDialog() {
//        backgroundAlpha(0.4f);
//
//        View view = LayoutInflater.from(this).inflate(R.layout.pop_input_discuss, null);
//
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        mPopupWindow.setAnimationStyle(R.style.pop_anim_style);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setOutsideTouchable(true);
//
//        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });
//
//        final Button status = (Button) view.findViewById(R.id.pop_input_discuss_status);
//        final EditText text = (EditText) view.findViewById(R.id.pop_input_discuss_edit);
//        Button publish = (Button) view.findViewById(R.id.pop_input_discuss_publish);
//
//        status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Boolean toUserStatus = (Boolean) status.getTag();
//                if (toUserStatus == null || !toUserStatus) {
//                    status.setTag(true);
//                    status.setBackgroundResource(R.mipmap.btn_status_press);
//                } else {
//                    status.setTag(false);
//                    status.setBackgroundResource(R.mipmap.btn_status_normal);
//                }
//            }
//        });
//
//        publish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String discussContent = text.getText().toString();
//                if (TextUtils.isEmpty(discussContent)) {
//                    return;
//                }
//                Data_DB_Discuss discuss = new Data_DB_Discuss();
//                discuss.setContent(discussContent);
//                discuss.setType(mNews.getType());
//                discuss.setCreateTime(Method.getTS());
//                discuss.setFatherId(mNews.getId());
//                discuss.setUserId(mUserManager.getCurrentUser().getUserId());
//                Boolean toUserStatus = (Boolean) status.getTag();
//                if (toUserStatus == null || !toUserStatus) {
//                    discuss.setToUserStatus(false);
//                } else {
//                    discuss.setToUserStatus(true);
//                }
//                discuss.setCreateTime(Method.getTS());
//                NewsPresenter.getInstance(DetailActivity.this).discussPublish(discuss);
//                mPopupWindow.dismiss();
//            }
//        });
//
//        mPopupWindow.showAtLocation(findViewById(R.id.activity_newsdetail_container),
//                Gravity.BOTTOM, 0, 0);
//    }
//
//    /**
//     * 设置添加屏幕的背景透明度
//     *
//     * @param bgAlpha
//     */
//    public void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        getWindow().setAttributes(lp);
//    }
}

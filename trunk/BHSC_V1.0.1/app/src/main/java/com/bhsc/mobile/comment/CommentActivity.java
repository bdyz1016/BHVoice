package com.bhsc.mobile.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.comment.model.Data_DB_Comment;
import com.bhsc.mobile.userpages.LoginAndRegisterActivity;
import com.bhsc.mobile.userpages.UserManager;
import com.bhsc.mobile.utils.L;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhanglei on 16/4/27.
 */
public class CommentActivity extends Activity {
    private final String TAG = CommentActivity.class.getSimpleName();
    public static final String INTENT_FATHER_ID = "father_id";
    public static final String INTENT_COMMENT_TYPE = "comment_type";

    private ListView Lv_Comments;
    private EditText Edit_Discuss;
    private View mSend;
    private View mEmptyView;
    private View mBack;
    private CommentAdapter mAdapter;
    private CommentManager mManager;

    private long mFatherId;
    private int mCommentType;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initWidget() {
        Lv_Comments = (ListView) findViewById(R.id.list);
        mEmptyView = findViewById(R.id.empty);
        Edit_Discuss = (EditText) findViewById(R.id.discuss);
        mSend = findViewById(R.id.send);
        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(mBackClickListener);
        mSend.setOnClickListener(mAddCommentListener);
        Lv_Comments.setEmptyView(mEmptyView);
    }

    private void initData() {
        mContext = this;
        mFatherId = getIntent().getLongExtra(INTENT_FATHER_ID, -1);
        mCommentType = getIntent().getIntExtra(INTENT_COMMENT_TYPE, -1);
        L.i(TAG, "father id:" + mFatherId);
        L.i(TAG, "comment type:" + mCommentType);
        mAdapter = new CommentAdapter(this);
        Lv_Comments.setAdapter(mAdapter);

        mManager = new CommentManager(this);
        mManager.loadComment(mFatherId, mCommentType, mCommentListener);
    }

    private View.OnClickListener mAddCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (UserManager.isLogin()) {
                mManager.addComment(UserManager.getUser().getId(), mFatherId,
                        Edit_Discuss.getText().toString(), mCommentType,
                        new CommentManager.OnAddCommentListener() {
                            @Override
                            public void success(Data_DB_Comment comment) {
                                mManager.loadComment(mFatherId, mCommentType, mCommentListener);
                            }

                            @Override
                            public void failed() {

                            }
                        });
                Edit_Discuss.setText("");
                hideSoftInputMethod(Edit_Discuss);
            } else {
                startActivity(new Intent(mContext, LoginAndRegisterActivity.class));
            }
        }
    };

    private void hideSoftInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private View.OnClickListener mBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private CommentManager.OnCommentListener mCommentListener = new CommentManager.OnCommentListener() {
        @Override
        public void onLoaded(List<Data_DB_Comment> list) {
            if (list.size() > 0) {
                Collections.sort(list);
                mAdapter.clear();
                mAdapter.addAll(list);
            } else {
                mEmptyView.setVisibility(View.GONE);
                Toast.makeText(mContext, "暂无评论", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoadFailed() {
            Toast.makeText(mContext, "评论加载失败", Toast.LENGTH_SHORT).show();
        }
    };
}

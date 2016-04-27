package com.bhsc.mobile.news.newsdetail.adapter;

/**
 * Created by lynn on 15-10-10.
 */
//public class HotDiscussAdapter extends BaseAdapter {

//    private LayoutInflater mInflater;
//    private List<Data_DB_Discuss> mDiscusses;
//
//    public HotDiscussAdapter(Context context, List<Data_DB_Discuss> discusses){
//        mInflater = LayoutInflater.from(context);
//        mDiscusses = discusses;
//    }
//
//    @Override
//    public int getCount() {
//        return mDiscusses.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mDiscusses.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if(convertView == null){
//            convertView = mInflater.inflate(R.layout.item_discuss, parent, false);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        Data_DB_Discuss discuss = mDiscusses.get(position);
//        holder.Tv_Content.setText(discuss.getContent());
//        holder.Tv_Time.setText(Method.getTime(discuss.getCreateTime()));
//        return convertView;
//    }
//
//
//    private class ViewHolder{
//        private ImageView Iv_Photo;
//        private TextView Tv_NickName, Tv_Time, Tv_Content;
//        public ViewHolder(View view){
//            Iv_Photo = (ImageView) view.findViewById(R.id.item_discuss_photo);
//            Tv_NickName = (TextView) view.findViewById(R.id.item_discuss_nickname);
//            Tv_Time = (TextView) view.findViewById(R.id.item_discuss_time);
//            Tv_Content = (TextView) view.findViewById(R.id.item_discuss_content);
//        }
//    }
//}

package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_search extends BaseAdapter {

    private Context context;
    private ArrayList<Item_user> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public Adapter_search(ArrayList<Item_user> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        Log.i("getcount",String.valueOf(list.size()));
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        Log.i("getItem","");
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        Log.i("getItemId","");
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
//        if(convertView == null){
//            convertView = inflate.inflate(R.layout.item_profile,null);
//
//            viewHolder = new ViewHolder();
//            viewHolder.이름 = (TextView) convertView.findViewById(R.id.프로필_이름);
//
//            convertView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder)convertView.getTag();
//        }
        View view=inflate.inflate(R.layout.item_profile,null);
        Log.i("뷰 선언","");

       CircleImageView 프로필이미지 = (CircleImageView) view.findViewById(R.id.프로필_이미지);
        Log.i("프로필이미지 선언","");
        TextView 이름 = (TextView)view.findViewById(R.id.프로필_이름);
        Log.i("이름 선언","");
        TextView 이메일 = (TextView)view.findViewById(R.id.프로필_이메일);
        Log.i("이메일 선언","");
        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        이름.setText(list.get(position).get이름());
        이름.setTextColor(Color.BLACK);
        Log.i("이름 설정",list.get(position).get이름());
        이메일.setText(list.get(position).get이메일());
        이메일.setTextColor(Color.BLACK);
        Log.i("이메일 설정",list.get(position).get이메일());
        if(프로필이미지!=null){
        프로필이미지.setImageBitmap(list.get(position).get프로필이미지());
        }
        return view;
    }

    class ViewHolder{
        public TextView 이름;
        public TextView 이메일;
        public ImageView 프로필이미지;


    }
    public void setarraylist(ArrayList<Item_user> 유저리스트) {
        this.list = 유저리스트;
    }
}
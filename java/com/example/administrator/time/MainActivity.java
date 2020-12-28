package com.example.administrator.time;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 刚装程序时的事件
     */
    String InitPlan[]={
            "学习","工作","背单词",
            "锻炼","玩手机","出去浪" };

/*    "学习","学习C++","学习JAVA" ,"学习算法" ,
            "学习安卓","工作" ,"背单词" ,
            "锻炼" ,"玩手机" ,"咸鱼" ,"出去浪"*/

    //全局变量

    private HistogramView green;//统计图
    /**
     * 为true时按时间,为false时按项目
     */
    boolean accordingToTime=true;
    //计时功能的开始时间
    long starttime=0;

    //选中的emoji表情
    int imageSelectNum=0;
    ImageButton imageSelect=null;
    ViewPager viewPager;
    private List<View> views = new ArrayList<View>();//中间的滑动视图

    ArrayList<String> plans = new ArrayList<String>();//左边的一列
    //ArrayList<String> drinks = new ArrayList<String>();//上面显示的一列
    ArrayList<String> demons = new ArrayList<String>();//浏览的下拉菜单
    ArrayList<String> plandemons = new ArrayList<String>();//计划的下拉菜单
    ArrayList<String> graphs = new ArrayList<String>();//统计图的下拉菜单

    MyAdapter myadapter;//左边的一列
    //ArrayAdapter <String> myadapter2;
    SimpleAdapter myadapter2;//主页的浏览

    SimpleAdapter mSimpleAdapter;//浏览所有
    SimpleAdapter mSimpleAdapter_plan;//计划

    SimpleAdapter mAdapter_config1_list1;
    SimpleAdapter mAdapter_config1_list2;
    SimpleAdapter mAdapter_config1_list3;
    ArrayList<HashMap<String, Object>> listItem_config1_list1;//浏览所有
    ArrayList<HashMap<String, Object>> listItem_config1_list2;//浏览所有
    ArrayList<HashMap<String, Object>> listItem_config1_list3;//浏览所有

    SimpleAdapter mAdapter_config2_list1;
    SimpleAdapter mAdapter_config2_list2;
    SimpleAdapter mAdapter_config2_list3;
    ArrayList<HashMap<String, Object>> listItem_config2_list1;//浏览所有
    ArrayList<HashMap<String, Object>> listItem_config2_list2;//浏览所有
    ArrayList<HashMap<String, Object>> listItem_config2_list3;//浏览所有



    ArrayAdapter <String> myadapter_spinner;
    ArrayAdapter <String> myadapter_plan_spinner;
    ArrayAdapter <String> myadapter_data_spinner;
    ArrayAdapter <String> myadapter_addplan_spinner;
    ArrayAdapter <String> myadapter_count_spinner;

    ArrayList<HashMap<String, Object>> listItem;//浏览所有
    ArrayList<HashMap<String, Object>> drinks;//主页的浏览
    ArrayList<HashMap<String, Object>> trips;//schedule

    ArrayAdapter <String> showItems;//按时间统计时要显示的条目

    TextView ItemSelectNow;
    UserDBHelper helpter;
    UserDBHelper2 helpter2;
    UserDBHelper3 helpter3;
    private int cur_pos = 0;

    //统计图显示设置的设置
    ArrayList<String> ItemsToShow = new ArrayList<String>(); //设置-显示的条目
    String set1Xscale="month";
    String set2Xscale="day";
    String set2timeset="now";
    int set2Xmax=0;
    String setYmax="auto";





    /**
     * 添加页面
     */
    View tab01;
    /**
     * 计划页面
     */
    View tab02;
    /**
     * 浏览页面
     */
    View tab03;
    /**
     * 统计图页面
     */
    View tab04;
    /**
     * 设置页面
     */
    View tab05;
    TextView textItem;//添加页面的类别文本框
    TextView textNum;//添加页面的数字文本框
    TextView textunit;//添加页面的单位文本框
    private int viewPagerNow = 0;//当前的页面是哪一个页面(0-4)

    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    }

    private class MyAdapter extends BaseAdapter {
            private ArrayList<String> listdata;
            private Context context;
            private LayoutInflater inflater;


            public MyAdapter(Context context,ArrayList<String> listdata){
                this.context = context;
                this.listdata = listdata;

                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }



        @Override
            public int getCount() {
                return plans.size();
            }

            @Override
            public Object getItem(int position) {
                return plans.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tmView;
                String temstr;
                if(convertView == null){
                    TextView tm = new TextView(context);
                    tmView = tm;
                    tmView.setText(listdata.get(position));
                    tmView.setTextSize(18);
                    tmView.setPadding(20 ,20,0,20);

                }else {
                    tmView = (TextView)convertView;
                    tmView.setText(listdata.get(position) );
                }
                if(cur_pos==position+1)tmView.setBackgroundColor(0x7f777777);
                else tmView.setBackgroundColor(0x00ffffff);
                return tmView;
            }
            public void add (String arg1){
                this.listdata.add(arg1);
            }
            public void remove(String arg1){
                this.listdata.remove(arg1);
            }
        }

    public class MyPagerAdapter extends PagerAdapter {

        /*
         * 使用 pagerAdapter  注意几点：
         * 重写方法的时候是 含有 ViewGroup的方法 :
         * 适合用在 使用 layout 布局实现
         * instantiateItem(ViewGroup container, int position)
         * destroyItem(ViewGroup container, int position, Object object)
         *
         */
        private List<View> views;
        private List<String> titiles;

        public MyPagerAdapter(List<View> views,List<String> titles) {
            this.views=views;
            this.titiles=titles;
        }
        public MyPagerAdapter(List<View> views) {
            this.views=views;
            //this.titiles=titles;
        }

        /**
         * 返回 页卡 的数量
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        /**
         *  判断 是 view 是否来自对象
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0==arg1;
        }

        /**
         * 实例化 一个 页卡
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 添加一个 页卡

            container.addView(views.get(position));

            return views.get(position);
        }

        /**
         * 销毁 一个 页卡
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 删除
            container.removeView(views.get(position));
        }

        /**
         *  重写 标题的 方法
         */
        @Override
        public CharSequence getPageTitle(int position) {
            // 给页面添加标题
            return titiles.get(position);
        }

    }

    public class UserDBHelper extends SQLiteOpenHelper {
            private static final String DB_NAME = "timeapp.db";
            private static final int DB_VERSION=1;
            //private static UserDBHelper mHelper=null;
            private static final String TABLE_NAME = "time_record";
            private SQLiteDatabase db;
            public UserDBHelper(Context context) {
                super(context, DB_NAME, null, DB_VERSION);
            }
            public UserDBHelper(Context context,int version){
                super(context, DB_NAME, null, version);
            }

            public  void CreateTable(){
                SQLiteDatabase db = getWritableDatabase();
                String CREATE_SQL="CREATE TABLE IF NOT EXISTS "+"remark"
                        + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"     //0
                        + "remark INTEGER NOT NULL,"                                  //1
                        +"createdate DATE);";                                       //2
                db.execSQL(CREATE_SQL);
            }


            @Override
            public void onCreate(SQLiteDatabase db) {
                this.db = db;
                String CREATE_SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                        + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + "event VARCHAR NOT NULL,"+"minutes INTEGER NOT NULL,"
                        +"createdate DATE,"+"createtime TIME);";
                db.execSQL(CREATE_SQL);
                CREATE_SQL="CREATE TABLE IF NOT EXISTS "+"plan_record"
                        + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"     //0
                        + "days INTEGER NOT NULL,"                                  //1
                        + "event VARCHAR NOT NULL,"+"minutes INTEGER NOT NULL," //2 + 3
                        +"createdate DATE);";                                       //4
                db.execSQL(CREATE_SQL);

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
            public void insert(ContentValues values) {
                SQLiteDatabase db = getWritableDatabase();
                db.insert(TABLE_NAME, null, values);
                db.close();
            }

            public Cursor query() {
                if (db == null)
                    db = getWritableDatabase();
                Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
                return c;
            }
            public void del(int id) {
                if (db == null)
                    db = getWritableDatabase();
                db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
            }
            public void close() {
                if (db != null)
                    db.close();
            }
        }//主数据表

    public class UserDBHelper2 extends SQLiteOpenHelper {
        private static final String DB_NAME = "timeapp.db";
        private static final int DB_VERSION=1;
        //private static UserDBHelper mHelper=null;
        private static final String TABLE_NAME = "plan_record";
        private SQLiteDatabase db;
        public UserDBHelper2(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
        public UserDBHelper2(Context context,int version){
            super(context, DB_NAME, null, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            this.db = db;
            String CREATE_SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                    + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"     //0
                    + "days INTEGER NOT NULL,"                                  //1
                    + "event VARCHAR NOT NULL,"+"minutes INTEGER NOT NULL," //2 + 3
                    +"createdate DATE);";                                       //4
            db.execSQL(CREATE_SQL);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
        public void insert(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_NAME, null, values);
            db.close();
        }

        public Cursor query() {
            if (db == null)
                db = getWritableDatabase();
            Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
            return c;
        }
        public void del(int id) {
            if (db == null)
                db = getWritableDatabase();
            db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
        }
        public void close() {
            if (db != null)
                db.close();
        }
    }//计划的数据表

    public class UserDBHelper3 extends SQLiteOpenHelper {
        private static final String DB_NAME = "timeapp.db";
        private static final int DB_VERSION=1;
        //private static UserDBHelper mHelper=null;
        private static final String TABLE_NAME = "remark";
        private SQLiteDatabase db;
        public UserDBHelper3(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
        public UserDBHelper3(Context context,int version){
            super(context, DB_NAME, null, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            this.db = db;
            String CREATE_SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                    + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"     //0
                    + "days INTEGER NOT NULL,"                                  //1
                    + "event VARCHAR NOT NULL,"+"minutes INTEGER NOT NULL," //2 + 3
                    +"createdate DATE);";                                       //4
            db.execSQL(CREATE_SQL);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
        public void insert(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_NAME, null, values);
            db.close();
        }

        public Cursor query() {
            if (db == null)
                db = getWritableDatabase();
            Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
            return c;
        }
        public void del(int id) {
            if (db == null)
                db = getWritableDatabase();
            db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
        }
        public void close() {
            if (db != null)
                db.close();
        }
    }//emoji
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);



        LayoutInflater mInflater = LayoutInflater.from(this);
        tab01 = mInflater.inflate(R.layout.layout1, null);
        tab02 = mInflater.inflate(R.layout.layout2, null);
        tab03 = mInflater.inflate(R.layout.layout3, null);
        tab04 = mInflater.inflate(R.layout.layout4, null);
        tab05 = mInflater.inflate(R.layout.layout5, null);
        views.add(tab01);
        views.add(tab02);
        views.add(tab03);
        views.add(tab04);
        views.add(tab05);

        MyPagerAdapter adapter = new MyPagerAdapter(views);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeTab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

        textItem=(TextView)tab01.findViewById(R.id.textitem);
        textNum=(TextView)tab01.findViewById(R.id.textnum);
        textunit=(TextView)tab01.findViewById(R.id.textunit);

        LinearLayout ll1=(LinearLayout)findViewById(R.id.llAdd);
        LinearLayout ll2=(LinearLayout)findViewById(R.id.llPlans);
        LinearLayout ll3=(LinearLayout)findViewById(R.id.llData);
        LinearLayout ll4=(LinearLayout)findViewById(R.id.llSettings);

        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);

        ImageView imageMore=(ImageView)findViewById(R.id.imageMore);
        final TextView textTitle=(TextView)findViewById(R.id.textTitle);
        imageMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textTitle.getText().toString().equals("Time")) {
                    showCountDialog();
                }else{
                    if (demons.get(0).contains("没有可以显示的")) {
                        Toast.makeText(MainActivity.this, "没有可以显示的条目", Toast.LENGTH_SHORT).show();
                    } else {
                        textTitle.setText("Statistics");
                        changeTab(34);
                    }
                }
            }
        });
        ImageView imageLess=(ImageView)findViewById(R.id.imageLess);
        imageLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTitle.setText("Browse");
                changeTab(43);
            }
        });


        ListView myList1 = (ListView) tab01.findViewById(R.id.listView1);
        ListView myList2=(ListView)tab01.findViewById(R.id.listview2);

        myList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListView myList=(ListView)tab01.findViewById(R.id.listView1);
                //myList.getAdapter().getView(position).setBackgroundColor(0x7f777777);



                TextView txv= (TextView) view;
                //txv.setBackgroundColor();
                String item = txv.getText().toString();


                if (item != "添加         ⊕"){
                    view.setBackgroundColor(0x7f777777);
                    TextView textItem = (TextView)tab01.findViewById(R.id.textitem);
                    textItem.setText(item);
                }

                //Toast.makeText(MainActivity.this, "您点击的是"+item , Toast.LENGTH_SHORT).show();
                if (ItemSelectNow!=null){
                    if (ItemSelectNow == txv){
                        txv.setBackgroundColor(0x00ffffff);
                        ItemSelectNow=null;
                        cur_pos=0;

                    }else{
                        ItemSelectNow.setBackgroundColor(0x00ffffff);
                        ItemSelectNow=txv;
                        cur_pos=position+1;
                    }

                }else {
                    ItemSelectNow=txv;
                    cur_pos=position+1;
                }
                if (item == "添加         ⊕"){
                    cur_pos=0;
                    ItemSelectNow=null;
                    showInsertDialog();
                }

            }
        });
        myList1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txv= (TextView) view;
                String item = txv.getText().toString();
                //Toast.makeText(this, "您长按的是"+item, Toast.LENGTH_SHORT).show();
                if (ItemSelectNow!=null) ItemSelectNow.setBackgroundColor(0x00ffffff);
                cur_pos=0;
                ItemSelectNow=null;


                if (item != "添加         ⊕")showNormalDialog(item);
                //if (parent.getId()==R.id.listview2 )showDelDialog(item);

                return true;
            }
        });


        //myList1.setOnItemSelectedListener(this);
        //Button MyBtn = (Button) findViewById(R.id.button);
        //MyBtn.setOnClickListener(this);


        SharedPreferences sps=getSharedPreferences("timeapp", Context.MODE_PRIVATE);
        if (sps.getBoolean("IsFirstTimeRunApp",true)){
            //"第一次运行APP"

            SharedPreferences.Editor editor=sps.edit();
            editor.putBoolean("IsFirstTimeRunApp",false);
            for (int i = 0 ;i<InitPlan.length;i++){
                plans.add(InitPlan[i]);
            }

            editor.putInt("Status_size",plans.size());

            for(int i=0;i<plans.size();i++) {
                editor.remove("Status_" + i);
                editor.putString("Status_" + i, plans.get(i));
            }

            editor.commit();
        }
        else {
            //"不是第一次运行APP"
            SharedPreferences mSharedPreference1 = getSharedPreferences("timeapp", MODE_PRIVATE);
            plans.clear();
            int size = mSharedPreference1.getInt("Status_size", 0);
            for(int i=0;i<size;i++) {
                plans.add(mSharedPreference1.getString("Status_" + i, null));
            }



        }



        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,plans);
        ListView myList=(ListView)tab01.findViewById(R.id.listView1);
        myadapter = new MyAdapter(this,plans);
        myList.setAdapter(myadapter);
        myadapter.add("添加         ⊕");
        myadapter.notifyDataSetChanged();
        //if (plans.contains("添加         ⊕")) Toast.makeText(this, "plans有添加", Toast.LENGTH_SHORT).show();
        //    else Toast.makeText(this, "plans没有添加", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db=getApplicationContext().openOrCreateDatabase("timeapp.db", Context.MODE_PRIVATE,null);
        //SQLiteDatabase db2=getApplicationContext().openOrCreateDatabase("plan.db", Context.MODE_PRIVATE,null);


        {//数据库的初始化（主数据表）
            drinks = new ArrayList<HashMap<String,     Object>>();
            HashMap<String, Object> map =null ;
            myadapter2 = new SimpleAdapter(this,drinks,//需要绑定的数据
                    R.layout.layouttile,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2",
                            "ItemText3"},
                    new int[] {R.id.textTile1,R.id.textTile2,R.id.textTile3}
            );

            myList2=(ListView)tab01.findViewById(R.id.listview2);
            myList2.setAdapter(myadapter2);

            //myadapter2.clear();

            drinks.clear();
            helpter = new UserDBHelper(this);
            helpter.CreateTable();
            Cursor c = helpter.query();
            if (c.getCount()!=0) {
                c.moveToFirst();
                c.getString(1);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str1 = formatter.format(curDate);

                for (int i = 1; i <= c.getCount(); i++) {
                    if (c.getString(3).equals(str1)) {

                        map = new HashMap<String, Object>();
                        map.put("ItemText1", c.getString(1).toString());
                        map.put("ItemText2",timeChange(Integer.valueOf(c.getString(2).toString()).intValue()));
                        map.put("ItemText3",c.getString(4).toString());

                        drinks.add(map);
//                    drinks.add(String.format("%-10s\t", c.getString(1).toString())
//                            + String.format("%-10s\t", TimeChange(Integer.valueOf(c.getString(2).toString()).intValue()))
//                            + c.getString(4).toString());
                        //plans.add(String.format("%-5s",c.getString(1).toString())+
                        //if (c.getPosition()==4)
                        //    Toast.makeText(this,c.getString(0).toString() , Toast.LENGTH_SHORT).show();

                    }
                    if (!c.isLast()) c.moveToNext();

                }

                myadapter2.notifyDataSetChanged();
            }
            c.close();

            myList2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    LinearLayout lv= (LinearLayout) view;
                    TextView txv=(TextView)lv.findViewById(R.id.textTile1);

                    String item1 = txv.getText().toString();

                    txv=(TextView)lv.findViewById(R.id.textTile2);
                    String item2 =txv.getText().toString();

                    txv=(TextView)lv.findViewById(R.id.textTile3);
                    String item3 =  txv.getText().toString();

                    //Toast.makeText(MainActivity.this, "您长按的是"+item, Toast.LENGTH_SHORT).show();
                    if (ItemSelectNow!=null) ItemSelectNow.setBackgroundColor(0x00ffffff);
                    cur_pos=0;
                    ItemSelectNow=null;


                    //if (parent.getId()==R.id.listView1 && item != "添加         ⊕")showNormalDialog(item);
                    showDelDialog(item1,item2,item3);

                    return true;
                }
            });

        }

        {//计划数据表的初始化
            helpter2 = new UserDBHelper2(this);
//            ContentValues values = new ContentValues();
//            values.put("remark", "10");
//            values.put("createdate","2017-08-15");

            helpter3 = new UserDBHelper3(this);
        }

        {//tab01 添加



            Button buttonReturn = (Button) tab01.findViewById(R.id.buttonreturn);
            buttonReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.buttonreturn){
                        String s;
                        s = textNum.getText().toString();
                        if (s.length()>1) s = s.substring(0,s.length()-1);
                        else s="";
                        textNum.setText(s);
                    }
                    if (textNum.getText().toString().equals("")) textNum.setText("0");
                }
            });

            Button button0=(Button) tab01.findViewById(R.id.button0);
            Button button1=(Button) tab01.findViewById(R.id.button1);
            Button button2=(Button) tab01.findViewById(R.id.button2);
            Button button3=(Button) tab01.findViewById(R.id.button3);
            Button button4=(Button) tab01.findViewById(R.id.button4);
            Button button5=(Button) tab01.findViewById(R.id.button5);
            Button button6=(Button) tab01.findViewById(R.id.button6);
            Button button7=(Button) tab01.findViewById(R.id.button7);
            Button button8=(Button) tab01.findViewById(R.id.button8);
            Button button9=(Button) tab01.findViewById(R.id.button9);
            Button buttondot=(Button) tab01.findViewById(R.id.buttondot);
            Button buttonH=(Button) tab01.findViewById(R.id.buttonh);
            Button buttonMin=(Button) tab01.findViewById(R.id.buttonmin);
            final ImageButton buttonRemark=(ImageButton) tab01.findViewById(R.id.buttonremark);
            buttonRemark.setImageResource(getEmojiRes(getEmojiIdByDate(getDateForToday())));
            Button buttonAdd=(Button) tab01.findViewById(R.id.buttonadd);

            button0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button0 && (!textNum.getText().toString().equals("0")))textNum.setText(textNum.getText().toString()+"0");
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button1)textNum.setText(textNum.getText().toString()+"1");
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button2)textNum.setText(textNum.getText().toString()+"2");
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button3)textNum.setText(textNum.getText().toString()+"3");
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button4)textNum.setText(textNum.getText().toString()+"4");
                }
            });
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button5)textNum.setText(textNum.getText().toString()+"5");
                }
            });
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button6)textNum.setText(textNum.getText().toString()+"6");
                }
            });
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button7)textNum.setText(textNum.getText().toString()+"7");
                }
            });
            button8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button8)textNum.setText(textNum.getText().toString()+"8");
                }
            });
            button9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.button9)textNum.setText(textNum.getText().toString()+"9");
                }
            });
            buttondot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textNum.getText().toString().equals("0")) textNum.setText("");
                    if (v.getId()==R.id.buttondot && textNum.getText().toString() != "" && !textNum.getText().toString().contains("."))textNum.setText(textNum.getText().toString()+".");
                    if (v.getId()==R.id.buttondot && textNum.getText().toString() == "" )textNum.setText("0.");
                    if (textNum.getText().toString().equals("")) textNum.setText("0");
                }
            });
            buttonH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId()==R.id.buttonh)textunit.setText("h");
                }
            });
            buttonMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId()==R.id.buttonmin)textunit.setText("min");
                }
            });
            //表情评价
            buttonRemark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEmojiDialog();
                }
            });
            buttonRemark.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonRemark.setImageResource(R.drawable.emoji_c);

                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        buttonRemark.setImageResource(getEmojiRes(getEmojiIdByDate(getDateForToday())));
                    }
                    return false;
                }
            });
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String event = textItem.getText().toString();
                    String minutes=null;

                    if (event.equals("Category")){
                        Toast.makeText(MainActivity.this, "请选择种类", Toast.LENGTH_SHORT).show();
                        textItem.setText("Category");
                        textNum.setText("0");
                        textunit.setText("time");
                        if (ItemSelectNow!=null) ItemSelectNow.setBackgroundColor(0x00ffffff);
                        cur_pos=0;
                        ItemSelectNow=null;
                        return;
                    }
                    if (textunit.getText().toString().equals("time")){
                        Toast.makeText(MainActivity.this, "请选择时间单位", Toast.LENGTH_SHORT).show();
                        textItem.setText("Category");
                        textNum.setText("0");
                        textunit.setText("time");
                        if (ItemSelectNow!=null) ItemSelectNow.setBackgroundColor(0x00ffffff);
                        cur_pos=0;
                        ItemSelectNow=null;
                        return;
                    }
                    if (textNum.getText().toString().equals("0")||textNum.getText().toString().equals("")){
                        Toast.makeText(MainActivity.this, "没有"+ event +"还好意思添加啊", Toast.LENGTH_SHORT).show();
                        textItem.setText("Category");
                        textNum.setText("0");
                        textunit.setText("time");
                        if (ItemSelectNow!=null) ItemSelectNow.setBackgroundColor(0x00ffffff);
                        cur_pos=0;
                        ItemSelectNow=null;
                        return;
                    }

                    if (textunit.getText().toString().equals("min")) {
                        if (textNum.getText().toString().contains(".")) {
                            textItem.setText("Category");
                            textNum.setText("0");
                            textunit.setText("time");
                            Toast.makeText(MainActivity.this, "请输入整数分钟", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                            minutes = textNum.getText().toString();
                    }
                    else if  (textunit.getText().toString().equals("h")) {
                        if (textNum.getText().toString().contains(".")){
                            Float temp;
                            temp=Float.valueOf(textNum.getText().toString()).floatValue()* 60 ;
                            minutes = String.valueOf(temp.intValue());
                        }
                            else
                            minutes = String.valueOf(Integer.valueOf(textNum.getText().toString()).intValue() * 60);

                    }
                    SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
                    Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
                    String    str1    =    formatter.format(curDate);

                    SimpleDateFormat formatter2    =   new    SimpleDateFormat    ("HH:mm:ss");
                    String    str2    =    formatter2.format(curDate);

                    //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

                    ContentValues values = new ContentValues();
                    values.put("event", event);
                    values.put("minutes", minutes);
                    values.put("createdate",str1);
                    values.put("createtime", str2);
                    new Date(System.currentTimeMillis());

                    //UserDBHelper helper = new UserDBHelper(getApplicationContext());
                    helpter.insert(values);
                    //-搜索- 添加time事件 添加事件 添加条目
                    HashMap<String, Object> map =null ;
//                    myadapter2.add(String.format("%-10s\t",event)
//                            +String.format("%-10s\t",TimeChange(Integer.valueOf( minutes).intValue()))
//                            + str2);


                    map = new HashMap<String, Object>();
                    map.put("ItemText1", event);
                    map.put("ItemText2",timeChange(Integer.valueOf( minutes).intValue()));
                    map.put("ItemText3",str2);

                    drinks.add(map);
                    myadapter2.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                    updateMainList();
                    changeBrowse("");


                    textItem.setText("Category");
                    textNum.setText("0");
                    textunit.setText("time");
                    if (ItemSelectNow!=null) ItemSelectNow.setBackgroundColor(0x00ffffff);
                    cur_pos=0;
                    ItemSelectNow=null;


                }
            });
        }


        {//tab02 -计划
            //计划的下拉菜单
            myadapter_plan_spinner= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,plandemons);
            Spinner myspinner = (Spinner) tab02.findViewById(R.id.spinnerPlan);
            myspinner.setAdapter(myadapter_plan_spinner);
            spinnerPlanInit();
            myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView txv=(TextView)view;
                    updatePlanList(txv.getText().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            //计划列表
            trips = new ArrayList<HashMap<String,     Object>>();
            HashMap<String, Object> map =null ;
            mSimpleAdapter_plan = new SimpleAdapter(this,trips,//需要绑定的数据
                    R.layout.layoutplan,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2",
                            "ItemText3","ItemText4"},
                    new int[] {R.id.textplan1,R.id.textplan2,R.id.textplan3,R.id.textplan4}
            );

            ListView mlist_plan=(ListView)tab02.findViewById(R.id.listPlan);
            mlist_plan.setAdapter(mSimpleAdapter_plan);

            //更新计划列表
            updatePlanList("");

            //短按显示计划的完成情况
            mlist_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, Object> map;
                    Cursor c;

                    TextView txv1=(TextView)view.findViewById(R.id.textplan1);
                    TextView txv2=(TextView)view.findViewById(R.id.textplan2);
                    TextView txv3=(TextView)view.findViewById(R.id.textplan3);
                    TextView txv4=(TextView)view.findViewById(R.id.textplan4);
                    String str1=txv1.getText().toString();
                    String str2=txv2.getText().toString();
                    String str3=txv3.getText().toString();
                    String str4=txv4.getText().toString();
                    if (txv2.getText().toString().equals("添加计划")){
                        showCustomizeDialog();

                    }else{
                        map = new HashMap<String, Object>();
                        c = helpter2.query();
                        if (c.getCount() != 0) {
                            c.moveToFirst();
                            c.getString(1);

                            for (int i = 1; i <= c.getCount(); i++) {
                                if (c.getString(4).toString().substring(5,10).equals(str4)) {
                                    str4=c.getString(4).toString();
                                    break;
                                }
                                if (!c.isLast()) c.moveToNext();
                            }
                        }
                        c.close();
                        //Toast.makeText(MainActivity.this, str1+str2+str3+str4, Toast.LENGTH_SHORT).show();
                        if (!str1.contains("每")){


                            long plus=Long.valueOf(str1.substring(0,str1.length()-1)).longValue();
                            String strdate=datePlus(str4,plus);
                            int minutes=0;
                            if (str3.contains("小时"))
                                minutes=Integer.valueOf(str3.substring(0,str3.length()-2))*60;
                            else if (str3.contains("分钟"))
                                minutes=Integer.valueOf(str3.substring(0,str3.length()-2));
                            int totalminutes=0;

                            map = new HashMap<String, Object>();
                            c = helpter.query();
                            if (c.getCount() != 0) {
                                c.moveToFirst();
                                c.getString(1);

                                for (int i = 1; i <= c.getCount(); i++) {
                                    if (c.getString(1).toString().equals(str2)
                                            &&  isDateAfter(c.getString(3).toString(),str4,true)
                                            &&  isDateAfter(strdate,c.getString(3).toString(),false)) {

                                        totalminutes += Integer.valueOf(c.getString(2));

                                    }
                                    if (!c.isLast()) c.moveToNext();
                                }
                            }
                            c.close();
//                            Toast.makeText(MainActivity.this, "第"+String.valueOf(getWeekinDate(str4)) +"周", Toast.LENGTH_SHORT).show();
                            if (totalminutes<minutes)
                                Toast.makeText(MainActivity.this, "已完成"+detailedMinutes(totalminutes)+",还差"+detailedMinutes(minutes-totalminutes), Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(MainActivity.this, "共需"+detailedMinutes(minutes)+",已完成"+detailedMinutes(totalminutes), Toast.LENGTH_LONG).show();

                        }else{
                            SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
                            Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
                            str4    =    formatter.format(curDate);
                            if (str1.equals("每天")) {
                                int minutes=0;
                                if (str3.contains("小时"))
                                    minutes=Integer.valueOf(str3.substring(0,str3.length()-2))*60;
                                else if (str3.contains("分钟"))
                                    minutes=Integer.valueOf(str3.substring(0,str3.length()-2));
                                int totalminutes=0;

                                map = new HashMap<String, Object>();
                                c = helpter.query();
                                if (c.getCount() != 0) {
                                    c.moveToFirst();
                                    c.getString(1);

                                    for (int i = 1; i <= c.getCount(); i++) {
                                        if (c.getString(1).toString().equals(str2)
                                                &&  c.getString(3).equals(str4)){
                                            totalminutes += Integer.valueOf(c.getString(2));
                                        }
                                        if (!c.isLast()) c.moveToNext();
                                    }
                                }
                                c.close();
//                            Toast.makeText(MainActivity.this, "第"+String.valueOf(getWeekinDate(str4)) +"周", Toast.LENGTH_SHORT).show();
                                if (totalminutes<minutes)
                                    Toast.makeText(MainActivity.this, "今天已"+str2+detailedMinutes(totalminutes)+",还差"+detailedMinutes(minutes-totalminutes), Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(MainActivity.this, "今天共需"+str2+detailedMinutes(minutes)+",已完成"+detailedMinutes(totalminutes), Toast.LENGTH_LONG).show();

                            }else if(str1.equals("每周")){
                                int minutes=0;
                                if (str3.contains("小时"))
                                    minutes=Integer.valueOf(str3.substring(0,str3.length()-2))*60;
                                else if (str3.contains("分钟"))
                                    minutes=Integer.valueOf(str3.substring(0,str3.length()-2));
                                int totalminutes=0;

                                map = new HashMap<String, Object>();
                                c = helpter.query();
                                if (c.getCount() != 0) {
                                    c.moveToFirst();
                                    c.getString(1);

                                    for (int i = 1; i <= c.getCount(); i++) {
                                        if (c.getString(1).toString().equals(str2)
                                                &&  getWeekinDate(c.getString(3)) == getWeekinDate(str4)
                                                && c.getString(3).substring(0,4).equals(str4.substring(0,4)) ){
                                            totalminutes += Integer.valueOf(c.getString(2));
                                        }
                                        if (!c.isLast()) c.moveToNext();
                                    }
                                }
                                c.close();
//                            Toast.makeText(MainActivity.this, "第"+String.valueOf(getWeekinDate(str4)) +"周", Toast.LENGTH_SHORT).show();
                                if (totalminutes<minutes)
                                    Toast.makeText(MainActivity.this, "本周已"+str2+detailedMinutes(totalminutes)+",还差"+detailedMinutes(minutes-totalminutes), Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(MainActivity.this, "本周共需"+str2+detailedMinutes(minutes)+",已完成"+detailedMinutes(totalminutes), Toast.LENGTH_LONG).show();

                            }else if(str1.equals("每月")){
                                int minutes=0;
                                if (str3.contains("小时"))
                                    minutes=Integer.valueOf(str3.substring(0,str3.length()-2))*60;
                                else if (str3.contains("分钟"))
                                    minutes=Integer.valueOf(str3.substring(0,str3.length()-2));
                                int totalminutes=0;

                                map = new HashMap<String, Object>();
                                c = helpter.query();
                                if (c.getCount() != 0) {
                                    c.moveToFirst();
                                    c.getString(1);

                                    for (int i = 1; i <= c.getCount(); i++) {
                                        if (c.getString(1).toString().equals(str2)
                                                && c.getString(3).substring(0,7).equals(str4.substring(0,7)) ){
                                            totalminutes += Integer.valueOf(c.getString(2));
                                        }
                                        if (!c.isLast()) c.moveToNext();
                                    }
                                }
                                c.close();
//                            Toast.makeText(MainActivity.this, "第"+String.valueOf(getWeekinDate(str4)) +"周", Toast.LENGTH_SHORT).show();
                                if (totalminutes<minutes)
                                    Toast.makeText(MainActivity.this, "本月已"+str2+detailedMinutes(totalminutes)+",还差"+detailedMinutes(minutes-totalminutes), Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(MainActivity.this, "本月共需"+str2+detailedMinutes(minutes)+",已完成"+detailedMinutes(totalminutes), Toast.LENGTH_LONG).show();




                            }
                        }

                    }
                }
            });
            //长按删除计划
            mlist_plan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txv1=(TextView)view.findViewById(R.id.textplan1);
                    TextView txv2=(TextView)view.findViewById(R.id.textplan2);
                    TextView txv3=(TextView)view.findViewById(R.id.textplan3);
                    TextView txv4=(TextView)view.findViewById(R.id.textplan4);
                    String str1=txv1.getText().toString();
                    String str2=txv2.getText().toString();
                    String str3=txv3.getText().toString();
                    String str4=txv4.getText().toString();

                    if (!txv2.getText().toString().equals("添加计划"))showDelPlanDialog(str1,str2,str3,str4);

                    return true;
                }
            });
        }

        {//tab03 浏览
            ListView lv = (ListView) tab03.findViewById(R.id.listBrowse);/*定义一个动态数组*/
            listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/

            HashMap<String, Object> map = null;

            myadapter_spinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, demons);
            Spinner myspinner = (Spinner) tab03.findViewById(R.id.spinnerBrowse);
            myspinner.setAdapter(myadapter_spinner);

            spinnerInit();//初始化下拉菜单

            //下拉菜单选中日期或"显示所有"
            myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    TextView txv = (TextView) view;

                    if (txv.getText().toString().length() != 11) {
                        if (demons.get(demons.size() - 1).length() == 11) {
                            demons.remove(demons.size() - 1);
                            myadapter_spinner.notifyDataSetChanged();
                        }
                    }
                    if ((txv.getText().toString().contains("年") && txv.getText().toString().contains("月"))
                            || (txv.getText().toString().contains("逐项显示"))
                            || (txv.getText().toString().contains("没有可以显示的条目"))) {
                        if (!(demons.get(demons.size() - 1).contains("年") && demons.get(demons.size() - 1).contains("月"))
                                && !demons.get(demons.size() - 1).contains("逐项显示")
                                && !demons.get(demons.size() - 1).contains("没有可以显示的条目"))
                            demons.remove(demons.size() - 1);
                        myadapter_spinner.notifyDataSetChanged();

                    }
                    if (txv.getText().toString().length() != 11) {
                        if (demons.get(demons.size() - 1).length() == 11) {
                            demons.remove(demons.size() - 1);
                            myadapter_spinner.notifyDataSetChanged();
                        }
                    }

                    changeBrowse(txv.getText().toString());


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //详细列表
            mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                    R.layout.layoutitem,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[]{"ItemText1", "ItemText2", "ItemText3", "ItemText4"
                            , "ItemImage"},
                    new int[]{R.id.textItem1, R.id.textItem2, R.id.textItem3, R.id.textItem4, R.id.imageItem}
            );
            lv.setAdapter(mSimpleAdapter);

            //短按显示所有相同的事件
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txt = (TextView) view.findViewById(R.id.textItem1);
                    TextView txt2 = (TextView) view.findViewById(R.id.textItem2);
                    Spinner myspinner = (Spinner) tab03.findViewById(R.id.spinnerBrowse);
                    TextView mytxt = (TextView) myspinner.getSelectedView();
                    if (txt.getText().toString().contains("月") &&
                            txt.getText().toString().contains("日")) {
                        String str1 = mytxt.getText().toString().substring(0, 4)
                                + "-"
                                + txt.getText().toString().substring(0, 2)
                                + "-"
                                + txt.getText().toString().substring(3, 5);
                        //Toast.makeText(MainActivity.this, str1, Toast.LENGTH_SHORT).show();
                        changeBrowse(str1);
                        if (!demons.contains(mytxt.getText().toString().substring(0, 5) + txt.getText().toString()))
                            demons.add(mytxt.getText().toString().substring(0, 5) + txt.getText().toString());
                        myadapter_spinner.notifyDataSetChanged();

                        myspinner.setSelection(demons.size() - 1);


                    } else if (txt2.getText().toString().equals("下一天")) {
                        String str3 = mytxt.getText().toString();


                        demons.remove(demons.size() - 1);
                        String str4 = nextDay(str3);
                        demons.add(str4);
                        myadapter_spinner.notifyDataSetChanged();
                        myspinner.setSelection(demons.size() - 1);
                        changeBrowse(str4);

                    } else {
                        changeBrowse(txt.getText().toString());
                        if (!demons.contains(txt.getText().toString()))
                            demons.add(txt.getText().toString());

                        myadapter_spinner.notifyDataSetChanged();
                        myspinner.setSelection(demons.size() - 1);


                    }
                }
            });

            //长按删除条目
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    LinearLayout ll = (LinearLayout) view;
                    TextView txv = (TextView) ll.findViewById(R.id.textItem1);

                    String item1 = txv.getText().toString();

                    txv = (TextView) ll.findViewById(R.id.textItem2);
                    String item2 = txv.getText().toString();

                    txv = (TextView) ll.findViewById(R.id.textItem3);
                    String item3 = txv.getText().toString();

                    txv = (TextView) ll.findViewById(R.id.textItem4);
                    String item4 = txv.getText().toString();

                    //Toast.makeText(MainActivity.this, "您长按的是"+item, Toast.LENGTH_SHORT).show();


                    //if (parent.getId()==R.id.listView1 && item != "添加         ⊕")showNormalDialog(item);
                    if (!item2.equals("下一天") && !item2.equals(""))
                        showDelDialog(item1, item2, item3, item4);

                    return false;
                }
            });
            changeBrowse("");
        }

        {//tab04 统计图
            ItemsToShow.add("All");
            //下拉框
            myadapter_data_spinner= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,graphs);
            final Spinner myspinner = (Spinner) tab04.findViewById(R.id.spinnerData);
            myspinner.setAdapter(myadapter_data_spinner);
            spinnerDataInit();
            myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                //统计图界面spinner的点击
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView txv=(TextView)view;
                    String str=((TextView) view).getText().toString();
                    if (accordingToTime){
                        if (set1Xscale.equals("month")) updateStatistics(ItemsToShow,"month",dateFormatSimple(str)+"-01",setYmax,0);
                        if (set1Xscale.equals("day")) updateStatistics(ItemsToShow,"day",dateFormatSimple(str),setYmax,0);
                        if (set1Xscale.equals("week")) updateStatistics(ItemsToShow,"week",dateFormatSimple(str),setYmax,0);

                        green.setTextForTheMost();
                    }else{//accordingToItem
                        updateStatisticsAccordingtoItem(str,set2Xscale,set2timeset,set2Xmax,setYmax,0);
                        green.setTextForTheMost();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final ImageButton buttonfilter=(ImageButton)tab04.findViewById(R.id.buttonfilter);

            final ImageButton buttonshare=(ImageButton)tab04.findViewById(R.id.buttonshare);

            //筛选按钮
            buttonfilter.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonfilter.setImageResource(R.drawable.filter_2);
                        buttonfilter.setBackgroundColor(0xffb3b3b3);

                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        buttonfilter.setImageResource(R.drawable.filter);
                        buttonfilter.setBackgroundColor(0);
                    }
                    return false;

                }
            });
            buttonfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (accordingToTime) {
                        TextView tx=(TextView) myspinner.getSelectedView();
                        showConfig1Dialog(dateFormatSimple(tx.getText().toString()));
                    }else {
                        //extView tx=(TextView) myspinner.getSelectedView();
                        showConfig2Dialog();
                    }
                }
            });
            //分享按钮
            buttonshare.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonshare.setImageResource(R.drawable.share_2);
                        buttonshare.setBackgroundColor(0xffb3b3b3);

                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        buttonshare.setImageResource(R.drawable.share);
                        buttonshare.setBackgroundColor(0);
                    }
                    return false;

                }
            });

            //切换按钮 按时间/按项目
            final ImageView imagechange=(ImageView) tab04.findViewById(R.id.imagechange);
            imagechange.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean orig = accordingToTime;
                    int x=(int) event.getX();
                    int half=(int)imagechange.getWidth()/2;
                    if (x>half) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (orig) {
                                imagechange.setImageResource(R.drawable.toitem);
                                accordingToTime = false;
                                spinnerDataInit();
                                updateStatisticsAccordingtoItem();
                                green.setTextForTheMost();
                            }else
                                green.notifyDataSetChanged();
                        }
                    }else{
                        if(event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (!orig) {
                                imagechange.setImageResource(R.drawable.totime);
                                accordingToTime = true;
                                spinnerDataInit();
                                updateStatistics();
                                green.setTextForTheMost();
                            }else
                                green.notifyDataSetChanged();
                        }

                    }


                    return false;
                }
            });

            //NEXT按钮
            final ImageButton buttonnext=(ImageButton)tab04.findViewById(R.id.buttonnext);
            buttonnext.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonnext.setImageResource(R.drawable.next_2);
                        buttonnext.setBackgroundColor(0xffdcdcdc);

                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        buttonnext.setImageResource(R.drawable.next);
                        buttonnext.setBackgroundColor(0);
                    }

                    return false;
                }
            });

            buttonnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myspinner.getCount()==0)return;
                    if (myspinner.getSelectedItemPosition()==myspinner.getCount()-1) {
                        myspinner.setSelection(0);
                    }else{
                        myspinner.setSelection(myspinner.getSelectedItemPosition()+1);
                    }

                }
            });



            updateStatistics(null,"month","now",setYmax,0);

           //筛选设置1
            listItem_config1_list1 = new ArrayList<HashMap<String,     Object>>();
            //HashMap<String, Object> map =null ;
            mAdapter_config1_list1 = new SimpleAdapter(this,listItem_config1_list1,//需要绑定的数据
                    R.layout.layoutcheckbox,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2"
                            },
                    new int[] {R.id.textcheckbox,R.id.checkBox}
            );
            listItem_config1_list2 = new ArrayList<HashMap<String,     Object>>();
            //HashMap<String, Object> map =null ;
            mAdapter_config1_list2 = new SimpleAdapter(this,listItem_config1_list2,//需要绑定的数据
                    R.layout.layoutradio,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2"
                    },
                    new int[] {R.id.textradio,R.id.radioButton}
            );
            listItem_config1_list3 = new ArrayList<HashMap<String,     Object>>();
            //HashMap<String, Object> map =null ;
            mAdapter_config1_list3 = new SimpleAdapter(this,listItem_config1_list3,//需要绑定的数据
                    R.layout.layoutradio,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2"
                    },
                    new int[] {R.id.textradio,R.id.radioButton}
            );

            //筛选设置2
            listItem_config2_list1 = new ArrayList<HashMap<String,     Object>>();
            //HashMap<String, Object> map =null ;
            mAdapter_config2_list1 = new SimpleAdapter(this,listItem_config2_list1,//需要绑定的数据
                    R.layout.layoutradio,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2"
                    },
                    new int[] {R.id.textradio,R.id.radioButton}
            );
            listItem_config2_list2 = new ArrayList<HashMap<String,     Object>>();
            //HashMap<String, Object> map =null ;
            mAdapter_config2_list2 = new SimpleAdapter(this,listItem_config2_list2,//需要绑定的数据
                    R.layout.layoutradio,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2"
                    },
                    new int[] {R.id.textradio,R.id.radioButton}
            );
            listItem_config2_list3 = new ArrayList<HashMap<String,     Object>>();
            //HashMap<String, Object> map =null ;
            mAdapter_config2_list3 = new SimpleAdapter(this,listItem_config2_list3,//需要绑定的数据
                    R.layout.layoutradio,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[] {"ItemText1","ItemText2"
                    },
                    new int[] {R.id.textradio,R.id.radioButton}
            );


        }


        {//tab05设置
            //清除所有数据按钮
            Button buttonclear=(Button)tab05.findViewById(R.id.buttonclear);
            buttonclear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showClearAllDialog();
                }
            });

            Button buttonhelp=(Button)tab05.findViewById(R.id.buttonhelp);
            buttonhelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showHelpDialog();
                }
            });

            Button buttonauto=(Button)tab05.findViewById(R.id.buttonauto);
            buttonauto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                }
            });
            Button buttonremark=(Button)tab05.findViewById(R.id.buttonremark);
            buttonremark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "功能尚未开通", Toast.LENGTH_SHORT).show();
                }
            });
            Button buttondonate=(Button)tab05.findViewById(R.id.buttondonate);
            buttondonate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "功能尚未开通，直接发红包吧~", Toast.LENGTH_SHORT).show();
                }
            });
/*            final EditText ed1=(EditText)tab05.findViewById(R.id.editEvent);
            final EditText ed2=(EditText)tab05.findViewById(R.id.editMinutes);
            final EditText ed3=(EditText)tab05.findViewById(R.id.editDate);
            final EditText ed4=(EditText)tab05.findViewById(R.id.editTime);*/


            Button buttonmanual =(Button)tab05.findViewById(R.id.buttonmanual);
            buttonmanual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showManualDialog();
                }
            });
        }





    }

    protected void onRestart(){
        updateMainList();
        super.onRestart();
    }

/*
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView txv= (TextView) view;
        String item = txv.getText().toString();
        //Toast.makeText(this, "您长按的是"+item, Toast.LENGTH_SHORT).show();
        if (ItemSelectNow!=null) ItemSelectNow.setBackgroundColor(0x00ffffff);
        cur_pos=0;
        ItemSelectNow=null;


        if (parent.getId()==R.id.listView1 && item != "添加         ⊕")showNormalDialog(item);
        if (parent.getId()==R.id.listview2 )showDelDialog(item);

        return true;
    }

    @Override
    public void onClick(View v) {

        if (textNum.getText().toString().equals("0")) textNum.setText("");
        if (v.getId()==R.id.buttonreturn){
            String s;
            s = textNum.getText().toString();
            if (s.length()>1) s = s.substring(0,s.length()-1);
                else s="";
            textNum.setText(s);
        }
        if (v.getId()==R.id.button0 && (!textNum.getText().toString().equals("0")))textNum.setText(textNum.getText().toString()+"0");
        if (v.getId()==R.id.button1)textNum.setText(textNum.getText().toString()+"1");
        if (v.getId()==R.id.button2)textNum.setText(textNum.getText().toString()+"2");
        if (v.getId()==R.id.button3)textNum.setText(textNum.getText().toString()+"3");
        if (v.getId()==R.id.button4)textNum.setText(textNum.getText().toString()+"4");
        if (v.getId()==R.id.button5)textNum.setText(textNum.getText().toString()+"5");
        if (v.getId()==R.id.button6)textNum.setText(textNum.getText().toString()+"6");
        if (v.getId()==R.id.button7)textNum.setText(textNum.getText().toString()+"7");
        if (v.getId()==R.id.button8)textNum.setText(textNum.getText().toString()+"8");
        if (v.getId()==R.id.button9)textNum.setText(textNum.getText().toString()+"9");
        if (v.getId()==R.id.buttondot && textNum.getText().toString() != "" && !textNum.getText().toString().contains("."))textNum.setText(textNum.getText().toString()+".");
        if (v.getId()==R.id.buttondot && textNum.getText().toString() == "" )textNum.setText("0.");

        if (v.getId()==R.id.buttonh)textunit.setText("h");
        if (v.getId()==R.id.buttonmin)textunit.setText("min");
        if (v.getId()==R.id.buttonremark);

        if (v.getId()==R.id.buttonadd){

            //EditText ed1=(EditText)findViewById(R.id.editText1);
            //EditText ed2=(EditText)findViewById(R.id.editText2);
            //Button btn=(Button)findViewById(R.id.button1);

            String event = textItem.getText().toString();
            String minutes=null;

            if (event.equals("Category")){
                Toast.makeText(this, "请选择种类", Toast.LENGTH_SHORT).show();
                textItem.setText("Category");
                textNum.setText("0");
                textunit.setText("time");
                return;
            }
            if (textunit.getText().toString().equals("time")){
                Toast.makeText(this, "请选择时间单位", Toast.LENGTH_SHORT).show();
                textItem.setText("Category");
                textNum.setText("0");
                textunit.setText("time");
                return;
            }
            if (textNum.getText().toString().equals("0")||textNum.getText().toString().equals("")){
                Toast.makeText(this, "没有"+ event +"还好意思添加啊", Toast.LENGTH_SHORT).show();
                textItem.setText("Category");
                textNum.setText("0");
                textunit.setText("time");
                return;
            }

            if (textunit.getText().toString().equals("min")) minutes = textNum.getText().toString();
                else if  (textunit.getText().toString().equals("h"))
                    minutes = String.valueOf(Integer.valueOf(textNum.getText().toString()).intValue() * 60);

            SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
            Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
            String    str1    =    formatter.format(curDate);

            SimpleDateFormat formatter2    =   new    SimpleDateFormat    ("HH:mm:ss");
            String    str2    =    formatter2.format(curDate);

            //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            ContentValues values = new ContentValues();
            values.put("event", event);
            values.put("minutes", minutes);
            values.put("createdate",str1);
            values.put("createtime", str2);
            new Date(System.currentTimeMillis());

            UserDBHelper helper = new UserDBHelper(getApplicationContext());
            helper.insert(values);

            myadapter2.add(String.format("%-10s\t",event)
                    +String.format("%-10s\t",TimeChange(Integer.valueOf( minutes).intValue()))
                    + str2);
            myadapter2.notifyDataSetChanged();

            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();

            textItem.setText("Category");
            textNum.setText("0");
            textunit.setText("time");



        }



        if (textNum.getText().toString().equals("")) textNum.setText("0");
    }
    */

    private void showInsertDialog() {
    /*@setView 装入一个EditView
     */
            final EditText editText = new EditText(MainActivity.this);
            AlertDialog.Builder inputDialog =
                    new AlertDialog.Builder(MainActivity.this);
            inputDialog.setTitle("添加...").setView(editText);
            inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            inputDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this,editText.getText().toString(),Toast.LENGTH_SHORT).show();
                            ListView lv=(ListView)tab01.findViewById(R.id.listView1);
                            myadapter.remove("添加         ⊕");
                            myadapter.add(editText.getText().toString());
                            SharedPreferences sps=getSharedPreferences("timeapp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sps.edit();
                            editor.putBoolean("IsFirstTimeRunApp",false);

                            editor.remove("Status_size");
                            editor.putInt("Status_size",plans.size());

                            for(int i=0;i<plans.size();i++) {
                                editor.remove("Status_" + i);
                                editor.putString("Status_" + i, plans.get(i));
                            }

                            editor.commit();
                            myadapter.add("添加         ⊕");
                            myadapter.notifyDataSetChanged();
                        }
                    }).show();
     }//添加类型

    private void showNormalDialog(final String item){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(MainActivity.this);
            // normalDialog.setIcon(R.drawable.icon_dialog);
            normalDialog.setTitle("更改...");
            normalDialog.setMessage("确定要删除"+ item +"吗?");
            normalDialog.setPositiveButton("删除",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            //删除一个项目
                            myadapter.remove("添加         ⊕");
                            plans.remove("添加         ⊕");
                            myadapter.remove(item);
                            plans.remove(item);
                            //Toast.makeText(this, "plans的大小为"+plans.size(), Toast.LENGTH_SHORT).show();
                            SharedPreferences sps=getSharedPreferences("timeapp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sps.edit();
                            editor.putBoolean("IsFirstTimeRunApp",false);

                            editor.remove("Status_size");
                            editor.putInt("Status_size",plans.size());

                            for(int i=0;i<plans.size();i++) {
                                editor.remove("Status_" + i);
                                editor.putString("Status_" + i, plans.get(i));
                            }

                            editor.remove("Status_" + plans.size());

                            editor.commit();
                            myadapter.add("添加         ⊕");
                            myadapter.notifyDataSetChanged();
                            //...To-do
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                            //...To-do
                        }
                    });
            // 显示
            normalDialog.show();
        }//删除类型

    private void showDelDialog(final String item1,final String item2,final String item3){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */ final HashMap<String, Object> map = new HashMap<String, Object>();;

            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(MainActivity.this);
            // normalDialog.setIcon(R.drawable.icon_dialog);
            normalDialog.setTitle("删除...");
            normalDialog.setMessage("确定要删除该条目吗?");
            normalDialog.setPositiveButton("删除",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            //删除一个项目

                            map.clear();

                            map.put("ItemText1", item1);
                            map.put("ItemText2",item2);
                            map.put("ItemText3",item3);
                            //myadapter2.remove(item);
                            drinks.remove(map);
                            //Toast.makeText(this, "plans的大小为"+plans.size(), Toast.LENGTH_SHORT).show();

                            myadapter2.notifyDataSetChanged();
                            final UserDBHelper helpter = new UserDBHelper(MainActivity.this);
                            Cursor c = helpter.query();
                            if (c.getCount()!=0) {
                                c.moveToFirst();
                                //c.getString(1);

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                String str1 = formatter.format(curDate);

                                for (int i = 1; i <= c.getCount(); i++) {
                                    if (c.getString(3).equals(str1)) {
                                        if(item1.equals(c.getString(1).toString())&& item2.equals(timeChange(Integer.valueOf(c.getString(2).toString()).intValue())) &&
                                                item3.equals( c.getString(4).toString())){

                                                    helpter.del(c.getInt(0));

                                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                    updateMainList();
                                                    spinnerInit();
                                                    changeBrowse("");
                                                    break;
                                        }

                                        //plans.add(String.format("%-5s",c.getString(1).toString())+
                                        //if (c.getPosition()==4)
                                         //   Toast.makeText(this,"" , Toast.LENGTH_SHORT).show();

                                    }
                                    if (!c.isLast()) c.moveToNext();

                                }
                            }
                            c.close();
                            //...To-do
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                            //...To-do
                        }
                    });
            // 显示
            normalDialog.show();
        }//主页的删除

    private void showDelDialog(final String item1,final String item2,final String item3,final String item4){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */ final HashMap<String, Object> map = new HashMap<String, Object>();

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        // normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("删除...");
        normalDialog.setMessage("确定要删除该条目吗?");
        normalDialog.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //删除一个项目

                        map.clear();

                        map.put("ItemText1", item1);
                        map.put("ItemText2",item2);
                        map.put("ItemText3",item3);
                        map.put("ItemText4",item4);
                        //map.put("ItemImage",getEmojiRes(getEmojiIdByDate()));
                        //myadapter2.remove(item);
                        listItem.remove(map);
                        //Toast.makeText(this, "plans的大小为"+plans.size(), Toast.LENGTH_SHORT).show();

                        mSimpleAdapter.notifyDataSetChanged();
                        //final UserDBHelper helpter = new UserDBHelper(this);
                        Cursor c = helpter.query();
                        if (c.getCount()!=0) {
                            c.moveToFirst();
                            //c.getString(1);

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            String str1 = formatter.format(curDate);

                            for (int i = 1; i <= c.getCount(); i++) {
                                if (c.getString(3).toString().substring(5).equals(item3)) {
                                    if(item1.equals(c.getString(1).toString())&& item2.equals(timeChange(Integer.valueOf(c.getString(2).toString()).intValue())) &&
                                            item4.equals( c.getString(4).toString().substring(0,5))){

                                        helpter.del(c.getInt(0));
                                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                        updateMainList();
                                        changeBrowse("");
                                        break;
                                    }

                                    //plans.add(String.format("%-5s",c.getString(1).toString())+
                                    //if (c.getPosition()==4)
                                    //   Toast.makeText(this,"" , Toast.LENGTH_SHORT).show();

                                }
                                if (!c.isLast()) c.moveToNext();

                            }
                        }
                        c.close();
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }//BROWSE的删除

    private void showDelPlanDialog(final String item1,final String item2,final String item3,final String item4){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */ final HashMap<String, Object> map = new HashMap<String, Object>();

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        // normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("删除...");
        normalDialog.setMessage("确定要删除该条目吗?");
        normalDialog.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //删除一个项目

                        map.clear();

                        map.put("ItemText1", item1);
                        map.put("ItemText2",item2);
                        map.put("ItemText3",item3);
                        map.put("ItemText4",item4);
                        //myadapter2.remove(item);
                        listItem.remove(map);
                        //Toast.makeText(this, "plans的大小为"+plans.size(), Toast.LENGTH_SHORT).show();

                        mSimpleAdapter_plan.notifyDataSetChanged();
                        //final UserDBHelper helpter = new UserDBHelper(this);
                        Cursor c = helpter2.query();
                        if (c.getCount()!=0) {
                            c.moveToFirst();
                            //c.getString(1);
/*                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            String str1 = formatter.format(curDate);*/
                            String str1=null;
                            String str2=null;
                            String str3=null;
                            String str4=null;
                            for (int i = 1; i <= c.getCount(); i++) {
                                if (c.getString(1).equals("1000"))
                                    str1="每天";
                                else if (c.getString(1).equals("7000"))
                                    str1="每周";
                                else if (c.getString(1).equals("30000"))
                                    str1="每月";
                                else
                                    str1=c.getString(1)+"天";
                                str2=c.getString(2);

                                str4=c.getString(4).substring(5,10);


                                //if (str3.equals(item3)) {
                                    if(item1.equals(str1)&& item2.equals(str2) &&
                                            item4.equals(str4)){

                                        helpter2.del(c.getInt(0));
                                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                        updatePlanList("");
                                        break;
                                    }
                                //}
                                if (!c.isLast()) c.moveToNext();

                            }
                        }
                        c.close();
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }//计划的删除

    private void showClearAllDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        // normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("警告!");
        normalDialog.setMessage("确定要清除所有数据吗?");
        normalDialog.setPositiveButton("清除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        final UserDBHelper helpter = new UserDBHelper(MainActivity.this);
                        Cursor c = helpter.query();
                        if (c.getCount()!=0) {
                            c.moveToFirst();
                            //c.getString(1);
                            for (int i = 1; i <= c.getCount(); i++) {
                                helpter.del(c.getInt(0));

                                if (!c.isLast()) c.moveToNext();
                            }

                        }

                        final UserDBHelper2 helpter2 = new UserDBHelper2(MainActivity.this);
                        c = helpter2.query();
                        if (c.getCount()!=0) {
                            c.moveToFirst();
                            for (int i = 1; i <= c.getCount(); i++) {
                                helpter2.del(c.getInt(0));
                                if (!c.isLast()) c.moveToNext();
                            }

                        }
                        c.close();


                        listItem.clear();//浏览所有
                        drinks.clear();//主页的浏览
                        //.clear();//schedule

                        myadapter2.notifyDataSetChanged();//主页的浏览

                        mSimpleAdapter.notifyDataSetChanged();//浏览所有
                        //mSimpleAdapter_plan.notifyDataSetChanged();//计划
                        updatePlanList("");
                        updateMainList();
                        spinnerInit();
                        changeBrowse("");
                        spinnerPlanInit();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }//删除类型

    private void showCustomizeDialog() {//添加计划
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.layoutplandialog,null);
        customizeDialog.setTitle("添加计划...");
        customizeDialog.setView(dialogView);

        {
            myadapter_addplan_spinner= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,plans);
            final Spinner myspinner = (Spinner) dialogView.findViewById(R.id.spinnerplan2);
            myspinner.setAdapter(myadapter_addplan_spinner);
            myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView txv=(TextView)view;
                    if (txv.getText().toString().contains("添加")) {
                        myspinner.setSelection(0);
                        showInsertDialog();
                        myadapter_addplan_spinner.notifyDataSetChanged();

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

        }
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        Spinner sp1=(Spinner)dialogView.findViewById(R.id.spinnerplan1);
                        Spinner sp2=(Spinner)dialogView.findViewById(R.id.spinnerplan2);
                        Spinner sp3=(Spinner)dialogView.findViewById(R.id.spinnerplan3);
                        EditText edit_text =
                                (EditText) dialogView.findViewById(R.id.editplan);
                        TextView tx1=(TextView) sp1.getSelectedView();
                        TextView tx2=(TextView) sp2.getSelectedView();
                        TextView tx3=(TextView) sp3.getSelectedView();
                        String str1=tx1.getText().toString();//1天 每天
                        String str2=tx2.getText().toString();//学习
                        String str3=edit_text.getText().toString();//2
                        String str4=tx3.getText().toString();//小时

                        if (str3.equals("")){
                            Toast.makeText(MainActivity.this, "请输入时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Integer.valueOf(str3)==0){
                            Toast.makeText(MainActivity.this, "不想"+str2+"你添加个毛线", Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        Toast.makeText(MainActivity.this,
//                                str1+str2+str3+str4,
//                                Toast.LENGTH_SHORT).show();

                        {//添加到数据库
                            SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
                            Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
                            String    str_date    =    formatter.format(curDate);

                            //SimpleDateFormat formatter2    =   new    SimpleDateFormat    ("HH:mm:ss");
                            //String    str_time    =    formatter2.format(curDate);

                            String minutes =null;
                            if (str4.equals("小时"))minutes=String.valueOf(Integer.valueOf(str3)*60);
                                else minutes=str3;


                            ContentValues values = new ContentValues();

                            if (str1.equals("每天"))
                                values.put("days", "1000");
                            else if (str1.equals("每周"))
                                values.put("days", "7000");
                            else if (str1.equals("每月"))
                                values.put("days", "30000");
                            else
                                values.put("days", str1.substring(0,str1.length()-1));

                            values.put("event", str2);
                            values.put("minutes", minutes);
                            values.put("createdate",str_date);


                            helpter2.insert(values);

//                            HashMap<String, Object> map =null ;
//
//                            map = new HashMap<String, Object>();
//                            map.put("ItemText1", str1);
//                            map.put("ItemText2",str2);
//                            map.put("ItemText3",str3+str4);
//                            map.put("ItemText4",str_date.substring(5,10));
//
//                            trips.add(map);
//                            mSimpleAdapter_plan.notifyDataSetChanged();

                            Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            //添加计划
                            spinnerPlanInit();
                            updatePlanList("");


                        }
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                        //...To-do
                    }
                });
        customizeDialog.show();
    }//添加计划的对话框

    private void showCountDialog() {//j计时
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */


        final AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.layoutcount,null);


        //customizeDialog.setTitle("计时...");
        customizeDialog.setView(dialogView);
        customizeDialog.setCancelable(false);
        final AlertDialog dialog = customizeDialog.show();

        final TextView textCount=(TextView)dialogView.findViewById(R.id.textViewCount);
        final Spinner myspinner = (Spinner) dialogView.findViewById(R.id.spinnercount1);
        //按钮
        final Button buttoncount=(Button)dialogView.findViewById(R.id.buttonCount);
        buttoncount.setText("开始");
        buttoncount.setOnClickListener(new View.OnClickListener() {
            String startevent="";
            String minutes="";
            @Override
            public void onClick(View v) {
                if (buttoncount.getText().toString().substring(0,2).equals("开始")){
                    textCount.setText("正在计时...");
                    buttoncount.setText(buttoncount.getText().toString().substring(2,buttoncount.getText().length())+"完毕");
                    starttime=getTimeForNow();
                    myspinner.setEnabled(false);
                }else if (buttoncount.getText().toString().contains("完毕")){
                    startevent=buttoncount.getText().toString().substring(0,buttoncount.getText().length()-2);
                    minutes=String.valueOf((getTimeForNow()-starttime)/60/1000);
                    textCount.setText(startevent+minutes+"分钟");
                    buttoncount.setText("添加到列表");
                }else if (buttoncount.getText().toString().contains("关闭")){

                }else{
                    if (minutes.equals("0")) {
                        Toast.makeText(MainActivity.this, "没有"+startevent+"还好意思添加啊", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
                        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
                        String    str1    =    formatter.format(curDate);

                        SimpleDateFormat formatter2    =   new    SimpleDateFormat    ("HH:mm:ss");
                        String    str2    =    formatter2.format(curDate);

                        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

                        ContentValues values = new ContentValues();
                        values.put("event", startevent);
                        values.put("minutes", minutes);
                        values.put("createdate",str1);
                        values.put("createtime", str2);
                        new Date(System.currentTimeMillis());

                        //UserDBHelper helper = new UserDBHelper(getApplicationContext());
                        helpter.insert(values);

                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();


                        updateMainList();
                        changeBrowse("");
                        dialog.dismiss();
                    }
                }
            }
        });


        {//下拉菜单
            myadapter_count_spinner= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,plans);

            myspinner.setAdapter(myadapter_count_spinner);
            myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView txv=(TextView)view;
                    if (txv.getText().toString().contains("添加")) {
                        myspinner.setSelection(0);
                        showInsertDialog();
                        myadapter_count_spinner.notifyDataSetChanged();

                    }else if (buttoncount.getText().toString().contains("开始")){
                        buttoncount.setText("开始"+txv.getText().toString());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

        }

/*        {
            myadapter_addplan_spinner= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,plans);
            final Spinner myspinner = (Spinner) dialogView.findViewById(R.id.spinnerplan2);
            myspinner.setAdapter(myadapter_addplan_spinner);
            myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView txv=(TextView)view;
                    if (txv.getText().toString().contains("添加")) {
                        myspinner.setSelection(0);
                        showInsertDialog();
                        myadapter_addplan_spinner.notifyDataSetChanged();

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

        }*/
/*        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                        //...To-do
                    }
                });*/


    }//计时的对话框

    private  void showEmojiDialog(){
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.layoutemoji,null);
        customizeDialog.setTitle("今天的心情");
        customizeDialog.setView(dialogView);
        customizeDialog.setCancelable(false);

        final ImageButton image1=(ImageButton)dialogView.findViewById(R.id.imageButton1);
        final ImageButton image2=(ImageButton)dialogView.findViewById(R.id.imageButton2);
        final ImageButton image3=(ImageButton)dialogView.findViewById(R.id.imageButton3);
        final ImageButton image4=(ImageButton)dialogView.findViewById(R.id.imageButton4);
        final ImageButton image5=(ImageButton)dialogView.findViewById(R.id.imageButton5);
        final ImageButton image6=(ImageButton)dialogView.findViewById(R.id.imageButton6);
        final ImageButton image7=(ImageButton)dialogView.findViewById(R.id.imageButton7);
        final ImageButton image8=(ImageButton)dialogView.findViewById(R.id.imageButton8);
        final ImageButton image9=(ImageButton)dialogView.findViewById(R.id.imageButton9);
        final ImageButton image10=(ImageButton)dialogView.findViewById(R.id.imageButton10);
        final ImageButton image11=(ImageButton)dialogView.findViewById(R.id.imageButton11);
        final ImageButton image12=(ImageButton)dialogView.findViewById(R.id.imageButton12);
        final ImageButton image13=(ImageButton)dialogView.findViewById(R.id.imageButton13);
        final ImageButton image14=(ImageButton)dialogView.findViewById(R.id.imageButton14);
        final ImageButton image15=(ImageButton)dialogView.findViewById(R.id.imageButton15);
        final ImageButton image16=(ImageButton)dialogView.findViewById(R.id.imageButton16);

        imageSelectNum=getEmojiIdByDate(getDateForToday());
        if (imageSelectNum==1)imageSelect=image1;
        if (imageSelectNum==2)imageSelect=image2;
        if (imageSelectNum==3)imageSelect=image3;
        if (imageSelectNum==4)imageSelect=image4;
        if (imageSelectNum==5)imageSelect=image5;
        if (imageSelectNum==6)imageSelect=image6;
        if (imageSelectNum==7)imageSelect=image7;
        if (imageSelectNum==8)imageSelect=image8;
        if (imageSelectNum==9)imageSelect=image9;
        if (imageSelectNum==10)imageSelect=image10;
        if (imageSelectNum==11)imageSelect=image11;
        if (imageSelectNum==12)imageSelect=image12;
        if (imageSelectNum==13)imageSelect=image13;
        if (imageSelectNum==14)imageSelect=image14;
        if (imageSelectNum==15)imageSelect=image15;
        if (imageSelectNum==16)imageSelect=image16;

        if (imageSelect!=null){
            imageSelect.setBackgroundResource(getEmojiResGrey(imageSelectNum));}

        image1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image1.setBackgroundResource(R.drawable.emoji_1);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image1) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image1;
                            imageSelectNum = 1;
                        }
                    }else{
                        imageSelect = image1;
                        imageSelectNum = 1;
                    }
                }
                return false;
            }
        });

        image2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image2.setBackgroundResource(R.drawable.emoji_2);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image2) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image2;
                            imageSelectNum = 2;
                        }
                    }else{
                        imageSelect = image2;
                        imageSelectNum = 2;
                    }
                }
                return false;
            }
        });
        image3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image3.setBackgroundResource(R.drawable.emoji_3);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image3) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image3;
                            imageSelectNum = 3;
                        }
                    }else{
                        imageSelect = image3;
                        imageSelectNum = 3;
                    }
                }
                return false;
            }
        });
        image4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image4.setBackgroundResource(R.drawable.emoji_4);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image4) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image4;
                            imageSelectNum = 4;
                        }
                    }else{
                        imageSelect = image4;
                        imageSelectNum = 4;
                    }
                }
                return false;
            }
        });
        image5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image5.setBackgroundResource(R.drawable.emoji_5);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image5) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image5;
                            imageSelectNum = 5;
                        }
                    }else{
                        imageSelect = image5;
                        imageSelectNum = 5;
                    }
                }
                return false;
            }
        });
        image6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image6.setBackgroundResource(R.drawable.emoji_6);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image6) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image6;
                            imageSelectNum = 6;
                        }
                    }else{
                        imageSelect = image6;
                        imageSelectNum = 6;
                    }
                }
                return false;
            }
        });
        image7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image7.setBackgroundResource(R.drawable.emoji_7);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image7) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image7;
                            imageSelectNum = 7;
                        }
                    }else{
                        imageSelect = image7;
                        imageSelectNum = 7;
                    }
                }
                return false;
            }
        });
        image8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image8.setBackgroundResource(R.drawable.emoji_8);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image8) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image8;
                            imageSelectNum = 8;
                        }
                    }else{
                        imageSelect = image8;
                        imageSelectNum = 8;
                    }
                }
                return false;
            }
        });
        image9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image9.setBackgroundResource(R.drawable.emoji_9);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image9) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image9;
                            imageSelectNum = 9;
                        }
                    }else{
                        imageSelect = image9;
                        imageSelectNum = 9;
                    }
                }
                return false;
            }
        });
        image10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image10.setBackgroundResource(R.drawable.emoji_10);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image10) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image10;
                            imageSelectNum = 10;
                        }
                    }else{
                        imageSelect = image10;
                        imageSelectNum = 10;
                    }
                }
                return false;
            }
        });
        image11.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image11.setBackgroundResource(R.drawable.emoji_11);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image11) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image11;
                            imageSelectNum = 11;
                        }
                    }else{
                        imageSelect = image11;
                        imageSelectNum = 11;
                    }
                }
                return false;
            }
        });
        image12.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image12.setBackgroundResource(R.drawable.emoji_12);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image12) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image12;
                            imageSelectNum = 12;
                        }
                    }else{
                        imageSelect = image12;
                        imageSelectNum = 12;
                    }
                }
                return false;
            }
        });
        image13.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image13.setBackgroundResource(R.drawable.emoji_13);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image13) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image13;
                            imageSelectNum = 13;
                        }
                    }else{
                        imageSelect = image13;
                        imageSelectNum = 13;
                    }
                }
                return false;
            }
        });
        image14.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image14.setBackgroundResource(R.drawable.emoji_14);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image14) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image14;
                            imageSelectNum = 14;
                        }
                    }else{
                        imageSelect = image14;
                        imageSelectNum = 14;
                    }
                }
                return false;
            }
        });
        image15.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image15.setBackgroundResource(R.drawable.emoji_15);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image15) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image15;
                            imageSelectNum = 15;
                        }
                    }else{
                        imageSelect = image15;
                        imageSelectNum = 15;
                    }
                }
                return false;
            }
        });
        image16.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    image16.setBackgroundResource(R.drawable.emoji_16);
                    if (imageSelect!=null){
                        imageSelect.setBackgroundResource(getEmojiRes(imageSelectNum));
                        if (imageSelect==image16) {
                            imageSelect = null;
                            imageSelectNum = 0;
                        }else{
                            imageSelect = image16;
                            imageSelectNum = 16;
                        }
                    }else{
                        imageSelect = image16;
                        imageSelectNum = 16;
                    }
                }
                return false;
            }
        });


        final ImageButton buttonRemark=(ImageButton) tab01.findViewById(R.id.buttonremark);

//emoji对话框的确认
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (imageSelectNum==0){
                            Toast.makeText(MainActivity.this, "没有选择今天的心情", Toast.LENGTH_SHORT).show();;
                            return;
                        }
                        if (getEmojiIdByDate(getDateForToday())==0){//今天还没有添加
                            helpter3=new UserDBHelper3(MainActivity.this);
                            ContentValues values = new ContentValues();
                            values.put("remark", String.valueOf(imageSelectNum));
                            values.put("createdate", getDateForToday());
                            helpter3.insert(values);
                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                            buttonRemark.setImageResource(getEmojiRes(getEmojiIdByDate(getDateForToday())));
                        }else{


                            Cursor c = helpter3.query();
                            if (c.getCount() != 0) {
                                c.moveToFirst();
                                c.getString(1);
                                for (int i = 1; i <= c.getCount(); i++) {
                                    if (c.getString(2).toString().equals(getDateForToday())) {
                                        helpter3.del(c.getInt(0));
                                    }
                                    if (!c.isLast()) c.moveToNext();
                                }
                            }
                            c.close();
                            helpter3=new UserDBHelper3(MainActivity.this);
                            ContentValues values = new ContentValues();
                            values.put("remark", String.valueOf(imageSelectNum));
                            values.put("createdate", getDateForToday());
                            helpter3.insert(values);
                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                            buttonRemark.setImageResource(getEmojiRes(getEmojiIdByDate(getDateForToday())));

                        }
                    }
                });
        customizeDialog.setNegativeButton("放弃",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageSelect=null;
                        imageSelectNum=0;
                    }
                });
        customizeDialog.show();
    }

    private void showConfig1Dialog(final String anything) {//添加计划
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        final String XScaleLastTime=set1Xscale;

        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.layoutconfig1, null);
        customizeDialog.setTitle("");
        customizeDialog.setView(dialogView);

        ListView mlist1 = (ListView) dialogView.findViewById(R.id.config1list1);
        ListView mlist2 = (ListView) dialogView.findViewById(R.id.config1list2);
        ListView mlist3 = (ListView) dialogView.findViewById(R.id.config1list3);

        HashMap<String, Object> map = new HashMap<String, Object>();
        final ArrayList<String> itemscanshow = new ArrayList<String>();
//

        {//list1
            String str1 = null;
            Cursor c = helpter.query();
            itemscanshow.clear();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);

                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                //String str1 = formatter.format(curDate);
                itemscanshow.clear();
                listItem_config1_list1.clear();
                map = new HashMap<String, Object>();
                map.put("ItemText1", "全选");
                if (ItemsToShow.contains("All"))map.put("ItemText2", true);
                else map.put("ItemText2", false);
                listItem_config1_list1.add(map);
                for (int i = 1; i <= c.getCount(); i++) {
                    //if (c.getString(3).equals(str1)) {
                    str1 = c.getString(1);
                    if (!itemscanshow.contains(str1)) {
                        itemscanshow.add(str1);
                        map = new HashMap<String, Object>();
                        map.put("ItemText1", str1);

                        if (ItemsToShow.contains(str1)||ItemsToShow.contains("All"))map.put("ItemText2", true);
                        else map.put("ItemText2", false);
                        listItem_config1_list1.add(map);
                    }
                        if (!c.isLast()) c.moveToNext();

                }

            } else {
                c.close();
                //graphs.add("没有可以显示的条目");
                mAdapter_config1_list1.notifyDataSetChanged();
            }
                c.close();
        }
        {//list2
            listItem_config1_list2.clear();
            map = new HashMap<String, Object>();
            map.put("ItemText1", "按天显示");
            if (set1Xscale.equals("day"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);

            listItem_config1_list2.add(map);
            map = new HashMap<String, Object>();
            map.put("ItemText1", "按周显示");
            if (set1Xscale.equals("week"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);

            listItem_config1_list2.add(map);

            map = new HashMap<String, Object>();
            map.put("ItemText1", "按月显示");
            if (set1Xscale.equals("month"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);

            listItem_config1_list2.add(map);
        }

        {
            listItem_config1_list3.clear();
            map = new HashMap<String, Object>();
            map.put("ItemText1", "自动");
            if (setYmax.equals("auto"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);
            listItem_config1_list3.add(map);

            String[] AYSCALE=new String[]{"1h","2h","4h","8h","15h",
                    "30h","50h","100h"};

            for (int i=0;i<AYSCALE.length;i++){
                map = new HashMap<String, Object>();
                map.put("ItemText1", AYSCALE[i]);
                if (setYmax.equals(AYSCALE[i]))
                    map.put("ItemText2", true);
                else
                    map.put("ItemText2", false);
                listItem_config1_list3.add(map);

            }


        }

        mlist1.setAdapter(mAdapter_config1_list1);
        mlist2.setAdapter(mAdapter_config1_list2);
        mlist3.setAdapter(mAdapter_config1_list3);

        mlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               CheckBox ck=(CheckBox)view.findViewById(R.id.checkBox);
//                TextView tx=(TextView)v.findViewById(R.id.textcheckbox);
//                Toast.makeText(MainActivity.this, tx.getText().toString()+"check被按下,状态为"+ck.isChecked(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "按下"+position, Toast.LENGTH_SHORT).show();
                if (position==0){
                    if(ck.isChecked()){
                        for (int i=1;i<listItem_config1_list1.size();i++){
                            listItem_config1_list1.get(i).put("ItemText2", false);

                        }
                        listItem_config1_list1.get(0).put("ItemText2", false);
                        mAdapter_config1_list1.notifyDataSetInvalidated();
                    }else{
                        for (int i=1;i<listItem_config1_list1.size();i++){

                            listItem_config1_list1.get(i).put("ItemText2", true);

                            //CheckBox cb=(CheckBox)parent.getChildAt(i).findViewById(R.id.checkBox);
                            //cb.setChecked(true);
                        }
                        listItem_config1_list1.get(0).put("ItemText2", true);
                        mAdapter_config1_list1.notifyDataSetInvalidated();
                    }

                }else {
                    if (ck.isChecked()&& (boolean)listItem_config1_list1.get(0).get("ItemText2"))
                        listItem_config1_list1.get(0).put("ItemText2", false );
                    listItem_config1_list1.get(position).put("ItemText2",
                            ! (boolean)listItem_config1_list1.get(position).get("ItemText2"));
                    mAdapter_config1_list1.notifyDataSetInvalidated();
                }

            }
        });

        mlist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<listItem_config1_list2.size();i++) {
                    listItem_config1_list2.get(i).put("ItemText2", false);
                }
                listItem_config1_list2.get(position).put("ItemText2", true);
                mAdapter_config1_list2.notifyDataSetInvalidated();
                if(((String)listItem_config1_list2.get(position).get("ItemText1")).equals("按天显示"))
                    set1Xscale="day";
                if(((String)listItem_config1_list2.get(position).get("ItemText1")).equals("按周显示"))
                    set1Xscale="week";
                if(((String)listItem_config1_list2.get(position).get("ItemText1")).equals("按月显示"))
                    set1Xscale="month";
            }
        });
        mlist3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<listItem_config1_list3.size();i++) {
                    listItem_config1_list3.get(i).put("ItemText2", false);
                }
                listItem_config1_list3.get(position).put("ItemText2", true);
                mAdapter_config1_list3.notifyDataSetInvalidated();
                if(((String)listItem_config1_list3.get(position).get("ItemText1")).equals("自动")){
                    setYmax="auto";
                }else{
                    setYmax=(String)listItem_config1_list3.get(position).get("ItemText1");
                }
            }
        });
        //统计图设置对话框确定按钮
                customizeDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if((boolean)listItem_config1_list1.get(0).get("ItemText2")) {
                                    //全选
                                    ItemsToShow.clear();
                                    ItemsToShow.add("All");
                                }
                                else{
                                    ItemsToShow.remove("All");
                                    for (int i = 1;i<listItem_config1_list1.size();i++){
                                        if ((boolean)listItem_config1_list1.get(i).get("ItemText2"))
                                            if (!ItemsToShow.contains((String)listItem_config1_list1.get(i).get("ItemText1")))
                                                ItemsToShow.add((String)listItem_config1_list1.get(i).get("ItemText1"));
                                            else;
                                        else ItemsToShow.remove((String)listItem_config1_list1.get(i).get("ItemText1"));

                                    }

                                }
                                if (!XScaleLastTime.equals(set1Xscale))
                                    spinnerDataInit();//更改了就更新spinner
                                Spinner myspinner = (Spinner) tab04.findViewById(R.id.spinnerData);
                                String date=dateFormatSimple(graphs.get(myspinner.getSelectedItemPosition()));
                                updateStatistics(ItemsToShow,set1Xscale,date,setYmax,0);
                                green.setTextForTheMost();
                            }

                        });
//        customizeDialog.setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
//                        //...To-do
//                    }
//                });

        customizeDialog.show();
    }//统计图设置1的对话框

    //统计图设置对话框2
    private void showConfig2Dialog() {//添加计划
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        //final String XScaleLastTime=set1Xscale;

        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.layoutconfig2, null);
        customizeDialog.setTitle("");
        customizeDialog.setView(dialogView);

        ListView mlist1 = (ListView) dialogView.findViewById(R.id.config2list1);
        ListView mlist2 = (ListView) dialogView.findViewById(R.id.config2list2);
        ListView mlist3 = (ListView) dialogView.findViewById(R.id.config2list3);

        HashMap<String, Object> map = new HashMap<String, Object>();
        final ArrayList<String> itemscanshow = new ArrayList<String>();
//

        {//list1
            listItem_config2_list1.clear();
            map = new HashMap<String, Object>();
            map.put("ItemText1", "按天显示");
            if (set2Xscale.equals("day"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);

            listItem_config2_list1.add(map);
            map = new HashMap<String, Object>();
            map.put("ItemText1", "按周显示");
            if (set2Xscale.equals("week"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);

            listItem_config2_list1.add(map);

            map = new HashMap<String, Object>();
            map.put("ItemText1", "按月显示");
            if (set2Xscale.equals("month"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);

            listItem_config2_list1.add(map);
        }

        {//list2
            String XMAXNUM[]=new String[]{"5","7","10","15","20","30"};


            listItem_config2_list2.clear();
            map = new HashMap<String, Object>();
            map.put("ItemText1", "自动");
            if (set2Xmax==0)
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);
            listItem_config2_list2.add(map);


            for (int i=0;i<XMAXNUM.length;i++){
                map = new HashMap<String, Object>();
                map.put("ItemText1", XMAXNUM[i]);
                if (XMAXNUM[i].equals(String.valueOf(set2Xmax)))
                    map.put("ItemText2", true);
                else
                    map.put("ItemText2", false);
                listItem_config2_list2.add(map);

            }

        }






        {//list3
            listItem_config2_list3.clear();
            map = new HashMap<String, Object>();
            map.put("ItemText1", "自动");
            if (setYmax.equals("auto"))
                map.put("ItemText2", true);
            else
                map.put("ItemText2", false);
            listItem_config2_list3.add(map);

            String[] AYSCALE=new String[]{"1h","2h","4h","8h","15h",
                    "30h","50h","100h"};

            for (int i=0;i<AYSCALE.length;i++){
                map = new HashMap<String, Object>();
                map.put("ItemText1", AYSCALE[i]);
                if (setYmax.equals(AYSCALE[i]))
                    map.put("ItemText2", true);
                else
                    map.put("ItemText2", false);
                listItem_config2_list3.add(map);

            }


        }

        mlist1.setAdapter(mAdapter_config2_list1);
        mlist2.setAdapter(mAdapter_config2_list2);
        mlist3.setAdapter(mAdapter_config2_list3);



        mlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<listItem_config2_list1.size();i++) {
                    listItem_config2_list1.get(i).put("ItemText2", false);
                }
                listItem_config2_list1.get(position).put("ItemText2", true);
                mAdapter_config2_list1.notifyDataSetInvalidated();
                if(((String)listItem_config2_list1.get(position).get("ItemText1")).equals("按天显示"))
                    set2Xscale="day";
                if(((String)listItem_config2_list1.get(position).get("ItemText1")).equals("按周显示"))
                    set2Xscale="week";
                if(((String)listItem_config2_list1.get(position).get("ItemText1")).equals("按月显示"))
                    set2Xscale="month";
            }
        });

        mlist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<listItem_config2_list2.size();i++) {
                    listItem_config2_list2.get(i).put("ItemText2", false);
                }
                listItem_config2_list2.get(position).put("ItemText2", true);
                mAdapter_config2_list2.notifyDataSetInvalidated();
                if(((String)listItem_config2_list2.get(position).get("ItemText1")).equals("自动")){
                    set2Xmax=0;
                }else{
                    set2Xmax=Integer.valueOf((String)listItem_config2_list2.get(position).get("ItemText1"));
                }
            }
        });

        mlist3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<listItem_config2_list3.size();i++) {
                    listItem_config2_list3.get(i).put("ItemText2", false);
                }
                listItem_config2_list3.get(position).put("ItemText2", true);
                mAdapter_config2_list3.notifyDataSetInvalidated();
                if(((String)listItem_config2_list3.get(position).get("ItemText1")).equals("自动")){
                    setYmax="auto";
                }else{
                    setYmax=(String)listItem_config2_list3.get(position).get("ItemText1");
                }
            }
        });



        //统计图设置对话框确定按钮
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Spinner myspinner = (Spinner) tab04.findViewById(R.id.spinnerData);
                        String item=graphs.get(myspinner.getSelectedItemPosition());
                        updateStatisticsAccordingtoItem(item,set2Xscale,set2timeset,set2Xmax,setYmax,0);
                        green.setTextForTheMost();
                    }

                });

        //统计图设置对话框参考时间按钮
        customizeDialog.setNegativeButton("参考时间",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showEditSet2TimeDialog();
                    }
                });
//        customizeDialog.setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
//                        //...To-do
//                    }
//                });



        customizeDialog.show();
    }//统计图设置2的对话框

    private void showEditSet2TimeDialog() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        if (set2timeset.equals("now"))
            editText.setText(getDateForToday());
        else
            editText.setText(set2timeset);

        inputDialog.setTitle("设置参考时间...").setView(editText);
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showConfig2Dialog();
            }
        });
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str=editText.getText().toString();
                        if (str.length()==10 && str.substring(4,5).equals("-") && str.substring(7,8).equals("-")) {
                            set2timeset = str;
                            Toast.makeText(MainActivity.this, "参考时间已更新!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "格式错误!", Toast.LENGTH_SHORT).show();

                        showConfig2Dialog();
                    }
                }).show();
    }//更改set2timeset

    private void showHelpDialog(){
       /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        // normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("关于Time与帮助...");
        normalDialog.setMessage("帮助\n" +
                "添加界面的左侧为预设的事件，可以添加（下拉到最后）、删除（长按），选择事件，输入时间和单位（分钟或小时）即可完成打卡，点击右下角的表情可以添加或修改当天的心情，点击右上角的时钟图标可以为将要做的事情计时；"
                +"计划页面可以添加计划，计划的时限可以是具体的多少天（内），也可以是每天、每周、每月，长按计划可以删除，单击计划可以查看计划的完成情况；"
                +"浏览页面可以浏览所有添加的打卡信息，可以按月份、日期浏览，也可以单击某个条目按事件浏览，长按可以删除添加的信息；"
                +"点击浏览界面的>符号可以查看统计图。");
        normalDialog.setPositiveButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
/*        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                        //...To-do
                    }
                });*/
        // 显示
        normalDialog.show();

    }

    private void showManualDialog() {//添加计划
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.layoutmanual,null);
        customizeDialog.setTitle("手动添加Time数据...");
        customizeDialog.setView(dialogView);


        final EditText ed1=(EditText)dialogView.findViewById(R.id.editEvent);
        final EditText ed2=(EditText)dialogView.findViewById(R.id.editMinutes);
        final EditText ed3=(EditText)dialogView.findViewById(R.id.editDate);
        final EditText ed4=(EditText)dialogView.findViewById(R.id.editTime);

        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    ContentValues values = new ContentValues();
                    values.put("event", ed1.getText().toString());
                    values.put("minutes", ed2.getText().toString());
                    values.put("createdate",ed3.getText().toString());
                    values.put("createtime", ed4.getText().toString());

                    helpter.insert(values);
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    updateMainList();
                    changeBrowse("");
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
                        //...To-do
                    }
                });
        customizeDialog.show();
    }//手动添加TIME条目的对话框

    /**
     *
     * @param arg1 int 多少分钟
     * @return String 多少分钟或者a.b小时
     */
    public String timeChange(int arg1){
        String str1=null;
        float hour=(float)((float)arg1 / 60.0f);
        if (arg1<60) str1= String.valueOf(arg1)+"分钟";
        else  {
            if (hour<10) str1=  String.format("%3.3s",String.valueOf ( (float)((float)arg1 / 60.0) ))+"小时";
            if (hour>=10) str1=  String.format("%3.4s",String.valueOf ( (float)((float)arg1 / 60.0) ))+"小时";
            if (hour>=100) str1=  String.format("%3.5s",String.valueOf ( (float)((float)arg1 / 60.0) ))+"小时";
        }
        //else  str1= String.valueOf ( (float)((float)arg1 / 60.0) )+"小时";

        return str1;
    }//90-"1.5小时"

    public void changeTab(int id){
        ImageView image1=(ImageView)findViewById(R.id.ivAdd);
        ImageView image2=(ImageView)findViewById(R.id.ivPlans);
        ImageView image3=(ImageView)findViewById(R.id.ivData);
        ImageView image4=(ImageView)findViewById(R.id.ivSettings);
        TextView textTitle=(TextView)findViewById(R.id.textTitle);
        ImageView imageMore=(ImageView)findViewById(R.id.imageMore);
        ImageView imageLess=(ImageView)findViewById(R.id.imageLess);
        switch (id) {

            //滑动传过来的参数为0-3
            case 0:
                //updateMainList();
                textTitle.setText("Time");
                imageMore.setImageResource(R.drawable.clk);
                imageMore.setVisibility(viewPager.VISIBLE);
                imageLess.setVisibility(viewPager.INVISIBLE);
                image1.setImageResource(R.drawable.add2);
                image2.setImageResource(R.drawable.plan);
                image3.setImageResource(R.drawable.data);
                image4.setImageResource(R.drawable.set);
                viewPagerNow=0;
                break;
            case 1:
                textTitle.setText("Schedule");
                imageMore.setVisibility(viewPager.INVISIBLE);
                imageLess.setVisibility(viewPager.INVISIBLE);
                image1.setImageResource(R.drawable.add);
                image2.setImageResource(R.drawable.plan2);
                image3.setImageResource(R.drawable.data);
                image4.setImageResource(R.drawable.set);
                viewPagerNow=1;
                break;
            case 2:

                textTitle.setText("Browse");
                imageMore.setImageResource(R.drawable.more);
                imageMore.setVisibility(viewPager.VISIBLE);
                imageLess.setVisibility(viewPager.INVISIBLE);
                image1.setImageResource(R.drawable.add);
                image2.setImageResource(R.drawable.plan);
                image3.setImageResource(R.drawable.data2);
                image4.setImageResource(R.drawable.set);
                viewPagerNow=2;
                spinnerInit();
                changeBrowse("");
                break;
            case 3:
                textTitle.setText("Statistics");
                imageMore.setVisibility(viewPager.INVISIBLE);
                imageLess.setVisibility(viewPager.VISIBLE);
                image1.setImageResource(R.drawable.add);
                image2.setImageResource(R.drawable.plan);
                image3.setImageResource(R.drawable.data2);
                image4.setImageResource(R.drawable.set);
                viewPagerNow=3;
                //updateStatistics(null,"month","now","auto",0);
                set1Xscale="month";
                ItemsToShow.clear();
                ItemsToShow.add("All");
                setYmax="auto";//set1与set2之公用
                set2Xscale="day";
                set2Xmax=0;
                set2timeset="now";

                spinnerDataInit();

                if (accordingToTime) updateStatistics();
                else updateStatisticsAccordingtoItem();
                //green.start(2);
                break;
            case 4:
                textTitle.setText("Settings");
                imageMore.setVisibility(viewPager.INVISIBLE);
                imageLess.setVisibility(viewPager.INVISIBLE);
                image1.setImageResource(R.drawable.add);
                image2.setImageResource(R.drawable.plan);
                image3.setImageResource(R.drawable.data);
                image4.setImageResource(R.drawable.set2);
                viewPagerNow=4;
                break;

            case R.id.llAdd:
                changeTab(0);
                viewPager.setCurrentItem(0,false);

                viewPagerNow=0;
                break;
            case R.id.llPlans:
                changeTab(1);
                viewPager.setCurrentItem(1,false);

                viewPagerNow=1;
                break;
            case R.id.llData:
                changeTab(2);
                viewPager.setCurrentItem(2,false);

                viewPagerNow=2;
                break;

            case R.id.llSettings:
                changeTab(4);
                viewPager.setCurrentItem(4,false);

                viewPagerNow=4;
                break;

            case 34:
                changeTab(3);
                viewPager.setCurrentItem(3,false);

                viewPagerNow=3;
                break;

            case 43:
                changeTab(2);
                viewPager.setCurrentItem(2);

                viewPagerNow=2;
                break;

        }

    }//滑动 改变界面

    /**
     * 更新主浏览的list
     * @param arg1 为""时显示所有，为日期或月份时显示相应时间内的事件，为事件时显示所有该事件
     */
    public void changeBrowse(String arg1){
        //浏览界面数据库的使用
        HashMap<String, Object> map =null ;
        if (arg1.equals("") || arg1.equals("逐项显示")) {
            listItem.clear();
            helpter = new UserDBHelper(this);
            Cursor c = helpter.query();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);

                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                //String str1 = formatter.format(curDate);

                for (int i = 1; i <= c.getCount(); i++) {
                    //if (c.getString(3).equals(str1)) {
                    map = new HashMap<String, Object>();
                    map.put("ItemText1", c.getString(1).toString());
                    map.put("ItemText2",timeChange(Integer.valueOf(c.getString(2).toString()).intValue()));
                    map.put("ItemText3",c.getString(3).toString().substring(5));
                    map.put("ItemText4",c.getString(4).toString().substring(0,5));


                    map.put("ItemImage",getEmojiRes(getEmojiIdByDate(c.getString(3).toString())));
                    listItem.add(map);


                        //plans.add(String.format("%-5s",c.getString(1).toString())+
                        //if (c.getPosition()==4)
                        //Toast.makeText(this,c.getString(0).toString() , Toast.LENGTH_SHORT).show();

                    //}
                    if (!c.isLast()) c.moveToNext();

                }

            }
            c.close();
            mSimpleAdapter.notifyDataSetChanged();

        }else if (arg1.length()==10){//显示某天的事件
            //String str1=arg1.substring(0,4)+"-"+arg1.substring(5,7);
            listItem.clear();
            Cursor c = helpter.query();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);

                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                //String str1 = formatter.format(curDate);

                for (int i = 1; i <= c.getCount(); i++) {
                    if (c.getString(3).toString().equals(arg1)) {
                        map = new HashMap<String, Object>();
                        map.put("ItemText1", c.getString(1).toString());
                        map.put("ItemText2", timeChange(Integer.valueOf(c.getString(2).toString()).intValue()));
                        map.put("ItemText3", c.getString(3).toString().substring(5));
                        map.put("ItemText4", c.getString(4).toString().substring(0, 5));

                        map.put("ItemImage",getEmojiRes(getEmojiIdByDate(c.getString(3).toString())));
                        listItem.add(map);
                    }


                        //plans.add(String.format("%-5s",c.getString(1).toString())+
                        //if (c.getPosition()==4)
                        //Toast.makeText(this,c.getString(0).toString() , Toast.LENGTH_SHORT).show();

                        //}
                    if (!c.isLast()) c.moveToNext();

                }
                map = new HashMap<String, Object>();
                map.put("ItemText1","");
                map.put("ItemText2",  "下一天");
                map.put("ItemText3", "");
                map.put("ItemText4", "");

                map.put("ItemImage", R.drawable.blank);
                listItem.add(map);

            }
            c.close();
            mSimpleAdapter.notifyDataSetChanged();
        }else if (arg1.length()==8 && arg1.contains("年") && arg1.contains("月")){
            String str1=arg1.substring(0,4)+"-"+arg1.substring(5,7);
            listItem.clear();
            Cursor c = helpter.query();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);

                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                //String str1 = formatter.format(curDate);

                for (int i = 1; i <= c.getCount(); i++) {
                    if (c.getString(3).toString().substring(0,7).equals(str1)) {
                        map = new HashMap<String, Object>();
                        map.put("ItemText1", c.getString(3).toString().substring(5,7)+
                                "月"+
                                c.getString(3).toString().substring(8,10)+"日");
                        map.put("ItemText2", "");
                        map.put("ItemText3", "");
                        map.put("ItemText4", "");

                        map.put("ItemImage",getEmojiRes(getEmojiIdByDate(c.getString(3).toString())));
                        if (!listItem.contains(map)) listItem.add(map);
                    }


                    //plans.add(String.format("%-5s",c.getString(1).toString())+
                    //if (c.getPosition()==4)
                    //Toast.makeText(this,c.getString(0).toString() , Toast.LENGTH_SHORT).show();

                    //}
                    if (!c.isLast()) c.moveToNext();

                }

            }
            c.close();
            mSimpleAdapter.notifyDataSetChanged();

        }else if (arg1.length()==11){
            String str2=arg1.substring(0,4)
                    +"-"
                    +arg1.substring(5,7)
                    +"-"
                    +arg1.substring(8,10);
            changeBrowse(str2);

        }else{//显示某个事件
            //String str1=arg1.substring(0,4)+"-"+arg1.substring(5,7);
            listItem.clear();
            Cursor c = helpter.query();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);

                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                //String str1 = formatter.format(curDate);

                for (int i = 1; i <= c.getCount(); i++) {
                    if (c.getString(1).toString().equals(arg1)) {
                        map = new HashMap<String, Object>();
                        map.put("ItemText1", c.getString(1).toString());
                        map.put("ItemText2", timeChange(Integer.valueOf(c.getString(2).toString()).intValue()));
                        map.put("ItemText3", c.getString(3).toString().substring(5));
                        map.put("ItemText4", c.getString(4).toString().substring(0, 5));

                        map.put("ItemImage", getEmojiRes(getEmojiIdByDate(c.getString(3).toString())));
                        listItem.add(map);
                    }


                    //plans.add(String.format("%-5s",c.getString(1).toString())+
                    //if (c.getPosition()==4)
                    //Toast.makeText(this,c.getString(0).toString() , Toast.LENGTH_SHORT).show();

                    //}
                    if (!c.isLast()) c.moveToNext();

                }
            }
            c.close();
            mSimpleAdapter.notifyDataSetChanged();

        }


    }//更新BROWSE的list

    /**
     * 更新BROWSE的下拉菜单
     */
    public void spinnerInit(){
        String str1=null;
        Cursor c = helpter.query();
        demons.clear();
        if (c.getCount() != 0) {
            c.moveToFirst();
            c.getString(1);

            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            //String str1 = formatter.format(curDate);
            demons.clear();
            demons.add("逐项显示");
            for (int i = 1; i <= c.getCount(); i++) {
                //if (c.getString(3).equals(str1)) {
                str1=c.getString(3).substring(0,4)+"年"+c.getString(3).substring(5,7)+"月";
                if (!demons.contains(str1))demons.add(str1);




                //plans.add(String.format("%-5s",c.getString(1).toString())+
                //if (c.getPosition()==4)
                //Toast.makeText(this,c.getString(0).toString() , Toast.LENGTH_SHORT).show();

                //}
                if (!c.isLast()) c.moveToNext();

            }

        }else {
            c.close();
            demons.add("没有可以显示的条目");
        }
        c.close();
        myadapter_spinner.notifyDataSetChanged();
        Spinner myspinner=(Spinner) tab03.findViewById(R.id.spinnerBrowse);
        myspinner.setSelection(0);

    }

    public void spinnerPlanInit(){
        helpter2=new UserDBHelper2(this);
        String str1=null;
        Cursor c = helpter2.query();
        plandemons.clear();
        plandemons.add("显示全部");
        if (c.getCount() != 0) {
            c.moveToFirst();
            c.getString(1);

            for (int i = 1; i <= c.getCount(); i++) {
                //if (c.getString(3).equals(str1)) {
                str1=c.getString(4).substring(0,4)+"年"+c.getString(4).substring(5,7)+"月";
                if (!plandemons.contains(str1))plandemons.add(str1);

                if (!c.isLast()) c.moveToNext();

            }

        }
        c.close();

        myadapter_plan_spinner.notifyDataSetChanged();
        Spinner myspinner=(Spinner) tab02.findViewById(R.id.spinnerPlan);
        myspinner.setSelection(0);

    }//更新PLAN的下拉菜单

    //更新统计图界面的spinner
    public void spinnerDataInit(){
        if (!accordingToTime) {//accordingToItem
            String str1 = null;
            Cursor c = helpter.query();
            graphs.clear();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);

                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                //String str1 = formatter.format(curDate);
                graphs.clear();
                for (int i = 1; i <= c.getCount(); i++) {
                    //if (c.getString(3).equals(str1)) {
                    str1 = c.getString(1);
                    if (!graphs.contains(str1)) graphs.add(str1);
                    if (!c.isLast()) c.moveToNext();

                }

            } else {
                graphs.add("没有可以显示的条目");
                c.close();
            }
            c.close();
            myadapter_data_spinner.notifyDataSetChanged();
            Spinner myspinner = (Spinner) tab04.findViewById(R.id.spinnerData);
            myspinner.setSelection(0);
        }else{//accordingToTime
            //按月显示
            if (set1Xscale.equals("month")){
                String str1 = null;
                Cursor c = helpter.query();
                graphs.clear();
                if (c.getCount() != 0) {
                    c.moveToFirst();
                    c.getString(1);

                    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    //String str1 = formatter.format(curDate);
                    graphs.clear();
                    for (int i = 1; i <= c.getCount(); i++) {
                        //if (c.getString(3).equals(str1)) {
                        str1 = dateFormat(getYearMonth(c.getString(3).toString()));
                        // c.getString(3).substring(0,4)+"年"+c.getString(3).substring(5,7)+"月";
                        if (!graphs.contains(str1)) graphs.add(str1);
                        if (!c.isLast()) c.moveToNext();
                    }

                } else {
                    graphs.add("没有可以显示的条目");
                    c.close();
                }c.close();
                myadapter_data_spinner.notifyDataSetChanged();
                Spinner myspinner = (Spinner) tab04.findViewById(R.id.spinnerData);
                myspinner.setSelection(0);
            }
            //按天显示
            if (set1Xscale.equals("day")){
                String str1 = null;
                Cursor c = helpter.query();
                graphs.clear();
                if (c.getCount() != 0) {
                    c.moveToFirst();
                    c.getString(1);

                    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    //String str1 = formatter.format(curDate);
                    graphs.clear();
                    for (int i = 1; i <= c.getCount(); i++) {
                        //if (c.getString(3).equals(str1)) {
                        str1 = dateFormat(c.getString(3).toString());
                        // c.getString(3).substring(0,4)+"年"+c.getString(3).substring(5,7)+"月";
                        if (!graphs.contains(str1)) graphs.add(str1);
                        if (!c.isLast()) c.moveToNext();
                    }

                } else {
                    graphs.add("没有可以显示的条目");
                    c.close();
                }
                c.close();
                Collections.sort(graphs);
                myadapter_data_spinner.notifyDataSetChanged();
                Spinner myspinner = (Spinner) tab04.findViewById(R.id.spinnerData);
                myspinner.setSelection(0);

            }
            if (set1Xscale.equals("week")){
                String str1 = null;
                Cursor c = helpter.query();
                graphs.clear();
                if (c.getCount() != 0) {
                    c.moveToFirst();
                    c.getString(1);

                    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    //Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    //String str1 = formatter.format(curDate);
                    graphs.clear();
                    for (int i = 1; i <= c.getCount(); i++) {
                        //if (c.getString(3).equals(str1)) {
                        str1 = dateFormat(getMonday(c.getString(3).toString()))
                                +"(第"+String.valueOf(getWeekinDate(getMonday(c.getString(3).toString())))+"周)";
                        // c.getString(3).substring(0,4)+"年"+c.getString(3).substring(5,7)+"月";
                        if (!graphs.contains(str1)) graphs.add(str1);
                        if (!c.isLast()) c.moveToNext();
                    }

                } else {
                    graphs.add("没有可以显示的条目");
                    c.close();
                }
                c.close();
                Collections.sort(graphs);
                myadapter_data_spinner.notifyDataSetChanged();
                Spinner myspinner = (Spinner) tab04.findViewById(R.id.spinnerData);
                myspinner.setSelection(0);
            }

        }

    }//更新统计图的下拉菜单

    public void updateMainList(){
        HashMap<String, Object> map =null ;

        myadapter2 = new SimpleAdapter(this,drinks,//需要绑定的数据
                R.layout.layouttile,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {"ItemText1","ItemText2",
                        "ItemText3"},
                new int[] {R.id.textTile1,R.id.textTile2,R.id.textTile3}
        );

        ListView myList2=(ListView)tab01.findViewById(R.id.listview2);

        //myadapter2.
        myList2.setAdapter(myadapter2);

        //myadapter2.clear();

        drinks.clear();
        helpter = new UserDBHelper(this);
        Cursor c = helpter.query();
        if (c.getCount()!=0) {
            c.moveToFirst();
            c.getString(1);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str1 = formatter.format(curDate);

            for (int i = 1; i <= c.getCount(); i++) {
                if (c.getString(3).equals(str1)) {

                    map = new HashMap<String, Object>();
                    map.put("ItemText1", c.getString(1).toString());
                    map.put("ItemText2",timeChange(Integer.valueOf(c.getString(2).toString()).intValue()));
                    map.put("ItemText3",c.getString(4).toString());

                    drinks.add(map);
//                    drinks.add(String.format("%-10s\t", c.getString(1).toString())
//                            + String.format("%-10s\t", TimeChange(Integer.valueOf(c.getString(2).toString()).intValue()))
//                            + c.getString(4).toString());
                    //plans.add(String.format("%-5s",c.getString(1).toString())+
                    //if (c.getPosition()==4)
                    //    Toast.makeText(this,c.getString(0).toString() , Toast.LENGTH_SHORT).show();

                }
                if (!c.isLast()) c.moveToNext();

            }
            myadapter2.notifyDataSetChanged();
        }
        c.close();


    }//更新主页的浏览

    public void updatePlanList(String arg1){
        if (arg1.equals("") || arg1.equals("显示全部")) {
            HashMap<String, Object> map = null;

            mSimpleAdapter_plan = new SimpleAdapter(this, trips,//需要绑定的数据
                    R.layout.layoutplan,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[]{"ItemText1", "ItemText2",
                            "ItemText3", "ItemText4"},
                    new int[]{R.id.textplan1, R.id.textplan2, R.id.textplan3, R.id.textplan4}
            );

            ListView mlist_plan = (ListView) tab02.findViewById(R.id.listPlan);
            mlist_plan.setAdapter(mSimpleAdapter_plan);


            trips.clear();
            //helpter2 = new UserDBHelper2(this);
            Cursor c = helpter2.query();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);


                for (int i = 1; i <= c.getCount(); i++) {
                    //if (c.getString(3).equals(str1)) {

                    map = new HashMap<String, Object>();
                    if (c.getString(1).toString().equals("1000"))
                        map.put("ItemText1", "每天");
                    else if (c.getString(1).toString().equals("7000"))
                        map.put("ItemText1", "每周");
                    else if (c.getString(1).toString().equals("30000"))
                        map.put("ItemText1", "每月");
                    else
                        map.put("ItemText1", c.getString(1).toString() + "天");

                    map.put("ItemText2", c.getString(2).toString());

                    if (Integer.valueOf(c.getString(3).toString()).intValue() < 60)
                        map.put("ItemText3", c.getString(3).toString() + "分钟");
                    else if (Integer.valueOf(c.getString(3).toString()).intValue() % 60 != 0)
                        map.put("ItemText3", c.getString(3).toString() + "分钟");
                    else
                        map.put("ItemText3", String.valueOf(Integer.valueOf(c.getString(3).toString()).intValue() / 60) + "小时");


                    map.put("ItemText4", c.getString(4).toString().substring(5, 10));
                    trips.add(map);

                    if (!c.isLast()) c.moveToNext();

                }

            }
            c.close();
            map = new HashMap<String, Object>();
            map.put("ItemText1", "");
            map.put("ItemText2", "添加计划");
            map.put("ItemText3", "...");
            map.put("ItemText4", "");
            trips.add(map);
            mSimpleAdapter_plan.notifyDataSetChanged();
        }else if(arg1.length()==8){

            HashMap<String, Object> map = null;

            mSimpleAdapter_plan = new SimpleAdapter(this, trips,//需要绑定的数据
                    R.layout.layoutplan,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                    new String[]{"ItemText1", "ItemText2",
                            "ItemText3", "ItemText4"},
                    new int[]{R.id.textplan1, R.id.textplan2, R.id.textplan3, R.id.textplan4}
            );

            ListView mlist_plan = (ListView) tab02.findViewById(R.id.listPlan);
            mlist_plan.setAdapter(mSimpleAdapter_plan);


            trips.clear();
            //helpter2 = new UserDBHelper2(this);
            Cursor c = helpter2.query();
            if (c.getCount() != 0) {
                c.moveToFirst();
                c.getString(1);


                for (int i = 1; i <= c.getCount(); i++) {
                    if (arg1.substring(0,4).equals(c.getString(4).substring(0,4))
                            && arg1.substring(5,7).equals(c.getString(4).substring(5,7))) {

                        map = new HashMap<String, Object>();
                        if (c.getString(1).toString().equals("1000"))
                            map.put("ItemText1", "每天");
                        else if (c.getString(1).toString().equals("7000"))
                            map.put("ItemText1", "每周");
                        else if (c.getString(1).toString().equals("30000"))
                            map.put("ItemText1", "每月");
                        else
                            map.put("ItemText1", c.getString(1).toString() + "天");

                        map.put("ItemText2", c.getString(2).toString());

                        if (Integer.valueOf(c.getString(3).toString()).intValue() < 60)
                            map.put("ItemText3", c.getString(3).toString() + "分钟");
                        else if (Integer.valueOf(c.getString(3).toString()).intValue() % 60 != 0)
                            map.put("ItemText3", c.getString(3).toString() + "分钟");
                        else
                            map.put("ItemText3", String.valueOf(Integer.valueOf(c.getString(3).toString()).intValue() / 60) + "小时");


                        map.put("ItemText4", c.getString(4).toString().substring(5, 10));
                        trips.add(map);
                    }

                    if (!c.isLast()) c.moveToNext();

                }

            }
            c.close();
            map = new HashMap<String, Object>();
            map.put("ItemText1", "");
            map.put("ItemText2", "添加计划");
            map.put("ItemText3", "...");
            map.put("ItemText4", "");
            trips.add(map);
            mSimpleAdapter_plan.notifyDataSetChanged();

        }


    }//更新SCHEDULE的LIST

    public String nextDay(String arg1){
        String str1=null;
        int year=Integer.valueOf(arg1.substring(0,4));
        int month=Integer.valueOf(arg1.substring(5,7));
        int day=Integer.valueOf(arg1.substring(8,10));
        day++;
        if (month==1 ||month==3||month==5||month==7||month==8||month==10){
            if (day==32){
                day=1;
                month++;
            }

        }else if (month==2){
            if (year %4!=0 && day==29){
                day=1;
                month++;
            }
            if (year %4==0 && day==30){
                day=1;
                month++;
            }

        }else if (month==12){
            if (day==32){
                day=1;
                month=1;
                year++;
            }
        }
        else{
            if (day==31){
                day=1;
                month++;
            }
        }

        str1= String.format("%4d年%02d月%02d日",year,month,day);
        return str1;
    }//"2017年08月01日"-"2017年08月02日

    public String lastDay(String arg1){
        String str1=null;
        int year=Integer.valueOf(arg1.substring(0,4));
        int month=Integer.valueOf(arg1.substring(5,7));
        int day=Integer.valueOf(arg1.substring(8,10));
        day--;
        if (month==2 ||month==4||month==6||month==8||month==9||month==11){
            if (day==0){
                day=31;
                month--;
            }

        }else if (month==3){
            if (year %4!=0 && day==0){
                day=28;
                month--;
            }
            if (year %4==0 && day==0){
                day=29;
                month--;
            }

        }else if (month==1){
            if (day==0){
                day=31;
                month=12;
                year--;
            }
        }
        else{
            if (day==0){
                day=30;
                month--;
            }
        }

        str1= String.format("%4d-%02d-%02d",year,month,day);
        return str1;
    }//"2017年08月01日"-"2017-07-31

    /**
     *
     * @param arg1 "17-08"
     * @return "17-07"
     */
    public String lastMonth(String arg1){
        String str1=null;
        int year=Integer.valueOf(arg1.substring(0,2));
        int month=Integer.valueOf(arg1.substring(3,5));

        month--;
        if (month==0){
            month=12;
            year--;
        }

        str1= String.format("%2d-%02d",year,month);
        return str1;
    }//"2017-08"-"2017-07

    public String getMonday(String date){
        String result=date;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date1;
        try {
            date1=format.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            int nbackdays=cal.get(Calendar.DAY_OF_WEEK)-2;
            if (nbackdays == -1)nbackdays=6;
            for (int i=1;i<=nbackdays;i++)result=lastDay(result);
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }//获取星期一
    public String getYear(String date){
        return date.substring(0,4);
    }
    public String getMonth(String date){
        return date.substring(5,7);
    }
    public String getDay(String date){
        return date.substring(9,10);
    }
    public String getYearMonth(String date){
        return date.substring(0,7);
    }
    public String getMonthDay(String date){
        return date.substring(5,10);
    }

    /**
     *
     * @return 2017-09-16
     */
    public String getDateForToday(){
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str_date    =    formatter.format(curDate);
        return str_date;
    }
    public long getTimeForNow(){
        long time1=0;
        Date date=new Date();
        SimpleDateFormat format    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str_date    =    format.format(curDate);
        try {
            date=format.parse(str_date);
            time1=date.getTime();

        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time1;
    }
    /**
     *
     * @param arg1 2017-07-31,2017-07
     * @return 2017年07月31日,2017年07月
     */
    public String dateFormat(String arg1){
        String str1=null;
        if (arg1.length()==10){
            str1=arg1.substring(0,4)+"年"+arg1.substring(5,7)+"月"+arg1.substring(8,10)+"日";
        }else if(arg1.length()==7){
            str1=arg1.substring(0,4)+"年"+arg1.substring(5,7)+"月";
        }
            return str1;
    }

    public String dateFormatSimple(String arg1){
        String str1=null;
        if (arg1.length()>=11){
            str1=arg1.substring(0,4)+"-"+arg1.substring(5,7)+"-"+arg1.substring(8,10);
        }else if(arg1.length()==8){
            str1=arg1.substring(0,4)+"-"+arg1.substring(5,7);
        }
        return str1;
    }

    public boolean isDateAfter(String date1,String date2,boolean canEqual){
        int year1 = Integer.valueOf(date1.substring(0,4));
        int month1 = Integer.valueOf(date1.substring(5,7));
        int day1 = Integer.valueOf(date1.substring(8,10));
        int year2 = Integer.valueOf(date2.substring(0,4));
        int month2 = Integer.valueOf(date2.substring(5,7));
        int day2 = Integer.valueOf(date2.substring(8,10));

        if (year1>year2)
            return true;
        else if(year1<year2)
            return false;
        else{
            if (month1>month2)
                return true;
            else if(month1<month2)
                return false;
            else{
                if (day1>day2)
                    return true;
                else if(day1<day2)
                    return false;
                else{
                    return canEqual;
                }
            }
        }
    }

    public String datePlus(String date,long plus){
        String str1=null;
        Date date1=new Date();

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1=format.parse(date);
            long time1=date1.getTime();
            long time2=time1+plus*1000 * 60 * 60 * 24;
            Date result=new Date();
            result.setTime(time2);
            str1=format.format(result);
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return str1;

    }

    public String detailedMinutes(int min){
        String str1=null;
        if (min<60)str1=String.valueOf(min)+"分钟";
        else if(min%60==0)str1=String.valueOf(min/60 )+"小时";
        else str1=String.valueOf(min/60 )+"小时" + String.valueOf(min% 60 )+"分钟";
        return str1;
    }//150分钟 - 2小时30分钟

    /**
     * 获取日期是一年中的第几周
     * @param date 日期
     * @return int 一年中的第几周
     */
    public int getWeekinDate(String date){

        Date date1=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1=format.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                return (cal.get(Calendar.WEEK_OF_YEAR) - 1);
            else return cal.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }//获取日期是一年中的第几周

    /**
     *
     * @param arg1 序号
     * @return 图片的资源编号
     */
    public int getEmojiRes(int arg1){
        if(arg1==1)return R.drawable.emoji1;
        if(arg1==2)return R.drawable.emoji2;
        if(arg1==3)return R.drawable.emoji3;
        if(arg1==4)return R.drawable.emoji4;
        if(arg1==5)return R.drawable.emoji5;
        if(arg1==6)return R.drawable.emoji6;
        if(arg1==7)return R.drawable.emoji7;
        if(arg1==8)return R.drawable.emoji8;
        if(arg1==9)return R.drawable.emoji9;
        if(arg1==10)return R.drawable.emoji10;
        if(arg1==11)return R.drawable.emoji11;
        if(arg1==12)return R.drawable.emoji12;
        if(arg1==13)return R.drawable.emoji13;
        if(arg1==14)return R.drawable.emoji14;
        if(arg1==15)return R.drawable.emoji15;
        if(arg1==16)return R.drawable.emoji16;
        return R.drawable.emoji1;
    }
    /**
     * 带灰色背景的emoji图片
     * @param arg1 序号
     * @return 图片的资源编号
     */
    public int getEmojiResGrey(int arg1){
        if(arg1==1)return R.drawable.emoji_1;
        if(arg1==2)return R.drawable.emoji_2;
        if(arg1==3)return R.drawable.emoji_3;
        if(arg1==4)return R.drawable.emoji_4;
        if(arg1==5)return R.drawable.emoji_5;
        if(arg1==6)return R.drawable.emoji_6;
        if(arg1==7)return R.drawable.emoji_7;
        if(arg1==8)return R.drawable.emoji_8;
        if(arg1==9)return R.drawable.emoji_9;
        if(arg1==10)return R.drawable.emoji_10;
        if(arg1==11)return R.drawable.emoji_11;
        if(arg1==12)return R.drawable.emoji_12;
        if(arg1==13)return R.drawable.emoji_13;
        if(arg1==14)return R.drawable.emoji_14;
        if(arg1==15)return R.drawable.emoji_15;
        if(arg1==16)return R.drawable.emoji_16;
        return 0;

    }

    public int getEmojiIdByDate(String date){
        int r=0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        Cursor c = helpter3.query();
        if (c.getCount() != 0) {
            c.moveToFirst();
            c.getString(1);

            for (int i = 1; i <= c.getCount(); i++) {
                if (c.getString(2).toString().equals(date)) {//emoji的日期等于今天的日期
                    r = Integer.valueOf(c.getString(1));
                    break;
                }
                if (!c.isLast()) c.moveToNext();

            }

        }

        c.close();
        return r;

    }

    /**
     *
     * @param names 要显示哪些事件的list,若为null则显示所有事件
     * @param accordingto month week day
     * @param time 日期,若为now则是当天的日期
     * @param YscaleMax Y轴最大值，单位为h或min,若为auto则自动
     * @param columNum Y轴分栏数目，若为0则自动分配
     */
    public void updateStatistics(ArrayList<String> names,String accordingto,String time,String YscaleMax,int columNum){
        //final HashMap<String, Object> map = new HashMap<String, Object>();;

        //ArrayList<String> items_name = new ArrayList<String>();

        ArrayList<String> arg1;


        if (names==null)arg1=null;
        else{
            arg1=new ArrayList<String>();
            for (int i=0;i<names.size();i++)arg1.add(names.get(i));
        }
        ArrayList<Integer> items_num = new ArrayList<Integer>();
        if (arg1==null || arg1.contains("All"))arg1= new ArrayList<String>();

        int maxV=0;

        if (!arg1.isEmpty()) {
            for (int i = 0; i < arg1.size(); i++) {
                items_num.add(0);
            }
        }

        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str_date;
        if (time.equals("now")) {
            str_date    =    formatter.format(curDate);
        }else{
            str_date    =    time;
        }


        Cursor c = helpter.query();
        if (c.getCount() != 0) {
            c.moveToFirst();
            c.getString(1);



            if (arg1.isEmpty()){
                for (int i = 1; i <= c.getCount(); i++) {
                    boolean month=accordingto.equals("month") && c.getString(3).toString().substring(0,7).equals(str_date.substring(0,7));
                    boolean week=accordingto.equals("week") && c.getString(3).toString().substring(0,4).equals(str_date.substring(0,4))
                            && getWeekinDate(c.getString(3).toString())==getWeekinDate(str_date) ;
                    boolean day=accordingto.equals("day") && c.getString(3).toString().equals(str_date);
                    if (month||week||day) {

                        if (arg1.contains(c.getString(1).toString())){
                            int index=arg1.indexOf(c.getString(1).toString());
                            items_num.set(index,items_num.get(index)+
                                    Integer.valueOf(c.getString(2).toString()));
                            if (maxV<items_num.get(index))maxV=items_num.get(index);
                        }else{
                            arg1.add(c.getString(1).toString());
                            items_num.add(Integer.valueOf(c.getString(2).toString()));
                            if (maxV<Integer.valueOf(c.getString(2).toString()))maxV=Integer.valueOf(c.getString(2).toString());
                        }
                        //if (!listItem.contains(map)) listItem.add(map);
                    }
                    if (!c.isLast()) c.moveToNext();

                }
            }else {
                for (int i = 1; i <= c.getCount(); i++) {
                    boolean month=accordingto.equals("month") && c.getString(3).toString().substring(0,7).equals(str_date.substring(0,7));
                    boolean week=accordingto.equals("week") && c.getString(3).toString().substring(0,4).equals(str_date.substring(0,4))
                            && getWeekinDate(c.getString(3).toString())==getWeekinDate(str_date) ;
                    boolean day=accordingto.equals("day") && c.getString(3).toString().equals(str_date);
                    if (month||week||day) {

                        if (arg1.contains(c.getString(1).toString())) {
                            int index = arg1.indexOf(c.getString(1).toString());
                            items_num.set(index, items_num.get(index) +
                                    Integer.valueOf(c.getString(2).toString()));
                            if (maxV < items_num.get(index)) maxV = items_num.get(index);
                        }/*else{
                        items_name.add(c.getString(1).toString());
                        items_num.add(Integer.valueOf(c.getString(2).toString()));
                    }*/
                        //if (!listItem.contains(map)) listItem.add(map);
                    }
                    if (!c.isLast()) c.moveToNext();
                }
            }

        }
        c.close();
        for (int i=0;i<arg1.size();i++){

                if (items_num.get(i) == 0) {
                    arg1.remove(i);
                    items_num.remove(i);
                    i=-1;
                }


        }


        green = (HistogramView) tab04.findViewById(R.id.green);

        green.setValuesAll(arg1,items_num);

        if (maxV<75)maxV=75;
        if (maxV<150 && maxV>100)maxV=150;
        if (maxV<225 && maxV>200)maxV=225;
        if (maxV>1000 ||YscaleMax.contains("h")) {
            for (int i =0;i<items_num.size();i++){
                items_num.set(i,items_num.get(i)/60);
            }
            maxV=maxV/60;


            if (YscaleMax.equals("auto")) {
                green.setUnit("小时");
                if(columNum==0)columNum=5+maxV/25;
                if (maxV<24 && maxV>19)maxV=24;
                green.setMaxValue((maxV / 3 * 4) / 10 * 10, columNum);
            }
            green.setValuesAll(arg1,items_num);
        }else{
            if (YscaleMax.equals("auto")) {
                green.setUnit("分钟");
                if(columNum==0 && maxV>=450)columNum=10;
                if(columNum==0 && maxV<450)columNum=5+maxV/120;

                green.setMaxValue((maxV / 3 * 4) / 100 * 100, columNum);
            }
        }

        if (YscaleMax.contains("min")) {
            green.setUnit("分钟");
            if(columNum==0 && maxV>=450)columNum=10;
            if(columNum==0 && maxV<450)columNum=5+maxV/120;
            green.setMaxValue(Integer.valueOf(YscaleMax.substring(0,YscaleMax.length()-3)),columNum);
        }else if (YscaleMax.contains("h")) {
            green.setUnit("小时");
            if(columNum==0)columNum=5;
            green.setMaxValue(Integer.valueOf(YscaleMax.substring(0,YscaleMax.length()-1)),columNum);
        }



        green.start(2);

    }
    /**
     * 默认
     */
    public void  updateStatistics(){
            if (graphs.get(0).equals("没有可以显示的条目")){
                green.setValuesAll(new String[]{""},new int[]{0});
                green.setMaxValue(100);

            }else
            updateStatistics(ItemsToShow,set1Xscale,dateFormatSimple(graphs.get(0)),setYmax,0);
    }
    /**
     *
     * @param item 要显示的事件,为空则获取数据库中的第一个事件
     * @param timeScale month week day
     * @param time 日期,若为now则是当天的日期
     * @param XcolumNum X轴要显示多少条目，若为0则自动
     * @param YscaleMax Y轴的最大值,单位可以为min或者h，若为auto则自动适应
     * @param columNum //Y轴分栏数目，若为0则自动适应
     */
    public void updateStatisticsAccordingtoItem(String item,String timeScale,String time,int XcolumNum,String YscaleMax, int columNum){
        ArrayList<String> items_name = new ArrayList<String>();
        ArrayList<Integer> items_num = new ArrayList<Integer>();

        if (XcolumNum==0) {
            if (timeScale.equals("day")) XcolumNum = 15;
            if (timeScale.equals("week")) XcolumNum = 15;
            if (timeScale.equals("month")) XcolumNum = 12;
        }
        if (item.equals("")) {
            Cursor c = helpter.query();
            if (c.getCount() != 0) {
                c.moveToFirst();
                item=c.getString(1);
            }
            c.close();
        }



        int maxV=0;


        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str_date;
        if (time.equals("now")) {
            str_date    =    formatter.format(curDate);
        }else{
            str_date    =    time;
        }

        String str_temp=str_date;
        if (timeScale.equals("day")){
            for (int i = 0; i < XcolumNum; i++) {
                items_name.add("");
            }
            for (int i=XcolumNum - 1 ;i >= 0;i--) {
                items_name.set(i,getMonthDay(str_temp));
                str_temp=lastDay(str_temp);
            }
        }else if (timeScale.equals("week")){
            Integer week_temp=getWeekinDate(str_date);
            if (XcolumNum>week_temp)XcolumNum = week_temp;
            for (int i = 0; i < XcolumNum; i++) {
                items_name.add("");
            }
            for (int i=XcolumNum - 1 ;i >= 0;i--) {
                items_name.set(i,String.valueOf(week_temp) +"周");
                week_temp--;
            }

        }else if (timeScale.equals("month")){
            str_temp=str_date.substring(2,7);
            for (int i = 0; i < XcolumNum; i++) {
                items_name.add("");
            }
            for (int i=XcolumNum - 1 ;i >= 0;i--) {
                items_name.set(i,str_temp);
                str_temp=lastMonth(str_temp);
            }
        }

        for (int i = 0; i < XcolumNum; i++) {
            items_num.add(0);
        }

        Cursor c = helpter.query();
        if (c.getCount() != 0) {
            c.moveToFirst();
            c.getString(1);
            for (int i = 1; i <= c.getCount(); i++) {
                boolean item_right = c.getString(1).equals(item);
                boolean month=timeScale.equals("month") && items_name.contains(c.getString(3).toString().substring(2,7));
                boolean week=timeScale.equals("week") && getYear(c.getString(3).toString()).equals(getYear(str_date))
                            && items_name.contains(String.valueOf(getWeekinDate(c.getString(3).toString()))+"周") ;
                boolean day = timeScale.equals("day") && items_name.contains(getMonthDay(c.getString(3).toString()));
                if (item_right) {
                    if (month) {
                        int index = items_name.indexOf(c.getString(3).toString().substring(2,7));
                        items_num.set(index, items_num.get(index) +
                                Integer.valueOf(c.getString(2).toString()));
                        if (maxV < items_num.get(index)) maxV = items_num.get(index);
                    }
                    if (week) {
                        int index = items_name.indexOf(String.valueOf(getWeekinDate(c.getString(3).toString()))+"周");
                        items_num.set(index, items_num.get(index) +
                                Integer.valueOf(c.getString(2).toString()));
                        if (maxV < items_num.get(index)) maxV = items_num.get(index);
                    }
                    if (day) {
                        int index = items_name.indexOf(getMonthDay(c.getString(3).toString()));
                        items_num.set(index, items_num.get(index) +
                                Integer.valueOf(c.getString(2).toString()));
                        if (maxV < items_num.get(index)) maxV = items_num.get(index);
                    }
                }

                if (!c.isLast()) c.moveToNext();

            }


        }
        c.close();
//没有的不现显示
//        for (int i=0;i<items_name.size();i++){
//
//            if (items_num.get(i) == 0) {
//                items_name.remove(i);
//                items_num.remove(i);
//                i=-1;
//            }
//
//
//        }

        green = (HistogramView) tab04.findViewById(R.id.green);

        green.setValuesAll(items_name,items_num);

        if (maxV<75)maxV=75;
        if (maxV<150 && maxV>100)maxV=150;
        if (maxV<225 && maxV>200)maxV=225;
        if (maxV>1000 ||YscaleMax.contains("h")) {
            for (int i =0;i<items_num.size();i++){
                items_num.set(i,items_num.get(i)/60);
            }
            maxV=maxV/60;


            if (YscaleMax.equals("auto")) {
                green.setUnit("小时");
                if(columNum==0)columNum=5+maxV/25;
                if (maxV<24 && maxV>19)maxV=24;
                green.setMaxValue((maxV / 3 * 4) / 10 * 10, columNum);
            }
            green.setValuesAll(items_name,items_num);
        }else{
            if (YscaleMax.equals("auto")) {
                green.setUnit("分钟");
                if(columNum==0 && maxV>=450)columNum=10;
                if(columNum==0 && maxV<450)columNum=5+maxV/120;

                green.setMaxValue((maxV / 3 * 4) / 100 * 100, columNum);
            }
        }

        if (YscaleMax.contains("min")) {
            green.setUnit("分钟");
            if(columNum==0 && maxV>=450)columNum=10;
            if(columNum==0 && maxV<450)columNum=5+maxV/120;
            green.setMaxValue(Integer.valueOf(YscaleMax.substring(0,YscaleMax.length()-3)),columNum);
        }else if (YscaleMax.contains("h")) {
            green.setUnit("小时");
            if(columNum==0)columNum=5;
            green.setMaxValue(Integer.valueOf(YscaleMax.substring(0,YscaleMax.length()-1)),columNum);
        }

        green.start(2);
    }
    /**
     * 默认
     */
    public void updateStatisticsAccordingtoItem(){
        updateStatisticsAccordingtoItem("",set2Xscale,set2timeset,set2Xmax,setYmax,0);
    }


}



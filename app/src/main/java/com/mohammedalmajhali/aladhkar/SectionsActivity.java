package com.mohammedalmajhali.aladhkar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;


public class SectionsActivity extends AppCompatActivity implements list_adhkar_fragment.onDhekrClicked {


    // معرف للازرار عشان ارسله الى الفراقمنت وهو هناك يعرف ايش البيانات اللي يجيب
    final static int
            NUM_btn_generic = 8,
            NUM_btn_mohammed_is_messenger_of_allah = 7,
            NUM_btn_god_you_are_my_Lord = 6,
            NUM_btn_threes = 5,
            NUM_btn_no_god_except_Allah = 4,
            NUM_btn_quran = 3,
            NUM_btn_evening_morning = 2,
            NUM_btn_istiaah_and_basmalah = 1;

    public static int sumNumAdhkar;
    static int[] counterClicked;

    TextView btn_threes,
            btn_generic,
            btn_mohammed_is_messenger_of_allah,
            btn_god_you_are_my_Lord,
            btn_no_god_except_Allah,
            btn_quran,
            btn_evening_morning,
            btn_istiaah_and_basmalah;


    TextView counter_threes,
            counter_generic,
            counter_mohammed_is_messenger_of_allah,
            counter_god_you_are_my_Lord,
            counter_no_god_except_Allah,
            counter_quran,
            counter_evening_morning,
            counter_istiaah_and_basmalah;

    ImageView iv_iconTop, btn_back;
    TextView tv_title;


    ProgressBar progressBar;

    // ايش الزر اللي ضغطه المستخدم لما جاء من الاكتفتي الرئيسية
    // باتكون قيمته واحده من هولا القيم EVENING = 1, MORNING = 0
    int time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);

        // انفلات للعناصر
        btn_threes = findViewById(R.id.section_btn_threes);
        btn_generic = findViewById(R.id.section_btn_generic);
        btn_mohammed_is_messenger_of_allah = findViewById(R.id.section_btn_mohammed_is_messenger_of_allah);
        btn_god_you_are_my_Lord = findViewById(R.id.section_btn_god_you_are_my_Lord);
        btn_no_god_except_Allah = findViewById(R.id.section_btn_no_god_except_Allah);
        btn_quran = findViewById(R.id.section_btn_quran);
        btn_evening_morning = findViewById(R.id.section_btn_evening_morning);
        btn_istiaah_and_basmalah = findViewById(R.id.section_btn_istiaah_and_basmalah);
        iv_iconTop = findViewById(R.id.section_iv_icon_evening_or_morning);
        tv_title = findViewById(R.id.section_mainTextView_evening_or_morning);
        btn_back = findViewById(R.id.section_btn_back);


        counter_threes = findViewById(R.id.section_counter_threes);
        counter_generic = findViewById(R.id.section_counter_generic);
        counter_mohammed_is_messenger_of_allah = findViewById(R.id.section_counter_mohammed_is_messenger_of_allah);
        counter_god_you_are_my_Lord = findViewById(R.id.section_counter_god_you_are_my_Lord);
        counter_no_god_except_Allah = findViewById(R.id.section_counter_no_god_except_Allah);
        counter_quran = findViewById(R.id.section_counter_quran);
        counter_evening_morning = findViewById(R.id.section_counter_evening_morning);
        counter_istiaah_and_basmalah = findViewById(R.id.section_counter_istiaah_and_basmalah);


        progressBar = findViewById(R.id.progress_adhkar);


        // نعرف القيمه اما MORNING = 0 او EVENING = 1
        Intent intent = getIntent();
        time = intent.getIntExtra("time", 0);


        counterClicked = new int[31];
        // Arrays.fill(counterClicked,0);

        // نحط الماكس حق البروقرس
        // نحط الايقونه والعنوان
        if (time == MainActivity.EVENING) {
            sumNumAdhkar = 184;
            iv_iconTop.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.evening_moon));
            tv_title.setText(R.string.evening);
        } else {
            sumNumAdhkar = 183;
            iv_iconTop.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.morning_sun));
            tv_title.setText(R.string.morning);
        }
        progressBar.setMax(sumNumAdhkar);


        // نحط القيم في الدوائر الحمر
         /*
        * عدد الاذكار في كل قسم
        * counter_threes -> 15
        *generic = 118
        myloard 2
        exept 6
        quran 11, 10
        eveningMorning 9
            istiaah 13
            * mohammed 10
        * */
        counter_threes.setText("15");
        counter_generic.setText("118");
        counter_mohammed_is_messenger_of_allah.setText("10");
        counter_god_you_are_my_Lord.setText("2");
        counter_no_god_except_Allah.setText("6");
        if (time == MainActivity.EVENING)
            counter_quran.setText("11");
        else
            counter_quran.setText("10");
        counter_evening_morning.setText("9");
        counter_istiaah_and_basmalah.setText("13");


        // نستدعي الداله التي تفتح الفراقمنت ونرسل لها ايش الزر اللي اضتغط الان
        btn_threes.setOnClickListener(v -> open_adkar_fragment(NUM_btn_threes));
        btn_generic.setOnClickListener(v -> open_adkar_fragment(NUM_btn_generic));
        btn_mohammed_is_messenger_of_allah.setOnClickListener(v -> open_adkar_fragment(NUM_btn_mohammed_is_messenger_of_allah));
        btn_god_you_are_my_Lord.setOnClickListener(v -> open_adkar_fragment(NUM_btn_god_you_are_my_Lord));
        btn_no_god_except_Allah.setOnClickListener(v -> open_adkar_fragment(NUM_btn_no_god_except_Allah));
        btn_quran.setOnClickListener(v -> open_adkar_fragment(NUM_btn_quran));
        btn_evening_morning.setOnClickListener(v -> open_adkar_fragment(NUM_btn_evening_morning));
        btn_istiaah_and_basmalah.setOnClickListener(v -> open_adkar_fragment(NUM_btn_istiaah_and_basmalah));

        btn_back.setOnClickListener(v -> this.finish());

    }

    

    void open_adkar_fragment(int from) {
        //  MORNING = 0 او EVENING = 1 نفتح الفراقمنت ونرسل لها الزر الذي تم الضغط عليه والوقت
        list_adhkar_fragment f = list_adhkar_fragment.newInstance(String.valueOf(from), String.valueOf(time));
        f.show(this.getSupportFragmentManager(), null);
    }


    // هذه الداله اللي سوينا لها implementation
    // وعملها تستدعي الداله اللي تجمع لي عدد الضغطات كامل الى حد الان وتضيفه الى البروقرس
    @Override
    public void onDhekrClick() {
        progressBar.setProgress(sumArray(counterClicked));

        int sum = 0;


        sum = 13 - sumOfThisSection(0, 4);

        if (sum == 0) {
            counter_istiaah_and_basmalah.setText("");
            counter_istiaah_and_basmalah.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));

        } else {
            // sum into counter_circle
            counter_istiaah_and_basmalah.setText(String.valueOf(sum));
        }


        sum = 0;
        sum = 9 - sumOfThisSection(5, 10);
        if (sum == 0) {
            //btn_evening_morning.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_blue_true));
            counter_evening_morning.setText("");
            counter_evening_morning.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));
        } else {
            counter_evening_morning.setText(String.valueOf(sum));
        }


        sum = 0;
        if (time == MainActivity.MORNING) {
            sum = 10 - sumOfThisSection(11, 16);
        } else {
            sum = 11 - sumOfThisSection(11, 16);
        }

        if (sum == 0) {
            //btn_quran.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_morning_true));
            counter_quran.setText("");
            counter_quran.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));
        } else {
            counter_quran.setText(String.valueOf(sum));

        }


        sum = 0;
        sum = 6 - sumOfThisSection(17, 18);
        if (sum == 0) {
            //btn_no_god_except_Allah.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_morning_true));
            counter_no_god_except_Allah.setText("");
            counter_no_god_except_Allah.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));
        } else {
            counter_no_god_except_Allah.setText(String.valueOf(sum));

        }


        sum = 0;
        sum = 15 - sumOfThisSection(19, 23);
        if (sum == 0) {
            //btn_threes.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_blue_true));
            counter_threes.setText("");
            counter_threes.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));
        } else {
            counter_threes.setText(String.valueOf(sum));
        }

        sum = 0;
        sum = 2 - sumOfThisSection(24, 25);
        if (sum == 0) {
            //btn_god_you_are_my_Lord.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_evening_true));
            counter_god_you_are_my_Lord.setText("");
            counter_god_you_are_my_Lord.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));
        } else {
            counter_god_you_are_my_Lord.setText(String.valueOf(sum));

        }


        sum = 0;
        sum = 10 - sumOfThisSection(26, 26);
        if (sum == 0) {
            //btn_mohammed_is_messenger_of_allah.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_true));
            counter_mohammed_is_messenger_of_allah.setText("");
            counter_mohammed_is_messenger_of_allah.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));
        } else {
            counter_mohammed_is_messenger_of_allah.setText(String.valueOf(sum));

        }

        sum = 0;
        sum = 118 - sumOfThisSection(27, 30);
        if (sum == 0) {
            //btn_generic.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_morning_true));
            counter_generic.setText("");
            counter_generic.setBackground(AppCompatResources.getDrawable(this, R.drawable.check_compelete));
        } else {
            counter_generic.setText(String.valueOf(sum));

        }

    }

    public int sumArray(int[] arr) {
        int sum = 0;
        for (int j : arr) {
            sum += j;
        }
        return sum;
    }

    public int sumOfThisSection(int begin, int end) {


        // هل خلصت الجزء هذا او لا
        // نحسب مجموع الضغطات حق كل العناصر من الموقع كذا الى الموقع كذا
        // معي رقم 12 في الاذكار موجود في اذكار المساء وليس في اذكار الصباح لذلك اذا كنا في الصباح ننقص المجموع الكلي واحد ونمشي
        int actualSum = 0;
        for (int i = begin; i <= end; i++) {
            if (time == MainActivity.MORNING && i == 12) {
                continue;
            }
            actualSum += counterClicked[i];
        }

        return actualSum;
    }


}
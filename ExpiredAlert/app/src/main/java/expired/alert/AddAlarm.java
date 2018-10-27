package expired.alert;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;


public class AddAlarm extends AppCompatActivity {

    SharedPreferences sp = getSharedPreferences("ALARM_DATA", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    String repeatNo;

    TextView txtDate , txtType , txtTime, txtRepeat;
    int checkDate = 0, checkType = 0, checkTime = 0, checkRepeat = 0;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_items_add);

        txtDate = (TextView) findViewById(R.id.dateOption);
        txtType = (TextView) findViewById(R.id.typeOption);
        txtTime = (TextView) findViewById(R.id.timeOption);
        txtRepeat = (TextView) findViewById(R.id.roundOption);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.addAlarm:
                checkInformation();
                showNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setDate(View v){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(AddAlarm.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        txtDate.setText(day + "/" + (month + 1) + "/" + year);
                        checkDate = 1;
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public void selectRepeatType(View v){
        final String[] items = new String[4];

        items[0] = "Food";
        items[1] = "Drink";
        items[2] = "Medicine";
        items[3] = "ETC";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
               txtType.setText(items[item]);
               checkType = 1;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setTime(View v){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    txtTime.setText( selectedHour + ":" + selectedMinute);
                    checkTime = 1;
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


    }

    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            repeatNo = Integer.toString(1);
                            txtRepeat.setText(repeatNo);


                        }
                        else {
                            repeatNo = input.getText().toString().trim();
                            txtRepeat.setText(repeatNo);
                        }
                        checkRepeat = 1;
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    private void showNotification(){
        Notification notification =
                new NotificationCompat.Builder(this) // this is context
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("DevAhoy News")
                        .setContentText("สวัสดีครับ ยินดีต้อนรับเข้าสู่บทความ Android Notification :)")
                        .setAutoCancel(true)
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);
    }

    public void checkInformation(){
        if (checkDate+checkRepeat+checkTime+checkType == 4){
            resetAttribute();
            setAlarm();
//            Accept
        }else{
            Toast errorToast = Toast.makeText(getApplicationContext(), "กรุณาใส่ข้อมูลให้ครบ", Toast.LENGTH_SHORT);
            errorToast.show();

        }
    }

    public void resetAttribute(){
        checkType = 0;
        checkTime = 0;
        checkRepeat = 0;
        checkDate = 0;
    }

    public void setAlarm(){

        int size = checkAlarmId();

        editor.putInt("alarm_id",size);
        editor.putString("alram_type",txtType.toString());
        editor.putString("alarm_date",txtDate.toString());
        editor.putString("alarm_time",txtTime.toString());
        editor.putString("alarm_repeat",txtRepeat.toString());
        editor.commit();

    }

    public int checkAlarmId(){
        int size = sp.getAll().size();

        for (int i = 0 ; i < size ; i++){
            if (sp.getInt("alarm_id",i) != i){
                return i;
            }
        }

        return size;
    }

}

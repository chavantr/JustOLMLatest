package com.mywings.justolm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.mywings.justolm.Model.InitOrderRequest;
import com.mywings.justolm.Model.Items;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.InitOrder;
import com.mywings.justolm.Process.OnInitOrderListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrder extends JustOlmCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnInitOrderListener {


    //region UI Controls
    private static int index = -1;
    private static int indexCount = 0;
    private final Calendar calendar = Calendar.getInstance();
    private final Calendar c = Calendar.getInstance();
    private LinearLayout lnrItems;
    private AppCompatTextView btnAddPrescription;
    private AppCompatImageView imgErase;
    private AppCompatTextView lblDate;
    private AppCompatTextView lblTime;
    //endregion
    private AppCompatCheckBox prescribed;
    private AppCompatTextView btnCancel;
    private AppCompatTextView btnConfirm;
    private AppCompatTextView btnOrder;
    //region Variables
    private DrawerLayout drawer;
    private Dialog dialog;
    private Map<Integer, View> ui;
    private SimpleDateFormat format;
    private DateFormat timeFormat;
    private String strDate;
    private String strTime;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iniitialization(toolbar);
        events();
        format = new SimpleDateFormat("dd-MM-yyyy");

        lblDate.setText("Order Date:\n" + format.format(new Date(calendar.getTimeInMillis())).replace("-", "/"));
        strDate = format.format(new Date(calendar.getTimeInMillis())).replace("-", "/");
        timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        lblTime.setText(getString(R.string.prefer_time_to_accept_delivery) + "\n" + timeFormat.format(new Date(calendar.getTimeInMillis())));
        strTime = timeFormat.format(new Date(calendar.getTimeInMillis()));
    }

    /**
     *
     */
    private void updateCount() {
        if (null != lnrItems) {
            if (lnrItems.getChildCount() > 1) {
                for (int i = 0; i < lnrItems.getChildCount(); i++) {
                    View view = lnrItems.getChildAt(i);
                    AppCompatTextView lblIndex = (AppCompatTextView) view.findViewById(R.id.lblIndex);
                    lblIndex.setText(String.valueOf(i + 1));
                    lnrItems.invalidate();
                }
            }
        }
    }


    private boolean isNotUnique() {
        if (null != lnrItems) {
            if (lnrItems.getChildCount() > 1) {
                for (int i = 0; i < lnrItems.getChildCount() - 1; i++) {
                    View view = lnrItems.getChildAt(i);
                    AppCompatEditText txtName = (AppCompatEditText) view.findViewById(R.id.txtName);
                    if (((AppCompatEditText) lnrItems.getChildAt(lnrItems.getChildCount() - 1).findViewById(R.id.txtName)).getText().toString().equalsIgnoreCase(txtName.getText().toString().trim())) {
                        return false;
                    }
                }
            }
        } else if (lnrItems.getChildCount() == 1) {
            return true;
        } else {
            return true;
        }
        return true;
    }

    private void events() {
        btnAddPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index > -1) {
                    View validateView = ui.get(index);
                    if (null != validateView) {
                        AppCompatEditText txtName = (AppCompatEditText) validateView.findViewById(R.id.txtName);
                        AppCompatEditText txtQty = (AppCompatEditText) validateView.findViewById(R.id.txtQty);
                        if (txtName.getText().toString().isEmpty()) {
                            show("Please enter item description.", v);
                        } else if (txtQty.getText().toString().isEmpty()) {
                            show("Please enter qty for item.", v);
                        } else if (!txtQty.getText().toString().isEmpty() && Integer.parseInt(txtQty.getText().toString().trim()) > 16) {
                            show(getString(R.string.lbl_more_than_16_qty), v);
                        } else if (!isNotUnique()) {
                            show("Prescription name should not double", v);
                        } else {
                            txtName.clearFocus();
                            txtQty.clearFocus();
                            indexCount = indexCount + 1;
                            View view = generate();
                            lnrItems.addView(view);
                            ui.put((int) view.getTag(), view);
                        }
                    } else {
                        indexCount = indexCount + 1;
                        View view = generate();
                        lnrItems.addView(view);
                        ui.put((int) view.getTag(), view);
                    }
                } else {
                    indexCount = indexCount + 1;
                    View view = generate();
                    lnrItems.addView(view);
                    ui.put((int) view.getTag(), view);
                }
            }
        });


        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        lblTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = cancelOrder();
                dialog.show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = prescribedItemConfirm();
                dialog.show();
            }
        });

    }

    private void showTimePicker() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "dtimePicker");
    }

    /**
     *
     */
    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void iniitialization(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (Boolean.parseBoolean(justOLMShared.getStringValue("isadmin"))) {
            if (null != navigationView) {
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_just_olm_admin_drawer);
            }
        }
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        lnrItems = (LinearLayout) findViewById(R.id.orders);
        btnAddPrescription = (AppCompatTextView) findViewById(R.id.btnAddPrescription);
        btnCancel = (AppCompatTextView) findViewById(R.id.btnCancel);
        btnConfirm = (AppCompatTextView) findViewById(R.id.btnConfirm);
        btnOrder = (AppCompatTextView) findViewById(R.id.btnOrder);
        lblDate = (AppCompatTextView) findViewById(R.id.lblDate);
        lblTime = (AppCompatTextView) findViewById(R.id.lblTime);
        prescribed = (AppCompatCheckBox) findViewById(R.id.rdbPrescribed);
        ui = new HashMap<Integer, View>();
    }


    private View generate() {
        View view = null;
        view = LayoutInflater.from(this).inflate(R.layout.newitem, null);
        index = index + 1;
        view.setTag(index);
        imgErase = (AppCompatImageView) view.findViewById(R.id.imgErase);
        AppCompatTextView lblIndexCount = (AppCompatTextView) view.findViewById(R.id.lblIndex);
        lblIndexCount.setText("" + indexCount);
        imgErase.setTag(index);
        imgErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrItems.removeView(ui.get(v.getTag()));
                indexCount = indexCount - 1;
                if (lnrItems.getChildCount() == 0) {
                    index = -1;
                    indexCount = 0;
                    ui = null;
                    ui = new HashMap<Integer, View>();
                }
                updateCount();
                lnrItems.invalidate();
            }
        });
        updateCount();
        return view;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startHomeScreen();
                break;
            case R.id.profile:
                startProfile();
                break;
            case R.id.abountus:
                startAboutUs();
                break;
            case R.id.pendingorder:
                startpendingscreen();
                break;
            case R.id.userorder:
                userorder();
                break;
            case R.id.contactus:
                startContactUs();
                break;
            case R.id.amendorder:
                startamendorder();
                break;
            case R.id.amendschedulerorder:
                startamendscheduler();
                break;
            case R.id.changepassword:
                startchangepassword();
                break;
            case R.id.logout:
                dialog = logout();
                dialog.show();
                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startpendingscreen() {
        Intent intent = new Intent(NewOrder.this, PendingOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void userorder() {
        Intent intent = new Intent(NewOrder.this, MyOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startamendscheduler() {
        Intent intent = new Intent(NewOrder.this, AmendScheduler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startamendorder() {
        Intent intent = new Intent(NewOrder.this, AmendOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startHomeScreen() {
        Intent intent = new Intent(NewOrder.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startContactUs() {
        Intent intent = new Intent(NewOrder.this, ContactUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startAboutUs() {
        Intent intent = new Intent(NewOrder.this, AboutUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startProfile() {
        Intent intent = new Intent(NewOrder.this, MyProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startchangepassword() {
        Intent intent = new Intent(NewOrder.this, ChangePassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    /**
     *
     */
    public Dialog prescribedItemConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.lbl_confirm_prescribed));
        builder.setMessage(getString(R.string.lbl_confirm_user));
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //btnAddPrescription.setEnabled(false);
                //lnrItems.setEnabled(false);
                initOrder(generate(ui));
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }

    private InitOrderRequest generate(Map<Integer, View> ui) {
        InitOrderRequest request = InitOrderRequest.getInstance();
        request.setOrderDate(strDate);
        request.setOrderTime(strTime);
        request.setOrderType("2");
        request.setUserId(String.valueOf(justOLMShared.getIntegerValue("userId")));

        if (null != ui && ui.size() > 0) {
            List<Items> items = new ArrayList<>();
            for (int i = 0; i < ui.size(); i++) {
                Items item = new Items();
                View view = ui.get(i);
                AppCompatEditText txtName = (AppCompatEditText) view.findViewById(R.id.txtName);
                AppCompatEditText txtQty = (AppCompatEditText) view.findViewById(R.id.txtQty);
                AppCompatSpinner spnSchedule = (AppCompatSpinner) view.findViewById(R.id.spnScheduler);
                item.setPeriod("1");
                item.setSchedular(spnSchedule.getSelectedItem().toString());
                item.setItemPhoto("");
                item.setQuantity(txtQty.getText().toString());
                item.setItemName(txtName.getText().toString());
                items.add(item);
            }
            request.setItems(items);
        } else {
            show("Empty prescription not allowed", btnAddPrescription);
        }
        return request;
    }

    /**
     *
     */
    public Dialog cancelOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Cancel Order");
        builder.setMessage("Do you really want to cancel your order?");
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                btnAddPrescription.setEnabled(true);
                lnrItems.setEnabled(true);
                if (lnrItems.getChildCount() > 0) {
                    lnrItems.removeAllViews();
                    lnrItems.invalidate();
                    index = -1;
                    indexCount = 0;
                    ui = null;
                    ui = new HashMap<Integer, View>();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }

    private void initOrder(InitOrderRequest request) {
        if (isConnected()) {
            if (ui != null && ui.size() > 0) {
                show();
                InitOrder initOrder = initHelper.initOrder(serviceFunctions);
                initOrder.setOnInitOrderListener(this, request);
            }
        }
    }

    @Override
    public void onInitOrderComplete(UserMessage result, Exception exception) {
        hide();
        show(result.getMessage(), btnAddPrescription);
    }

    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        Calendar c = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeCalendar.set(Calendar.MINUTE, minute);
            strTime = timeFormat.format(new Date(timeCalendar.getTimeInMillis()));
            lblTime.setText(getString(R.string.prefer_time_to_accept_delivery) + "\n" + timeFormat.format(new Date(timeCalendar.getTimeInMillis())));
        }
    }

    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            strDate = dateTimeUtils.update(year, month, day);

            lblDate.setText("Order Date:\n" + dateTimeUtils.update(year, month, day));
        }

    }
}

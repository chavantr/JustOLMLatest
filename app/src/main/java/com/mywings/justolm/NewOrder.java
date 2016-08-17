package com.mywings.justolm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.mywings.justolm.Model.InitOrderRequest;
import com.mywings.justolm.Model.Items;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.InitOrder;
import com.mywings.justolm.Process.OnInitOrderListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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


    private static final int CAMETA = 101;
    private static final int GALLERY = 102;
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
    private int selectedPrescribled;


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
            if (lnrItems.getChildCount() >= 1) {
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

        if (prescribed.isChecked()) return true;

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
                        AppCompatEditText txtName = null;
                        AppCompatTextView btnImage = null;
                        if (prescribed.isChecked()) {
                            btnImage = (AppCompatTextView) validateView.findViewById(R.id.btnImage);
                        } else {
                            txtName = (AppCompatEditText) validateView.findViewById(R.id.txtName);
                        }
                        AppCompatEditText txtQty = (AppCompatEditText) validateView.findViewById(R.id.txtQty);
                        if (!prescribed.isChecked() && null != txtName && txtName.getText().toString().isEmpty()) {
                            // if (txtName.getText().toString().isEmpty()) {
                            show("Please enter item description.", v);
                            // }
                        } else if (prescribed.isChecked() && null != btnImage && !btnImage.getText().toString().isEmpty()) {
                            //if (!btnImage.getText().toString().isEmpty()) {
                            show("Please select item prescription.", v);
                            // }
                        } else if (txtQty.getText().toString().isEmpty()) {
                            show("Please enter qty for item.", v);
                        } else if (!txtQty.getText().toString().isEmpty() && Integer.parseInt(txtQty.getText().toString().trim()) > 16) {
                            show(getString(R.string.lbl_more_than_16_qty), v);
                        } else if (!isNotUnique()) {
                            show("Prescription name should not double", v);
                        } else {
                            if (!prescribed.isChecked()) {
                                txtName.clearFocus();
                            }
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


        prescribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != lnrItems) {
                    if (lnrItems.getChildCount() > 0) {
                        Dialog dialog = prescribedChange(prescribed.isChecked());
                        dialog.show();
                    }
                }
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


    private Intent getImagePickerIntent() {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile))


        intentList = addIntenttoList(intentList, pickIntent);

        intentList = addIntenttoList(intentList, takePhotoIntent);

        if (intentList.size() > 0) {

            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "Select");


            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));

        }

        return chooserIntent;
    }


    private List<Intent> addIntenttoList(List<Intent> list, Intent intent) {

        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfos) {

            String packageName = resolveInfo.activityInfo.packageName;

            Intent targetIntent = new Intent(intent);
            targetIntent.setPackage(packageName);

            list.add(targetIntent);
        }
        return list;
    }


    private void startCamera() {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

//        takePicture.putExtra(MediaStore.EXTRA_OUTPUT,getImageUrl());

        startActivityForResult(takePicture, CAMETA);
    }


    private Uri getImageUrl() {
        Uri imgUrl = null;

        //File file = new File("file:///sdcard/image.jpg");

        imgUrl = Uri.parse("file:///sdcard/image.jpg");

        return imgUrl;

    }

    private void startGallery() {
        Intent takePicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(takePicture, GALLERY);
    }

    /**
     *
     */
    private Dialog image() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select");
        LayoutInflater inflater = getLayoutInflater();
        View popup = inflater.inflate(R.layout.pick_camera, null);
        Button btnCamera = (Button) popup.findViewById(R.id.btnCamera);
        Button btnGallery = (Button) popup.findViewById(R.id.btnGallery);
        Button btnCancel = (Button) popup.findViewById(R.id.btnCancel);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startCamera();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGallery();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        builder.setView(popup);
        builder.setCancelable(false);
        return builder.create();
    }


    private View generate() {
        View view = null;
        if (prescribed.isChecked()) {
            view = LayoutInflater.from(this).inflate(R.layout.newitem_prescribe, null);

            final View finalView = view;
            ((AppCompatTextView) view.findViewById(R.id.btnImage)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //selectedPrescribled = (int) finalView.getTag();
                    selectedPrescribled = lnrItems.indexOfChild(finalView);
                    dialog = image();
                    dialog.show();


                }
            });

        } else {
            view = LayoutInflater.from(this).inflate(R.layout.newitem, null);
        }
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

                index = index - 1;

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


    /**
     *
     */
    public Dialog prescribedChange(final boolean is) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.prescribeditemchange));
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                index = -1;
                indexCount = 0;
                ui = null;
                ui = new HashMap<Integer, View>();
                lnrItems.removeAllViews();
                lnrItems.invalidate();

            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                prescribed.setChecked(!is);
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
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
                AppCompatEditText txtName = null;
                AppCompatTextView btnImage = null;
                if (prescribed.isChecked()) {
                    btnImage = (AppCompatTextView) view.findViewById(R.id.btnImage);
                } else {
                    txtName = (AppCompatEditText) view.findViewById(R.id.txtName);
                }
                AppCompatEditText txtQty = (AppCompatEditText) view.findViewById(R.id.txtQty);
                AppCompatSpinner spnSchedule = (AppCompatSpinner) view.findViewById(R.id.spnScheduler);
                item.setPeriod("1");
                item.setSchedular(spnSchedule.getSelectedItem().toString());
                if (prescribed.isChecked()) {
                    BitmapDrawable imageDrawable = (BitmapDrawable) btnImage.getBackground();
                    Bitmap imageto64 = imageDrawable.getBitmap();
                    item.setItemPhoto(encodeToBase64(imageto64, Bitmap.CompressFormat.JPEG, 100));
                    item.setItemName("");
                } else {
                    item.setItemPhoto("");
                    item.setItemName(txtName.getText().toString());
                }
                item.setQuantity(txtQty.getText().toString());

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

    /**
     * @param request
     */
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY) {
                Uri uri = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    AppCompatTextView lblImage = (AppCompatTextView) lnrItems.getChildAt(selectedPrescribled).findViewById(R.id.btnImage);
                    lblImage.setBackground(new BitmapDrawable(getResources(), image));
                    lblImage.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMETA) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                AppCompatTextView lblImage = (AppCompatTextView) lnrItems.getChildAt(selectedPrescribled).findViewById(R.id.btnImage);
                lblImage.setBackground(new BitmapDrawable(getResources(), image));
                lblImage.setText("");
            }
        }

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

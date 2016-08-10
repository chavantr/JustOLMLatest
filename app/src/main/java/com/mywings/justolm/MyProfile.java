package com.mywings.justolm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.mywings.justolm.Model.Area;
import com.mywings.justolm.Model.City;
import com.mywings.justolm.Model.Country;
import com.mywings.justolm.Model.LoginResponse;
import com.mywings.justolm.Model.State;
import com.mywings.justolm.Model.UserInfo;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.GetAreas;
import com.mywings.justolm.Process.GetCity;
import com.mywings.justolm.Process.GetCountries;
import com.mywings.justolm.Process.GetProfile;
import com.mywings.justolm.Process.GetStates;
import com.mywings.justolm.Process.OnAreaListener;
import com.mywings.justolm.Process.OnCityListener;
import com.mywings.justolm.Process.OnCountryListener;
import com.mywings.justolm.Process.OnGetProfileListener;
import com.mywings.justolm.Process.OnStateListener;
import com.mywings.justolm.Process.OnUpdateProfileListener;
import com.mywings.justolm.Process.UpdateUser;
import com.mywings.justolm.Widgets.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyProfile extends JustOlmCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnUpdateProfileListener, OnCountryListener, OnStateListener, OnCityListener, OnAreaListener, OnGetProfileListener {


    //region Variables
    final Calendar c = Calendar.getInstance();
    final com.mywings.justolm.Model.Registration registration = com.mywings.justolm.Model.Registration.getInstance();
    //region UI Controls
    private AppCompatEditText txtFirstName;
    private AppCompatEditText txtMiddleName;
    private AppCompatEditText txtLastName;
    private AppCompatEditText txtDateOfBirth;
    private AppCompatEditText txtProfession;
    private AppCompatEditText txtAddress;
    private SearchableSpinner txtCountry;
    private SearchableSpinner txtState;
    private SearchableSpinner txtCity;
    private SearchableSpinner txtArea;
    private AppCompatEditText txtPinCode;
    private AppCompatEditText txtEmail;
    private AppCompatEditText txtMobileNumber;
    private Button btnUpdateProfile;
    private Button btnEdit;
    private DrawerLayout drawer;
    private Dialog dialog;
    //endregion

    //region UI
    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private UserInfo userInfo = UserInfo.getInstance();
    private List<AppCompatEditText> inputList;
    private List<AppCompatSpinner> selectList;
    private LoginResponse loginResponse = LoginResponse.getInstance();
    private List<Country> countries;
    private List<String> strCountries;
    private boolean isCountryClickable = false;
    private List<State> states;
    private List<String> strStates;
    private List<City> cities;
    private List<String> strCities;
    private List<Area> areas;
    private List<String> strAreas;
    private int selectedCountryId;
    private int selectedStateId;
    private int selectedCityId;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        initialization();

        events();

        disableInput();

        registration.setId(userInfo.getId());

        countries = new ArrayList<>();
        Country countryNode = new Country();
        countryNode.setId(Integer.parseInt(justOLMShared.getStringValue("country")));
        countryNode.setName(justOLMShared.getStringValue("countryName"));
        countries.add(countryNode);
        strCountries = new ArrayList<>();
        for (Country country : countries) {
            strCountries.add(country.getName());
        }
        setCountries();

        states = new ArrayList<>();

        State stateNode = State.getInstance();
        stateNode.setCountryId(Integer.parseInt(justOLMShared.getStringValue("country")));
        stateNode.setId(Integer.parseInt(justOLMShared.getStringValue("state")));
        stateNode.setName(justOLMShared.getStringValue("stateName"));
        states.add(stateNode);
        strStates = new ArrayList<>();

        for (State state : states) {
            strStates.add(state.getName());
        }
        setStates();

        cities = new ArrayList<>();
        City cityNode = City.getInstance();
        cityNode.setStateId(Integer.parseInt(justOLMShared.getStringValue("state")));
        cityNode.setId(Integer.parseInt(justOLMShared.getStringValue("city")));
        cityNode.setName(justOLMShared.getStringValue("cityName"));
        cities.add(cityNode);

        strCities = new ArrayList<>();

        for (City city : cities) {
            strCities.add(city.getName());
        }

        setCity();

        areas = new ArrayList<>();

        Area areaNode = Area.getInstance();
        areaNode.setCityId(Integer.parseInt(justOLMShared.getStringValue("city")));
        areaNode.setName(justOLMShared.getStringValue("areaName"));
        areaNode.setId(Integer.parseInt(justOLMShared.getStringValue("city")));
        areas.add(areaNode);

        strAreas = new ArrayList<>();

        for (Area area : areas) {
            strAreas.add(area.getName());
        }


        setArea();


        countries = justOLMHelper.getCountries();

        if (countries.size() > 0) {
            strCountries = new ArrayList<>();
            for (Country country : countries) {
                strCountries.add(country.getName());
            }
            setCountries();
            isCountryClickable = true;
        } else {
            initCountries();
        }

        if (isConnected()) {
            initGetProfile();
        }
    }

    private void events() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnEdit.getText().toString().trim().equalsIgnoreCase("Edit")) {
                    cancel();
                } else {
                    edit();
                }
            }
        });

        txtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRegistration();
            }
        });


        txtCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int ids = 0;
                for (int i = 0; i < countries.size(); i++) {
                    if (strCountries.get(position).equalsIgnoreCase(countries.get(i).getName())) {
                        ids = countries.get(i).getId();
                        selectedCountryId = ids;
                        break;
                    }
                }
                if (ids > 0) {
                    states = justOLMHelper.getStates(ids);
                    strStates = new ArrayList<String>();
                    if (states.size() > 0) {
                        for (State state : states) {
                            strStates.add(state.getName());
                        }
                        setStates();
                    } else {
                        initStates(ids);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int ids = 0;
                for (int i = 0; i < states.size(); i++) {
                    if (strStates.get(position).equalsIgnoreCase(states.get(i).getName())) {
                        ids = states.get(i).getId();
                        selectedStateId = ids;
                        break;
                    }
                }
                if (ids > 0) {
                    cities = justOLMHelper.getCities(ids);
                    strCities = new ArrayList<String>();
                    if (cities.size() > 0) {
                        for (City city : cities) {
                            strCities.add(city.getName());
                        }
                        setCity();
                    } else {
                        initCities(ids);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txtCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int ids = 0;
                for (int i = 0; i < cities.size(); i++) {
                    if (strCities.get(position).equalsIgnoreCase(cities.get(i).getName())) {
                        ids = cities.get(i).getId();
                        selectedCityId = ids;
                        break;
                    }
                }
                if (ids > 0) {
                    areas = justOLMHelper.getAreas(ids);
                    strAreas = new ArrayList<String>();
                    if (areas.size() > 0) {
                        for (Area area : areas) {
                            strAreas.add(area.getName());
                        }
                        setArea();
                    } else {
                        initAreas(ids);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setCountries() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strCountries);
        txtCountry.setAdapter(countryAdapter);
        setSpinText(txtCountry, loginResponse.getUserInfo().getCountryName());
    }


    private void setStates() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strStates);
        txtState.setAdapter(countryAdapter);
        setSpinText(txtState, loginResponse.getUserInfo().getStateName());
    }


    private void setCity() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strCities);
        txtCity.setAdapter(countryAdapter);
        setSpinText(txtCity, loginResponse.getUserInfo().getCityName());
    }


    private void setArea() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strAreas);
        txtArea.setAdapter(countryAdapter);
        setSpinText(txtArea, loginResponse.getUserInfo().getAreaName());
    }


    private void updateUserInfoFromRegistration() {
        userInfo.setAddress(registration.getAddress());
        userInfo.setArea(registration.getArea());
        userInfo.setCity(registration.getCity());
        userInfo.setCountryCode(registration.getCountry());
        userInfo.setDob(registration.getDateOfBirth());
        userInfo.setEmail(registration.getEmail());
        userInfo.setFirstName(registration.getFirstName());
        userInfo.setLastName(registration.getLastName());
        userInfo.setMiddleName(registration.getMiddleName());
        userInfo.setMobile(registration.getMobileNumber());
        userInfo.setGender(registration.getGender());
        userInfo.setZip(registration.getPinCode());
        userInfo.setProfession(registration.getProfession());
        userInfo.setState(registration.getState());


    }


    private void initCountries() {
        //setSpinText(txtArea, loginResponse.getUserInfo().getArea());
        if (isConnected()) {
            GetCountries countries = initHelper.getCountries(serviceFunctions);
            countries.setOnCountryListener(this, this);
            isCountryClickable = false;
        }

    }

    private void initStates(int id) {
        if (isConnected()) {
            GetStates states = initHelper.getStates(serviceFunctions, id);
            states.setOnStateListener(this, this);
        }
    }

    private void initCities(int id) {
        if (isConnected()) {
            GetCity cities = initHelper.getCities(serviceFunctions, id);
            cities.setOnCityListener(this, this);
        }
    }

    private void initAreas(int id) {
        if (isConnected()) {
            GetAreas areas = initHelper.getAreas(serviceFunctions, id);
            areas.setOnAreaListener(this, this);
        }
    }


    private void cancel() {
        btnEdit.setText(R.string.cancel);
        btnUpdateProfile.setVisibility(View.VISIBLE);
        enableInput();
    }

    private void edit() {
        btnEdit.setText(R.string.edit);
        btnUpdateProfile.setVisibility(View.GONE);
        disableInput();
    }

    private void initialization() {

        txtFirstName = (AppCompatEditText) findViewById(R.id.txtFirstName);
        txtMiddleName = (AppCompatEditText) findViewById(R.id.txtMiddleName);
        txtLastName = (AppCompatEditText) findViewById(R.id.txtLastName);
        txtDateOfBirth = (AppCompatEditText) findViewById(R.id.txtDateOfBirth);
        txtProfession = (AppCompatEditText) findViewById(R.id.txtProfession);
        txtAddress = (AppCompatEditText) findViewById(R.id.txtAddress);
        txtState = (SearchableSpinner) findViewById(R.id.txtState);
        txtCity = (SearchableSpinner) findViewById(R.id.txtCity);
        txtArea = (SearchableSpinner) findViewById(R.id.txtArea);
        txtCountry = (SearchableSpinner) findViewById(R.id.txtCountry);
        txtPinCode = (AppCompatEditText) findViewById(R.id.txtPinCode);
        txtEmail = (AppCompatEditText) findViewById(R.id.txtEmail);
        txtMobileNumber = (AppCompatEditText) findViewById(R.id.txtMobileNumber);

        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbFemale);

        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        setUserInfo();
        initInput();

    }

    private String getGender() {
        String gender = "Select gender";
        if (!rdbMale.isChecked() && !rdbFemale.isChecked()) {
            gender = "Select gender";
            return gender;
        } else if (rdbMale.isChecked()) {
            gender = "Male";
            return gender;
        } else if (rdbFemale.isChecked()) {
            gender = "Female";
            return gender;
        }

        return gender;
    }


    /**
     * @param registration
     */
    private void initUpdateInfo(com.mywings.justolm.Model.Registration registration) {
        show();
        UpdateUser updateUser = initHelper.updateUser(serviceFunctions);
        updateUser.setOnUpdateProfileListener(this, registration);
    }

    private void initGetProfile() {
        GetProfile getProfile = initHelper.getProfile(serviceFunctions, String.valueOf(justOLMShared.getIntegerValue("userId")));
        getProfile.setOnGetProfileListener(this, this);
    }

    private void setUserInfo() {
        txtFirstName.setText(loginResponse.getUserInfo().getFirstName());
        txtMiddleName.setText(loginResponse.getUserInfo().getMiddleName());
        txtLastName.setText(loginResponse.getUserInfo().getLastName());
        txtDateOfBirth.setText(loginResponse.getUserInfo().getDob());
        txtProfession.setText(loginResponse.getUserInfo().getProfession());
        txtAddress.setText(loginResponse.getUserInfo().getAddress());
        txtPinCode.setText(loginResponse.getUserInfo().getZip());
        txtEmail.setText(loginResponse.getUserInfo().getEmail());
        txtMobileNumber.setText(loginResponse.getUserInfo().getMobile());
        if (loginResponse.getUserInfo().getGender().equalsIgnoreCase("Male")) {
            rdbMale.setChecked(true);
            //rdbMale.setChecked(false);
        } else {
            //rdbMale.setChecked(false);
            rdbFemale.setChecked(true);
        }
    }

    private void initInput() {
        inputList = new ArrayList<AppCompatEditText>();
        selectList = new ArrayList<AppCompatSpinner>();

        inputList.add(txtFirstName);
        inputList.add(txtMiddleName);
        inputList.add(txtLastName);
        inputList.add(txtDateOfBirth);
        inputList.add(txtProfession);
        inputList.add(txtAddress);
        inputList.add(txtPinCode);
        inputList.add(txtEmail);
        inputList.add(txtMobileNumber);

        // selectList.add(txtGender);
        selectList.add(txtCity);
        selectList.add(txtState);
        selectList.add(txtArea);
        selectList.add(txtCountry);
    }

    private void enableInput() {

        rdbMale.setEnabled(true);
        rdbFemale.setEnabled(true);

        for (AppCompatEditText control : inputList) {
            if (control.getId() != R.id.txtDateOfBirth) {
                control.setFocusable(true);
                control.setFocusableInTouchMode(true);
            }
            control.setClickable(true);
            control.setEnabled(true);
        }
        for (AppCompatSpinner control : selectList) {
            control.setEnabled(true);
            control.setClickable(true);
        }
    }


    private void disableInput() {

        rdbMale.setEnabled(false);
        rdbFemale.setEnabled(false);

        for (AppCompatEditText control : inputList) {
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);
            control.setClickable(false);
            control.setEnabled(false);
        }
        for (AppCompatSpinner control : selectList) {
            control.setEnabled(false);
            control.setClickable(false);
        }
    }


    /**
     *
     */
    private void updateRegistration() {

        registration.setFirstName(txtFirstName.getText().toString().trim());
        registration.setMiddleName(txtMiddleName.getText().toString().trim());
        registration.setLastName(txtLastName.getText().toString().trim());
        registration.setDateOfBirth(txtDateOfBirth.getText().toString().trim());
        registration.setGender(getGender());
        registration.setProfession(txtProfession.getText().toString().trim());
        registration.setAddress(txtAddress.getText().toString().trim());
        String country = "Select country";

        try {


            country = justOLMHelper.getCountry(txtCountry.getSelectedItem().toString());


            if (null == country || TextUtils.isEmpty(country)) {
                country = "Select country";
            }
            registration.setCountry(country);
        } catch (NullPointerException e) {
            if (null == country || TextUtils.isEmpty(country)) {
                country = "Select country";
            }
            registration.setCountry(country);
        }

        String state = "Select state";

        try {
            state = justOLMHelper.getState(txtState.getSelectedItem().toString());
            if (null == state || TextUtils.isEmpty(state)) {
                state = "Select state";
            }
            registration.setState(state);
        } catch (NullPointerException e) {
            if (null == state || TextUtils.isEmpty(state)) {
                state = "Select state";
            }
            registration.setState(state);
        }

        String city = "Select city";

        try {
            city = justOLMHelper.getCity(txtCity.getSelectedItem().toString());
            if (null == city || TextUtils.isEmpty(city)) {
                state = "Select city";
            }
            registration.setCity(city);
        } catch (NullPointerException e) {
            if (null == city || TextUtils.isEmpty(city)) {
                state = "Select city";
            }
            registration.setCity(city);
        }

        String area = "Select area";

        try {
            area = justOLMHelper.getArea(txtArea.getSelectedItem().toString());
            if (null == area || TextUtils.isEmpty(area)) {
                area = "Select area";
            }
            registration.setArea(area);
        } catch (NullPointerException e) {
            if (null == area || TextUtils.isEmpty(area)) {
                area = "Select area";
            }
            registration.setArea(area);
        }

        registration.setPinCode(txtPinCode.getText().toString().trim());
        registration.setEmail(txtEmail.getText().toString().trim());
        registration.setMobileNumber(txtMobileNumber.getText().toString().trim());
        validateRegistrationInfo();
    }


    private void validateRegistrationInfo() {
        if (null != registration) {
            if (registration.isEmptyFieldUpdate()) {
                show(getString(R.string.action_all_fields_mandetory), btnUpdateProfile);
            } else if (registration.isNotEmptyFieldUpdate()) {
                if (isConnected()) {
                    initUpdateInfo(registration);
                }
            } else if (registration.getMobileNumber().length() < 10) {
                show(getString(R.string.enter_valid_ten_digit_number), btnUpdateProfile);
            }
        }
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
                finish();
                break;
            case R.id.abountus:
                startAboutUs();
                finish();
                break;
            case R.id.contactus:
                contactus();
                finish();
                break;


            case R.id.userorder:
                userorder();
                break;
            case R.id.neworder:
                neworder();
                break;

            case R.id.amendorder:
                startamendorder();
                finish();
                break;

            case R.id.amendschedulerorder:
                startamendscheduler();
                finish();
                break;

            case R.id.pendingorder:
                startpendingscreen();
                finish();
                break;

            case R.id.logout:
                dialog = logout();
                dialog.show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setSpinText(Spinner spinner, String text) {
        if (spinner.getAdapter() != null) {
            for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
                if (spinner.getAdapter().getItem(i).toString().equalsIgnoreCase(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }


    private void startamendscheduler() {
        Intent intent = new Intent(MyProfile.this, AmendScheduler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startamendorder() {
        Intent intent = new Intent(MyProfile.this, AmendOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void contactus() {
        Intent intent = new Intent(MyProfile.this, ContactUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void neworder() {
        Intent intent = new Intent(MyProfile.this, NewOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void userorder() {
        Intent intent = new Intent(MyProfile.this, MyOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startpendingscreen() {
        Intent intent = new Intent(MyProfile.this, PendingOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    @Override
    public void onAreaComplete(List<Area> result, Exception exception) {
        if (null != result && exception == null) {
            strAreas = new ArrayList<>();
            for (Area area : result) {
                strAreas.add(area.getName());
            }
            justOLMHelper.areas(result);
            areas = justOLMHelper.getAreas(selectedCityId);
            setArea();
        } else {
            show(exception.getMessage(), btnUpdateProfile);
        }
    }

    @Override
    public void onCityComplete(List<City> result, Exception exception) {

        if (null != result && exception == null) {
            strCities = new ArrayList<>();
            for (City city : result) {
                strCities.add(city.getName());
            }
            justOLMHelper.cities(result);
            cities = justOLMHelper.getCities(selectedStateId);
            setCity();
        } else {
            show(exception.getMessage(), btnUpdateProfile);
        }

    }

    @Override
    public void onStateComplete(List<State> result, Exception exception) {
        if (null != result && exception == null) {

            strStates = new ArrayList<>();

            for (State state : result) {
                strStates.add(state.getName());
            }

            justOLMHelper.states(result);
            states = justOLMHelper.getStates(selectedCountryId);
            setStates();

        } else {
            show(exception.getMessage(), btnUpdateProfile);
        }
    }

    @Override
    public void onCountryComplete(List<Country> result, Exception exception) {
        if (null != result && exception == null) {
            isCountryClickable = true;
            strCountries = new ArrayList<>();
            for (Country country : result) {
                strCountries.add(country.getName());
            }


            justOLMHelper.countries(result);
            countries = justOLMHelper.getCountries();
            setCountries();

        } else {
            show(exception.getMessage(), btnUpdateProfile);
        }
    }

    @Override
    public void onProfileUpdateComplete(UserMessage userMessage, Exception exception) {
        hide();
        if (null != userMessage && exception == null) {
            edit();
            updateUserInfoFromRegistration();
            show(userMessage.getMessage(), btnUpdateProfile);
        } else {
            show(exception.getMessage(), btnUpdateProfile);
        }
    }

    /**
     *
     */
    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void startAboutUs() {
        Intent intent = new Intent(MyProfile.this, AboutUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startHomeScreen() {
        Intent intent = new Intent(MyProfile.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onGetProfileComplete(LoginResponse loginResponse, Exception exception) {
        this.loginResponse = loginResponse;
        setUserInfo();
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
            txtDateOfBirth.setText(dateTimeUtils.update(year, month, day));
        }
    }
}

package com.mywings.justolm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.mywings.justolm.Model.Area;
import com.mywings.justolm.Model.City;
import com.mywings.justolm.Model.Country;
import com.mywings.justolm.Model.LoginResponse;
import com.mywings.justolm.Model.State;
import com.mywings.justolm.Model.UserInfo;
import com.mywings.justolm.Process.GetAreas;
import com.mywings.justolm.Process.GetCity;
import com.mywings.justolm.Process.GetCountries;
import com.mywings.justolm.Process.GetStates;
import com.mywings.justolm.Process.OnAreaListener;
import com.mywings.justolm.Process.OnCityListener;
import com.mywings.justolm.Process.OnCountryListener;
import com.mywings.justolm.Process.OnStateListener;
import com.mywings.justolm.Process.OnUpdateListener;
import com.mywings.justolm.Process.RegisterUser;
import com.mywings.justolm.Utilities.Halted;
import com.mywings.justolm.Utilities.JustOLMShared;
import com.mywings.justolm.Widgets.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Registration extends JustOlmCompactActivity implements OnUpdateListener, OnCountryListener, OnStateListener, OnCityListener, OnAreaListener {


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
    private AppCompatEditText lblSecurityCode;
    private AppCompatEditText txtSecurityCode;
    private AppCompatEditText txtCreatePassword;
    private AppCompatEditText txtConformPassword;
    private AppCompatCheckBox chkTermsAndCoditions;
    private Button btnCreateAccount;
    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private int selectedCountryId;
    private int selectedStateId;
    private int selectedCityId;
    //endregion

    //region Variables
    private Halted halted;
    private List<Country> countries;
    private List<String> strCountries;
    private boolean isCountryClickable = false;
    private List<State> states;
    private List<String> strStates;
    private List<City> cities;
    private List<String> strCities;
    private List<Area> areas;
    private List<String> strAreas;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        initializaUIComponents();

        events();

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

    private void events() {
        txtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
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


    private void initializaUIComponents() {
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
        lblSecurityCode = (AppCompatEditText) findViewById(R.id.lblSecurityCode);
        txtSecurityCode = (AppCompatEditText) findViewById(R.id.txtSecurityCode);
        txtCreatePassword = (AppCompatEditText) findViewById(R.id.txtPassword);
        txtConformPassword = (AppCompatEditText) findViewById(R.id.txtConfirmPassword);
        chkTermsAndCoditions = (AppCompatCheckBox) findViewById(R.id.chkTermsAndConditions);

        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbFemale);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        lblSecurityCode.setText(halted.getSaltString());
    }

    /**
     *
     */
    private void init() {
        halted = new Halted();
    }


    private void initCountries() {
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

    /**
     *
     */
    private void startJustOLMScreen() {
        Intent intent = new Intent(Registration.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        this.finish();
    }


    @Override
    public void onUpdateComplete(LoginResponse result, Exception exception) {
        hide();
        if (null != result && exception == null) {
            setCookies(result.getUserInfo());
            startJustOLMScreen();
        } else {
            show(exception.getMessage(), btnCreateAccount);
        }
    }

    private void setCookies(UserInfo userInfo) {

        JustOLMShared justOLM = justOLMShared;
        justOLM.setIntegerValue("userId", userInfo.getId());
        justOLM.setStringValue("username", txtEmail.getText().toString());
        justOLM.setStringValue("password", txtCreatePassword.getText().toString());
        justOLM.setStringValue("firstname", userInfo.getFirstName());
        justOLM.setStringValue("middlename", userInfo.getMiddleName());
        justOLM.setStringValue("lastname", userInfo.getLastName());
        justOLM.setStringValue("dob", userInfo.getDob());
        justOLM.setStringValue("gender", userInfo.getGender());
        justOLM.setStringValue("profession", userInfo.getProfession());
        justOLM.setStringValue("address", userInfo.getAddress());
        justOLM.setStringValue("country", userInfo.getCountryCode());
        justOLM.setStringValue("state", userInfo.getState());
        justOLM.setStringValue("city", userInfo.getCity());
        justOLM.setStringValue("area", userInfo.getArea());
        justOLM.setStringValue("countryName", userInfo.getCountryName());
        justOLM.setStringValue("stateName", userInfo.getStateName());
        justOLM.setStringValue("cityName", userInfo.getCityName());
        justOLM.setStringValue("areaName", userInfo.getAreaName());
        justOLM.setStringValue("zip", userInfo.getZip());
        justOLM.setStringValue("email", userInfo.getEmail());
        justOLM.setStringValue("mobile", userInfo.getMobile());
        justOLM.setStringValue("isadmin", String.valueOf(userInfo.isAdmin()));
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
            show(exception.getMessage(), btnCreateAccount);
        }
    }

    private void setCountries() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strCountries);
        txtCountry.setAdapter(countryAdapter);
        txtCountry.setTitle("Select country");
    }


    private void setStates() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strStates);
        txtState.setAdapter(countryAdapter);
        txtState.setTitle("Select state");
    }


    private void setCity() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strCities);
        txtCity.setAdapter(countryAdapter);
        txtCity.setTitle("Select city");
    }


    private void setArea() {
        ArrayAdapter countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strAreas);
        txtArea.setAdapter(countryAdapter);
        txtArea.setTitle("Select area");
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
            show(exception.getMessage(), btnCreateAccount);
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
            show(exception.getMessage(), btnCreateAccount);
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
            show(exception.getMessage(), btnCreateAccount);
        }
    }

    /**
     *
     */
    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String getCountryId(String name, List<Country> countries) {
        String id = "Select country";
        if (null != countries) {
            for (Country country : countries) {
                if (country.getName().equalsIgnoreCase(name)) {
                    id = String.valueOf(country.getId());
                    break;
                }
            }
        }
        return id;
    }

    private String getStateId(String name, List<State> states) {
        String id = "Select state";
        if (null != states) {
            for (State state : states) {
                if (state.getName().equalsIgnoreCase(name)) {
                    id = String.valueOf(state.getId());
                    break;
                }
            }
        }
        return id;
    }

    private String getCityId(String name, List<City> cities) {
        String id = "Select city";
        if (null != cities) {
            for (City city : cities) {
                if (city.getName().equalsIgnoreCase(name)) {
                    id = String.valueOf(city.getId());
                    break;
                }
            }
        }
        return id;
    }

    private String getAreaId(String name, List<Area> areas) {
        String id = "Select area";
        if (null != areas) {
            for (Area area : areas) {
                if (area.getName().equalsIgnoreCase(name)) {
                    id = String.valueOf(area.getId());
                    break;
                }
            }
        }
        return id;
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
            country = txtCountry.getSelectedItem().toString();
            if (null == country || TextUtils.isEmpty(country)) {
                country = "Select country";
            }
            registration.setCountry(getCountryId(country, countries));

        } catch (NullPointerException e) {
            if (null == country || TextUtils.isEmpty(country)) {
                country = "Select country";
            }
            registration.setCountry(country);
        }

        String state = "Select state";

        try {
            state = txtState.getSelectedItem().toString();
            if (null == state || TextUtils.isEmpty(state)) {
                state = "Select state";
            }
            registration.setState(getStateId(state, states));
        } catch (NullPointerException e) {
            if (null == state || TextUtils.isEmpty(state)) {
                state = "Select state";
            }
            registration.setState(state);
        }

        String city = "Select city";

        try {
            city = txtCity.getSelectedItem().toString();
            if (null == city || TextUtils.isEmpty(city)) {
                city = "Select city";
            }
            registration.setCity(getCityId(city, cities));
        } catch (NullPointerException e) {
            if (null == city || TextUtils.isEmpty(city)) {
                city = "Select city";
            }
            registration.setCity(city);
        }

        String area = "Select area";

        try {
            area = txtArea.getSelectedItem().toString();
            if (null == area || TextUtils.isEmpty(area)) {
                area = "Select area";
            }
            registration.setArea(getAreaId(area, areas));
        } catch (NullPointerException e) {
            if (null == area || TextUtils.isEmpty(area)) {
                area = "Select area";
            }
            registration.setArea(area);
        }


        registration.setPinCode(txtPinCode.getText().toString().trim());
        registration.setEmail(txtEmail.getText().toString().trim());
        registration.setMobileNumber(txtMobileNumber.getText().toString().trim());
        registration.setSecurityCode(txtSecurityCode.getText().toString().trim());
        registration.setPassword(txtCreatePassword.getText().toString().trim());

        validateRegistrationInfo();
    }

    private void initRegistration(com.mywings.justolm.Model.Registration registration) {
        show();
        RegisterUser registerUser = initHelper.registerUser(serviceFunctions, this);
        registerUser.setOnUpdateListener(this, registration);
    }

    private void validateRegistrationInfo() {
        if (null != registration) {
            if (registration.isEmptyField()) {
                show(getString(R.string.action_all_fields_mandetory), btnCreateAccount);
            } else if (registration.isNotEmptyField()) {
                if (!chkTermsAndCoditions.isChecked()) {
                    show(getString(R.string.please_accept_terms_conditions), btnCreateAccount);
                    return;
                }
                if (isConnected()) {
                    initRegistration(registration);
                }
            } else if (registration.getMobileNumber().length() < 10) {
                show(getString(R.string.enter_valid_ten_digit_number), btnCreateAccount);
            } else if (!registration.getSecurityCode().equalsIgnoreCase(lblSecurityCode.getText().toString().trim())) {
                show(getString(R.string.security_code_not_match), btnCreateAccount);
            } else if (registration.getPassword().length() < 8) {
                show(getString(R.string.enter_minimum_eight), btnCreateAccount);
            } else if (validationHelper.isStrongPassword(registration.getPassword())) {
                show("Please enter valid password", btnCreateAccount);
            } else if (!registration.getPassword().equalsIgnoreCase(txtConformPassword.getText().toString().trim())) {
                show(getString(R.string.password_doesnot_match), btnCreateAccount);
            }
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
            txtDateOfBirth.setText(dateTimeUtils.update(year, month, day));
        }

    }

}

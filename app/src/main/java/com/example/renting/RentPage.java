package com.example.renting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RentPage extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private TextView selectedDatesTextView;
    private TextView numberOfDaysTextView;
    private TextView totalAmountTextView;
    private TextView pricePerDayTextView;
    private RadioGroup paymentMethodRadioGroup;
    private RadioButton paymentMethodCard;
    private RadioButton paymentMethodCash;
    private Calendar startDate;
    private Calendar endDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private RentalService rentalService;
    private DroneApi droneService;
    private String selectedDroneId;
    private int selectedDays;
    private double selectedPricePerDay;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_page);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);
        if (userEmail == null) {
            Intent intent = new Intent(RentPage.this, SignUp.class);
            startActivity(intent);
            finish();
            return;
        }

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        selectedDatesTextView = findViewById(R.id.selectedDatesTextView);
        numberOfDaysTextView = findViewById(R.id.numberOfDaysTextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        pricePerDayTextView = findViewById(R.id.pricePerDayTextView);
        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        paymentMethodCard = findViewById(R.id.paymentMethodCard);
        paymentMethodCash = findViewById(R.id.paymentMethodCash);
        Button selectDateButton = findViewById(R.id.selectDateButton);
        Button submitButton = findViewById(R.id.submitButton);

        String firstName = sharedPreferences.getString("user_first_name", "");
        String lastName = sharedPreferences.getString("user_last_name", "");
        String phone = sharedPreferences.getString("user_phone", "");

        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        emailEditText.setText(userEmail);
        phoneEditText.setText(phone);

        selectDateButton.setOnClickListener(v -> showDateRangePicker());
        submitButton.setOnClickListener(v -> createAndSendRental());

        Retrofit retrofit = RetrofitClient.getClient();
        rentalService = retrofit.create(RentalService.class);
        droneService = retrofit.create(DroneApi.class);

        selectedDroneId = getIntent().getStringExtra("DRONE_ID");
        preluareDetaliiDrona(selectedDroneId);
    }

    private void preluareDetaliiDrona(String droneId) {
        Call<DroneData> call = droneService.getDroneById(droneId);
        call.enqueue(new Callback<DroneData>() {
            @Override
            public void onResponse(Call<DroneData> call, Response<DroneData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DroneData droneData = response.body();
                    selectedPricePerDay = droneData.getPricePerDay();
                    pricePerDayTextView.setText(String.valueOf(selectedPricePerDay));
                } else {
                    showCustomToast("Failed to retrieve drone data", 1000);
                }
            }

            @Override
            public void onFailure(Call<DroneData> call, Throwable t) {
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void showDateRangePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog startDatePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            startDate = Calendar.getInstance();
            startDate.set(year, month, dayOfMonth);
            showEndDatePicker();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        startDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        startDatePicker.show();
    }

    private boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    private void checkDroneAvailability(Date startDate, Date endDate) {
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        String startDateString = apiDateFormat.format(startDate);
        String endDateString = apiDateFormat.format(endDate);

        CheckAvailabilityDto checkAvailabilityDto = new CheckAvailabilityDto(selectedDroneId, startDateString, endDateString);

        Call<AvailabilityResultDto> call = droneService.checkAvailability(checkAvailabilityDto);
        call.enqueue(new Callback<AvailabilityResultDto>() {
            @Override
            public void onResponse(Call<AvailabilityResultDto> call, Response<AvailabilityResultDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AvailabilityResultDto availabilityResponse = response.body();
                    if (!availabilityResponse.isAvailable()) {
                        UnavailableDroneDialogFragment dialogFragment = UnavailableDroneDialogFragment.newInstance("This drone is not available for the selected dates.");
                        dialogFragment.show(getSupportFragmentManager(), "unavailableDroneDialog");

                        Button submitButton = findViewById(R.id.submitButton);
                        submitButton.setEnabled(false);
                    } else {

                        Button submitButton = findViewById(R.id.submitButton);
                        submitButton.setEnabled(true);

                        updateSelectedDates();
                    }
                } else {
                    Log.e("RentPage", "Response code: " + response.code());
                    Log.e("RentPage", "Response message: " + response.message());
                    Log.e("RentPage", "Response error body: " + response.errorBody());
                    showCustomToast("Failed to check availability", 1000);
                }
            }

            @Override
            public void onFailure(Call<AvailabilityResultDto> call, Throwable t) {
                Log.e("RentPage", "Network error: " + t.getMessage());
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void showEndDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog endDatePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            endDate = Calendar.getInstance();
            endDate.set(year, month, dayOfMonth);
            checkDroneAvailability(startDate.getTime(), endDate.getTime());
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        endDatePicker.getDatePicker().setMinDate(startDate.getTimeInMillis());
        endDatePicker.show();
    }

    private void updateSelectedDates() {
        if (startDate != null && endDate != null) {
            String startDateString = dateFormat.format(startDate.getTime());
            String endDateString = dateFormat.format(endDate.getTime());
            selectedDatesTextView.setText(String.format("From %s to %s", startDateString, endDateString));

            long diff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
            int days = (int) (diff / (1000 * 60 * 60 * 24)) + 1; // Include both start and end date
            numberOfDaysTextView.setText(String.valueOf(days));

            Log.d("RentPage", "Start date: " + startDateString);
            Log.d("RentPage", "End date: " + endDateString);
            Log.d("RentPage", "Calculated days: " + days);

            totalAmount = days * selectedPricePerDay;
            totalAmountTextView.setText(String.valueOf(totalAmount));

            selectedDays = days;
        }
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 10 && phoneNumber.matches("\\d{10}");
    }

    private void createAndSendRental() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);
        String userEmail = sharedPreferences.getString("user_email", null);
        String userPhone = sharedPreferences.getString("user_phone", null);

        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if (firstNameEditText.getText().toString().isEmpty() ||
                lastNameEditText.getText().toString().isEmpty() ||
                emailEditText.getText().toString().isEmpty() ||
                phoneEditText.getText().toString().isEmpty() ||
                !isValidAddress(address)) {
            showCustomToast("All fields must be filled out, including address.", 1000);
            return;
        }
        if (!isValidEmail(email)) {
            showCustomToast("Invalid email address.", 1000);
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            showCustomToast("Phone number must be 10 digits.", 1000);
            return;
        }
        if (userId == null) {
            showCustomToast("User not logged in", 1000);
            return;
        }

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setEnabled(false);

        CreateOrUpdateRentalDto rentalDto = new CreateOrUpdateRentalDto();

        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        rentalDto.setStartDay(apiDateFormat.format(startDate.getTime()));
        rentalDto.setEndDay(apiDateFormat.format(endDate.getTime()));
        rentalDto.setTotal(totalAmount);
        rentalDto.setStatus(1);
        rentalDto.setCustomerId(userId);

        int selectedPaymentMethod = paymentMethodRadioGroup.getCheckedRadioButtonId();
        if (selectedPaymentMethod == R.id.paymentMethodCard) {
            rentalDto.setPaymentMethod(2);
        } else if (selectedPaymentMethod == R.id.paymentMethodCash) {
            rentalDto.setPaymentMethod(1);
        }

        List<RentalItemDto> rentalItems = new ArrayList<>();
        RentalItemDto rentalItem = new RentalItemDto();
        rentalItem.setDroneId(selectedDroneId);
        rentalItem.setDaysNumber(selectedDays);
        rentalItem.setPricePerDay(selectedPricePerDay);
        rentalItem.calculateTotalPrice();
        rentalItems.add(rentalItem);
        rentalDto.setRentalItems(rentalItems);

        rentalDto.setAddress(address);

        Gson gson = new Gson();
        String rentalDtoJson = gson.toJson(rentalDto);
        Log.d("RentPage", "Sending rental data: " + rentalDtoJson);

        Call<RentalData> call = rentalService.createRental(rentalDto);
        call.enqueue(new Callback<RentalData>() {
            @Override
            public void onResponse(Call<RentalData> call, Response<RentalData> response) {
                if (response.isSuccessful()) {
                    RentalData rentalData = response.body();
                    if (rentalData != null) {
                        Call<DroneData> droneCall = droneService.getDroneById(selectedDroneId);
                        droneCall.enqueue(new Callback<DroneData>() {
                            @Override
                            public void onResponse(Call<DroneData> call, Response<DroneData> droneResponse) {
                                if (droneResponse.isSuccessful() && droneResponse.body() != null) {
                                    DroneData droneData = droneResponse.body();
                                    showConfirmationDialog();
                                    sendConfirmationEmail(userEmail, rentalData, droneData);
                                } else {
                                    showCustomToast("Failed to retrieve drone data for email", 1000);
                                }
                            }

                            @Override
                            public void onFailure(Call<DroneData> call, Throwable t) {
                                showCustomToast("Network error: " + t.getMessage(), 1000);
                            }
                        });
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("RentPage", "Error response: " + errorBody);
                        showCustomToast("Failed to create rental: " + errorBody, 1000);
                        submitButton.setEnabled(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RentalData> call, Throwable t) {
                showCustomToast("Network error: " + t.getMessage(), 1000);
                submitButton.setEnabled(true);
            }
        });
    }

    private void sendConfirmationEmail(String userEmail, RentalData rentalData, DroneData droneData) {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String startDateFormatted = displayDateFormat.format(startDate.getTime());
        String endDateFormatted = displayDateFormat.format(endDate.getTime());

        String subject = "Rental Confirmation";
        String body = "<html><body>" +
                "<h1>Rental Confirmation</h1>" +
                "<p>Your rental has been successfully placed. Here are the details:</p>" +
                "<table border='1' style='border-collapse: collapse; width: 100%;'>" +
                "<tr><th>First Name</th><td>" + firstNameEditText.getText().toString() + "</td></tr>" +
                "<tr><th>Last Name</th><td>" + lastNameEditText.getText().toString() + "</td></tr>" +
                "<tr><th>Email</th><td>" + emailEditText.getText().toString() + "</td></tr>" +
                "<tr><th>Phone</th><td>" + phoneEditText.getText().toString() + "</td></tr>" +
                "<tr><th>Address</th><td>" + addressEditText.getText().toString() + "</td></tr>" +
                "<tr><th>Start Date</th><td>" + startDateFormatted + "</td></tr>" +
                "<tr><th>End Date</th><td>" + endDateFormatted + "</td></tr>" +
                "<tr><th>Number of Days</th><td>" + selectedDays + "</td></tr>" +
                "<tr><th>Total Amount</th><td>" + totalAmount + "</td></tr>" +
                "<tr><th>Payment Method</th><td>" + rentalData.getPaymentMethodAsString() + "</td></tr>" +
                "<tr><th>Drone Details</th><td>" +
                "Name: " + droneData.getName() + "<br>" +
                "Model: " + droneData.getModel() + "<br>" +
                "Category: " + droneData.getCategoryAsString() + "<br>" +
                "Utility: " + droneData.getUtilityAsString() + "<br>" +
                "Price Per Day: " + droneData.getPricePerDay() + "</td></tr>" +
                "</table>" +
                "<p>Thank you for using our service!</p>" +
                "<img src='https://upload.wikimedia.org/wikipedia/commons/7/7a/Smiley.svg' alt='Smiley face' style='width: 50px; height: 50px;'>" +
                "</body></html>";

        EmailSender emailSender = new EmailSender(userEmail, subject, body);
        emailSender.execute();
    }

    private void showConfirmationDialog() {
        Dialog dialog = new Dialog(RentPage.this);
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setCancelable(false);

        TextView messageTextView = dialog.findViewById(R.id.messageTextView);
        messageTextView.setText("The drone rental has been placed, you will soon enjoy it");

        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Intent intent = new Intent(RentPage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private void showCustomToast(String message, int duration) {
        Toast toast = Toast.makeText(RentPage.this, message, Toast.LENGTH_SHORT);
        toast.show();

        new Handler().postDelayed(toast::cancel, duration);
    }
}

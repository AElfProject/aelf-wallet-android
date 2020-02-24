/*
 * **************************************************************************************
 *  Copyright © 2014-2018 Ontology Foundation Ltd.
 *  All rights reserved.
 *
 *  This software is supplied only under the terms of a license agreement,
 *  nondisclosure agreement or other written agreement with Ontology Foundation Ltd.
 *  Use, redistribution or other disclosure of any parts of this
 *  software is prohibited except in accordance with the terms of such written
 *  agreement with Ontology Foundation Ltd. This software is confidential
 *  and proprietary information of Ontology Foundation Ltd.
 *
 * **************************************************************************************
 */

package com.legendwd.hyperpay.aelf.business.discover.dapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.util.Patterns;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liutao17 on 2018/2/6.
 */
public class CommonUtil {
    private static final String TAG = "MainActivity";

    private static final String WalletFilename = "wallet.json";
    private static final String DeviceTokenFilename = "deviceToken.json";

    private static Context mtx;

    static public void setContext(Context c) {
        mtx = c;
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        //https://stackoverflow.com/questions/23214434/regular-expression-in-android-for-password-field
        final String PASSWORD_PATTERN = "^[0-9]{6}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        Pattern pattern = Patterns.PHONE;
        return pattern.matcher(phone).matches();
    }

    public static boolean isValidEnglishName(final String name) {

        Pattern pattern;
        Matcher matcher;

        //https://stackoverflow.com/questions/23214434/regular-expression-in-android-for-password-field
        final String PASSWORD_PATTERN = "^[A-Za-z ]+$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(name);

        return matcher.matches();
    }

    public static boolean isValidUserName(final String name) {

        Pattern pattern;
        Matcher matcher;

        //https://stackoverflow.com/questions/23214434/regular-expression-in-android-for-password-field
        final String PASSWORD_PATTERN = "^[a-zA-Z0-9_-]{3,20}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(name);

        return matcher.matches();
    }

//    public static void getBalance(String address, String host, JSCallback callback) {
//        JSNetWebView webView = WebViewSingleton.getInstance(callback);
//        webView.loadUrl("file:///android_asset/");
//        String code = String.format("javascript:Ont.SDK.getBalance('%s', '%s')", address, host);
//        webView.setJavaScriptCode(code);
//    }


//    public static void createFile(String fileName, String jsonString) throws FileNotFoundException, IOException {
//        FileOutputStream outputStream;
//        outputStream = mtx.openFileOutput(fileName, Context.MODE_PRIVATE);
//        outputStream.write(jsonString.getBytes());
//        outputStream.close();
//    }
//
//    public static String readFile(String fileName) throws FileNotFoundException, IOException {
//        String json = "";
//        FileInputStream fis = mtx.openFileInput(fileName);
//        InputStreamReader isr = new InputStreamReader(fis);
//        BufferedReader bufferedReader = new BufferedReader(isr);
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//            sb.append(line);
//        }
//        json = sb.toString();
//        return json;
//    }

//    public static String getPath(final Context context, final Uri uri) {
//        // DocumentProvider
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{split[1]};
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }

//    private static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    private static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }
//
//    private static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {column};
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null) cursor.close();
//        }
//        return null;
//    }
//
//    public static String formatDate(String date) {
//        StringBuffer sb = new StringBuffer();
////        sb.append(src);
//        if (!TextUtils.isEmpty(date)) {
//            try {
//                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                sb.append(format2.format(format1.parse(date)));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return sb.toString();
//    }

    public static String getEllipString(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        int length = value.length();
        if (length > 10) {
            return value.substring(0, 5) + "..." + value.substring(length - 5, length);
        }
        return value;
    }

    public static String formatMoney(String money) {
        if (!TextUtils.isEmpty(money)) {
            NumberFormat nf = new DecimalFormat("###,###,###,##0.00");
            BigDecimal d = new BigDecimal(money);
            money = nf.format(d);
        }
        return money;
    }

    public static String formatMoneyOng(String money) {
        if (!TextUtils.isEmpty(money)) {
            NumberFormat nf = new DecimalFormat("###,###,###,##0.000000000");
            BigDecimal d = new BigDecimal(money);
            money = nf.format(d);
        }
        return money;
    }

    public static String formatMoneyOnt(String money) {
        if (!TextUtils.isEmpty(money)) {
            NumberFormat nf = new DecimalFormat("###,###,###,###");
            BigDecimal d = new BigDecimal(money);
            money = nf.format(d);
        }
        return money;
    }

    public static String formatMoneyFee(String money) {
        if (!TextUtils.isEmpty(money)) {
            NumberFormat nf = new DecimalFormat("###,###,###,###.#########");
            BigDecimal d = new BigDecimal(money);
            money = nf.format(d);
        }
        return money;
    }


    public static String formatTimestamp(Long timetamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(timetamp);
    }

    public static String formatTimeClaim(Long timetamp) {
        SimpleDateFormat format = new SimpleDateFormat("MMM d,yyyy");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(timetamp * 1000);
    }

    public static String formatTimeIdentity(Long timetamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(timetamp * 1000);
    }


    public static boolean isAddress(String toAddress) {
        if (toAddress != null && toAddress.length() == 34 && toAddress.startsWith("A")) {
            return true;
        }
        return false;
    }

    public static boolean checkIsMoney(String amountStr) {
        if (TextUtils.isEmpty(amountStr) || Double.parseDouble(amountStr) == 0) {
            return false;
        }
        return true;
    }

//    public static boolean checkFee(long price, long limit) {
//        String ontIdContract = SettingSingleton.getInstance().getOntIdContract();
//        try {
//            if (!ontIdContract.isEmpty()) {
//                JSONObject jsonObject = new JSONObject(ontIdContract);
//                if (limit < jsonObject.optLong(Constant.GAS_LIMIT)) {
//                    return false;
//                }
//                if (price < jsonObject.optLong(Constant.GAS_PRICE)) {
//                    return false;
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    public static ArrayList<Pair<String, String>> formatClaimDetail(Iterator<Pair<String, String>> iterator) {
        ArrayList<Pair<String, String>> tempList = new ArrayList<>();
        Pair<String, String> next;
        while (iterator.hasNext()) {
            next = iterator.next();
            String second = next.second;
            switch (next.first) {
                case "IdNumber":
                case "IDDocNumber":
                case "CredentialNumber":
                case "身份证号":
                    if (next.second.length() >= 4) {
                        String replace = second.replace(second.substring(second.length() - 4, second.length()), "****");
                        tempList.add(new Pair<String, String>(next.first, replace));
                    } else {
                        tempList.add(next);
                    }
                    break;
                default:
                    tempList.add(next);
            }
        }
        return tempList;
    }

    public static void setPoint(final EditText editText, final int decimal_digits) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > decimal_digits) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + decimal_digits + 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}

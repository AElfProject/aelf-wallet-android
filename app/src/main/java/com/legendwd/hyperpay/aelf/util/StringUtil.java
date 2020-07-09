package com.legendwd.hyperpay.aelf.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.github.ontio.crypto.Base58;
import com.github.ontio.crypto.Digest;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author lovelyzxing
 * @date 2019/6/25
 * @Description
 */
public class StringUtil {
    private static final String EMAIL_PATTERN = "^\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}$";
    private static final String ADDRESS_PATTERN = "^[A-Za-z0-9]+$";

    public static String stringToJSON(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();

        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == ':') {
                jsonFormat.append(c + " ");
            } else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                }
            } else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c);
                }
            } else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();
    }

    public static boolean isEmail(String pwd) {
        return pwd.matches(EMAIL_PATTERN);
    }


    public static String formatAddress(String address) {
        return Constant.DEFAULT_PREFIX + address + "_" + CacheUtil.getInstance().getProperty(Constant.Sp.CHAIN_ID);
    }

    public static String formatAddress(String address, String chain) {
        return Constant.DEFAULT_PREFIX + address + "_" + chain;
    }

    public static boolean isAddressValid(String address) {
        return address.matches(ADDRESS_PATTERN);
    }

    public static String checkAddress(String showAddress) {
        String[] addressarray = showAddress.split("_");
        if (addressarray.length != 3) {
            showAddress = formatAddress(getRealAelfAddress(showAddress));
        }
        return showAddress;
    }

    public static String getRealAelfAddress(String address) {
        String[] addressarray = address.split("_");
        if (addressarray.length > 0) {
            for (String s : addressarray) {
                if (s != null && s.length() > 10) {
                    return s;
                }
            }
        } else {
            return "";
        }
        return "";
    }

    public static boolean isAELFAddress(String address) {
        try {

//            address = address.replace("ELF_","")
//                    .replace("_AELF","");

            String[] addressarray = address.split("_");
            if (addressarray.length > 0) {
                for (String s : addressarray) {
                    if (s != null && s.length() > 10) {
                        address = s;
                        break;
                    }
                }
            } else {
                return false;
            }


            byte[] data = Base58.decode(address);
            byte[] checksum = new byte[4];
            for (int i = data.length - 4, j = 0; i < data.length; i++) {
                checksum[j++] = data[i];
            }

            byte[] publicHash = new byte[data.length - 4];

            for (int i = 0, j = 0; i < data.length - 4; i++) {
                publicHash[j++] = data[i];
            }

            byte[] checksumConfirm = Digest.sha256(Digest.sha256(publicHash, 0, publicHash.length));

            for (int i = 0; i < 4; ++i) {
                if (checksumConfirm[i] != checksum[i]) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 去除特殊字符串\n \t \r
     *
     * @param data
     * @return
     */
    public static String removeNull(String data) {
        if (data.contains("\n")) {
            data = data.replace("\n", "");
        }
        if (data.contains("\t")) {
            data = data.replace("\t", "");
        }
        if (data.contains("\r")) {
            data = data.replace("\r", "");
        }
        return data;
    }

    /**
     * 非严格模式，位数不够不补0
     */
    public static String formatDataNoZero(int n, double value) {
        StringBuilder format = new StringBuilder("#.");
        for (int i = 0; i < n; i++) {
            format.append("#");
        }
        DecimalFormat df = new DecimalFormat(format.toString());
        return df.format(value);
    }

    public static String formatDataNoZero(int n, String value) {
        return formatDataNoZero(n, Double.valueOf(value));
    }

    /**
     * 获取精度转换的值 除10^decimals
     */
    public static double divideDataDouble(int decimals, String value) {
        BigDecimal bigDecimal = new BigDecimal(value)
                .divide(new BigDecimal(Math.pow(10, decimals)));
        return bigDecimal.doubleValue();
    }

    /**
     * 获取精度转换的值 除10^decimals
     */
    public static String divideDataString(int decimals, String value) {
        return StringUtil.formatDataNoZero(decimals, divideDataDouble(decimals, value));
    }

    /**
     * 获取精度转换的值 乘10^decimals
     */
    public static double multiplyDataDouble(int decimals, String value) {
        BigDecimal bigDecimal = new BigDecimal(value)
                .multiply(new BigDecimal(Math.pow(10, decimals)));
        return bigDecimal.doubleValue();
    }

    /**
     * 获取精度转换的值 乘10^decimals
     */
    public static String multiplyDataString(int decimals, String value) {
        return StringUtil.formatDataNoZero(decimals, multiplyDataDouble(decimals, value));
    }

    public static double parseDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static Double parseDoubleU(String data) {
        return Double.valueOf(parseDouble(data));
    }

    public static String getAssetsJson(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("networkConfig.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

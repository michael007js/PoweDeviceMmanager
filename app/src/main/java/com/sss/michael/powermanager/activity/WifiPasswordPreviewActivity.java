package com.sss.michael.powermanager.activity;

import android.os.Build;

import com.blankj.utilcode.util.Utils;
import com.sss.michael.powermanager.R;
import com.sss.michael.powermanager.base.BaseActivity;
import com.sss.michael.powermanager.databinding.ActivityWifiPasswordPreviewBinding;
import com.sss.michael.powermanager.util.MichaelUtil;
import com.sss.michael.powermanager.util.ShellUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Michael by 61642
 * @date 2023/1/17 10:16
 * @Description 查看连接过的 WiFi 密码
 * https://blog.csdn.net/Sugar_wolf/article/details/127806122?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-3-127806122-blog-123650861.pc_relevant_aa&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-3-127806122-blog-123650861.pc_relevant_aa
 * https://blog.csdn.net/lb245557472/article/details/84068519
 * https://github.com/mzlogin/awesome-adb?login=from_csdn
 * <p>
 * 查看连接过的 WiFi 密码
 * cat /data/misc/wifi/*.conf   如果 Android O 或以后，WiFi 密码保存的地址有变化，是在 WifiConfigStore.xml 里面  cat /data/misc/wifi/WifiConfigStore.xml
 */
public class WifiPasswordPreviewActivity extends BaseActivity<ActivityWifiPasswordPreviewBinding> {

    @Override
    protected int setLayout() {
        return R.layout.activity_wifi_password_preview;
    }

    @Override
    protected void init() {
        String path;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                path = "/data/misc/apexdata/com.android.wifi/WifiConfigStore.xml";
            } else {
                path = "/data/misc/wifi/WifiConfigStore.xml";
            }
        } else {
            path = "/data/misc/wifi/wpa_supplicant.conf";
        }

        final String finalPath = path;
        ShellUtils.execCmdAsync(new String[]{"cat " + path}, false, true, new Utils.Consumer<ShellUtils.CommandResult>() {
            @Override
            public void accept(ShellUtils.CommandResult commandResult) {
                StringBuilder stringBuilder = new StringBuilder();
                if (commandResult.successMsg != null) {
                    if (finalPath.endsWith(".xml")) {
                        Document document = Jsoup.parse(commandResult.successMsg);
                        for (Element wifiConfigStoreDataElement : document.getElementsByTag("wifiConfigStoreData")) {
                            if ("wifiConfigStoreData".toLowerCase().equals(wifiConfigStoreDataElement.nodeName())) {
                                for (Element networkListElement : wifiConfigStoreDataElement.getElementsByTag("NetworkList")) {
                                    if ("NetworkList".toLowerCase().equals(networkListElement.nodeName())) {
                                        for (Element networkElement : wifiConfigStoreDataElement.getElementsByTag("Network")) {
                                            if ("Network".toLowerCase().equals(networkElement.nodeName())) {
                                                for (Element wifiConfigurationElement : wifiConfigStoreDataElement.getElementsByTag("WifiConfiguration")) {
                                                    if ("WifiConfiguration".toLowerCase().equals(wifiConfigurationElement.nodeName())) {
                                                        String wifi = MichaelUtil.getSubString(wifiConfigurationElement.tagName("SSID").html(), "SSID\">", "</string>").replaceAll("\n", "");
                                                        String password = MichaelUtil.getSubString(wifiConfigurationElement.tagName("SSID").html(), "PreSharedKey\">", "</string>").replaceAll("\n", "");
                                                        stringBuilder.append("Wifi:").append(wifi).append("\n");
                                                        stringBuilder.append("密码:").append(password.contains("string name=\"ConfigKey") ? "无密码" : password).append("\n");
                                                        stringBuilder.append("\n");
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }

                            }
                        }
                    } else {
                        Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
                        Matcher networkMatcher = network.matcher(commandResult.successMsg);
                        while (networkMatcher.find()) {
                            String networkBlock = networkMatcher.group();
                            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
                            Matcher ssidMatcher = ssid.matcher(networkBlock);

                            if (ssidMatcher.find()) {
                                stringBuilder.append("Wifi:").append("\"").append(ssidMatcher.group(1)).append("\"").append("\n");
                                Pattern psk = Pattern.compile("psk=\"([^\"]+)\"");
                                Matcher pskMatcher = psk.matcher(networkBlock);
                                if (pskMatcher.find()) {
                                    stringBuilder.append("密码:").append("\"").append(pskMatcher.group(1)).append("\"").append("\n");
                                } else {
                                    stringBuilder.append("密码:无密码").append("\n");
                                }
                                stringBuilder.append("\n");
                            }

                        }
                    }
                    binding.tvWifiPreview.setText(stringBuilder.toString());
                }
            }
        });

    }
}
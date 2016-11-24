package fr.kissy.hue_controller.infrastructure;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class PHHueSDKInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PHHueSDKInitializer.class);

    private SDKListener sdkListener;

    @Autowired
    public PHHueSDKInitializer(SDKListener sdkListener) {
        this.sdkListener = sdkListener;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        PHHueSDK phHueSDK = PHHueSDK.create();
        phHueSDK.setAppName("HueController");
        phHueSDK.getNotificationManager().registerSDKListener(sdkListener);

        Path configPath = Paths.get("config.properties");
            try (FileReader fileReader = new FileReader(configPath.toFile())) {
                Properties properties = new Properties();
                properties.load(fileReader);
                PHAccessPoint accessPoint = new PHAccessPoint();
                accessPoint.setIpAddress(String.valueOf(properties.get("IpAddress")));
                accessPoint.setUsername(String.valueOf(properties.get("Username")));
                if (!phHueSDK.isAccessPointConnected(accessPoint)) {
                    phHueSDK.connect(accessPoint);
                }
            } catch (FileNotFoundException e) {
                PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
                sm.search(true, true);
            } catch (IOException e) {
                LOGGER.error("Error while reading config file {}", configPath.toAbsolutePath(), e);
            }
    }
}
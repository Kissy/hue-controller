package fr.kissy.hue_controller.infrastructure;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import static fr.kissy.hue_controller.interfaces.BridgeController.*;

/**
 * Created by guillaume on 23/10/2016.
 */
@Component
public class SDKListener implements PHSDKListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SDKListener.class);
    private static final PHHueSDK PH_HUE_SDK = PHHueSDK.getInstance();
    private static final int HEARTBEAT_2S = 2000;
    private static final int HEARTBEAT_5S = 5000;

    private SimpMessagingTemplate template;

    @Autowired
    public SDKListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void onCacheUpdated(List<Integer> cacheNotifications, PHBridge phBridge) {
        if (cacheNotifications.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) {
            template.convertAndSend(LIGHTS_TOPIC, phBridge.getResourceCache().getLights().values());
        }
        if (cacheNotifications.contains(PHMessageType.GROUPS_CACHE_UPDATED)) {
            template.convertAndSend(GROUPS_TOPIC, phBridge.getResourceCache().getGroups().values());
        }
        if (cacheNotifications.contains(PHMessageType.BRIDGE_CONFIGURATION_CACHE_UPDATED)) {
            template.convertAndSend(BRIDGE_CONFIGURATION_TOPIC, phBridge.getResourceCache().getBridgeConfiguration());
        }
        if (cacheNotifications.contains(PHMessageType.SCHEDULES_CACHE_UPDATED)) {
            template.convertAndSend(SCHEDULES_TOPIC, phBridge.getResourceCache().getSchedules().values());
        }
        if (cacheNotifications.contains(PHMessageType.SCENE_CACHE_UPDATED)) {
            template.convertAndSend(SCENES_TOPIC, phBridge.getResourceCache().getScenes().values());
        }
        if (cacheNotifications.contains(PHMessageType.SENSOR_CACHE_UPDATED)) {
            template.convertAndSend(SENSORS_TOPIC, phBridge.getResourceCache().getSensors().values());
        }
        if (cacheNotifications.contains(PHMessageType.RULE_CACHE_UPDATED)) {
            template.convertAndSend(RULES_TOPIC, phBridge.getResourceCache().getRules().values());
        }
    }

    public void onBridgeConnected(PHBridge bridge, String username) {
        PH_HUE_SDK.setSelectedBridge(bridge);
        PH_HUE_SDK.enableHeartbeat(bridge, HEARTBEAT_5S);
        Path configPath = Paths.get("config.properties");
        Properties properties = new Properties();
        properties.setProperty("IpAddress", bridge.getResourceCache().getBridgeConfiguration().getIpAddress());
        properties.setProperty("Username", username);
        try (FileWriter fileWriter = new FileWriter(configPath.toFile())) {
            properties.store(fileWriter, "Auto generated");
        } catch (IOException e) {
            LOGGER.error("Impossible to write config file", e);
        }
    }

    public void onAuthenticationRequired(PHAccessPoint accessPoint) {
        PH_HUE_SDK.startPushlinkAuthentication(accessPoint);
        LOGGER.info("Pushlink Authentication required...");
    }

    public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
        if (accessPoints.size() == 1) {
            LOGGER.info("Connecting to access point...");
            PH_HUE_SDK.connect(accessPoints.get(0));
        } else {
            LOGGER.error("{} access points found, cannot connect", accessPoints.size());
        }
    }

    public void onError(int code, String message) {
        LOGGER.error("Error from bridge {} - {}", code, message);
    }

    public void onConnectionResumed(PHBridge phBridge) {

    }

    public void onConnectionLost(PHAccessPoint phAccessPoint) {

    }

    public void onParsingErrors(List<PHHueParsingError> list) {

    }
}

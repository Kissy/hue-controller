package fr.kissy.hue_controller.interfaces;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.*;
import com.philips.lighting.model.rule.PHRule;
import com.philips.lighting.model.sensor.PHSensor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Collection;

@Controller
public class BridgeController {

    public static final String BRIDGE_CONFIGURATION_TOPIC = "/topic/bridge-configuration";
    public static final String LIGHTS_TOPIC = "/topic/lights";
    public static final String GROUPS_TOPIC = "/topic/groups";
    public static final String SCENES_TOPIC = "/topic/scenes";
    public static final String SCHEDULES_TOPIC = "/topic/schedules";
    public static final String SENSORS_TOPIC = "/topic/sensors";
    public static final String RULES_TOPIC = "/topic/rules";

    @SubscribeMapping(BRIDGE_CONFIGURATION_TOPIC)
    public PHBridgeConfiguration getBridgeConfiguration(Principal principal) {
        return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getBridgeConfiguration();
    }

    @SubscribeMapping(LIGHTS_TOPIC)
    public Collection<PHLight> getLights(Principal principal) {
        return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getLights().values();
    }

    @SubscribeMapping(GROUPS_TOPIC)
    public Collection<PHGroup> getGroups(Principal principal) {
        return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getGroups().values();
    }

    @SubscribeMapping(SCENES_TOPIC)
    public Collection<PHScene> getScenes(Principal principal) {
        return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getScenes().values();
    }

    @SubscribeMapping(SCHEDULES_TOPIC)
    public Collection<PHSchedule> getSchedules(Principal principal) {
        return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getSchedules().values();
    }

    @SubscribeMapping(SENSORS_TOPIC)
    public Collection<PHSensor> getSensors(Principal principal) {
        return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getSensors().values();
    }

    @SubscribeMapping(RULES_TOPIC)
    public Collection<PHRule> getRules(Principal principal) {
        return PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getRules().values();
    }

}